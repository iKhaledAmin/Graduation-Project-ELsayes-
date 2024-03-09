package com.GP.ELsayes.model.entity.relations;

import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import com.GP.ELsayes.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.stereotype.Service;

import java.util.Date;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor


@Entity
@Table(name = "services_of_branches")
public class ServicesOfBranches {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @JoinColumn(name = "service_id")
    @ManyToOne
    private ServiceEntity service;


    @JsonBackReference
    @JoinColumn(name = "branch_id")
    @ManyToOne
    private Branch branch;

    @Enumerated(EnumType.STRING)
    private Status serviceStatus;
    private Date addingDate;

}
