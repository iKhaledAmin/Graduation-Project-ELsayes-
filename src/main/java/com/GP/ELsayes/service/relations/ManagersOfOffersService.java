package com.GP.ELsayes.service.relations;

import com.GP.ELsayes.model.entity.Offer;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.entity.relations.ManagersOfOffers;
import com.GP.ELsayes.model.enums.OperationType;
import org.springframework.stereotype.Service;

@Service
public interface ManagersOfOffersService extends
        CrudOfRelationsService<ManagersOfOffers, Manager, Offer, OperationType, Long> {
}
