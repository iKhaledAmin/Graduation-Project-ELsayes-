package com.GP.ELsayes.model.dto.SystemUsers.User;

import com.GP.ELsayes.model.enums.UserGender;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserRequest {


    @NotNull(message = "First Name Must Not Be Null")
    @NotEmpty(message = "First Name Must Not Be Empty")
    @Size(min = 3 ,message = "First name Must be more than 3 letters")
    @Size(max = 10 ,message = "First name Must be less than 10 letters")
    private String firstName;

    @NotNull(message = "Last Name Must Not Be Null")
    @NotEmpty(message = "Last Name Must Not Be Empty")
    @Size(min = 3 ,message = "Last name Must be more than 3 letters")
    @Size(max = 10 ,message = "Last name Must be less than 10 letters")
    private String lastName;

    @NotNull(message = "User-name Must Not Be Null")
    @NotEmpty(message = "User-name Must Not Be Empty")
    private String userName;

    @Pattern(regexp="(^$|[0-9]{11})" , message = "Invalid Phone Number")
    private String phoneNumber;

    @NotNull(message = "Email name must not be null")
    @NotEmpty(message = "Email name must not be empty")
    @Email(message = "Invalid email address")
    private String email;

    @NotNull (message = "Birthday must not be null")
    private Date birthday;

    @NotNull (message = "Gender must not be null")
    private UserGender gender ;


}
