package com.GP.ELsayes.model.dto.relations;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicesOfPackagesResponse {

    private String serviceName;
    private String packageName;
    @JsonFormat(pattern="yyyy-MM-dd")
    Date addingDate;
}
