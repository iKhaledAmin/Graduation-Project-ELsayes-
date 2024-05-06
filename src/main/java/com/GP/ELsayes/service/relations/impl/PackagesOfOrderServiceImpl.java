package com.GP.ELsayes.service.relations.impl;

import com.GP.ELsayes.model.dto.PackagesOfOrderResponse;
import com.GP.ELsayes.model.dto.ServicesOfOrderResponse;
import com.GP.ELsayes.model.entity.Order;
import com.GP.ELsayes.model.entity.Package;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.entity.relations.PackagesOfOrder;
import com.GP.ELsayes.model.entity.relations.ServicesOfOrders;
import com.GP.ELsayes.model.enums.ProgressStatus;
import com.GP.ELsayes.model.mapper.relations.PackagesOfOrderMapper;
import com.GP.ELsayes.repository.relations.PackagesOfOrderRepo;
import com.GP.ELsayes.service.*;
import com.GP.ELsayes.service.relations.PackagesOfOrderService;
import com.GP.ELsayes.service.relations.ServicesOfOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PackagesOfOrderServiceImpl implements PackagesOfOrderService {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final PackageService packageService;
    private final PackagesOfOrderRepo packagesOfOrderRepo;
    private final PackagesOfOrderMapper packagesOfOrderMapper;
    private final ServiceService serviceService;
    private final ServicesOfOrderService servicesOfOrderService;



    private void throwExceptionIfServiceHasAlreadyExistedInOrder(Long packageId , Long orderId){
        Optional<PackagesOfOrder> packagesOfOrder = packagesOfOrderRepo.findByPackageIdAndOrderId(packageId,orderId);
        if(packagesOfOrder.isEmpty()){
            return;
        }
        throw new RuntimeException("This package with id "+ packageId +" already existed in this order");
    }

    @Override
    public Optional<PackagesOfOrder> getByCustomerIdAndOrderId(Long customerId, Long orderId) {
        return packagesOfOrderRepo.findByCustomerIdAndOrderId(customerId,orderId);
    }




    private  List<PackagesOfOrderResponse> getResponseAllUnConfirmedPackagesByCustomerId(Long customerId){

        List<PackagesOfOrder> packagesOfOrder = packagesOfOrderRepo.findAllUnConfirmedByCustomerId(customerId);
        return packagesOfOrder
                .stream()
                .map(serviceOfOrderResponse ->  packagesOfOrderMapper.toResponse(serviceOfOrderResponse))
                .toList();
    }

    private  List<PackagesOfOrderResponse> getResponseAllUnConfirmedPackagesByCustomerIdAccordingBranch(Long customerId,Long branchId){

        List<PackagesOfOrder> packagesOfOrderList = packagesOfOrderRepo.findAllUnConfirmedByCustomerId(customerId);
        return packagesOfOrderList
                .stream()
                .map(packagesOfOrder -> {
                    Package aPackage = packageService.getById(packagesOfOrder.getPackageEntity().getId());
                    return packagesOfOrderMapper.toResponseAccordingToBranch(aPackage,branchId,packageService);
                })
                .toList();
    }

    @Override
    public List<PackagesOfOrderResponse> getUnConfirmedPackagesOfOrderByCustomerId(Long customerId, Long branchId){

        System.out.println("THe  " + branchId);
        if (branchId == null)
            return getResponseAllUnConfirmedPackagesByCustomerId(customerId);
        else {
            return getResponseAllUnConfirmedPackagesByCustomerIdAccordingBranch(customerId,branchId);
        }

    }


    @Override
    public List<PackagesOfOrderResponse> getResponseAllByOrderId(Long orderId) {
        return packagesOfOrderRepo.findAllByOrderId(orderId)
                .stream()
                .map(packagesOfOrder ->  packagesOfOrderMapper.toResponse(packagesOfOrder))
                .toList();
    }

    @Override
    public void addPackageToOrder(Long customerId, Long packageId) {

        Customer customer = customerService.getById(customerId);
        Package aPackage = packageService.getById(packageId);


        Optional<Order> unConfirmedOrder = orderService.getUnConfirmedByCustomerId(customerId);
        if(unConfirmedOrder.isEmpty()){
            unConfirmedOrder = Optional.ofNullable(orderService.add(customerId));
        }

        throwExceptionIfServiceHasAlreadyExistedInOrder(packageId,unConfirmedOrder.get().getId());

        PackagesOfOrder packagesOfOrder = new PackagesOfOrder();
        packagesOfOrder.setProgressStatus(ProgressStatus.UN_CONFIRMED);
        packagesOfOrder.setOrder(unConfirmedOrder.get());
        packagesOfOrder.setCustomer(customer);
        packagesOfOrder.setPackageEntity(aPackage);
        packagesOfOrderRepo.save(packagesOfOrder);

        List<ServiceEntity> packageServices = serviceService.getAllByPackageId(packageId);
        packageServices.stream().forEach(service -> {
            servicesOfOrderService.addServiceToOrder(customerId,service.getId(),true);
        });

        double totalOrderPrice = Double.parseDouble(aPackage.getCurrentPackagePrice() )+ Double.parseDouble(unConfirmedOrder.get().getTotalPrice() );
        unConfirmedOrder.get().setTotalPrice(
                String.valueOf(totalOrderPrice)
        );
        orderService.update(unConfirmedOrder.get());
    }

    @Override
    public void deletePackageFromOrderList(Long packageOfOrderId) {
        PackagesOfOrder packagesOfOrder = packagesOfOrderRepo.findById(packageOfOrderId).orElseThrow(
                () -> new NoSuchElementException("There is no package with id = " + packageOfOrderId + " in this order")
        );
        if (packagesOfOrder.getProgressStatus() != ProgressStatus.UN_CONFIRMED){
            throw new RuntimeException("Order is confirmed, you can not delete");
        }

        List<ServicesOfOrders> servicesOfOrder = servicesOfOrderService.
                getAllUnConfirmedByPackageOfOrderId(packagesOfOrder.getId());
        System.out.println("The Size " + servicesOfOrder.size());
        servicesOfOrder.stream().forEach(service -> {
            servicesOfOrderService.deleteServiceFromOrderList(service.getId());
        });

        packagesOfOrderRepo.deleteById(packageOfOrderId);

    }


    @Override
    public void confirmAllPackagesOfOrder(Long orderId) {
        packagesOfOrderRepo.confirmAllPackageOfOrder(orderId);
    }

    @Override
    public void updateStatusOfAllOrderPackage(Long orderId, ProgressStatus progressStatus){
        packagesOfOrderRepo.updateAllProgressStatusByOrderId(orderId,progressStatus);
    }




}
