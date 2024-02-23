package com.GP.ELsayes.service.relations;

import com.GP.ELsayes.model.entity.relations.ManagersOfServices;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.enums.OperationType;

import org.springframework.stereotype.Service;

@Service
public interface ManagersOfServicesService
        extends CrudOfRelationsService<ManagersOfServices, Manager, ServiceEntity, OperationType, Long>{
}
