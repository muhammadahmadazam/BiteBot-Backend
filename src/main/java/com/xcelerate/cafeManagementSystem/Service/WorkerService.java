package com.xcelerate.cafeManagementSystem.Service;

import com.xcelerate.cafeManagementSystem.DTOs.Worker_Create_DTO;
import com.xcelerate.cafeManagementSystem.Model.Worker;
import com.xcelerate.cafeManagementSystem.Repository.WorkerRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class WorkerService {
    private final WorkerRespository workerRepository;

    @Autowired
    public WorkerService(WorkerRespository workerRepository) {
        this.workerRepository = workerRepository;
    }

    public Worker createWorker(Worker_Create_DTO workerDTO) {
        Worker worker = new Worker();
        worker.setPosition(workerDTO.getPosition());
        worker.setEmail(workerDTO.getEmail());
        worker.setPassword(workerDTO.getPassword());
        worker.setSalary(workerDTO.getSalary());
        worker.setJoinDate(new Date());
        worker.setRole("WORKER");
        return workerRepository.save(worker);
    }
}
