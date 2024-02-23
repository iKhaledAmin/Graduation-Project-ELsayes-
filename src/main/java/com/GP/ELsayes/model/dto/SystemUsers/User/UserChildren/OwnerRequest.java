package com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren;


import com.GP.ELsayes.model.dto.SystemUsers.User.UserRequest;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.lang.String;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class OwnerRequest extends UserRequest {

    @NotNull(message = "Percentage must not be null")
    @NotEmpty(message = "Percentage must mot be empty")
    @DecimalMin(value = "0.1",message = "Percentage must be greater than 0")
    @DecimalMax(value = "99",message = "Percentage must be less than 100")
    private String percentage;

    private Long oldOwnerId;

}
