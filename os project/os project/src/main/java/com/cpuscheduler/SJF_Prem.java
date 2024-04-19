package com.cpuscheduler;

import java.util.*;

public class SJF_Prem implements Schedular{
    private int  time=0;
    private int prevID=-1;
    private Deque<LiveTime> liveTimeQueue =new LinkedList<>() ;
    @Override

    public Queue<Process> Schedule(ArrayList<Process> MyProcesses){return null;}

    public Queue<LiveTime> Schedule(ArrayList<Process> MyProcesses,int currentTime){
       time=0;
        boolean flag=true;
        ArrayList<Process> processes = (ArrayList<Process>) MyProcesses.clone();
        Collections.sort(processes);
        Queue<Process> p =new LinkedList<Process>();
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

                if (t <= time) {


                    if (processes.get(index).isFirst_response()) {
                        processes.get(index).setStart_time(time);
                        processes.get(index).setFirst_response(false);

                    }
                    if(time==currentTime && currentTime!=0 && !liveTimeQueue.isEmpty()) {
                        if (liveTimeQueue.getLast().getEndingTime() > currentTime) {
                            if (processes.get(index).getProcess_ID() == prevID) {

                                processes.get(index).setRemaining_burst_time(processes.get(index).getRemaining_burst_time() - 1);


                                liveTimeQueue.getLast().setAccumlativeTime((processes.get(index).getBurstTime() - processes.get(index).getRemaining_burst_time()));
                                liveTimeQueue.getLast().setRemainingTime(liveTimeQueue.getLast().getRemainingTime() - 1);
                            } else {
                                liveTimeQueue.getLast().setCompletedTime(liveTimeQueue.getLast().getCompletedTime() - (liveTimeQueue.getLast().getEndingTime() - currentTime)-1);
                                liveTimeQueue.getLast().setAccumlativeTime(liveTimeQueue.getLast().getAccumlativeTime() - (liveTimeQueue.getLast().getEndingTime() - currentTime)-1);

                                p.add(processes.get(index));
                                processes.get(index).setRemaining_burst_time(processes.get(index).getRemaining_burst_time() - 1);
                                liveTimeQueue.add(new LiveTime(time, processes.get(index).getRemaining_burst_time(), 1, (processes.get(index).getBurstTime() - processes.get(index).getRemaining_burst_time()), processes.get(index)));
                                liveTimeQueue.getLast().setEndingTime(time);
                            }


                        }
                        else{
                            p.add(processes.get(index));
                            processes.get(index).setRemaining_burst_time(processes.get(index).getRemaining_burst_time() - 1);
                            liveTimeQueue.add(new LiveTime(time, processes.get(index).getRemaining_burst_time(), 1, (processes.get(index).getBurstTime() - processes.get(index).getRemaining_burst_time()), processes.get(index)));
                            liveTimeQueue.getLast().setEndingTime(time);
                        }
                    }
                    else if(time>=currentTime){
                        if (processes.get(index).getProcess_ID() == prevID && !liveTimeQueue.isEmpty()) {

                            processes.get(index).setRemaining_burst_time(processes.get(index).getRemaining_burst_time() - 1);


                            liveTimeQueue.getLast().setCompletedTime(liveTimeQueue.getLast().getCompletedTime() + 1);
                            liveTimeQueue.getLast().setAccumlativeTime((processes.get(index).getBurstTime() - processes.get(index).getRemaining_burst_time()));
                            liveTimeQueue.getLast().setRemainingTime(liveTimeQueue.getLast().getRemainingTime() - 1);
                            liveTimeQueue.getLast().setEndingTime(liveTimeQueue.getLast().getEndingTime()+1);
                        } else {

                            p.add(processes.get(index));
                            processes.get(index).setRemaining_burst_time(processes.get(index).getRemaining_burst_time() - 1);
                            liveTimeQueue.add(new LiveTime(time, processes.get(index).getRemaining_burst_time(), 1, (processes.get(index).getBurstTime() - processes.get(index).getRemaining_burst_time()), processes.get(index)));
                            liveTimeQueue.getLast().setEndingTime(time);
                        }

                    }
                    else {processes.get(index).setRemaining_burst_time(processes.get(index).getRemaining_burst_time() - 1);}

                    prevID = processes.get(index).getProcess_ID();
                    if (processes.get(index).getRemaining_burst_time() == 0) {
                        processes.get(index).setEnd_time(time + 1);
                        processes.remove(index);
                    }}


            time++;
        }
        for (Process pp : MyProcesses)
        {
            pp.setRemaining_burst_time(pp.getBurstTime());
        }
//        return p;
        return liveTimeQueue;
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
    public static ArrayList<ByTime>  chosenProcess(Queue<LiveTime> result){

        ArrayList<ByTime> t_by_t =new ArrayList<>();
        System.out.println(t_by_t);

        int time=0;
        for (LiveTime p: result) {
            for (int i=0;i<p.getCompletedTime();i++){
                t_by_t.add(new ByTime(time,p.getP(),p.getRemainingTime()+p.getCompletedTime()-i - 1));
                time++;
            }
        }
        return t_by_t;

    }

    public static void main(String[] args) {
        Process p1=new Process(1,0,3);
        Process p2=new Process(2,0,2);
        Process p3=new Process(3,8,2);
        ArrayList<Process>p=new ArrayList<>();
        p.add(p1);
        p.add(p2);
        p.add(p3);
        SJF_Prem sch=new SJF_Prem();
        Queue<LiveTime> q =sch.Schedule(p,0);

//        double t =sch.calculate_avg_turn_around_time(p);
//        double t2 =sch.calculate_avg_wait_time(p);
        for(LiveTime o : q){
           o.printLiveTime();
//            System.out.println(o.);

        }

//        System.out.println("Turn around= "+t);
//        System.out.println("waiting= "+t2);
    }
}
