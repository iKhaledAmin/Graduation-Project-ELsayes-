package com.GP.ELsayes.model.entity;

import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import com.GP.ELsayes.model.enums.CrudType;
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
@Table(name = "owners_of_Managers")
public class OwnersOfManagers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @JoinColumn(name = "owner_id")
    @ManyToOne(cascade = CascadeType.ALL)
    private Owner owner;


    @JsonBackReference
    @JoinColumn(name = "manager_id")
    @ManyToOne(cascade = CascadeType.ALL)
    private Manager manager;

    @Enumerated(EnumType.STRING)
    CrudType operationType;
    Date operationDate;
}
