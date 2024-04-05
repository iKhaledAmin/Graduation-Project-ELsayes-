package com.GP.ELsayes.utils;

import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.GP.ELsayes.model.enums.roles.WorkerRole;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorkerStorage {
    private HashMap<Long, Worker> workerById;
    private HashMap<Long, List<Worker>> workersByBranchId;
    private HashMap<WorkerRole, HashMap<Long, Worker>> workersByRole;

    public WorkerStorage() {
        workerById = new HashMap<>();
        workersByBranchId = new HashMap<>();
        workersByRole = new HashMap<>();
    }


    public void addWorker(Worker worker) {
        workerById.put(worker.getId(), worker);

        // Assuming getBranch() returns the Branch object and getId() returns the branch_id.
        workersByBranchId.computeIfAbsent(worker.getBranch().getId(), k -> new ArrayList<>()).add(worker);
    }
    public void printAllWorkers() {
        for (Worker worker : workerById.values()) {
            System.out.println(worker);
        }
    }

    public Worker getWorkerById(Long workerId) {
        return workerById.get(workerId);
    }

    public List<Worker> getWorkersByBranchId(Long branchId) {
        return workersByBranchId.getOrDefault(branchId, new ArrayList<>());
    }

    public void removeWorker(Long workerId) {
        Worker worker = workerById.remove(workerId);
        if (worker != null && worker.getBranch() != null) {
            List<Worker> workersList = workersByBranchId.get(worker.getBranch().getId());
            workersList.remove(worker);
            // Clean up the list from the map if it's empty
            if (workersList.isEmpty()) {
                workersByBranchId.remove(worker.getBranch().getId());
            }
        }
    }






    // New method to add workers by role
    public void addWorkersByRole(WorkerRole role, HashMap<Long, Worker> workers) {
        workersByRole.put(role, workers);
        for (Worker worker : workers.values()) {
            addWorker(worker); // Use the existing method to add individual workers
        }
    }

    // New method to get available workers by role
    public HashMap<Long, Worker> getAvailableWorkersByRole(WorkerRole role) {
        return workersByRole.getOrDefault(role, new HashMap<>());
    }

    // New method to remove worker by role and ID
    public void removeWorkerByRoleAndId(WorkerRole role, Long workerId) {
        HashMap<Long, Worker> workers = workersByRole.get(role);
        if (workers != null) {
            workers.remove(workerId);
        }
        removeWorker(workerId); // Use the existing method to remove the worker
    }
}
