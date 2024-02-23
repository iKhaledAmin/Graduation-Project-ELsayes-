package com.GP.ELsayes.model.entity.relations;

import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.enums.OperationType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;




@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor

@Component
@Entity
@Table(name = "managers_of_services")
public class ManagersOfServices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceEntity service;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @Enumerated(EnumType.STRING)
    OperationType operationType;
    Date operationDate;

}
