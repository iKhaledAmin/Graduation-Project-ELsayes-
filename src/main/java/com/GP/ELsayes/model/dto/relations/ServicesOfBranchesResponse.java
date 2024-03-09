package com.GP.ELsayes.model.dto.relations;

import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicesOfBranchesResponse {


    private String serviceName;
    private String  branchName;
    Status serviceStatus;

    @JsonFormat(pattern="yyyy-MM-dd")
    Date addingDate;
}
