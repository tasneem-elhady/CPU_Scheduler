package com.cpuscheduler;

public class LiveTime {
    private int liveTime;
    private  int remainingTime;
    private  int completedTime;
    private  int accumlativeTime;
    public Process p;
    public Process getP() {
        return p;
    }

    public void setP(Process p) {
        this.p = p;
    }



    public LiveTime(int liveTime, int remainingTime, int completedTime, int accumlativeTime, Process p) {
        this.liveTime = liveTime;
        this.remainingTime = remainingTime;
        this.completedTime = completedTime;
        this.accumlativeTime = accumlativeTime;
        this.p = p;
    }

    public LiveTime(int remainingTime, int completedTime, int accumlativeTime) {
        this.remainingTime = remainingTime;
        this.completedTime = completedTime;
        this.accumlativeTime = accumlativeTime;
    }

    public LiveTime(int liveTime, int remainingTime, int completedTime, int accumlativeTime) {
        this.liveTime = liveTime;
        this.remainingTime = remainingTime;
        this.completedTime = completedTime;
        this.accumlativeTime = accumlativeTime;
    }

    public int getLiveTime() {
        return liveTime;
    }

    public void setLiveTime(int liveTime) {
        this.liveTime = liveTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(int completedTime) {
        this.completedTime = completedTime;
    }

    public int getAccumlativeTime() {
        return accumlativeTime;
    }

    public void setAccumlativeTime(int accumlativeTime) {
        this.accumlativeTime = accumlativeTime;
    }

    public void printLiveTime(){
        p.printProcess();

        System.out.println("@ Time= "+getLiveTime()+" : Accumulative completed = "+getAccumlativeTime()
                +", completed in this period "+" = "+getCompletedTime()+" , remaining= "+getRemainingTime());
    }
    public String toString (){
        return ("@ Time= "+getLiveTime()+" : Accumulative completed = "+getAccumlativeTime()
                +", completed in this period "+" = "+getCompletedTime()+" , remaining= "+getRemainingTime());
    }
}
