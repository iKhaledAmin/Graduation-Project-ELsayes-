package com.GP.ELsayes.model.entity.relations;

import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.Package;
import com.GP.ELsayes.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor


@Entity
@Table(name = "offer_of_branches")
public class OffersOfBranches {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @JoinColumn(name = "offer_id")
    @ManyToOne
    private Package aPackage;


    @JsonBackReference
    @JoinColumn(name = "branch_id")
    @ManyToOne
    private Branch branch;

    @Enumerated(EnumType.STRING)
    private Status offerStatus;
    private Date addingDate;
}
