package com.cpuscheduler;

public class Process {
    private processInTable processIndex;
    private int processID;
    private int ArrivalTime;
    private int BurstTime;
    private int Priority;
    private int Start_time;
    private int end_time;

    public Process(processInTable processIndex) {
        this.processIndex = processIndex;
        this.processID = Integer.parseInt(processIndex.getProcessID());
        ArrivalTime = Integer.parseInt(processIndex.getArrivalTime());
        BurstTime = Integer.parseInt(processIndex.getBurstTime());
        try {
            Priority = Integer.parseInt(processIndex.getPriority());
        }catch (Exception e)
        {
            Priority = 0;
        }
//        processIndex.setRemainingTime("6");
    }

    public int getRemaining_burst_time() {
        return remaining_burst_time;
    }

    public void setRemaining_burst_time(int remaining_burst_time) {
        this.remaining_burst_time = remaining_burst_time;
    }

    private int remaining_burst_time;
    public Process(int process_ID, int arrivalTime, int burstTime, int priority) {
        this.processID = process_ID;
        ArrivalTime = arrivalTime;
        BurstTime = burstTime;
        Priority = priority;
    }
    public Process(int process_ID, int arrivalTime, int burstTime) {
        this.processID = process_ID;
        ArrivalTime = arrivalTime;
        BurstTime = burstTime;
    }
    public int getBurstTime() {
        return BurstTime;
    }
    public int getProcess_ID() {
        return processID;
    }
    public void setProcess_ID(int process_ID) {
        this.processID = process_ID;
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

    public int getProcessID() {
        return processID;
    }

    public void setProcessID(int processID) {
        this.processID = processID;
    }

    public void setEnd_time(int end_time) {
        this.end_time = end_time;
    }

    public processInTable getProcessIndex() {
        return processIndex;
    }

    public void setProcessIndex(processInTable processIndex) {
        this.processIndex = processIndex;
    }
}
