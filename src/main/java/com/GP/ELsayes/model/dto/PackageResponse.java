package com.GP.ELsayes.model.dto;



import com.GP.ELsayes.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackageResponse {

    private Long id;
    private String name;
    private String description;
    private String packageImageURL;
    @JsonIgnore
    private String percentageOfDiscount;
    private String originalTotalPrice;
    private String originalTotalRequiredTime;
    private String currentPackagePrice;
    private Status packageStatus;
    private String profit;







}