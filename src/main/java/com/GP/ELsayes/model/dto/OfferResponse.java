package com.GP.ELsayes.model.dto;



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
    private String offerPercentage;

}