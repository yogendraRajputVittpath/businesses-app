package com.user.business.service.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class ApplicationUtil {
    public static DayOfWeek getDayOfWeek() {
        return LocalDate.now().getDayOfWeek();
    }
    
    public static boolean isMidNightPassed() {
    	
    	 LocalTime currentTime = LocalTime.now();
         LocalTime midnight =LocalTime.of(23, 55);
    	
    	return currentTime.isAfter(midnight);
    }
}
