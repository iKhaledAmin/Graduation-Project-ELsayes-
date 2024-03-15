package com.GP.ELsayes.repository.relations;

import com.GP.ELsayes.model.entity.relations.CustomerVisitationsOfBranches;
import jakarta.jws.Oneway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CustomerVisitationsOfBranchesRepo extends JpaRepository<CustomerVisitationsOfBranches, Long> {

    /**
     * Finds the current visitation record for a car with a given plate number at a specific branch.
     * This method returns an Optional containing the visitation if the car is currently present (dateOfLeaving is not null),
     * otherwise, it returns an empty Optional.

     * @return an Optional containing the current visitation record, if found
     */
    @Query("SELECT cv FROM CustomerVisitationsOfBranches cv JOIN cv.car c WHERE c.carPlateNumber = :carPlateNumber" +
            " AND cv.branch.id = :branchId AND cv.dateOfLeaving IS NULL")
    Optional<CustomerVisitationsOfBranches> findCurrentlyByCarPlateNumberAndBranchId(String carPlateNumber, Long branchId);

    /**
     * Retrieves the most recent visitation record for a car with a given plate number at a specific branch.
     * The results are ordered by the date of arrival in descending order, ensuring the most recent record is returned first.
     * This method returns a Page of CustomerVisitationsOfBranches, which should be limited to a single result.

     * @param pageable a Pageable object to limit the query to the most recent record
     * @return a Page containing the most recent visitation record, if found
     */
    @Query("SELECT cv FROM CustomerVisitationsOfBranches cv JOIN cv.car c WHERE c.carPlateNumber = :carPlateNumber" +
            " AND cv.branch.id = :branchId ORDER BY cv.dateOfArriving DESC")
    Page<CustomerVisitationsOfBranches> findRecentByCarPlateNumberAndBranchId(String carPlateNumber, Long branchId, Pageable pageable);

    @Query("SELECT cv FROM CustomerVisitationsOfBranches cv WHERE" +
            " cv.branch.id = :branchId AND cv.dateOfLeaving IS NULL")
    List<CustomerVisitationsOfBranches> findAllCurrentVisitationsInBranch(Long branchId);
    @Query("SELECT cv FROM CustomerVisitationsOfBranches cv WHERE " +
            "cv.branch.id = :branchId AND EXTRACT(DAY FROM cv.dateOfArriving) = EXTRACT(DAY FROM :date)")
    List<CustomerVisitationsOfBranches> findAVisitationsInBranchByADate(Long branchId, Date date );
}