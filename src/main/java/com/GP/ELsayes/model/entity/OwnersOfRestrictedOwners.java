package com.GP.ELsayes.model.entity;

import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import com.GP.ELsayes.model.enums.CrudType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

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
    @Cascade({org.hibernate.annotations.CascadeType.REPLICATE, org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DETACH})
    @ManyToOne
    private Owner oldOwner;

    @JsonBackReference
    @JoinColumn(name = "restricted_owner_id")
    @Cascade({org.hibernate.annotations.CascadeType.REPLICATE, org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DETACH})
    @ManyToOne
    private Owner restrictedOwner;

    @Enumerated(EnumType.STRING)
    CrudType operationType;
    Date operationDate;

}
