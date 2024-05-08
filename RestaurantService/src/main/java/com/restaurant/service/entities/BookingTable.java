package com.restaurant.service.entities;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "bookingTable")
public class BookingTable extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Name is required")
	@Column(name = "name")
	private String name;

	@Email(message = "Email must be valid")
	@NotBlank(message = "Email is required")
	@Column(name = "email")
	private String email;
	
	@Pattern(regexp="^0\\d{9,10}$", message="Phone number must start with 0 and have 9-10 digits")
    @NotBlank(message = "Phone number is required")
	@Column(name = "phone_number")
	private String phoneNumber;

	@NotBlank(message = "Date is required")
	@Column(name = "date")
	private String date;
	
	@NotBlank(message = "Time is required")
	@Column(name = "time")
	private String time;
	
	@NotBlank(message = "Number of persons is required")
	@Column(name = "person_number")
	private String person_number;

	@Column(name = "status")
	private Integer status = 0;

	@Column(name = "special")
	private Integer special = 0;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPerson_number() {
		return person_number;
	}

	public void setPerson_number(String person_number) {
		this.person_number = person_number;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSpecial() {
		return special;
	}

	public void setSpecial(Integer special) {
		this.special = special;
	}

	public BookingTable() {
		super();
	}

	public BookingTable(String name, String email, String phoneNumber, String date, String time, String person_number) {
		super();
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.date = date;
		this.time = time;
		this.person_number = person_number;
	}

//	@Transient
//	public String getDate() {
//		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//		return formatter.format(date);
//	}
	
}
