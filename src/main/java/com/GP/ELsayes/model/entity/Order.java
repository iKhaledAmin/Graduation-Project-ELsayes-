package com.GP.ELsayes.model.entity;

import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.entity.relations.ServicesOfOrders;
import com.GP.ELsayes.model.enums.ProgressStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor


@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    private Date orderDate;
    private Date orderFinishDate;

    @Builder.Default
    private String totalRequiredTime = "0";
    @Builder.Default
    private String totalPrice = "0";

    @Enumerated(EnumType.STRING)
    private ProgressStatus progressStatus;


    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @JsonBackReference
    @JoinColumn(name = "branch_id")
    @ManyToOne
    private Branch branch;

    @JsonManagedReference
    @OneToMany(mappedBy = "order")
    private List<ServicesOfOrders> servicesOfOrder;


    public void incrementRequiredTime(String serviceTime){
        double totalRequiredTime = Double.parseDouble(this.totalRequiredTime) + Double.parseDouble(serviceTime) ;
        this.setTotalRequiredTime(
                String.valueOf(totalRequiredTime)
        );
    }
    public void incrementTotalPrice(String serviceCost){
        double totalPrice = Double.parseDouble(this.getTotalPrice()) + Double.parseDouble(serviceCost) ;
        this.setTotalPrice(
                String.valueOf(totalPrice)
        );
    }

}
