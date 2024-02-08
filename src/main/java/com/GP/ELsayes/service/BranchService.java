package com.GP.ELsayes.service;

import com.GP.ELsayes.model.dto.BranchRequest;
import com.GP.ELsayes.model.dto.BranchResponse;
import com.GP.ELsayes.model.entity.Branch;
import org.springframework.stereotype.Service;

@Service
public interface BranchService extends CrudService<BranchRequest, Branch, BranchResponse,Long>{
}
