package com.xcelerate.cafeManagementSystem.Controller;

import com.xcelerate.cafeManagementSystem.DTOs.*;
import com.xcelerate.cafeManagementSystem.Model.DeliveryMan;
import com.xcelerate.cafeManagementSystem.Model.User;
import com.xcelerate.cafeManagementSystem.Model.UserFactory;
import com.xcelerate.cafeManagementSystem.Model.Worker;
import com.xcelerate.cafeManagementSystem.Service.WorkerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin(origins = "${frontendURL}")
@RequestMapping("/api/")
public class WorkerController {

    WorkerService workerService;
    WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @PostMapping("/worker/create")
    ResponseEntity<Worker_Response_DTO> createWorker(@RequestBody @Valid Worker_Create_DTO worker) {
        Worker w = workerService.createWorker(worker);
        Worker_Response_DTO response = new Worker_Response_DTO(w);
        return new ResponseEntity<Worker_Response_DTO>(response, HttpStatus.OK);
    }

    @GetMapping("/workers/get/all")
    ResponseEntity<List<Worker_Response_DTO>> getWorkers() {
        List<Worker_Response_DTO> workers = workerService.getAllWorkers().stream().map(Worker_Response_DTO::new).toList();
        return new ResponseEntity<>(workers, HttpStatus.OK);
    }

    @PostMapping("/worker/delete")
    ResponseEntity<ApiResponseDTO<String>> deleteWorker(@RequestBody @Valid Worker_Delete_DTO worker) {

        System.out.println("workerId: " + worker.getWorkerId());
//        if (workerService.existsById(worker.getWorkedId())){
            System.out.println("deleting worker");
            if (workerService.deleteWorker(worker.getWorkerId())){
                ApiResponseDTO<String> response = new ApiResponseDTO<>();
                response.message = "Worker deleted successfully";
                response.data = "WORKER_DELETED";
                return new ResponseEntity<>(response,HttpStatus.OK);
            }else{
                ApiResponseDTO<String> response = new ApiResponseDTO<>();
                response.message = "Failed to delete worker with id: " + worker.getWorkerId();
                response.data = "";
                return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
            }

//        }else{
//            ApiResponseDTO<String> response = new ApiResponseDTO<>();
//            response.message = "Failed to delete worker";
//            response.data = "";
//            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
//        }
    }


    @PostMapping("/worker/update")
    ResponseEntity<ApiResponseDTO<String>> updateWorker(@RequestBody @Valid Worker_Update_DTO worker) {
        ApiResponseDTO<String> response = new ApiResponseDTO<>();
        if (workerService.updateWorker(worker)){
            response.message = "Worker updated successfully";
            response.data = "WORKER_UPDATED";
            return new ResponseEntity<>(response,HttpStatus.OK);
        }else{
            response.message = "Failed to update worker with id: " + worker.getWorkerId();
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }

    }
}
