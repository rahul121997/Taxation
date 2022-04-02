package com.purpuligo.taxation.Model;

public class CommunicationModel {
    String date, name, details;

    public CommunicationModel(String date, String name, String details){
        this.date = date;
        this.name = name;
        this.details = details;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}
