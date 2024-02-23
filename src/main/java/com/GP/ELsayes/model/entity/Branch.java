package com.GP.ELsayes.model.entity;

import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.GP.ELsayes.model.entity.relations.OwnersOfBranches;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor


@Entity
@Table(name = "branch")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branch_id")
    private Long id;
    private String name;
    private String location;
    private String capacityOfCars;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;



    @OneToOne(mappedBy = "branch")
    @JsonManagedReference
    private Manager manager;

    @JsonManagedReference
    @OneToMany(mappedBy = "branch")
    private List<Worker> workers;


    @JsonManagedReference
   // @Cascade({org.hibernate.annotations.CascadeType.PERSIST})
    @OneToMany(mappedBy = "branch")
    private List<OwnersOfBranches> ownersOfBranches;

}
