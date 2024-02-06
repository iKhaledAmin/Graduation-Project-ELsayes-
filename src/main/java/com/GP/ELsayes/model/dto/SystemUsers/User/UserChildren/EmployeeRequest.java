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
public class EmployeeRequest extends UserRequest {

    @NotNull(message = "Base Salary Must Not Be Null")
    @NotEmpty(message = "Base Salary Must Not Be Empty")
    @Positive(message = "Base Salary must be greater than 0")
    private String baseSalary;

}
