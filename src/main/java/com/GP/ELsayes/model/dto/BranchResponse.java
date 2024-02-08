package com.GP.ELsayes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BranchResponse {
    private Long id;
    private String name;
    private String location;
    private String capacityOfCars;

}
