package com.cpuscheduler;

import java.util.*;

public class priorityNonPreemptiveScheduler implements Schedular  {


    @Override
    public  Queue<Process> Schedule(ArrayList<Process> processes  ) {return null;}

    public  Queue<Process> Schedule(ArrayList<Process> processes ,int currentTime ) {

        ArrayList<Process> myProcesses = (ArrayList<Process>) processes.clone();

        Queue<Process> queue = new LinkedList<Process>();
        int time = 0;
      while (!myProcesses.isEmpty()) {
          ArrayList<Process> arrivedProcesses = new ArrayList<Process>();

          for (Process process : myProcesses) {
              if (process.getArrivalTime() <= time) {
                  arrivedProcesses.add(process);

              }
          }

if (arrivedProcesses.isEmpty()) {
    time++;
    continue;}
          int currentMinPriority = arrivedProcesses.get(0).getPriority();
          for (Process arrivedProcess : arrivedProcesses) {
              if (arrivedProcess.getPriority() <= currentMinPriority) {
                  currentMinPriority = arrivedProcess.getPriority();

              }
          }

          ArrayList<Process> lessPriority = new ArrayList<Process>();
          for (Process arrivedProcess : arrivedProcesses) {
              if (arrivedProcess.getPriority() == currentMinPriority) {
                  lessPriority.add(arrivedProcess);

              }

          }


          int currentMinArrivalTime = lessPriority.get(0).getArrivalTime();
          for (Process process : lessPriority) {
              if (process.getArrivalTime() <= currentMinArrivalTime) {
                  currentMinArrivalTime= process.getArrivalTime();
              }
          }

          ArrayList<Process> leastPriorityEqualArrivalTime = new ArrayList<Process>();
          for( Process process : lessPriority) {
              if (process.getArrivalTime() == currentMinArrivalTime)
                  leastPriorityEqualArrivalTime.add(process);

          }

          myProcesses.remove(leastPriorityEqualArrivalTime.get(0));
          time += leastPriorityEqualArrivalTime.get(0).getBurstTime();

          leastPriorityEqualArrivalTime.get(0).setRemaining_burst_time(leastPriorityEqualArrivalTime.get(0).getBurstTime());



          if(leastPriorityEqualArrivalTime.get(0).getEnd_time()>= currentTime ){
              if(currentTime != 0 && queue.isEmpty() ){
                  leastPriorityEqualArrivalTime.get(0).setRemaining_burst_time(leastPriorityEqualArrivalTime.get(0).getBurstTime()-(currentTime-leastPriorityEqualArrivalTime.get(0).getStart_time()));
              }
          queue.add(leastPriorityEqualArrivalTime.get(0));
          }

          if(leastPriorityEqualArrivalTime.get(0).getEnd_time()==0 && currentTime!=0 ){
              if(currentTime != 0 && queue.isEmpty() ){
                  leastPriorityEqualArrivalTime.get(0).setRemaining_burst_time(leastPriorityEqualArrivalTime.get(0).getBurstTime()-(currentTime-leastPriorityEqualArrivalTime.get(0).getStart_time()));
              }
              queue.add(leastPriorityEqualArrivalTime.get(0));
          }

      }//While



        return queue;
    }

    @Override
    public double calculate_avg_wait_time(ArrayList<Process> processes){return 0;}
    public double calculate_avg_wait_time_F(ArrayList<Process> processes, int currentTime) {
        setProcessStartTimeAndEndTime(processes , currentTime);
        double counter = 0.0;
        for(Process process : processes){
            counter += process.getStart_time()-process.getArrivalTime();
        }
        return counter/processes.size();
    }

    @Override
    public double calculate_avg_turn_around_time(ArrayList<Process> processes){return 0;}


    public double calculate_avg_turn_around_time_F(ArrayList<Process> processes, int currentTime) {
        setProcessStartTimeAndEndTime(processes , currentTime);
        double counter = 0.0;
        for(Process process : processes){
            counter += process.getEnd_time()-process.getArrivalTime();
        }
        return counter/processes.size();
    }


public void setProcessStartTimeAndEndTime(ArrayList<Process> processes ,int currentTime){



    Queue<Process> queue= this.Schedule(processes , currentTime);


    int time = 0 ;

    for (Process process : queue) {
        if (process.getArrivalTime()<=time){

            process.setStart_time(time);
            time += process.getRemaining_burst_time();

        }
        else {
            process.setStart_time(process.getArrivalTime());
            time = process.getArrivalTime()+ process.getBurstTime();
        }

    process.setEnd_time(process.getStart_time()+process.getRemaining_burst_time());
      //  System.out.println("Process ID "+process.getProcess_ID()+" with Start time "+process.getStart_time()+" and end time "+process.getEnd_time());
    }

    }
}
