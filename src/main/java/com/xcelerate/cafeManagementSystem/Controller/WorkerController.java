package com.xcelerate.cafeManagementSystem.Controller;

import com.xcelerate.cafeManagementSystem.DTOs.Worker_Create_DTO;
import com.xcelerate.cafeManagementSystem.DTOs.Worker_Response_DTO;
import com.xcelerate.cafeManagementSystem.Model.Worker;
import com.xcelerate.cafeManagementSystem.Service.WorkerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin(origins = "${frontendURL}")
@RequestMapping("/api/")
public class WorkerController {

    WorkerService workerService;
    WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @PostMapping("/worker/create")
    ResponseEntity<Worker_Response_DTO> createWorker(@RequestBody Worker_Create_DTO worker) {
        Worker w = workerService.createWorker(worker);
        Worker_Response_DTO response = new Worker_Response_DTO(w);
        return  new ResponseEntity<Worker_Response_DTO>(response, HttpStatus.OK);
    }


}
