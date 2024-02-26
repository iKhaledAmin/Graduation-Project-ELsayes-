package com.GP.ELsayes.model.entity.relations;

import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
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
@Table(name = "owners_of_Managers")
public class OwnersOfManagers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @JoinColumn(name = "owner_id")
    @OnDelete(action= OnDeleteAction.SET_NULL)
    @ManyToOne
    private Owner owner;


    @JsonBackReference
    @JoinColumn(name = "manager_id")
    @OnDelete(action= OnDeleteAction.SET_NULL)
    @ManyToOne
    private Manager manager;


    @Enumerated(EnumType.STRING)
    private OperationType operationType;
    private Date operationDate;
}
