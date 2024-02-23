package com.GP.ELsayes.model.entity.relations;

import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import com.GP.ELsayes.model.enums.OperationType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @JoinColumn(name = "old_owner_id")
    @ManyToOne
    private Owner oldOwner;

    @JsonBackReference
    @JoinColumn(name = "restricted_owner_id")
    @ManyToOne
    private Owner restrictedOwner;

    @Enumerated(EnumType.STRING)
    OperationType operationType;
    Date operationDate;

}
