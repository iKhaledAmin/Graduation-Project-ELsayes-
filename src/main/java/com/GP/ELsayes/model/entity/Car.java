package com.GP.ELsayes.model.entity;

import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.entity.relations.CustomerVisitationsOfBranches;
import com.GP.ELsayes.model.enums.CarType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor


@Entity
@Table(name = "car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private Long id;

    private String carPlateNumber;

    @Enumerated(EnumType.STRING)
    private CarType carType;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date model;


    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "customer_id")
    private Customer customer;


    @JsonManagedReference
    @OneToMany(mappedBy = "car")
    private List<CustomerVisitationsOfBranches> customerVisitationsOfBranch;


}
