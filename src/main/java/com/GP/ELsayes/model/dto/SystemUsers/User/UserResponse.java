package com.GP.ELsayes.model.dto.SystemUsers.User;

import com.GP.ELsayes.model.enums.UserGender;
import com.GP.ELsayes.model.enums.roles.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
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
    private String phoneNumber;
    private String email;
    private Date birthday;
    private UserGender gender ;
    private UserRole userRole;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
