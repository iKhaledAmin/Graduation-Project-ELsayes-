package com.GP.ELsayes.model.entity.relations;

import com.GP.ELsayes.model.entity.Order;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.GP.ELsayes.model.enums.ProgressStatus;
import com.GP.ELsayes.model.enums.ServiceCategory;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "services_of_orders")
public class ServicesOfOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProgressStatus progressStatus;

    private Date serviceDate;
    private Date serviceFinishDate;

    @Enumerated(EnumType.STRING)
    private ServiceCategory serviceCategory;

    @JsonBackReference
    @JoinColumn(name = "order_id")
    @ManyToOne
    private Order order;

    @JsonBackReference
    @JoinColumn(name = "worker_id")
    @ManyToOne
    private Worker worker;

    @JsonBackReference
    @JoinColumn(name = "customer_id")
    @ManyToOne
    private Customer customer;

    @JsonBackReference
    @JoinColumn(name = "service_id")
    @ManyToOne
    private ServiceEntity service;

    public Long getBranchId() {
        return order.getBranch().getId();
    }
}
