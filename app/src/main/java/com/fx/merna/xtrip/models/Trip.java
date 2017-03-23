package com.fx.merna.xtrip.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Merna on 3/15/17.
 */

public class Trip implements Serializable {

    private  String id;
    private String name;
    private String startPoint;
    private String startLong;
    private String startLat;
    private String endLong;
    private String endLat;
    private String endPoint;
    private String type;
    private String status;
    private double distance;
    private Long date;

    public Trip() {
    }

    public Trip(String id, String name, String startPoint, String sLong, String sLat, String endPoint, String eLong, String eLat, String type, Long date) {

        this.id=id;
        this.name = name;
        this.startPoint = startPoint;
        this.startLong = sLong;
        this.startLat = sLat;
        this.endPoint = endPoint;
        this.endLong = eLong;
        this.endLat = eLat;
        this.type = type;
        this.status="upcoming";
        this.date = date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getStartLong() {
        return startLong;
    }

    public void setStartLong(String startLong) {
        this.startLong = startLong;
    }

    public String getStartLat() {
        return startLat;
    }

    public void setStartLat(String startLat) {
        this.startLat = startLat;
    }

    public String getEndLong() {
        return endLong;
    }

    public void setEndLong(String endLong) {
        this.endLong = endLong;
    }

    public String getEndLat() {
        return endLat;
    }

    public void setEndLat(String endLat) {
        this.endLat = endLat;
    }
}
