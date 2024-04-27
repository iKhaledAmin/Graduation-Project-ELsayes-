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
public class EditUserProfileRequest {


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

    @NotNull(message = "Password must not be null")
    @NotEmpty(message = "Password must not be empty")
    @Size(max = 20, min = 8, message = "student_password Must Be Between 8 and 20 character.")
    private String password;

    @Email(message = "Invalid email address")
    private String email;

    private String image;

    @Pattern(regexp="(^$|[0-9]{11})" , message = "Invalid phone number")
    private String phoneNumber;

    private Date birthday;

    private UserGender gender ;



}
