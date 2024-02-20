package com.GP.ELsayes.model.entity.SystemUsers.userChildren;

import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.SystemUsers.User;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor

//@Where(clause = "deleted_at is null")

@Entity
@Table(name = "customer")
@PrimaryKeyJoinColumn(name = "customer_id")
public class Customer extends User {
    @Id
    private Long id;
    private Date dateOfJoining;
    private String firstTrialCode;

//    @JsonBackReference
//    @ManyToOne
//    @JoinColumn(name = "worker_id")
//    private Worker worker;
}
