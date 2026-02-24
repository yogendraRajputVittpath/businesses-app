package com.user.business.service.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.user.business.service.constant.ServiceConstants;
import com.user.business.service.entity.ServiceEntity;
import com.user.business.service.repository.ServiceRepository;
import com.user.business.service.request.ServiceAddRequest;
import com.user.business.service.request.ServiceUpdateRequest;
import com.user.business.service.service.ServiceService;

@Service
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository repository;

    public ServiceServiceImpl(ServiceRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public ServiceEntity addService(ServiceAddRequest request, Long userId) {

        if (!request.getServiceType().equalsIgnoreCase(ServiceConstants.ONSITE_PROMOTION) &&
            !request.getServiceType().equalsIgnoreCase(ServiceConstants.PRODUCT_PROMOTION)) {
            throw new IllegalArgumentException("Invalid ServiceType");
        }

//        ServiceEntity service = ServiceEntity.builder()
//                .userId(userId)
//                .serviceType(request.getServiceType())
//                .serviceCharge(request.getServiceCharge())
//                .status(ServiceConstants.ACTIVE)
//                .createdDate(LocalDateTime.now())
//                .modifiedDate(LocalDateTime.now())
//                .build();
//
//        return repository.save(service);
//    }
        
    if (repository.existsByUserIdAndServiceTypeAndStatus(
            userId,
            request.getServiceType(),
            ServiceConstants.ACTIVE)) {
        throw new IllegalArgumentException("Service already exists");
    }

   ServiceEntity service = ServiceEntity.builder()
           .userId(userId)
           .serviceType(request.getServiceType())
           .serviceCharge(request.getServiceCharge())
           .status(ServiceConstants.ACTIVE)
           .createdDate(LocalDateTime.now())
           .modifiedDate(LocalDateTime.now())
           .build();

   return repository.save(service);
    }

    @Override
    @Transactional
    public ServiceEntity updateService(ServiceUpdateRequest request, Long userId) {

        ServiceEntity service = repository
                .findByIdAndUserIdAndStatus(request.getServiceId(), userId, ServiceConstants.ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException("Active service not found"));

        service.setServiceCharge(request.getServiceCharge());
        service.setModifiedDate(LocalDateTime.now());

        return repository.save(service);
    }

    @Override
    @Transactional
    public void removeService(Long serviceId, Long userId) {

        ServiceEntity service = repository
                .findByIdAndUserIdAndStatus(serviceId, userId, ServiceConstants.ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException("Active service not found"));

        service.setStatus(ServiceConstants.REMOVED);
        service.setModifiedDate(LocalDateTime.now());

        repository.save(service);
    }
    
    @Override
    public List<ServiceEntity> getServices(Long userId) {
        return repository.findByUserId(userId);
    }

}

