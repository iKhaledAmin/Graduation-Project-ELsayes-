package com.GP.ELsayes.model.entity.SystemUsers.userChildren;

import com.GP.ELsayes.model.entity.SystemUsers.User;
import com.GP.ELsayes.model.enums.permissions.OwnerPermission;
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
@Table(name = "owner")
@PrimaryKeyJoinColumn(name = "owner_id")
public class Owner extends User {


    @Id
    private Long id;
    private String percentage;

    @Enumerated(EnumType.STRING)
    private OwnerPermission ownerPermission;
}
