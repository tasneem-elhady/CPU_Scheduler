package com.cpuscheduler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class SJF_Non_Prem implements Schedular{
    int LiveTime = 0;
    @Override
    public Queue<Process> Schedule(ArrayList<Process> MyProcesses){return null;}

    public Queue<Process> Schedule(ArrayList<Process> MyProcesses,  int currenttime ){
//        LiveTime = currenttime;
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
            ArrayList<Process> temp = new ArrayList<>();
            temp.add(processes.get(index));
            processes.remove(processes.get(index));
            if(temp.get(0).getArrivalTime() <= LiveTime) {
                temp.get(0).setStart_time(LiveTime);
            }
            else{
                temp.get(0).setStart_time(temp.get(0).getArrivalTime());
            }
            LiveTime+= temp.get(0).getBurstTime();
            temp.get(0).setEnd_time(temp.get(0).getStart_time() + temp.get(0).getBurstTime());
            temp.get(0).setRemaining_burst_time(temp.get(0).getBurstTime() - (currenttime - temp.get(0).getStart_time()));
            n--;
            p.add(temp.get(0));
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
//    public void setProcessStartTimeAndEndTime(ArrayList<Process> processes, int currenttime) {
//        Queue<Process> q = this.Schedule(processes, currenttime);
//        int time = 0;
//        for (Process process : q) {
//            if (process.getArrivalTime() <= time){
//                process.setStart_time(time);
//                time += process.getBurstTime();
//            }
//            else{
//                process.setStart_time(process.getArrivalTime());
//                time = process.getArrivalTime() + process.getBurstTime();
//            }
//            process.setEnd_time(process.getStart_time() + process.getBurstTime());
//
////            System.out.println("Start time = " + process.getStart_time() + " End time = " + process.getEnd_time());
//        }
//    }
    public static void main(String[] args) {
        Process p1=new Process(1,0,5);
        Process p2=new Process(2,0,3);
        Process p3=new Process(3,0,1);
        ArrayList<Process>p=new ArrayList<>();
        p.add(p1);
        p.add(p2);
        p.add(p3);
        SJF_Non_Prem sch=new SJF_Non_Prem();
        Queue<Process> q =sch.Schedule(p , 0);
//        sch.setProcessStartTimeAndEndTime(p, 0);
        double t =sch.calculate_avg_turn_around_time(p);
        double t2 =sch.calculate_avg_wait_time(p);
        for(Process o : q){
            System.out.println( o.getProcess_ID());

        }
        System.out.println("Turn around= "+t);
        System.out.println("waiting= "+t2);
    }
}
