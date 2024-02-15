package com.GP.ELsayes.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BranchRequest {

    @NotNull(message = "Name must not be null")
    @NotEmpty(message = "Name must not be empty")
    private String name;

    @NotNull(message = "Location must not be null")
    @NotEmpty(message = "Location must not be empty")
    private String location;

    @NotNull(message = "Capacity of cars must not be null")
    @NotEmpty(message = "Capacity of cars must not be empty")
    private String capacityOfCars;
}
