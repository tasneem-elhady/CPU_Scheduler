package com.cpuscheduler;

import java.util.*;

public class RoundRobin implements Schedular{

    private int no_of_processes;
    private ArrayList<Process> processesCopy;
//    private ArrayList<Integer> completedTime=new ArrayList<>();
//    private ArrayList<Integer> accumlativeCompletedTime=new ArrayList<>();
//    private ArrayList<Integer> remainingTime=new ArrayList<>();
    private Queue<LiveTime> liveTimeQueue =new LinkedList<>() ;

    public Queue<LiveTime> getLiveTimeQueue() {
        return liveTimeQueue;
    }

    public  ArrayList<Process> getProcessesCopy() {
        return processesCopy;
    }

    public void setProcessesCopy(ArrayList<Process> processesCopy) {
        this.processesCopy = processesCopy;
    }

//    public ArrayList<Integer> getCompletedTime() {
//        return completedTime;
//    }
//
//    public void setCompletedTime(ArrayList<Integer> completedTime) {
//        this.completedTime = completedTime;
//    }
//
//    public ArrayList<Integer> getRemainingTime() {
//        return remainingTime;
//    }
//
//    public void setRemainingTime(ArrayList<Integer> remainingTime) {
//        this.remainingTime = remainingTime;
//    }
//

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
        int diff, no_of_switches=0,time=0;
        setNo_of_processes(processes.size());
        processesCopy= new ArrayList<>(processes);
//        processesCopy=new ArrayList<>(no_of_processes);
//        Collections.copy(processesCopy,processes);
        Queue<Process> Output = new LinkedList<Process>();
        ArrayList<Process> completedProcesses = new ArrayList<>();
        while(!processes.isEmpty()){
//            ListIterator<Process> pItr = processes.listIterator();
//            while (pItr.hasNext()){
//                Process p=pItr.next();
            //check if there is only one process in the arraylist run it totally without slots
            if(processes.size()==1){
                Process p=processes.get(0);
                Output.add(p);
                p.setCompleted_time(p.getRemaining_burst_time());
                p.setRemaining_burst_time(0);
                p.setEnd_time(time);
                completedProcesses.add(p);
                liveTimeQueue.add(new LiveTime(time,p.getRemaining_burst_time(),p.getCompleted_time(),(p.getBurstTime()-p.getRemaining_burst_time())));
                time+=p.getRemaining_burst_time();

            }else {
                for (Process p : processes) {
                    if (p.isFirst_response()) {
                        p.setStart_time(time);
                        p.setFirst_response(false);
                    }
                    diff = p.getRemaining_burst_time() - quantum_time;
                    if (diff > 0) {
                        p.setCompleted_time(quantum_time);
//                    completedTime.add(quantum_time);
                        p.setRemaining_burst_time(diff);
//                    remainingTime.add(diff);
                        Output.add(p);
//                    accumlativeCompletedTime.add(p.getBurstTime()-p.getRemaining_time());
//                    processes.add(p);
                        liveTimeQueue.add(new LiveTime(time,p.getRemaining_burst_time(), p.getCompleted_time(), (p.getBurstTime() - p.getRemaining_burst_time()),p));
                        time += quantum_time;
                    } else {
                        p.setCompleted_time(p.getRemaining_burst_time());
//                    completedTime.add(p.getRemaining_time());
                        Output.add(p);
//                    no_of_processes--;
                        p.setEnd_time(time+p.getRemaining_burst_time());
                        liveTimeQueue.add(new LiveTime(time,p.getRemaining_burst_time(), p.getCompleted_time(), (p.getBurstTime() - p.getRemaining_burst_time()),p));
                        time += p.getRemaining_burst_time();
                        p.setRemaining_burst_time(0);
//                    remainingTime.add(0);
//                    accumlativeCompletedTime.add(p.getBurstTime()-p.getRemaining_time());
                        completedProcesses.add(p);
//                    processes.remove(p);
//                    pItr.remove();
//                    pItr=processes.listIterator();
                    }
                    no_of_switches++;
                }
            }
//            while (!completedProcesses.isEmpty()){
//                Process p=
//                processes.remove(p);
//                completedProcesses.remove(p);
//            }
            //remove the completed processes from the current processes arraylist
            if(!completedProcesses.isEmpty()) {
                for (Process p : completedProcesses) {
                    processes.remove(p);
//                completedProcesses.remove(p);
                }
            }
        }
//        return Output;
        return liveTimeQueue;
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

    public static void main(String[] args) {
        ArrayList<Process> processes = new ArrayList<>();
        processes.add(new Process(1,0,10));
        processes.add(new Process(2,0,1));
        processes.add(new Process(3,0,2));
        processes.add(new Process(4,0,1));
        processes.add(new Process(5,0,5));


        Queue<LiveTime> output;
        RoundRobin RR = new RoundRobin();
        output=(RR).Schedule(processes,3);
//        int i=0;
//        for (Process p:output) {
//            System.out.print(" | "+p.getProcess_ID());
//            System.out.println(p.getProcess_ID()+" : arrival= "+p.getArrivalTime()+" , burst= "+ p.getBurstTime()
//                    +", Accumulative completed = "+RR.accumlativeCompletedTime.get(i)
//                    +", completed in this period "+(i+1)+" = "+RR.getCompletedTime().get(i)+" , remaining= "+RR.getRemainingTime().get(i)
//            );
//            p.printProcess();
//        for (LiveTime p: output) {
            while (!output.isEmpty()){
            output.peek().printLiveTime();
            System.out.println("-----------------------------------------------------------");
            output.remove();
//            i++;
        }

        System.out.println("Average waiting time = "+RR.calculate_avg_wait_time(RR.getProcessesCopy()));
        System.out.println("Average turnaround time = "+RR.calculate_avg_turn_around_time(RR.getProcessesCopy()));


    }





}