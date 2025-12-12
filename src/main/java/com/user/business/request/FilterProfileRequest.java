package com.user.business.request;

import lombok.Data;

@Data
public class FilterProfileRequest {
    private String firstName;
    private String lastName;
    private String profession;
    private String category;
    private String city;
    private String state;
}
