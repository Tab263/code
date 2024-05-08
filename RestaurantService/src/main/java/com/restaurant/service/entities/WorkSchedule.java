package com.restaurant.service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "work_schedule")
public class WorkSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "work_date")
    private String workDate;

    @Column(name = "start_time_1")
    private String startTime;

    @Column(name = "end_time_1")
    private String endTime;

    @Column(name = "start_time_2")
    private String startTime1;

    @Column(name = "end_time_2")
    private String endTime1;

    @Column(name = "note", length = 45, nullable = true)
    private String note;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public WorkSchedule() {
        super();
    }

    public WorkSchedule(Long id, User user, String workDate, String startTime, String endTime, String startTime1, String endTime1, String note) {
        super();
        this.id = id;
        this.user = user;
        this.workDate = workDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startTime1 = startTime1;
        this.endTime1 = endTime1;
        this.note = note;
    }
}
