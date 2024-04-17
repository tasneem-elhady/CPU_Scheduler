package com.cpuscheduler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class SJF_Non_Prem implements Schedular{
    int LiveTime = 0;
    @Override
//    public Queue<Process> Schedule(ArrayList<Process> MyProcesses){return null;}

    public Queue<Process> Schedule(ArrayList<Process> MyProcesses ){
        ArrayList<Process> processes = (ArrayList<Process>) MyProcesses.clone();
        Collections.sort(processes);
        int n = processes.size();
        Queue<Process> p = new LinkedList<>();
        while(n!=0){
            int index = 0;
            int min = processes.get(0).getRemaining_burst_time();
            for(int j = 0 ; j < processes.size() ; j++){
                if(processes.get(j).getArrivalTime() > LiveTime || processes.get(j).getRemaining_burst_time() == 0) continue;
                if(min > processes.get(j).getRemaining_burst_time()||LiveTime >= processes.get(j).getArrivalTime()){
                    min = processes.get(j).getRemaining_burst_time();
                    index = j;
                }
            }
            if(processes.get(index).isFirst_response()){
                p.add(processes.get(index));
                processes.get(index).setStart_time(LiveTime);
                processes.get(index).setEnd_time(LiveTime + processes.get(index).getBurstTime());
                processes.get(index).setRemaining_burst_time(0);
                LiveTime = LiveTime + processes.get(index).getBurstTime() - 1;
                processes.get(index).setFirst_response(false);
                n--;
            }
            LiveTime++;
        }
        return p;
    }
    @Override
    public double calculate_avg_wait_time(ArrayList<Process> processes){
        double sum = 0;
        if(processes.isEmpty() ) return 0;
        for(Process o : processes){
            sum += o.waitingTime();
        }
        return sum/processes.size();
    }
    @Override
    public double calculate_avg_turn_around_time(ArrayList<Process> processes){
        double sum = 0;
        if(processes.isEmpty() ) return 0;
        for(Process o : processes){
            sum += o.turnAroundTime();
        }
        return sum/processes.size();
    }
    public static void main(String[] args) {
        Process p1=new Process(1,0,5);
        Process p2=new Process(2,0,3);
        Process p3=new Process(3,0,1);
        ArrayList<Process>p=new ArrayList<>();
        p.add(p1);
        p.add(p2);
        p.add(p3);
        SJF_Non_Prem sch=new SJF_Non_Prem();
        Queue<Process> q =sch.Schedule(p);
        double t =sch.calculate_avg_turn_around_time(p);
        double t2 =sch.calculate_avg_wait_time(p);
        for(Process o : q){
            System.out.println( o.getProcess_ID());

        }
        System.out.println("Turn around= "+t);
        System.out.println("waiting= "+t2);
    }
}