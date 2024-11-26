package com.xcelerate.cafeManagementSystem.Service;

import com.xcelerate.cafeManagementSystem.Enums.UserRoles;
import com.xcelerate.cafeManagementSystem.Model.Customer;
import com.xcelerate.cafeManagementSystem.Model.User;
import com.xcelerate.cafeManagementSystem.Repository.CustomerRepository;
import com.xcelerate.cafeManagementSystem.Repository.OrderRepository;
import com.xcelerate.cafeManagementSystem.Utils.EmailUtil;
import com.xcelerate.cafeManagementSystem.Utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;


    @Autowired
    public CustomerService(CustomerRepository customerRepository, OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    /*
    * This method is used to create a new user
    * @param email
    * @param password
    * @param name
    * @param phone
    * @return the customer object
     */
    @Transactional(rollbackFor = Exception.class)
    public Customer createUser(String email, String password, String name, String phone) {

        if(email.isEmpty() || password.isEmpty() || name.isEmpty()  || phone.isEmpty()) {
            return null;
        }

        if(EmailUtil.verifyEmailFormat(email) == false) {
            return null;
        }

        if(customerRepository.findByEmail(email) != null) {
            return null;
        }

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


    @Transactional(rollbackFor = Exception.class)
    public Customer getCustomerByEmail(String email) {
        Optional<Customer> customer = customerRepository.findByEmailIgnoreCase(email);
        return customer.orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    public Customer getCustomerById(long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.orElse(null);
    }
}



