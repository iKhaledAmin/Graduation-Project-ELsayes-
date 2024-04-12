package com.GP.ELsayes.model.entity.relations;

import com.GP.ELsayes.model.entity.Order;
import com.GP.ELsayes.model.entity.Package;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.enums.ProgressStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Entity
@Table(name = "packages_of_orders")
public class PackagesOfOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProgressStatus progressStatus;

    @JsonBackReference
    @JoinColumn(name = "customer_id")
    @ManyToOne
    private Customer customer;

    @JsonBackReference
    @JoinColumn(name = "order_id")
    @ManyToOne
    private Order order;

    @JsonBackReference
    @JoinColumn(name = "package_id")
    @ManyToOne
    private Package packageEntity;

    @JsonManagedReference
    @OneToMany(mappedBy = "packagesOfOrder" )
    private List<ServicesOfOrders> servicesOfOrder ;


}
