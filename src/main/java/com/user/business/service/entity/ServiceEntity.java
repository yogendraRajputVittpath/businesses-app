package com.user.business.service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "services")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;

    private String serviceType;

    private Integer serviceCharge;

    private String status;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;
}
