package com.xcelerate.cafeManagementSystem.Service;

import com.xcelerate.cafeManagementSystem.DTOs.Worker_Create_DTO;
import com.xcelerate.cafeManagementSystem.DTOs.Worker_Update_DTO;
import com.xcelerate.cafeManagementSystem.Model.Worker;
import com.xcelerate.cafeManagementSystem.Repository.WorkerRespository;
import com.xcelerate.cafeManagementSystem.Utils.PasswordUtil;
import jdk.dynalink.linker.LinkerServices;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        worker.setPassword(PasswordUtil.hashPassword(workerDTO.getPassword()));
        worker.setSalary(workerDTO.getSalary());
        worker.setJoinDate(new Date());
        worker.setRole("WORKER");
        return workerRepository.save(worker);
    }

    public boolean existsById(long workerId) {
        Worker worker = workerRepository.findById(workerId).orElse(null);
        if (worker == null) {
            return false;
        }else
        {
            return true;
        }
    }

    public Worker findByEmail(String email) {
        Optional<Worker> w =  workerRepository.findByEmail(email);
        return w.orElse(null);
    }

    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    @Transactional
    public boolean deleteWorker(long workerId) {
        Worker w = workerRepository.findById(workerId).orElse(null);
        if (w != null) {
            workerRepository.delete(w);
            return true;
        }else{
            return false;
        }
    }

    @Transactional
    public boolean updateWorker(Worker_Update_DTO workerDTO) {
        Worker w = workerRepository.findById(workerDTO.getWorkerId()).orElse(null);
        if (w != null) {
            w.setEmail(workerDTO.getEmail());
            w.setPassword(PasswordUtil.hashPassword(workerDTO.getPassword()));
            w.setSalary(workerDTO.getSalary());
            w.setPosition(workerDTO.getPosition());
            workerRepository.save(w);
            return true;
        }else{
            return false;
        }
    }
}
