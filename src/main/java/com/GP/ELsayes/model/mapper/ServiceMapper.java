package com.GP.ELsayes.model.mapper;

import com.GP.ELsayes.model.dto.ServiceRequest;
import com.GP.ELsayes.model.dto.ServiceResponse;
import com.GP.ELsayes.model.dto.relations.ServicesOfBranchesResponse;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.relations.ServicesOfBranches;
import com.GP.ELsayes.model.enums.Status;
import com.GP.ELsayes.repository.ServiceRepo;
import com.GP.ELsayes.repository.relations.ServicesOfBranchesRepo;
import com.GP.ELsayes.service.ServiceService;
import com.GP.ELsayes.service.impl.ServiceServiceImpl;
import com.GP.ELsayes.service.relations.ServicesOfBranchesService;
import com.GP.ELsayes.service.relations.impl.ServicesOfBranchesServiceImpl;
import lombok.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    ServiceMapper INSTANCE = Mappers.getMapper(ServiceMapper.class);

    ServiceEntity toEntity(ServiceRequest request);


    ServiceResponse toResponse(ServiceEntity entity);


    // Map ServiceEntity to ServiceResponse with availability in a specific branch
    @Mapping(target = "availableInBranch", expression = "java(isAvailableInBranch(entity.getId(), branchId))")
    ServiceResponse toResponseAccordingToBranch(ServiceEntity entity, Long branchId);


    // Custom method to determine availability based on serviceStatus
    default boolean isAvailableInBranch(Long serviceId, Long branchId) {
        ServicesOfBranches servicesOfBranches = getServicesOfBranches(serviceId, branchId);

        if (servicesOfBranches != null) {
            Status serviceStatus = servicesOfBranches.getServiceStatus();
            return serviceStatus == Status.AVAILABLE;
        }

        // If the service is not found in the branch, consider it unavailable
        return false;
    }

    // Placeholder method for retrieving ServicesOfBranches
    ServicesOfBranches getServicesOfBranches(Long serviceId, Long branchId);

}