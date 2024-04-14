package com.GP.ELsayes.service.relations;

import com.GP.ELsayes.model.dto.relations.VisitationsOfBranchesResponse;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.Car;
import com.GP.ELsayes.model.entity.relations.VisitationsOfBranches;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public interface VisitationsOfBranchesService {
    public String getHOUR_PRICE();
    public VisitationsOfBranches addVisitation(Car car , Branch branch);
    public VisitationsOfBranches endVisitation(Car car, Branch branch);
    public VisitationsOfBranches update(VisitationsOfBranches visitation);
    public String toFormattedPeriod(Duration period);
    public String calculateParkingPrice(Duration period , String hourPrice);
    VisitationsOfBranches getRecentByCarPlateNumberAndBranchId(String carPlateNumber , Long branchId);
    public List<VisitationsOfBranchesResponse> getResponseAllCurrentVisitationsInBranch(Long branchId);
    public List<VisitationsOfBranchesResponse> getResponseAllVisitationsInBranchByADate(Long branchId , Date date);

    Optional<VisitationsOfBranches> getCurrentVisitationByCustomerId(Long customerId);
}
