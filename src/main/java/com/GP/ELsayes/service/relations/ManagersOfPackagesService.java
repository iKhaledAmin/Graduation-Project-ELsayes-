package com.GP.ELsayes.service.relations;


import com.GP.ELsayes.model.entity.Package;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.entity.relations.ManagersOfPackages;
import org.springframework.stereotype.Service;

@Service
public interface ManagersOfOffersService {
    ManagersOfPackages addManagerToOffer(Manager manager , Package aPackage);
    ManagersOfPackages updateManagerToOffer(Long managerId , Long offerId);
}
