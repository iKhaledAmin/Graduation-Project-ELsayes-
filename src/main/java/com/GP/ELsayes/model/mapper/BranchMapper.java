package com.GP.ELsayes.model.mapper;

import com.GP.ELsayes.model.dto.BranchRequest;
import com.GP.ELsayes.model.dto.BranchResponse;
import com.GP.ELsayes.model.entity.Branch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BranchMapper {

    BranchMapper INSTANCE = Mappers.getMapper(BranchMapper.class);

    Branch toEntity(BranchRequest request);

    @Mapping(target = "managerId" , source = "entity.manager.id")
    //@Mapping(target = "ownerIdCreateIt" , source = "entity.ownersOfBranches[1].owner.id")
    BranchResponse toResponse(Branch entity);

}
