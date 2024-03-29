package com.GP.ELsayes.model.entity;

import com.GP.ELsayes.model.entity.relations.ManagersOfOffers;
import com.GP.ELsayes.model.entity.relations.OffersOfBranches;
import com.GP.ELsayes.model.entity.relations.ServicesOfOffers;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
    private String profit;
    //private String actualOfferRequiredTime;



    @JsonManagedReference
    @OneToMany(mappedBy = "offer",cascade = CascadeType.REMOVE)
    private List<ManagersOfOffers> managersOfOffer;

    @JsonManagedReference
    @OneToMany(mappedBy = "offer" )
    private List<ServicesOfOffers> servicesOfOffer ;

    @JsonManagedReference
    @OneToMany(mappedBy = "offer" ,cascade = CascadeType.REMOVE)
    private List<OffersOfBranches> offersOfBranch;
}


