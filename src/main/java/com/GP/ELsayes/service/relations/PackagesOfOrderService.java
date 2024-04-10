package com.GP.ELsayes.service.relations;

import com.GP.ELsayes.model.entity.relations.PackagesOfOrder;
import com.GP.ELsayes.model.enums.ProgressStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface PackagesOfOrderService {
    void addPackageToOrder(Long customerId,Long packageIdId);
    public void deletePackageFromOrderList(Long packageId);
    public void confirmAllPackagesOfOrder(Long orderId);

    public void updateStatusOfAllOrderPackage(Long orderId, ProgressStatus progressStatus);

    Optional<PackagesOfOrder> getByCustomerIdAndOrderId(Long customerId, Long orderId);
}
