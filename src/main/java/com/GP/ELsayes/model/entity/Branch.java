package com.GP.ELsayes.model.entity;

import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.entity.relations.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor


@Entity
@Table(name = "branch")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branch_id")
    private Long id;
    private String name;
    private String location;
    private String capacityOfCars;

    private String profitOfDay;
    private String profitOfMonth;
    private String profitOfYear;
    private String totalProfit;




    @OneToOne(mappedBy = "branch")
    @JsonManagedReference
    private Manager manager;


    @JsonManagedReference
    @OneToMany(mappedBy = "branch")
    private List<OwnersOfBranches> ownersOfBranches;

    @JsonManagedReference
    @OneToMany(mappedBy = "branch")
    private List<WorkersOfBranches> WorkersOfBranch;


    @JsonManagedReference
    @OneToMany(mappedBy = "branch",cascade = CascadeType.REMOVE)
    private List<ServicesOfBranches> servicesOfBranch;

    @JsonManagedReference
    @OneToMany(mappedBy = "branch" ,cascade = CascadeType.REMOVE)
    private List<OffersOfBranches> offerOfBranch ;

    @JsonManagedReference
    @OneToMany(mappedBy = "branch")
    private List<VisitationsOfBranches> visitationsOfBranch;

}
