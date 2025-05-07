package com.cts.lms.repository;

import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.cts.lms.entity.User;
import com.cts.lms.enums.Role;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("select u from User u where u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);
    
    @Query("select u from User u where u.email = :email")
    User findByEmail(@Param("email") String email);

    @Query("select u from User u where u.phone = :phone")
    User findByPhone(@Param("phone") String phone);
    
    @Query("select u from User u")
	public List<User> getUsers();
    
    @Query("select u from User u where u.memberId=:memberId")
    Optional<User> findByMemberId(@Param("memberId") String memberId);
    
    @Query("Delete from User u where u.memberId=:memberId")
    public void deleteByMemberId(@Param("memberId") String memberId);

	@Query("select u from User u where u.userType = :userType")
	Optional<User>findByRole(@Param("userType") String string);
    
}