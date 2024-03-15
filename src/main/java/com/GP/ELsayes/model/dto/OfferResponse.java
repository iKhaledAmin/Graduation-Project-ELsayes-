package com.GP.ELsayes.model.dto;



import com.GP.ELsayes.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferResponse {

    private Long id;
    private String name;
    private String description;
    private String offerImageURL;
    @JsonIgnore
    private String percentageOfDiscount;
    private String originalTotalPrice;
    private String originalTotalRequiredTime;
    private String actualOfferPrice;
    private Status offerStatus;
    private String profit;


}