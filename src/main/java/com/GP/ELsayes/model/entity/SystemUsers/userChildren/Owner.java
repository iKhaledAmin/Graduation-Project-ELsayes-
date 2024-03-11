package com.GP.ELsayes.model.entity.SystemUsers.userChildren;

import com.GP.ELsayes.model.entity.relations.OwnersOfBranches;
import com.GP.ELsayes.model.entity.relations.OwnersOfManagers;
import com.GP.ELsayes.model.entity.relations.OwnersOfRestrictedOwners;
import com.GP.ELsayes.model.entity.SystemUsers.User;
import com.GP.ELsayes.model.enums.permissions.OwnerPermission;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor

//@Where(clause = "deleted_at is null")
@Entity
@Table(name = "owner")
@PrimaryKeyJoinColumn(name = "owner_id")
public class Owner extends User {


    @Id
    private Long id;
    //private String percentage;

    @Enumerated(EnumType.STRING)
    private OwnerPermission ownerPermission;



    @JsonManagedReference
    @OneToMany(mappedBy = "owner")
    private List<OwnersOfBranches> ownersOfBranches;

    @JsonManagedReference
    @OneToMany(mappedBy = "owner")
    private List<OwnersOfManagers> ownersOfManagers;

    @JsonManagedReference
    @OneToMany(mappedBy = "oldOwner")
    private List<OwnersOfRestrictedOwners> ownersOfRestrictedOwners1;

    @JsonManagedReference
    @OneToMany(mappedBy = "restrictedOwner")
    private List<OwnersOfRestrictedOwners> ownersOfRestrictedOwners2;


}
