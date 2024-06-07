package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.entity.Order;
import com.GP.ELsayes.model.entity.SystemUsers.User;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.GP.ELsayes.model.entity.relations.ServicesOfOrders;
import com.GP.ELsayes.model.enums.NotificationType;
import com.GP.ELsayes.model.enums.ProgressStatus;
import com.GP.ELsayes.model.enums.roles.WorkerRole;
import com.GP.ELsayes.service.OrderHandlingService;
import com.GP.ELsayes.service.OrderService;
import com.GP.ELsayes.service.UserService;
import com.GP.ELsayes.service.WorkerService;
import com.GP.ELsayes.service.relations.ServicesOfOrderService;
import com.GP.ELsayes.utils.WorkerStorage;

import com.GP.ELsayes.websocket.notification.Notification;
import com.GP.ELsayes.websocket.notification.NotificationService;
import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.util.*;




@Service
@RequiredArgsConstructor
public class OrderHandlingServiceImpl implements OrderHandlingService {

    private final WorkerService workerService;
    private final OrderService orderService;
    private final ServicesOfOrderService servicesOfOrderService;
    private final NotificationService notificationService;
    private final UserService userService;


    private WorkerStorage workerStorage = new WorkerStorage();
    private Deque<Order> unServedOrders = new ArrayDeque<>();
    private Queue<ServicesOfOrders> pendingServices = new LinkedList<>();
    private HashMap<Long, Worker> availableCleaningWorkers;
    private HashMap<Long, Worker> availableParkingWorkers ;
    private HashMap<Long, Worker> availableMaintenanceWorkers ;


    private HashMap<Long, Worker> getAllAvailableCleaningWorkers() {
        List<Worker> workers = workerService.getAllAvailableWorkerByWorkerRoleOrderByScore(WorkerRole.CLEANING_WORKER);
        HashMap<Long, Worker> workersMap = new HashMap<>();
        for (Worker worker : workers) {
            workersMap.put(worker.getId(), worker);
        }
        return workersMap;
    }
    private HashMap<Long, Worker> getAllAvailableParkingWorkers() {
        List<Worker> workers = workerService.getAllAvailableWorkerByWorkerRoleOrderByScore(WorkerRole.PARKING_WORKER);
        HashMap<Long, Worker> workersMap = new HashMap<>();
        for (Worker worker : workers) {
            workersMap.put(worker.getId(), worker);
        }
        return workersMap;
    }
    private HashMap<Long, Worker> getAllAvailableMaintenanceWorkers() {
        List<Worker> workers = workerService.getAllAvailableWorkerByWorkerRoleOrderByScore(WorkerRole.MAINTENANCE_WORKER);
        HashMap<Long, Worker> workersMap = new HashMap<>();
        for (Worker worker : workers) {
            workersMap.put(worker.getId(), worker);
        }
        return workersMap;
    }


    // Method to store available workers in WorkerStorage
    private void storeAvailableWorkers() {
        workerStorage.addWorkersByRole(WorkerRole.CLEANING_WORKER, availableCleaningWorkers);
        workerStorage.addWorkersByRole(WorkerRole.PARKING_WORKER, availableParkingWorkers);
        workerStorage.addWorkersByRole(WorkerRole.MAINTENANCE_WORKER, availableMaintenanceWorkers);
    }
    @Scheduled(fixedRate = 30000 ) // 60,000 milliseconds = 1 minute
    private void getAllAvailableWorkers() {
        availableCleaningWorkers = getAllAvailableCleaningWorkers();
        availableParkingWorkers = getAllAvailableParkingWorkers();
        availableMaintenanceWorkers = getAllAvailableMaintenanceWorkers();
        storeAvailableWorkers(); // Store the workers in WorkerStorage

//        System.out.println("availableCleaningWorkers "+availableCleaningWorkers.keySet());
//        System.out.println("availableParkingWorkers "+availableParkingWorkers.keySet());
//        System.out.println("availableMaintenanceWorker s"+availableMaintenanceWorkers.keySet());
//        printPendingServices();
//        System.out.println("||||||||||||||||||||||||||||||||||||||||||||||");
    }

