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
    private String endPoint;
    private String type;
    private String status;
    private double distance;
    private Date date;

    public Trip() {
    }

    public Trip(String id,String name, String startPoint, String endPoint, String type) {

        this.id=id;
        this.name = name;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
