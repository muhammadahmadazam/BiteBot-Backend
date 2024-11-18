package com.xcelerate.cafeManagementSystem.Service;

import com.xcelerate.cafeManagementSystem.Enums.UserRoles;
import com.xcelerate.cafeManagementSystem.Model.Customer;
import com.xcelerate.cafeManagementSystem.Model.User;
import com.xcelerate.cafeManagementSystem.Repository.CustomerRepository;
import com.xcelerate.cafeManagementSystem.Utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CustomerService {
    private final CustomerRepository customerRepository;


    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;

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

        Customer customer = new Customer(name, phone, email, hashPassword, UserRoles.CUSTOMER);

        return customerRepository.save(customer);
    }

    /*
    * This method is used to verify the user's account
    * @param email
    * @return the customer object
     */
    public Customer verifyUser(String email) {
        Customer customer = customerRepository.findByEmail(email);
        customer.setVerified(true);
        return customerRepository.save(customer);
    }

    /*
    * This method is used to check if the customer is verified or not
    * @param email
    * @return 0 if the customer does not exist, 1 if the customer is not verified, 2 if the customer is verified
    */
    public int isCustomerVerified(String email) {
        Customer customer = customerRepository.findByEmail(email);
        if(customer == null) {
            return 0;
        }
        return customer.getVerified() ? 2 : 1;
    }

}



