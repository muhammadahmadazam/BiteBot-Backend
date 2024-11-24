package com.xcelerate.cafeManagementSystem.Repository;

import com.xcelerate.cafeManagementSystem.Model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerRespository extends JpaRepository<Worker, Long> {
}
