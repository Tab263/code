package com.restaurant.service.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkScheduleDTO {
	 private Long id;
	 private Long userId;
		private String userName;
	   
		public WorkScheduleDTO(Long id, Long userId, String userName, String workDate, String startTime, String endTime,
				String startTime1, String endTime1, String note) {
			super();
			this.id = id;
			this.userId = userId;
			this.userName = userName;
			this.workDate = workDate;
			this.startTime = startTime;
			this.endTime = endTime;
			this.startTime1 = startTime1;
			this.endTime1 = endTime1;
			this.note = note;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
		private String workDate;
	    private String startTime;
	    private String endTime;
	    private String startTime1;
	    private String endTime1;
	    private String note;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
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
		
		
		public WorkScheduleDTO(Long id, String workDate, String startTime, String endTime, String startTime1,
				String endTime1, String note) {
			super();
			this.id = id;
			this.workDate = workDate;
			this.startTime = startTime;
			this.endTime = endTime;
			this.startTime1 = startTime1;
			this.endTime1 = endTime1;
			this.note = note;
		}
		public String getWorkDate() {
			return workDate;
		}
		public void setWorkDate(String workDate) {
			this.workDate = workDate;
		}
		public WorkScheduleDTO() {
			super();
			// TODO Auto-generated constructor stub
		}
    

}
