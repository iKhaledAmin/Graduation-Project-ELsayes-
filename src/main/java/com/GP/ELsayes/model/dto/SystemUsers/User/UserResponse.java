package com.GP.ELsayes.model.dto.SystemUsers.User;

import com.GP.ELsayes.model.enums.UserGender;
import com.GP.ELsayes.model.enums.roles.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;
    private String image;
    private String phoneNumber;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthday;
    private UserGender gender ;
    private UserRole userRole;


}
