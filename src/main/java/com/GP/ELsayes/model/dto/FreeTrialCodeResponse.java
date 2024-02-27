package com.GP.ELsayes.model.dto;

import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FreeTrialCodeResponse {

    private Long id;
    private String code;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateOfGenerate;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateOfUsing;
    private Long workerId;
    private Long customerId;
}
