package com.mytech.RestaurantsWeb.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeConverterUtil {
	public static LocalDateTime convertStringToDateTime(String dateString, String timeString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String dateTimeString = dateString + " " + timeString;
		LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
		return dateTime;
	}
}
