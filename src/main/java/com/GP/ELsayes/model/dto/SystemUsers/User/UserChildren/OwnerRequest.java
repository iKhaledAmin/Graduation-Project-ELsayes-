package com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren;


import com.GP.ELsayes.model.dto.SystemUsers.User.UserRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;



@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class OwnerRequest extends UserRequest {

    @NotNull(message = "Percentage Must Not Be Null")
    @NotEmpty(message = "Percentage Must Not Be Empty")
    @Positive(message = "percentage must be greater than 0")
    private String percentage;

    private Long oldOwnerId;

}
