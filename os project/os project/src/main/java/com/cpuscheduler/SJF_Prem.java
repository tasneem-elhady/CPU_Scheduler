package com.cpuscheduler;

import java.util.*;

public class SJF_Prem implements Schedular{
    private int  time=0;
//    private int prevID=-1;
//    private Deque<LiveTime> liveTimeQueue =new LinkedList<>() ;
    @Override



    public Queue<Process> Schedule(ArrayList<Process> MyProcesses){
        ArrayList<Process> processes = (ArrayList<Process>) MyProcesses.clone();
        Collections.sort(processes);
        Queue<Process> Completed=new LinkedList<Process>();
        Deque<Process> p =new LinkedList<Process>();
        int n=processes.size();


        while(!processes.isEmpty()) {

            int min=processes.get(0).getRemaining_burst_time();
            int index=0;
            for (int j = 0; j < processes.size(); j++) {
                if (processes.get(j).getArrivalTime()<=time ) {
                    if ((min >= processes.get(j).getRemaining_burst_time()&&processes.get(j).getRemaining_burst_time()!=0)||processes.get(index).getArrivalTime()>time) {
                        min = processes.get(j).getRemaining_burst_time();
                        index = j;
                    }
                }
            }
            int t =processes.get(index).getArrivalTime();
            if(t<=time) {


                    if (processes.get(index).isFirst_response()) {
                        processes.get(index).setStart_time(time);
                        processes.get(index).setFirst_response(false);

                    }
//                    if(processes.get(index).getProcess_ID()==prevID){
//
//                        processes.get(index).setRemaining_burst_time(processes.get(index).getRemaining_burst_time() - 1);
//
//
//                        p.getLast().setRemaining_burst_time(processes.get(index).getRemaining_burst_time());
//                        p.getLast().setCompleted_time(time+1);
//                        p.getLast().setTime_taken(p.getLast().getTime_taken()+1);
////                        liveTimeQueue.getLast().setCompletedTime(liveTimeQueue.getLast().getCompletedTime()+1);
////                        liveTimeQueue.getLast().setAccumlativeTime((processes.get(index).getBurstTime() - processes.get(index).getRemaining_burst_time()));
////                        liveTimeQueue.getLast().setRemainingTime(liveTimeQueue.getLast().getRemainingTime()-1);
//                    }
                    else {
                        processes.get(index).setRemaining_burst_time(processes.get(index).getRemaining_burst_time() - 1);
                        p.add(new Process(processes.get(index),time));


//                        liveTimeQueue.add(new LiveTime(time, processes.get(index).getRemaining_burst_time(), 1, (processes.get(index).getBurstTime() - processes.get(index).getRemaining_burst_time()), processes.get(index)));
                    }
//                prevID=processes.get(index).getProcess_ID();
                    if(processes.get(index).getRemaining_burst_time()==0){
                        processes.get(index).setEnd_time(time+1);
                       processes.remove(index);
                    }


            }

            time++;
        }
//        return p;
//        return liveTimeQueue;
        return p;
    }
    @Override
    public double calculate_avg_wait_time(ArrayList<Process> processes){
        double sum = 0;
        if(processes.size()!=0) {

            for (Process o : processes) {
                sum += o.waitingTime();

            }
            sum=sum/processes.size();
        }
        return sum;
    }
    @Override
    public double calculate_avg_turn_around_time(ArrayList<Process> processes){

        double sum = 0;
        if(processes.size()!=0) {

            for (Process o : processes) {
                sum += o.turnAroundTime();

            }
            sum=sum/processes.size();
        }
        return sum;
    }


    public static void main(String[] args) {
        Process p1=new Process(1,0,6);
        Process p2=new Process(2,3,3);
        Process p3=new Process(3,5,1);
        ArrayList<Process>p=new ArrayList<>();
        p.add(p1);
        p.add(p2);
        p.add(p3);
        SJF_Prem sch=new SJF_Prem();
        Queue<Process> q =sch.Schedule(p);
        double t =sch.calculate_avg_turn_around_time(p);
        double t2 =sch.calculate_avg_wait_time(p);
        for(Process o : q){
            System.out.println(o.getProcess_ID());
            System.out.println("start @ "+o.getStart_time());
            System.out.println("Completed @ "+o.getCompleted_time());
            System.out.println("take @ "+o.getTime_taken());
            System.out.println("remaining @ "+o.getRemaining_burst_time());

//            System.out.println(o.);

        }

        System.out.println("Turn around= "+t);
        System.out.println("waiting= "+t2);
    }
}
