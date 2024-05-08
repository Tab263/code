package com.restaurant.service.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StaffScheduleDTO {
	 private Long id;
	    private String workDate;
	    private String startTime;
	    private String endTime;
	    private String startTime1;
	    private String endTime1;
	    private String note;
	    private Long cusId;
	    private String cusName;
	    private Long categoryId;
	    private String categoryName;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getWorkDate() {
			return workDate;
		}
		public void setWorkDate(String workDate) {
			this.workDate = workDate;
		}
		public String getStartTime() {
			return startTime;
		}
		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}
		public String getEndTime() {
			return endTime;
		}
		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}
		public String getStartTime1() {
			return startTime1;
		}
		public void setStartTime1(String startTime1) {
			this.startTime1 = startTime1;
		}
		public String getEndTime1() {
			return endTime1;
		}
		public void setEndTime1(String endTime1) {
			this.endTime1 = endTime1;
		}
		public String getNote() {
			return note;
		}
		public void setNote(String note) {
			this.note = note;
		}
		public Long getCusId() {
			return cusId;
		}
		public void setCusId(Long cusId) {
			this.cusId = cusId;
		}
		public String getCusName() {
			return cusName;
		}
		public void setCusName(String cusName) {
			this.cusName = cusName;
		}
		public Long getCategoryId() {
			return categoryId;
		}
		public void setCategoryId(Long categoryId) {
			this.categoryId = categoryId;
		}
		public String getCategoryName() {
			return categoryName;
		}
		public void setCategoryName(String categoryName) {
			this.categoryName = categoryName;
		}
		public StaffScheduleDTO(Long id, String workDate, String startTime, String endTime, String startTime1,
				String endTime1, String note, Long cusId, String cusName, Long categoryId, String categoryName) {
			super();
			this.id = id;
			this.workDate = workDate;
			this.startTime = startTime;
			this.endTime = endTime;
			this.startTime1 = startTime1;
			this.endTime1 = endTime1;
			this.note = note;
			this.cusId = cusId;
			this.cusName = cusName;
			this.categoryId = categoryId;
			this.categoryName = categoryName;
		}
		public StaffScheduleDTO() {
			super();
			// TODO Auto-generated constructor stub
		}
}
