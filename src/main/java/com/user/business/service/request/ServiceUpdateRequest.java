package com.user.business.service.request;

import lombok.Data;

@Data
public class ServiceUpdateRequest {
    private long serviceId;
    private Integer serviceCharge;
}
