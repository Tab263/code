package com.restaurant.service.entities;

import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "reservation")
public class Reservation extends AbstractEntity {
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public TableM getTableM() {
		return tableM;
	}

	public void setTableM(TableM tableM) {
		this.tableM = tableM;
	}

	private static final long serialVersionUID = 1L;
	@Column(name = "name")
	private String name;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "date")
	private String date;
	
	@Column(name = "time")
	private String time;
	
	@Column(name = "person_number")
	private String person_number;
	
	@Column(name = "status")
	private int status = 0;
	
	@ManyToOne
	@JoinColumn(name = "table_id")
	private  TableM tableM;

	
	public Reservation() {
		super();
	}

	public Reservation(String name, String email, String phoneNumber, String date, String time, String person_number) {
		super();
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.date = date;
		this.time = time;
		this.person_number = person_number;

	}
//	@Transient
//	public String getDateToString() {
//	    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//	    return formatter.format(date);
//	}
	

}
