package com.GP.ELsayes.model.entity.SystemUsers;

import com.GP.ELsayes.model.enums.UserGender;
import com.GP.ELsayes.model.enums.roles.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.experimental.SuperBuilder;

import java.util.Date;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor

//@Where(clause = "deleted_at is null")

@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)

public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String userName;
    private String password;
    private String email;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private String image;
    private String phoneNumber;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthday;





    @Enumerated(EnumType.STRING)
    private UserGender gender ;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;




//    @JsonManagedReference
//    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
//    private Notification notification;

}
