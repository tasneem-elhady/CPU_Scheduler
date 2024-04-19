package com.cpuscheduler;

//import com.cpuscheduler.Process;

public class ByTime{
    private int time;
    private Process p;

    public int getRemaining_time() {
        return remaining_time;
    }
    private int remaining_time;

    public ByTime(int time, Process p, int remaining_time) {

        this.time = time;
        this.p = p;
        this.remaining_time = remaining_time;
    }


    public ByTime(int time, Process p_id) {
        this.time = time;
        this.p = p_id;
    }

    public int getTime() {
        return time;
    }

    public Process getP() {
        return p;
    }

    @Override
    public String toString() {
        return "At time = "+time+", There is process no. = "+p.getProcess_ID() +"\n";
    }
}