package com.GP.ELsayes.model.entity;

import com.GP.ELsayes.model.entity.relations.ManagersOfOffers;
import com.GP.ELsayes.model.entity.relations.ServicesOfOffers;
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
@Table(name = "offer")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "offer_id")
    private Long id;

    private String name;
    private String description;
    private String offerImageURL;
    private String percentageOfDiscount;
    private String originalTotalPrice;
    private String originalTotalRequiredTime;
    private String actualOfferPrice;
    //private String actualOfferRequiredTime;

//    @CreationTimestamp
//    @Column(updatable = false)
//    private LocalDateTime createdAt;
//
//    @UpdateTimestamp
//    private LocalDateTime updatedAt;
//
//    private LocalDateTime deletedAt;



    @JsonManagedReference
    @OneToMany(mappedBy = "offer",cascade = CascadeType.REMOVE)
    private List<ManagersOfOffers> managersOfOffer;

    @JsonManagedReference
    @OneToMany(mappedBy = "offer" ,cascade = CascadeType.REMOVE)
    private List<ServicesOfOffers> servicesOfOffer ;
}
