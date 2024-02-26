package com.GP.ELsayes.model.entity.relations;

import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import com.GP.ELsayes.model.enums.OperationType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor


@Entity
@Table(name = "owners_of_restricted_owners")
public class OwnersOfRestrictedOwners {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JsonBackReference
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "old_owner_id")
    @ManyToOne
    private Owner oldOwner;

    @JsonBackReference
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "restricted_owner_id")
    @ManyToOne
    private Owner restrictedOwner;

    @Enumerated(EnumType.STRING)
    private OperationType operationType;
    private Date operationDate;

}
