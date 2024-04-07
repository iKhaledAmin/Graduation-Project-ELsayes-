package com.GP.ELsayes.service.relations;

import com.GP.ELsayes.model.dto.relations.ServicesOfPackagesResponse;
import org.springframework.stereotype.Service;

@Service
public interface ServicesOfPackagesService {
    public void handleAvailabilityOfPackageInAllBranches(Long packageId , Long serviceId);
    ServicesOfPackagesResponse addServiceToPackage(Long serviceId , Long packageId);

}
