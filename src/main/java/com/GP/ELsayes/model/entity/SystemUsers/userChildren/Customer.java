package com.GP.ELsayes.model.entity.SystemUsers.userChildren;


import com.GP.ELsayes.model.entity.Car;
import com.GP.ELsayes.model.entity.FreeTrialCode;
import com.GP.ELsayes.model.entity.Order;
import com.GP.ELsayes.model.entity.SystemUsers.User;
import com.GP.ELsayes.model.entity.relations.PackagesOfOrder;
import com.GP.ELsayes.model.entity.relations.ServicesOfOrders;
import com.GP.ELsayes.model.entity.relations.VisitationsOfBranches;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor

//@Where(clause = "deleted_at is null")

@Entity(name = "customer")
@Table(name = "customer")
@PrimaryKeyJoinColumn(name = "customer_id")
public class Customer extends User {
    @Id
    private Long id;
    private Date dateOfJoining;



    @JsonManagedReference
    @OneToOne(mappedBy = "customer")
    private FreeTrialCode customerFreeTrialCode;

    @JsonManagedReference
    @OneToOne(mappedBy = "customer", cascade = CascadeType.REMOVE)
    private Car car;

    @JsonManagedReference
    @OneToMany(mappedBy = "customer")
    private List<VisitationsOfBranches> visitationsOfBranch;

    @JsonManagedReference
    @OneToMany(mappedBy = "customer")
    private List<ServicesOfOrders> servicesOfOrder;

    @JsonManagedReference
    @OneToMany(mappedBy = "customer")
    private List<PackagesOfOrder> packagesOfOrder;

    @JsonManagedReference
    @OneToMany(mappedBy = "customer")
    private List<Order> orders;

}
