package com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren;

import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Employee;
import com.GP.ELsayes.model.enums.permissions.ManagerPermission;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
    private Branch managedBranch;

}
