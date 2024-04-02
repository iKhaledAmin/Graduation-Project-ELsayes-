package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.entity.Order;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.GP.ELsayes.model.entity.relations.ServicesOfOrders;
import com.GP.ELsayes.model.enums.roles.WorkerRole;
import com.GP.ELsayes.service.OrderHandlingService;
import com.GP.ELsayes.service.OrderService;
import com.GP.ELsayes.service.ServiceService;
import com.GP.ELsayes.service.WorkerService;
import com.GP.ELsayes.service.relations.ServicesOfOrderService;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.util.*;


import java.util.function.Function;
import java.util.stream.Collectors;


@Service

public class OrderHandlingServiceImpl implements OrderHandlingService {

    private final WorkerService workerService;
    private final OrderService orderService;
    private ServiceService serviceService;

    private final ServicesOfOrderService servicesOfOrderService;
    private final JmsTemplate jmsTemplate;

    // Constructor with JmsTemplate parameter
    public OrderHandlingServiceImpl(WorkerService workerService, OrderService orderService,
                                    ServiceService serviceService, ServicesOfOrderService servicesOfOrderService, JmsTemplate jmsTemplate) {
        this.workerService = workerService;
        this.orderService = orderService;
        this.serviceService = serviceService;
        this.servicesOfOrderService = servicesOfOrderService;
        this.jmsTemplate = jmsTemplate;
    }


    private Map<Long, Worker> availableCleaningWorkers;
    private Map<Long, Worker> availableParkingWorkers ;
    private Map<Long, Worker> availableMaintenanceWorkers ;


    Deque<Order> unServedOrders = new ArrayDeque<>();



    Deque<ServiceEntity> unServedCleaningService = new ArrayDeque<>();
    Deque<ServiceEntity> unServedTakeAwayService = new ArrayDeque<>();
    Deque<ServiceEntity> unServedMaintenanceService = new ArrayDeque<>();



    private Map<Long, Worker> getAllAvailableCleaningWorkers() {
        List<Worker> workers = workerService.getAllAvailableWorkerByWorkerRoleOrderByScore(WorkerRole.CLEANING_WORKER);
        return workers.stream().collect(Collectors.toMap(Worker::getId, Function.identity()));
    }
    private Map<Long, Worker> getAllAvailableParkingWorkers() {
        List<Worker> workers = workerService.getAllAvailableWorkerByWorkerRoleOrderByScore(WorkerRole.PARKING_WORKER);
        return workers.stream().collect(Collectors.toMap(Worker::getId, Function.identity()));
    }
    private Map<Long, Worker> getAllAvailableMaintenanceWorkers() {
        List<Worker> workers = workerService.getAllAvailableWorkerByWorkerRoleOrderByScore(WorkerRole.MAINTENANCE_WORKER);
        return workers.stream().collect(Collectors.toMap(Worker::getId, Function.identity()));
    }


    @Scheduled(fixedRate = 60000) // 60,000 milliseconds = 1 minute
    private  void  getAllAvailableWorkers(){
        availableCleaningWorkers = getAllAvailableCleaningWorkers();
        availableParkingWorkers = getAllAvailableParkingWorkers();
        availableMaintenanceWorkers = getAllAvailableMaintenanceWorkers();
    }



    @Override
    public synchronized void saveOrder(Long orderId) {
        Optional<Order> order = orderService.getObjectById(orderId);
        if (order.isPresent()) {
            // Send the order ID to the queue instead of directly serving the order
            //jmsTemplate.convertAndSend("orderQueue", orderId);

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
        List<ServicesOfOrders> ServicesOfOrder = servicesOfOrderService.getObjectByOrderId(orderId);
        for (ServicesOfOrders service : ServicesOfOrder){
            assignServiceToWorker(service);
        }
    }

    private void assignServiceToWorker(ServicesOfOrders servicesOfOrder){

            switch (servicesOfOrder.getServiceCategory()) {
                case CLEANING_SERVICE:
                    //
                    break;
                case TAKE_AWAY_SERVICE:
                    //
                    break;
                case MAINTENANCE_SERVICE:
                    //
                    break;
                default:
                    // Handle any unexpected categories
                    break;
            }

    }



//    void extractServicesOfOrder(Long orderId) {
//        List<ServiceEntity> servicesOfOrder = serviceService.getAllByOrderId(orderId);
//        for (ServiceEntity service : servicesOfOrder) {
//            switch (service.getServiceCategory()) {
//                case CLEANING_SERVICE:
//                    unServedCleaningService.addLast(service);
//                    break;
//                case TAKE_AWAY_SERVICE:
//                    unServedTakeAwayService.addLast(service);
//                    break;
//                case MAINTENANCE_SERVICE:
//                    unServedMaintenanceService.addLast(service);
//                    break;
//                default:
//                    // Handle any unexpected categories
//                    break;
//            }
//        }
//    }







}