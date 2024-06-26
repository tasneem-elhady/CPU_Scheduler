package com.cpuscheduler;

import java.util.*;

public class RoundRobin implements Schedular{

    private int no_of_processes;
    private ArrayList<Process> processesCopy;


    public  ArrayList<Process> getProcessesCopy() {
        return processesCopy;
    }

    public void setProcessesCopy(ArrayList<Process> processesCopy) {
        this.processesCopy = processesCopy;
    }


    public int getNo_of_processes() {
        return no_of_processes;
    }

    public void setNo_of_processes(int no_of_processes) {
        this.no_of_processes = no_of_processes;
    }

    @Override
    public Queue<Process> Schedule(ArrayList<Process> processes) {
        return null;
    }

    public Queue<LiveTime> Schedule(ArrayList<Process> processes,int quantum_time) {
        Queue<LiveTime> liveTimeQueue =new LinkedList<>() ;

        int diff, no_of_switches=0,time=0;
        boolean first =true;
        setNo_of_processes(processes.size());
        processesCopy= (ArrayList<Process>) processes.clone();
        Collections.sort(processesCopy);

        Queue<Process> Output = new LinkedList<Process>();
        ArrayList<Process> completedProcesses = new ArrayList<>();
        while(!processesCopy.isEmpty()){

            //check if there is only one process in the arraylist run it totally without slots
            while (checkNoPAtTime(processesCopy,time) && first){
                time++;
            }
            first=false;
            if(processesCopy.size()==1){
                while (processesCopy.get(0).getArrivalTime()>time){
                    time++;
                }
                    Process p = processesCopy.get(0);
                    Output.add(p);
                    p.setCompleted_time(p.getRemaining_burst_time());
                    p.setEnd_time(time+ p.getRemaining_burst_time());
                    completedProcesses.add(p);
                    liveTimeQueue.add(new LiveTime(time, 0, p.getCompleted_time(), p.getBurstTime(), p));
                    time += p.getRemaining_burst_time();
                    p.setRemaining_burst_time(0);



            }else {
                for (Process p : processesCopy) {
                    if(p.getArrivalTime()<=time){
                        if (p.isFirst_response()) {
                            p.setStart_time(time);
                            p.setFirst_response(false);
                        }
                        diff = p.getRemaining_burst_time() - quantum_time;
                        if (diff > 0) {
                            p.setCompleted_time(quantum_time);

                            p.setRemaining_burst_time(diff);

                            Output.add(p);
                            liveTimeQueue.add(new LiveTime(time, p.getRemaining_burst_time(), p.getCompleted_time(), (p.getBurstTime() - p.getRemaining_burst_time()), p));
                            time += quantum_time;
                        } else {
                            p.setCompleted_time(p.getRemaining_burst_time());

                            Output.add(p);

                            p.setEnd_time(time + p.getRemaining_burst_time());
                            liveTimeQueue.add(new LiveTime(time, 0, p.getCompleted_time(), p.getBurstTime() , p));
                            time += p.getRemaining_burst_time();
                            p.setRemaining_burst_time(0);
                            completedProcesses.add(p);

                        }
                        no_of_switches++;
                    }
                }
            }

            //remove the completed processesCopy from the current processesCopy arraylist
            if(!completedProcesses.isEmpty()) {
                for (Process p : completedProcesses) {
                    processesCopy.remove(p);

                }
            }
        }
        for (Process p : processes)
        {
            p.setRemaining_burst_time(p.getBurstTime());
        }

        return liveTimeQueue;
    }


    public boolean checkNoPAtTime(ArrayList<Process> processes, int time){
        boolean flag_no=true;
        for (Process p:processes
             ) {
            if(p.getArrivalTime()==time) {
                flag_no = false;
                break;
            }
        }
        return flag_no;
    }

    @Override
    public double calculate_avg_wait_time(ArrayList<Process> processes) {
        double sum=0;
        for(Process p :processes){
            sum+=p.waitingTime();
        }

        return (sum/ getNo_of_processes());
    }

    @Override
    public double calculate_avg_turn_around_time(ArrayList<Process> processes) {
        double sum=0;
        for(Process p :processes){
            sum+=p.turnAroundTime();
        }
        return (sum/ getNo_of_processes());
    }
    public static ArrayList<ByTime>  choosenProcess(Queue<LiveTime> result){

        ArrayList<ByTime> t_by_t =new ArrayList<>();
        System.out.println(t_by_t);
        int time=0;
        for (LiveTime p: result) {
            for (int i=0;i<p.getCompletedTime();i++){
                t_by_t.add(new ByTime(time,p.getP(),p.getRemainingTime()+p.getCompletedTime()-i - 1));
                time++;
            }
        }

//        System.out.println("in round robinnn/////////////////////////////////////////////////////////////////////////////////" + result);
//        System.out.println("in round robinnn/////////////////////////////////////////////////////////////////////////////////" + t_by_t);
        return t_by_t;

}
    public static void main(String[] args) {
        ArrayList<Process> processes = new ArrayList<>();
        processes.add(new Process(1,2,5));
        processes.add(new Process(2,0,3));
        processes.add(new Process(3,1,1));
//        processes.add(new Process(4,0,1));
//        processes.add(new Process(5,0,5));


        Queue<LiveTime> output;
        RoundRobin RR = new RoundRobin();
        output=(RR).Schedule(processes,2);

            while (!output.isEmpty()){
            output.peek().printLiveTime();
            System.out.println("-----------------------------------------------------------");
            output.remove();
        }

        System.out.println("Average waiting time = "+RR.calculate_avg_wait_time(processes));
        System.out.println("Average turnaround time = "+RR.calculate_avg_turn_around_time(processes));


    }

}

class ByTime{
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
