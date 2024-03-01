package com.GP.ELsayes.model.entity.SystemUsers.userChildren;


import com.GP.ELsayes.model.entity.Car;
import com.GP.ELsayes.model.entity.FreeTrialCode;
import com.GP.ELsayes.model.entity.SystemUsers.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor

//@Where(clause = "deleted_at is null")

@Entity(name = "customer")
@Table(name = "customer")
@PrimaryKeyJoinColumn(name = "customer_id")
public class Customer extends User {
    @Id
    private Long id;
    private Date dateOfJoining;

    @JsonManagedReference
    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE)
    private List<Car> cars;


    @JsonManagedReference
    @OneToOne(mappedBy = "customer")
    private FreeTrialCode customerFreeTrialCode;


}
