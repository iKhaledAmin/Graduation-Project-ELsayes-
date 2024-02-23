package com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren;

import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.relations.ManagersOfServices;
import com.GP.ELsayes.model.entity.relations.OwnersOfManagers;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Employee;
import com.GP.ELsayes.model.enums.permissions.ManagerPermission;
import com.fasterxml.jackson.annotation.JsonBackReference;
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

//@Where(clause = "deleted_at is null")

@Entity
@Table(name = "manager")
@PrimaryKeyJoinColumn(name = "manager_id")
public class Manager extends Employee {
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private ManagerPermission managerPermission;


    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "branch_Id")
    private Branch branch;

    @JsonManagedReference
    @OneToMany(mappedBy = "manager")
    private List<Worker> workers;

    @JsonManagedReference
    @OneToMany(mappedBy = "manager")
    private List<OwnersOfManagers> ownersOfManagers;

    @JsonManagedReference
    @OneToMany(mappedBy = "manager")
    private List<ManagersOfServices>managersOfServices;

}
