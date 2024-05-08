package com.restaurant.service.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.restaurant.service.dtos.CustomerDTO;
import com.restaurant.service.entities.Customer;
import com.restaurant.service.entities.ShipAddress;
import com.restaurant.service.enums.AuthenticationType;
import com.restaurant.service.exceptions.CustomerNotFoundException;
import com.restaurant.service.exceptions.UserNotFoundException;
import com.restaurant.service.repositories.CustomerRepository;
import com.restaurant.service.repositories.ShipAddressRepository;

import io.micrometer.common.lang.NonNull;
import jakarta.transaction.Transactional;
import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepo;
	@Autowired
	private ShipAddressRepository shipAddressRepo;

	@Autowired
	PasswordEncoder passwordEncoder;

	public boolean isEmailUnique(String email) {
		Customer customer = customerRepo.findByEmail(email);
		return customer == null;
	}
	
	public Customer findByEmail(String email) {
        return customerRepo.findByEmail(email);
    }
	
	public void updateCustomer(Customer customer) {
	 
		customerRepo.save(customer);
	}
	public int calculateCustomerPoints(String email) {
        Customer customer = customerRepo.findByEmail(email);
        if (customer != null) {
            return customer.getPoints(); 
        }
        return 0; 
    }
	
	public void updatePointsByEmail(String email, double total) {
        Customer customer = customerRepo.findByEmail(email);
        if (customer != null) {
            int newPoints = (int) (total / 10); 
            
            Integer currentPoints = customer.getPoints();
            if (currentPoints == null) {
                currentPoints = 0;
            }
            
          
            customer.setPoints(currentPoints + newPoints);
            customerRepo.save(customer);
        }
    }
	
	public Customer CreateStaff(CustomerDTO customerDTO) {
		String encodedPassword = passwordEncoder.encode(customerDTO.getPassword());
	   Customer customer = new Customer();
	   customer.setFullName(customerDTO.getFullName());
	   customer.setPassword(encodedPassword);
	   customer.setEmail(customerDTO.getEmail());
	   customer.setEnabled(customerDTO.isEnabled());
	   customer.setPhoto(customerDTO.getPhoto());
	   customer.setGender(customerDTO.getGender());
	   customer.setStaff(customerDTO.isStaff());
	   customer.setPhoneNumber(customerDTO.getPhoneNumber());
	   customer.setAddress(customerDTO.getAddress());
	   
	  return customerRepo.save(customer);
	}
	
	public Customer get(Long id) throws UserNotFoundException {
		try {
			return customerRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}
	}
	
	

	public Customer createCustomer(Customer customer, ShipAddress shipAddress) {
		shipAddress.setCustomer(customer);
		ShipAddress savedShipAddress = shipAddressRepo.save(shipAddress);
		
		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);
		customer.setEnabled(true);
		customer.setEmailVerified(false);
		customer.setPhoneVerified(false);
		customer.setAuthenticationType(AuthenticationType.DATABASE);

		String randomCode = RandomString.make(64);
		customer.setVerificationCode(randomCode);

		return customerRepo.save(customer);
	}
	
	public Customer registerCustomer(Customer customer) {
		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);
		customer.setEnabled(true);
		customer.setEmailVerified(false);
		customer.setPhoneVerified(false);
		customer.setAuthenticationType(AuthenticationType.DATABASE);

		String randomCode = RandomString.make(64);
		customer.setVerificationCode(randomCode);

		return customerRepo.save(customer);
	}

	
	//TODO: Verifying
	public void registerCustomerOAuth2(String name, String email,
			AuthenticationType authenticationType) {
		Customer customer = new Customer();
		customer.setEmail(email);
		customer.setFullName(name);

		customer.setEnabled(true);
		customer.setAuthenticationType(authenticationType);
		customer.setPassword("");

		customerRepo.save(customer);
	}
	
	public List<Customer> findAllStaffCustomers() {
        return customerRepo.findAllStaffCustomers();
    }
	
	public List<Customer> findAllCustomers() {
        return customerRepo.findAllCustomers();
    }

	public Customer getCustomerByEmail(String email) {
		return customerRepo.findByEmail(email);
	}
	
	public ShipAddress getDefaultShipAddress(String email) {
		return shipAddressRepo.findDefaultShipAddrress(email);
	}

	public boolean verify(String verificationCode) {
		Customer customer = customerRepo.findByVerificationCode(verificationCode);

		if (customer == null || customer.isEnabled()) {
			return false;
		} else {
			customerRepo.enable(customer.getId());
			return true;
		}
	}

	public void updateAuthenticationType(Customer customer, AuthenticationType type) {
		if (!customer.getAuthenticationType().equals(type)) {
			customerRepo.updateAuthenticationType(customer.getId(), type);
		}
	}

	public void update(@NonNull Customer customerForm) {
		Customer customer = customerRepo.findById(customerForm.getId()).get();

		if (customer.getAuthenticationType().equals(AuthenticationType.DATABASE)) {
			if (!customerForm.getPassword().isEmpty()) {
				// String encodedPassword = passwordEncoder.encode(customerForm.getPassword());
				// customerForm.setPassword(encodedPassword);
			} else {
				customerForm.setPassword(customer.getPassword());
			}
		} else {
			customerForm.setPassword(customer.getPassword());
		}

		customerForm.setEnabled(customer.isEnabled());
		customerForm.setVerificationCode(customer.getVerificationCode());
		customerForm.setAuthenticationType(customer.getAuthenticationType());
		customerForm.setResetPasswordToken(customer.getResetPasswordToken());

		customerRepo.save(customerForm);
	}

	public String updateResetPasswordToken(String email) throws CustomerNotFoundException {
		Customer customer = customerRepo.findByEmail(email);
		if (customer != null) {
			String token = RandomString.make(30);
			customer.setResetPasswordToken(token);
			customerRepo.save(customer);

			return token;
		} else {
			throw new CustomerNotFoundException("Could not find any customer with the email " + email);
		}
	}

	public Customer getByResetPasswordToken(String token) {
		return customerRepo.findByResetPasswordToken(token);
	}

	public void updatePassword(String token, String newPassword) throws CustomerNotFoundException {
		Customer customer = customerRepo.findByResetPasswordToken(token);
		if (customer == null) {
			throw new CustomerNotFoundException("No customer found: invalid token");
		}

		customer.setPassword(newPassword);
		customer.setResetPasswordToken(null);
		// encodePassword(customer);

		customerRepo.save(customer);
	}


}
