package com.xcelerate.cafeManagementSystem.Service;

import com.xcelerate.cafeManagementSystem.DTOs.Admin_Create_DTO;
import com.xcelerate.cafeManagementSystem.Model.Admin;
import com.xcelerate.cafeManagementSystem.Repository.AdminRepository;
import com.xcelerate.cafeManagementSystem.Utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {
    AdminRepository adminRepository;

    @Value("${CAFE_SECRET_KEY}")
    private String cafeSecretkey;
    @Autowired
    AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public Admin createAdmin(Admin_Create_DTO adminDTO) {
        if (adminDTO.getCafeSecretKey().equals(cafeSecretkey)) {
            Admin admin = new Admin();
            admin.setName(adminDTO.getName());
            admin.setPassword(PasswordUtil.hashPassword(adminDTO.getPassword()));
            admin.setEmail(adminDTO.getEmail());
            admin.setRole("ADMIN");
            admin.setPhoneNumber(adminDTO.getPhoneNumber());
            return adminRepository.save(admin);
        }else{
            return null;
        }
    }


}
