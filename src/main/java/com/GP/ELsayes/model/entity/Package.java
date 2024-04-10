package com.GP.ELsayes.model.entity;

import com.GP.ELsayes.model.entity.relations.*;
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
@Table(name = "package")
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_id")
    private Long id;

    private String name;
    private String description;
    private String packageImageURL;
    private String percentageOfDiscount;
    private String originalTotalPrice;
    private String originalTotalRequiredTime;
    private String currentPackagePrice;
    private String profit;
    //private String currentPackageRequiredTime;



    @JsonManagedReference
    @OneToMany(mappedBy = "aPackage",cascade = CascadeType.REMOVE)
    private List<ManagersOfPackages> managersOfPackage;

    @JsonManagedReference
    @OneToMany(mappedBy = "aPackage" )
    private List<ServicesOfPackage> servicesOfPackage ;

    @JsonManagedReference
    @OneToMany(mappedBy = "aPackage" )
    private List<PackagesOfOrder> packagesOfOrder ;

    @JsonManagedReference
    @OneToMany(mappedBy = "aPackage" ,cascade = CascadeType.REMOVE)
    private List<PackagesOfBranches> packagesOfBranch;

}


