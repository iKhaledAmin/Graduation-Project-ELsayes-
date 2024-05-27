package com.GP.ELsayes.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackageRequest {


    @NotNull(message = "Name must not be null")
    @NotEmpty(message = "Name must not be empty")
    private String name;

    @NotNull(message = "Description must not be null")
    @NotEmpty(message = "Description must not be empty")
    private String description;

    @NotNull(message = "Image must not be null")
    @NotEmpty(message = "Image must not be empty")
    private String image;

    @NotNull(message = "Percentage of discount must not be null")
    @NotEmpty(message = "Percentage of discount must not be empty")
    private String percentageOfDiscount;

    List<Long> serviceIds;

    private String originalTotalPrice;
    private String originalTotalRequiredTime;
    private String currentPackagePrice;
    private String currentPackageRequiredTime;

    @NotNull(message = "Manager id must not be null")
    private Long managerId;





}