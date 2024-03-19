package com.cpuscheduler;

import java.util.ArrayList;
import java.util.Queue;

public interface Schedular {
    public Queue<Process> Schedule(ArrayList<Process> processes);
    public double calculate_avg_wait_time(ArrayList<Process> processes);
    public double calculate_avg_turn_around_time(ArrayList<Process> processes);

}
