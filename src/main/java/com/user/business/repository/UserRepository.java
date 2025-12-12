package com.user.business.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.user.business.entity.User;
import com.user.business.entity.UserStatus;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    // check email exists
    boolean existsByEmail(String email);

    // check phone exists
    boolean existsByPhoneNo(Long phoneNo);
    boolean existsByEmailOrPhoneNo(String email, Long phoneNo);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByIdAndPassword(Long id, String password);
    
    Optional<User> findByIdAndEmail(Long id, String email);
	
	Optional<User> findByEmailAndPassword(String email, String password);

	
	Long findPhoneNumberById(Long id);
	
	@Query("SELECT u.status FROM User u WHERE u.email = :email")
	String findStatusByEmail(@Param("email") String email);
	
	User findByAadharCardNo(String aadhar);
	User findByPanCardNo(String pan);


	
}
