package com.restaurant.service.entities;



import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "ship_addresses")
public class ShipAddress extends AbstractEntity {

	private static final long serialVersionUID = 4544572534147985647L;
	
	public ShipAddress(String name, String phone, String address) {
		this.name = name;
		this.phone = phone;
		this.address = address;
	}

	@Column(name = "receipt_name", nullable = false)
	private String name;
	@Column(name = "receipt_phone", nullable = false)
	private String phone;
	@Column(name = "receipt_address", nullable = false)
	private String address;
	private String postalCode;
	private String ward;
	private String district;
	private String city;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@Column(name = "ship_default", columnDefinition = "tinyint(1) default 0")
	private boolean shipDefault;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public boolean isShipDefault() {
		return shipDefault;
	}

	public void setShipDefault(boolean shipDefault) {
		this.shipDefault = shipDefault;
	}
	
	
}
