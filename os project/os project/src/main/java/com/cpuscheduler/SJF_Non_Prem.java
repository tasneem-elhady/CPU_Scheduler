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
        LiveTime = currenttime;
        ArrayList<Process> processes = (ArrayList<Process>) MyProcesses.clone();
        Collections.sort(processes);
        int CurrentServing = 0;
        for (Process retrial : processes){
            if(retrial.getRemaining_burst_time() == 0){continue;}
            if(!retrial.isFirst_response()) {
                CurrentServing = retrial.getProcessID();
                LiveTime = retrial.getEnd_time();
                break;
            }
        }
        for(Process fixing : processes){
            if(fixing.getProcess_ID() != CurrentServing && fixing.getRemaining_burst_time() !=0){
                fixing.setFirst_response(true);
            }
        }
        int n = processes.size();
        Queue<Process> p = new LinkedList<>();
        while(n!=0){
            int index = 0;
            int min = processes.get(0).getRemaining_burst_time();
            int min = 100;
            for(int j = 0 ; j < processes.size() ; j++){
                if(processes.get(j).getArrivalTime() > LiveTime || processes.get(j).getRemaining_burst_time() == 0) continue;
                if(processes.get(j).isFirst_response() == false){
                if(processes.get(j).getProcess_ID() == CurrentServing){
                    min = processes.get(j).getRemaining_burst_time();
                    index = j;
                    break;
                }
                if(min > processes.get(j).getRemaining_burst_time()&&LiveTime >= processes.get(j).getArrivalTime()){
                    min = processes.get(j).getRemaining_burst_time();
                    index = j;
                }
            }
            ArrayList<Process> temp = new ArrayList<>();
            temp.add(processes.get(index));
            temp.get(0).setFirst_response(false);
            processes.remove(processes.get(index));
            if(temp.get(0).getArrivalTime() <= LiveTime) {
                temp.get(0).setStart_time(LiveTime);
            if(processes.get(index).getRemaining_burst_time() != 0) {
                temp.add(processes.get(index));
                processes.remove(processes.get(index));
            }
            else{
                temp.get(0).setStart_time(temp.get(0).getArrivalTime());
            else{n--; continue;}
            if(temp.get(0).isFirst_response()) {
                if (temp.get(0).getArrivalTime() <= LiveTime) {
                    temp.get(0).setStart_time(LiveTime);
                } else {
                    temp.get(0).setStart_time(temp.get(0).getArrivalTime());
                }
                LiveTime+= temp.get(0).getBurstTime();
            }
            if(temp.get(0).getStart_time() == LiveTime){
                temp.get(0).setRemaining_burst_time(temp.get(0).getBurstTime() - currenttime );
            }
            else if(temp.get(0).getStart_time() > LiveTime){
                temp.get(0).setRemaining_burst_time(temp.get(0).getBurstTime() - (currenttime - temp.get(0).getStart_time()) );
            }
            LiveTime+= temp.get(0).getBurstTime();
            temp.get(0).setFirst_response(false);
            temp.get(0).setEnd_time(temp.get(0).getStart_time() + temp.get(0).getBurstTime());
//            temp.get(0).setRemaining_burst_time(temp.get(0).getBurstTime() - (currenttime - temp.get(0).getStart_time()));
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
        Process p2=new Process(2,0,6);
        Process p3=new Process(3,0,7);
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
