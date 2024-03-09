package com.GP.ELsayes.model.entity;

import com.GP.ELsayes.model.entity.relations.ManagersOfServices;
import com.GP.ELsayes.model.entity.relations.ServicesOfBranches;
import com.GP.ELsayes.model.entity.relations.ServicesOfOffers;
import com.GP.ELsayes.model.enums.ServiceCategory;
import com.GP.ELsayes.model.enums.Status;
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
@Table(name = "service")
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Long id;

    private String name;
    private String description;
    private String serviceImageURL;
    private String price;
    private String requiredTime;

    @Enumerated(EnumType.STRING)
    private ServiceCategory serviceCategory;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;


    @JsonManagedReference
    @OneToMany(mappedBy = "service")
    private List<ManagersOfServices> managersOfService;

    @JsonManagedReference
    @OneToMany(mappedBy = "service",cascade = CascadeType.REMOVE)
    private List<ServicesOfBranches> servicesOfBranch;

    @JsonManagedReference
    @OneToMany(mappedBy = "service")
    private List<ServicesOfOffers> servicesOfOffer ;



}
