package com.user.business.service.controller;

import com.user.business.response.ApiResponse;
import com.user.business.security.JwtUtil;
import com.user.business.service.entity.ServiceEntity;
import com.user.business.service.request.ServiceAddRequest;
import com.user.business.service.request.ServiceUpdateRequest;
import com.user.business.service.service.ServiceService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/service")
public class ServiceController {

    private final ServiceService serviceService;
    private final JwtUtil jwtUtil;

    public ServiceController(ServiceService serviceService, JwtUtil jwtUtil) {
        this.serviceService = serviceService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<ServiceEntity>> addService(@RequestHeader("Authorization") String bearerToken,
                                                   @Valid @RequestBody ServiceAddRequest request) {
        Long userId = jwtUtil.extractUserId(bearerToken);
        ServiceEntity service = serviceService.addService(request, userId);
        ApiResponse<ServiceEntity> response = new ApiResponse<>("SUCCESS",200,"Record Added Successfully",service);
//        return ResponseEntity.ok(service);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<ServiceEntity>> updateService(@RequestHeader("Authorization") String bearerToken,
                                                      @Valid @RequestBody ServiceUpdateRequest request) {
        Long userId = jwtUtil.extractUserId(bearerToken);
        ServiceEntity service = serviceService.updateService(request, userId);
        ApiResponse<ServiceEntity> response = new ApiResponse<>("SUCCESS",200,"Record Updated Successfully",service);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse<Void>> removeService(@RequestHeader("Authorization") String bearerToken,
                                                @RequestParam Long serviceId) {
        Long userId = jwtUtil.extractUserId(bearerToken);
        serviceService.removeService(serviceId, userId);
        ApiResponse<Void> response = new ApiResponse<>("SUCCESS",200,"Account Marked as REMOVED",null);
        return ResponseEntity.ok(response);
    }
}