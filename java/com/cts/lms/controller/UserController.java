package com.cts.lms.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.lms.dto.ApiResponse;
import com.cts.lms.dto.UserDto;
import com.cts.lms.dto.AuthRequest;
import com.cts.lms.exception.AdminAlreadyExists;
import com.cts.lms.exception.EmailAlreadyExists;
import com.cts.lms.exception.PhoneAlreadyExists;
import com.cts.lms.exception.UserNameAlreadyExists;
import com.cts.lms.exception.UserNotFoundException;
import com.cts.lms.service.UserService;

import jakarta.validation.Valid;
@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> signUp(@Valid @RequestBody UserDto userDto, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, result.getAllErrors().get(0).getDefaultMessage(), null));
            }
            userService.registerUser(userDto);
            return ResponseEntity.ok(new ApiResponse<>(true, "User registered successfully", null));
        } catch (EmailAlreadyExists | PhoneAlreadyExists | UserNameAlreadyExists | AdminAlreadyExists e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> logIn(@RequestBody AuthRequest authRequest) {
        try {
            String token = userService.verify(authRequest.getEmail(),authRequest.getPassword(),authRequest.getUserType());  
            	return ResponseEntity.ok (new ApiResponse<>(true,token , null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false,e.getMessage(),null));
        }
    }
    
    @GetMapping("/admin/allusers")
    public List<UserDto> allUsers(){
    	return userService.getUsers();
    }
    
    @GetMapping("/user/{email}")
    public UserDto findByMail(@PathVariable String email) {
        try {
            return userService.findByEmail(email);
        } catch (UserNotFoundException e) {
            // Handle the exception, e.g., return a meaningful response or throw a custom exception
            return null; // or throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }
    
    @GetMapping("/admin/finduser/{id}")
    public ResponseEntity<?>  findUser(@PathVariable String id){
    	try {
    		 UserDto userDto =userService.findUser(id);
    		return new ResponseEntity<>(userDto,HttpStatus.OK);
    		
    	}
    	catch(UserNotFoundException e) {
    		return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
    	}
    	
    }
    
    @PostMapping("/admin/adduser")
    public ResponseEntity<ApiResponse<String>> addUser(@RequestBody UserDto userDto) {
        try {
            userService.registerUser(userDto);
            return ResponseEntity.ok(new ApiResponse<>(true,"User registered successfully",null));
        } catch (EmailAlreadyExists | PhoneAlreadyExists | UserNameAlreadyExists e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false,e.getMessage(),null));
        }
    }
    
    @DeleteMapping("/admin/deleteuser/{memberId}")
    public ResponseEntity<ApiResponse<String>> removeUser(@PathVariable String memberId) {
        try {
        	 userService.deleteUser(memberId);
        	return ResponseEntity.ok(new ApiResponse<>(true,"User removed successfully",null));
        } catch (UserNotFoundException  e) {
        	return ResponseEntity.badRequest().body(new ApiResponse<>(false,e.getMessage(), null));
        }
    }
    
    @PatchMapping("/user/update")
    public ResponseEntity<ApiResponse<String>>updateUser(@RequestBody UserDto userDto){
    	try {
    		userService.updateUser(userDto);
    		return ResponseEntity.ok(new ApiResponse<>(true,"userDetails Updated",null));
    	}
    	catch(UserNotFoundException | EmailAlreadyExists | PhoneAlreadyExists | UserNameAlreadyExists e) {
    		return ResponseEntity.badRequest().body(new ApiResponse<>(false,e.getMessage(),null));
    	}
    } 
}