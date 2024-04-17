package com.cpuscheduler;

public class Process implements Comparable<Process> {
    private processInTable processIndex;
    private int processID;
    private int ArrivalTime;
    private int BurstTime;
    private int Priority;
    private int Start_time;
    private int end_time;
    private int remaining_burst_time;
    private int completed_time;
    private boolean first_response=true;


//    sarah's trial
    private int tempBurstTime;
    private int responseTime;


    public int gettempBurstTime() {
        return tempBurstTime;
    }
    public void settempBurstTime(int tempBurstTime) {
        tempBurstTime = tempBurstTime;
    }
    public int getresponseTime() {
        return responseTime;
    }
    public void setresponseTime(int responseTime) {
        responseTime = responseTime;
    }

    public Process(processInTable processIndex) {
        this.processIndex = processIndex;
        this.processID = Integer.parseInt(processIndex.getProcessID());
        this.ArrivalTime = Integer.parseInt(processIndex.getArrivalTime());
        this.BurstTime = Integer.parseInt(processIndex.getBurstTime());
        this.remaining_burst_time = Integer.parseInt(processIndex.getBurstTime());

        try {
            Priority = Integer.parseInt(processIndex.getPriority());
            this.tempBurstTime =Integer.parseInt(processIndex.getBurstTime());
            this.responseTime = -1;
            this.end_time = 0;
            this.Start_time = -1;
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
     public Process(){}

    public Process(int process_ID, int arrivalTime, int burstTime, int priority) {
        this.processID = process_ID;
        ArrivalTime = arrivalTime;
        BurstTime = burstTime;
        Priority = priority;
        this.remaining_burst_time = burstTime;
        this.tempBurstTime =burstTime;
        this.responseTime = -1;
        this.end_time = 0;
        this.Start_time = -1;
    }
    public Process(int process_ID, int arrivalTime, int burstTime) {
        this.processID = process_ID;
        this.ArrivalTime = arrivalTime;
        this.BurstTime = burstTime;
        this.remaining_burst_time = burstTime;
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

    public int getCompleted_time() {
        return completed_time;
    }

    public void setCompleted_time(int completed_time) {
        this.completed_time = completed_time;
    }

    public boolean isFirst_response() {
        return first_response;
    }

    public void setFirst_response(boolean first_response) {
        this.first_response = first_response;
    }

    public processInTable getProcessIndex() {
        return processIndex;
    }

    public void setProcessIndex(processInTable processIndex) {
        this.processIndex = processIndex;
    }

    public int waitingTime(){

        return (getEnd_time()-getArrivalTime()-getBurstTime());
    }

    public int turnAroundTime(){

        return (getEnd_time()-getArrivalTime());
    }

    public int responseTime(){

        return (getStart_time()-getArrivalTime());
    }

     public void printProcess(){
        System.out.println(getProcess_ID()+" : arrival= "+getArrivalTime()+" , burst= "+ getBurstTime()
                +" , Start time= "+getStart_time()+" , End time= "+getEnd_time());
        System.out.println("waiting time = "+waitingTime()+" , turnaround time= "+ turnAroundTime());
    }
    public String toString(){
        return (getProcess_ID()+" : arrival= "+getArrivalTime()+" , burst= "+ getBurstTime() + ", priority = " + getPriority()
                +" , Start time= "+getStart_time()+" , End time= "+getEnd_time() + ", remaining burst time " + getRemaining_burst_time()+"\n");
    }

    @Override
    public int compareTo(Process o) {
        return this.getArrivalTime()-((Process)o).getArrivalTime();
    }
}
