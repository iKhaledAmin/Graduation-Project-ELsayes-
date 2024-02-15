package com.GP.ELsayes.service;

import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerResponse;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import org.springframework.stereotype.Service;

@Service
public interface WorkerService extends CrudService<WorkerRequest, Worker, WorkerResponse,Long> {
}
