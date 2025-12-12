package com.user.business.service.request;

import lombok.Data;

@Data
public class ServiceAddRequest {
    private String serviceType;
    private Integer serviceCharge;
}
