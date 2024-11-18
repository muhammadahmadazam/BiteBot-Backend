package com.xcelerate.cafeManagementSystem.Service;

import java.time.LocalDateTime;
import java.util.List;

import com.xcelerate.cafeManagementSystem.Model.Customer;
import com.xcelerate.cafeManagementSystem.Model.User;
import com.xcelerate.cafeManagementSystem.Repository.CustomerRepository;
import com.xcelerate.cafeManagementSystem.Repository.UserRepository;
import com.xcelerate.cafeManagementSystem.Utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;


@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    /*
    * This method is used to create a new user
    * @param email
    * @param password
    * @param name
    * @param phone
    * @return the customer object
     */
    public Customer createUser(String email, String password, String name, String phone) {
        String hashPassword = PasswordUtil.hashPassword(password);
        User user = new User(email, hashPassword);
        userRepository.save(user);

        Customer customer = new Customer(name, phone, user);

        return customerRepository.save(customer);
    }

    /*
    * This method is used to verify the user's account
    * @param email
    * @return the customer object
     */
    public Customer verifyUser(String email) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        customer.setVerified(true);
        return customerRepository.save(customer);
    }

    /*
    * This method is used to check if the customer is verified or not
    * @param email
    * @return 0 if the customer does not exist, 1 if the customer is not verified, 2 if the customer is verified
    */
    public int isCustomerVerified(String email) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        if(customer == null) {
            return 0;
        }
        return customer.getVerified() ? 2 : 1;
    }

//    public boolean loginUser(String email, String password) {
//        Customer customer = customerRepository.findByEmail(email);
//        if(customer == null) {
//            return false;
//        }
//        return PasswordUtil.verifyPassword(password, customer.getPassword());
//    }

}



