package com.GP.ELsayes.model.dto.SystemUsers.User;

import com.GP.ELsayes.model.enums.UserGender;
import com.GP.ELsayes.model.enums.roles.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    private String password;
    private String email;
    private String profileImageURL;
    private String phoneNumber;
    private Date birthday;
    private UserGender gender ;
    private UserRole userRole;


}
