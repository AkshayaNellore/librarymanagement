package com.cts.lms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.cts.lms.dto.AuthRequest;
import com.cts.lms.dto.UserDto;
import com.cts.lms.exception.AdminAlreadyExists;
import com.cts.lms.exception.EmailAlreadyExists;
import com.cts.lms.exception.PhoneAlreadyExists;
import com.cts.lms.exception.UserNameAlreadyExists;
import com.cts.lms.exception.UserNotFoundException;
import com.cts.lms.entity.User;
import com.cts.lms.entity.UserPrincipal;
import com.cts.lms.enums.Role;
import com.cts.lms.repository.UserRepository;

@Service
public class UserService implements UserDetailsService{
    @Autowired
    UserRepository userRepository;
    
    @Autowired
	private JWTService jwtService;
    
    @Lazy
	@Autowired
	private AuthenticationManager authManager;
 
	@Lazy
	@Autowired
	private PasswordEncoder passwordEncoder;
    
    
    
    public String verify(String email, String password, String role) {
	    Authentication authentication = authManager
	            .authenticate(new UsernamePasswordAuthenticationToken(email, password));
	    
	    if (!authentication.isAuthenticated()) {
	        throw new UserNotFoundException("Invalid Login Credentials!");
	    }
	    User authUser = userRepository.findByEmail(email);
	  
	    if (!authUser.getUserType().equals(role)) {
	        throw new UserNotFoundException("Forbidden! You don't have access!");
	    }
	    
	    return jwtService.getToken(authUser.getMemberId(), role.toString(), email);
	}
    
    @Override
	public UserDetails loadUserByUsername(String useremail) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(useremail);
 
		if(user == null) {
			throw new UsernameNotFoundException("User Not Found....");
		}
		
		return new UserPrincipal(user);
	}
    
    

    

    public void registerUser(UserDto userDto) {

        Optional<User> userOptional = userRepository.findByUsername(userDto.getUsername());
        if (userOptional.isPresent()) {
            throw new UserNameAlreadyExists("Username already exists");
        }

        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new EmailAlreadyExists("Email already exists");
        }

        if (userRepository.findByPhone(userDto.getPhone()) != null) {
            throw new PhoneAlreadyExists("Phone number already exists");
        }	
        
        
        
        if(userDto.getUserType().toString().equals("ADMIN") ){
        	
        	Optional<User> existingAdmin = userRepository.findByRole("ADMIN");
        	
        	if(!existingAdmin.isEmpty()) {
        
 
        	throw new AdminAlreadyExists("Admin already exists");
        	}
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setPassword(passwordEncoder.encode(userDto.getPassword())); // Encrypting the password
        user.setAddress(userDto.getAddress());
        user.setUserType(userDto.getUserType());
        user.setMembershipStatus(userDto.getMembershipStatus());
        userRepository.save(user);
    }

    public  Optional<UserDto> authenticateUser(@RequestBody AuthRequest authRequest) {
        User user = userRepository.findByEmail(authRequest.getEmail());
        UserDto userDto = new UserDto();
         if(user != null && passwordEncoder.matches(authRequest.getPassword(), user.getPassword()) && user.getUserType().toString().equals(authRequest.getUserType()) ) {
        	 	
	            userDto.setName(user.getName());
	            userDto.setUsername(user.getUsername());
	            userDto.setEmail(user.getEmail());
	            userDto.setAddress(user.getAddress());
	            userDto.setPhone(user.getPhone());
	            userDto.setPassword(user.getPassword());
	            userDto.setMemberId(user.getMemberId());
	            userDto.setMembershipStatus(user.getMembershipStatus());
	            userDto.setUserType(user.getUserType());
         }
         else {
        	 throw new UserNotFoundException("Invalid User Credentials");
         }
         return Optional.ofNullable(userDto); 
         
    }

    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> dtoUsers = new ArrayList<>();
        for (User user : users) {
	            UserDto userDto = new UserDto();
	            userDto.setName(user.getName());
	            userDto.setUsername(user.getUsername());
	            userDto.setEmail(user.getEmail());
	            userDto.setAddress(user.getAddress());
	            userDto.setPhone(user.getPhone());
	            userDto.setPassword(user.getPassword());
	            userDto.setMemberId(user.getMemberId());
	            userDto.setMembershipStatus(user.getMembershipStatus());
	            userDto.setUserType(user.getUserType());
	            dtoUsers.add(userDto);
        }
        return dtoUsers;
    }

    public UserDto findUser(@PathVariable String memberId) {
        Optional<User> userOptional = userRepository.findByMemberId(memberId);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("User not found");
        }

        User user = userOptional.get();
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setAddress(user.getAddress());
        userDto.setPhone(user.getPhone());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setMemberId(user.getMemberId());
        userDto.setMembershipStatus(user.getMembershipStatus());
        userDto.setUserType(user.getUserType());
        return userDto;
    }

    public void updateUser(UserDto userDto) {

        Optional<User> userOptional = userRepository.findByMemberId(userDto.getMemberId());
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("User not found with Id: " + userDto.getMemberId());
        }

        User user = userOptional.get();

        if (userDto.getAddress() != null) {
            user.setAddress(userDto.getAddress());
        }
        if (userDto.getEmail() != null) {
            User existingUser = userRepository.findByEmail(userDto.getEmail());
            if (existingUser != null && !existingUser.getMemberId().equals(userDto.getMemberId())) {
                throw new EmailAlreadyExists("Email already exists");
            }
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getMembershipStatus() != null) {
            user.setMembershipStatus(userDto.getMembershipStatus());
        }
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword())); // Encrypting the password
        }
        if (userDto.getPhone() != null) {
            User existingUser = userRepository.findByPhone(userDto.getPhone());
            if (existingUser != null && !existingUser.getMemberId().equals(userDto.getMemberId())) {
                throw new PhoneAlreadyExists("Phone number already exists");
            }
            user.setPhone(userDto.getPhone());
        }

        userRepository.save(user);
    }

    public void deleteUser(String memberId) {
        Optional<User> userOptional = userRepository.findByMemberId(memberId);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("User not found with Id: " + memberId);
        }
        userRepository.deleteById(memberId);
    }

    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User not found with email: " + email);
        }
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setAddress(user.getAddress());
        userDto.setPhone(user.getPhone());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setMemberId(user.getMemberId());
        userDto.setMembershipStatus(user.getMembershipStatus());
        userDto.setUserType(user.getUserType());
        return userDto;
    }
}