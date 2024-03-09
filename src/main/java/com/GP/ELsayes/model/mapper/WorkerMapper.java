package com.GP.ELsayes.model.mapper;

import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerResponse;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.GP.ELsayes.model.entity.relations.WorkersOfBranches;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkerMapper {

    WorkerMapper INSTANCE = Mappers.getMapper(WorkerMapper.class);

    default Long getBranchId(List<WorkersOfBranches> workersOfBranch) {
        if (workersOfBranch != null) {
            return workersOfBranch.isEmpty() ? null : workersOfBranch.get(0).getBranch().getId();
        } else {
            return null;
        }
    }
    default Long getManagerId(List<WorkersOfBranches> workersOfBranch) {
        if (workersOfBranch != null) {
            return workersOfBranch.get(0).getManager().getId();
        } else {
            return null;
        }
    }

    Worker toEntity(WorkerRequest request);

    @Mapping(target = "branchId" , expression = "java(getBranchId(entity.getWorkersOfBranch()))")
    @Mapping(target = "managerId" , expression = "java(getManagerId(entity.getWorkersOfBranch()))")
    WorkerResponse toResponse(Worker entity);


}
