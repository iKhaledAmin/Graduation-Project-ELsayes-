package com.GP.ELsayes.service.relations;

import com.GP.ELsayes.model.dto.PackagesOfOrderResponse;
import com.GP.ELsayes.model.entity.relations.PackagesOfOrder;
import com.GP.ELsayes.model.enums.ProgressStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PackagesOfOrderService {
    public void addPackageToOrder(Long customerId, Long packageIdId);

    public void confirmAllPackagesOfOrder(Long orderId);

    public void updateStatusOfAllOrderPackage(Long orderId, ProgressStatus progressStatus);

    public Optional<PackagesOfOrder> getByCustomerIdAndOrderId(Long customerId, Long orderId);

    public List<PackagesOfOrderResponse> getUnConfirmedPackagesOfOrderByCustomerId(Long customerId, Long branchId);

    List<PackagesOfOrderResponse> getResponseAllByOrderId(Long orderId);

    public void deletePackageFromOrderList(Long packageId);

}