    private void sendNotificationToAssignedWorker(Long workerId , String carPlateNumber,String serviceName ){

        Notification notification = new Notification();
        notification.setNotificationTitle(serviceName);
        notification.setNotificationContent("You have new task for car : " + carPlateNumber);
        notification.setType(NotificationType.Worker_Notification);
        User user = userService.getById(workerId);
        notification.setUser(user);
        notificationService.sendPrivateNotification(String.valueOf(workerId), notification);


    }

    @Override
    public synchronized void saveOrder(Long orderId) {
        Optional<Order> order = orderService.getObjectById(orderId);
        if (order.isPresent()) {
            // Send the order ID to the queue instead of directly serving the order
            //jmsTemplate.convertAndSend("orderQueue", orderId);
            unServedOrders.addLast(order.get());
            serveOrder(orderId);

        }

    }

//    // Message listener that processes orders from the queue
//    @JmsListener(destination = "orderQueue")
//    public void processOrder(Long orderId) {
//        // Logic to process the order
//        serveOrder(orderId);
//    }

    private void serveOrder(Long orderId) {
        // Logic to serve the order
        List<ServicesOfOrders> servicesOfOrder = servicesOfOrderService.getAllByOrderId(orderId);
        for (ServicesOfOrders service : servicesOfOrder){
            assignServiceToWorker(service);
        }

    }

    private boolean assignServiceToWorker(ServicesOfOrders servicesOfOrder) {
        Worker assignedWorker = null;

        // Determine the correct map of available workers based on the service category
        HashMap<Long, Worker> availableWorkers;
        switch (servicesOfOrder.getServiceCategory()) {
            case CLEANING_SERVICE:
                availableWorkers = availableCleaningWorkers;
                break;
            case TAKE_AWAY_SERVICE:
                availableWorkers = availableParkingWorkers;
                break;
            case MAINTENANCE_SERVICE:
                availableWorkers = availableMaintenanceWorkers;
                break;
            default:
                // Handle other service categories or throw an exception
                throw new IllegalArgumentException("Invalid service category");
        }
        // Find the available worker with the highest score from the selected map
        if (availableWorkers != null) {
            assignedWorker = availableWorkers.values().stream()
                    // Filter workers by matching branch ID
                    .filter(worker -> worker.getBranch().getId().equals(servicesOfOrder.getBranchId()))
                    // Sort workers by score in descending order
                    .sorted(Comparator.comparing((Worker worker) -> Double.parseDouble(worker.getScore())).reversed())
                    // Get the first worker if present
                    .findFirst()
                    .orElse(null);
        }
        if (assignedWorker != null) {
            servicesOfOrder.setWorker(assignedWorker);
            servicesOfOrderService.setWorkerServiceTask(
                    servicesOfOrder.getCustomer().getId(),
                    servicesOfOrder.getService().getId(),
                    assignedWorker
            );

            orderService.updateOrderStatus(
                    servicesOfOrder.getOrder().getId(), ProgressStatus.ON_WORKING
            );
            availableWorkers.remove(assignedWorker.getId());

            // Send notification to the customer
            sendNotificationToAssignedWorker(
                    assignedWorker.getId(),
                    servicesOfOrder.getCustomer().getCar().getCarPlateNumber(),
                    servicesOfOrder.getService().getName()
            );
            return true;
        } else {
            // No available worker found, return false
            getAllAvailableWorkers();
            if (pendingServices.contains(servicesOfOrder)){
                return false;
            }else {
                pendingServices.add(servicesOfOrder);
                return false;
            }
        }
    }

    @Scheduled(fixedRate = 20000) // Runs every 1m
    public void processPendingServices() {
        while (!pendingServices.isEmpty()) {
            ServicesOfOrders service = pendingServices.peek();
            // Try to assign a worker to the service
            boolean assigned = assignServiceToWorker(service);
            if (assigned) {
                // If a worker was assigned, remove the service from the queue
                pendingServices.remove();
            } else {
                // If no worker was available, break the loop to try again later
                break;
            }
        }
    }

    public void printPendingServices() {
        for (ServicesOfOrders service : pendingServices) {
            System.out.println(Optional.ofNullable(service).get().getService().getId());
        }
    }

}