package com.GP.ELsayes.model.entity.relations;

import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.GP.ELsayes.model.enums.OperationType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor


@Entity
@Table(name = "workers_of_branches")
public class WorkersOfBranches {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date startAt ;

    private Date operationDate;

    @Enumerated(EnumType.STRING)
    private OperationType operationType;


    @JsonBackReference
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "worker_id")
    @ManyToOne
    private Worker worker;

    @JsonBackReference
    @JoinColumn(name = "branch_id")
    @ManyToOne
    private Branch branch;

    @JsonBackReference
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "manager_id")
    @ManyToOne
    private Manager manager;

}
