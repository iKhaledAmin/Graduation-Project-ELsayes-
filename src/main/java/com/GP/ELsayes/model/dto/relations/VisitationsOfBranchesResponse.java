package com.GP.ELsayes.model.dto.relations;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitationsOfBranchesResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String firstName;
    private String lastName;

    private String carPlateNumber;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateOfArriving;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateOfLeaving;
    private String period;
}
