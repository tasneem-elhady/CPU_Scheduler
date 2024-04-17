package com.cpuscheduler;

import javafx.beans.property.SimpleStringProperty;

public class processInTable {
    private SimpleStringProperty processID;
    private SimpleStringProperty ArrivalTime;
    private SimpleStringProperty BurstTime;
    private SimpleStringProperty Priority;
    private SimpleStringProperty RemainingTime;

    public String getRemainingTime() {
        return RemainingTime.get();
    }

    public SimpleStringProperty remainingTimeProperty() {
        return RemainingTime;
    }

    public void setRemainingTime(String remainingTime) {
        this.RemainingTime.set(remainingTime);
    }
    public String getProcessID() {
        return processID.get();
    }

    public SimpleStringProperty processIDProperty() {
        return processID;
    }

    public void setProcessID(String processID) {
        this.processID.set(processID);
    }

    public String getArrivalTime() {
        return ArrivalTime.get();
    }

    public SimpleStringProperty arrivalTimeProperty() {
        return ArrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.ArrivalTime.set(arrivalTime);
    }

    public String getBurstTime() {
        return BurstTime.get();
    }

    public SimpleStringProperty burstTimeProperty() {
        return BurstTime;
    }

    public void setBurstTime(String burstTime) {
        this.BurstTime.set(burstTime);
    }

    public String getPriority() {
        return Priority.get();
    }

    public SimpleStringProperty priorityProperty() {
        return Priority;
    }

    public void setPriority(String priority) {
        this.Priority.set(priority);
    }



    public processInTable(String process_ID, String arrivalTime, String burstTime, String priority) {
        this.processID = new SimpleStringProperty(process_ID);
        ArrivalTime = new SimpleStringProperty(arrivalTime);
        BurstTime = new SimpleStringProperty(burstTime);
        Priority = new SimpleStringProperty(priority);
        RemainingTime = new SimpleStringProperty(BurstTime.getValue());
    }
    public processInTable(String process_ID, String arrivalTime, String burstTime) {
        this.processID = new SimpleStringProperty(process_ID);
        ArrivalTime = new SimpleStringProperty(arrivalTime);
        BurstTime = new SimpleStringProperty(burstTime);
        Priority = new SimpleStringProperty();
        RemainingTime = new SimpleStringProperty(BurstTime.getValue());
    }
}


