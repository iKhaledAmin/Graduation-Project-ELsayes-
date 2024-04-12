package com.GP.ELsayes.model.entity.relations;


import com.GP.ELsayes.model.entity.Package;
import com.GP.ELsayes.model.entity.ServiceEntity;
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
@Table(name = "services_of_package")
public class ServicesOfPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @JoinColumn(name = "service_id")
    @ManyToOne
    private ServiceEntity service;

    @JsonBackReference
    @JoinColumn(name = "package_id")
    @ManyToOne
    private Package packageEntity;

    Date addingDate;

}
