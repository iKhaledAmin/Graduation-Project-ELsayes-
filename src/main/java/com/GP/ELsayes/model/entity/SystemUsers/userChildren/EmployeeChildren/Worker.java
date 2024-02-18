package com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren;

import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Employee;
import com.GP.ELsayes.model.enums.roles.WorkerRole;
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
@Table(name = "worker")
@PrimaryKeyJoinColumn(name = "worker_id")
public class Worker extends Employee {
    @Id
    @Column(name = "worker_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private WorkerRole workerRole;

    private String Score;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "branch_Id")
    private Branch branch;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "manager_Id")
    private Manager manager;
}
