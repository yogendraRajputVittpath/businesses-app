package com.user.business.service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.user.business.service.entity.ServiceEntity;
import com.user.business.service.request.ServiceAddRequest;
import com.user.business.service.request.ServiceUpdateRequest;

public interface ServiceService {

	ServiceEntity addService(ServiceAddRequest request, Long userId);
	ServiceEntity updateService(ServiceUpdateRequest request, Long userId);
	void removeService(Long serviceId, Long userId);

	List<ServiceEntity> getServices(Long userId);
	
}
