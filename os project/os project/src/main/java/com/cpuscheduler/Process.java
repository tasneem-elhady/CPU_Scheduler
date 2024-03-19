package com.cpuscheduler;

public class Process {
    private int process_ID;
    private int ArrivalTime;
    private int BurstTime;
    private int Priority;
    private int Start_time;
    private int end_time;
    public Process(int process_ID, int arrivalTime, int burstTime, int priority) {
        this.process_ID = process_ID;
        ArrivalTime = arrivalTime;
        BurstTime = burstTime;
        Priority = priority;
    }
    public Process(int process_ID, int arrivalTime, int burstTime) {
        this.process_ID = process_ID;
        ArrivalTime = arrivalTime;
        BurstTime = burstTime;
    }
    public int getBurstTime() {
        return BurstTime;
    }
    public int getProcess_ID() {
        return process_ID;
    }
    public void setProcess_ID(int process_ID) {
        this.process_ID = process_ID;
    }
    public int getArrivalTime() {
        return ArrivalTime;
    }
    public void setArrivalTime(int arrivalTime) {
        ArrivalTime = arrivalTime;
    }
    public void setBurstTime(int burstTime) {
        BurstTime = burstTime;
    }
    public int getPriority() {
        return Priority;
    }

    public void setPriority(int priority) {
        Priority = priority;
    }
    public int getStart_time() {
        return Start_time;
    }
    public void setStart_time(int start_time) {
        Start_time = start_time;
    }
    public int getEnd_time() {
        return end_time;
    }
    public void setEnd_time(int end_time) {
        this.end_time = end_time;
    }
}
