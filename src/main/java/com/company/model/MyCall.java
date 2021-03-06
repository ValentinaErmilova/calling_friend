package com.company.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "calls")
public class MyCall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "call_sid")
    private String callSid;

    @Column(name = "id_from")
    private int from;

    @Column(name = "id_to")
    private int to;

    @Column(name = "from_number")
    private String fromNumber;

    @Column(name = "to_number")
    private String toNumber;

    private String duration;

    private String status;

    @Column(name = "start_date")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private Timestamp date;

    public MyCall() {
    }

    public MyCall(String callSid, int to, int from, String toNumber, String fromNumber, String duration, String status, Timestamp date) {
        this.callSid = callSid;
        this.duration = duration;
        this.status = status;
        this.from = from;
        this.to = to;
        this.date = date;
        this.toNumber = toNumber;
        this.fromNumber = fromNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCallSid() {
        return callSid;
    }

    public void setCallSid(String callSid) {
        this.callSid = callSid;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getFromNumber() {
        return fromNumber;
    }

    public void setFromNumber(String fromNumber) {
        this.fromNumber = fromNumber;
    }

    public String getToNumber() {
        return toNumber;
    }

    public void setToNumber(String toNumber) {
        this.toNumber = toNumber;
    }


    @Override
    public String toString() {
        return "MyCall{" +
                "id=" + id +
                ", callSid='" + callSid + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", fromNumber='" + fromNumber + '\'' +
                ", toNumber='" + toNumber + '\'' +
                ", duration='" + duration + '\'' +
                ", status='" + status + '\'' +
                ", date=" + date +
                '}';
    }
}
