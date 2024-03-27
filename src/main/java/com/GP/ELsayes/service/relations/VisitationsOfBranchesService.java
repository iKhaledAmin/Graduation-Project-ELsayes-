package com.GP.ELsayes.service.relations;

import com.GP.ELsayes.model.dto.relations.VisitationsOfBranchesResponse;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.Car;
import com.GP.ELsayes.model.entity.relations.VisitationsOfBranches;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface VisitationsOfBranchesService {
    public VisitationsOfBranches addVisitation(Car car , Branch branch);
    public VisitationsOfBranches endVisitation(Car car, Branch branch);
    VisitationsOfBranches getRecentByCarPlateNumberAndBranchId(String carPlateNumber , Long branchId);

    public List<VisitationsOfBranchesResponse> getResponseAllCurrentVisitationsInBranch(Long branchId);
    public List<VisitationsOfBranchesResponse> getResponseAllVisitationsInBranchByADate(Long branchId , Date date);
}
