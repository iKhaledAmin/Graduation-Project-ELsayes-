package com.GP.ELsayes.model.entity.SystemUsers.userChildren;

import com.GP.ELsayes.model.entity.OwnersOfBranches;
import com.GP.ELsayes.model.entity.OwnersOfManagers;
import com.GP.ELsayes.model.entity.OwnersOfRestrictedOwners;
import com.GP.ELsayes.model.entity.SystemUsers.User;
import com.GP.ELsayes.model.enums.permissions.OwnerPermission;
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
@Table(name = "owner")
@PrimaryKeyJoinColumn(name = "owner_id")
public class Owner extends User {


    @Id
    private Long id;
    private String percentage;

    @Enumerated(EnumType.STRING)
    private OwnerPermission ownerPermission;


//    @ManyToMany(cascade = CascadeType.ALL , fetch = FetchType.EAGER)
//    @JoinTable(
//            //name = "Course Student Allowed",
//            joinColumns = @JoinColumn(name = "owner_id"),
//            inverseJoinColumns = @JoinColumn(name = "branch_id")
//    )
//    private List<Branch> branches;

    @JsonManagedReference
    @OneToMany(mappedBy = "owner")
    private List<OwnersOfBranches> ownersOfBranches;

    @JsonManagedReference
    @OneToMany(mappedBy = "owner")
    private List<OwnersOfManagers> ownersOfManagers;

    @JsonManagedReference
    @OneToMany(mappedBy = "oldOwner",cascade = CascadeType.MERGE)
    private List<OwnersOfRestrictedOwners> ownersOfRestrictedOwners;

    @JsonManagedReference
    @OneToMany(mappedBy = "restrictedOwner",cascade = CascadeType.MERGE)
    private List<OwnersOfRestrictedOwners> ownersOfRestrictedOwner;


}
