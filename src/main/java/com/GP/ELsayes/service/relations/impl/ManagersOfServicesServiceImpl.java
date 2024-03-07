package com.GP.ELsayes.service.relations.impl;

import com.GP.ELsayes.model.entity.relations.ManagersOfServices;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.enums.OperationType;
import com.GP.ELsayes.repository.relations.ManagersOfServicesRepo;
import com.GP.ELsayes.service.relations.ManagersOfServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Date;
@RequiredArgsConstructor
@Service
public class ManagersOfServicesServiceImpl implements ManagersOfServicesService{

    private final ManagersOfServicesRepo managersOfServicesRepo;


    @Override
    public ManagersOfServices add(Manager manager, ServiceEntity service, OperationType operationType) {

        ManagersOfServices managersOfServices = new ManagersOfServices();
        managersOfServices.setManager(manager);
        managersOfServices.setService(service);
        managersOfServices.setOperationType(operationType);
        managersOfServices.setOperationDate(new Date());

        return this.managersOfServicesRepo.save(managersOfServices);
    }
}
