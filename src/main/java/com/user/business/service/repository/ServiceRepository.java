package com.user.business.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.user.business.service.entity.ServiceEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    Optional<ServiceEntity> findByIdAndUserIdAndStatus(Long id, Long userId, String status);
    List<ServiceEntity> findByUserId(Long userId);
}
