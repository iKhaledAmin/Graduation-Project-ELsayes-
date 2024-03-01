package com.GP.ELsayes.service;

import com.GP.ELsayes.model.dto.FreeTrialCodeRequest;
import com.GP.ELsayes.model.dto.FreeTrialCodeResponse;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.FreeTrialCode;
import org.springframework.stereotype.Service;

@Service
public interface FreeTrialCodeService
        extends CrudService<FreeTrialCodeRequest,FreeTrialCode, FreeTrialCodeResponse,Long> {
    public FreeTrialCode getByWorkerId(Long workerId);
    public FreeTrialCode getByCode(String code);
    public FreeTrialCodeResponse generateCode(Long workerId);
    public FreeTrialCodeResponse applyCode(Long customerId , String code);
}
