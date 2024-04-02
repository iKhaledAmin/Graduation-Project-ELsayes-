package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;

import java.util.HashMap;
import java.util.Map;

public class WorkerManager {
    private Map<Long, Worker> workerMap;

    public WorkerManager() {
        workerMap = new HashMap<>();
    }

    public void addWorker(Worker worker) {
        workerMap.put(worker.getId(), worker);
    }

    public Worker getWorkerById(Long id) {
        return workerMap.get(id);
    }

    public void removeWorker(Long id) {
        workerMap.remove(id);
    }
}
