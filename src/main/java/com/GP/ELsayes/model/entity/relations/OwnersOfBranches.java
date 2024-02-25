package com.GP.ELsayes.model.entity.relations;

import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
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
@Table(name = "owners_of_branches")
public class OwnersOfBranches {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "owner_id")
    @ManyToOne
    private Owner owner;


    @JsonBackReference
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "branch_id")
    @ManyToOne
    private Branch branch;

    @Enumerated(EnumType.STRING)
    OperationType operationType;
    Date operationDate;
}
