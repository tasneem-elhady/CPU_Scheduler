package com.cpuscheduler;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class FCFS implements Schedular  {

   
    public void sortProcess(ArrayList<Process> P) throws NullPointerException{
    	Process temp = new Process();
    	for(int i = 0 ; i < P.size(); i++) {
    		for(int j = 1 ; j < P.size()-i ; j++) {
    			if(P.get(j-1).getArrivalTime()> P.get(j).getArrivalTime()) {
    				temp = P.get(j-1);
    				P.set(j-1, P.get(j));
    				P.set(j, temp);
    			}
    		}
    	}
    }

	@Override
    public  Queue<Process> Schedule(ArrayList<Process> processes) {return null;}

	public Queue<Process> Schedule(ArrayList<Process> Processes,int currentTime){

		ArrayList<Process> myProcesses = (ArrayList<Process>) Processes.clone();

		Queue<Process> queue = new LinkedList<Process>();

        int end=0;
        int time = 0;
		while (!myProcesses.isEmpty()) {
		sortProcess(myProcesses);
		ArrayList<Process> arrivedProcesses = new ArrayList<Process>();

          for (Process process : myProcesses) {
              if (process.getArrivalTime() <= time) {
                  arrivedProcesses.add(process);

              }
          }
		  if (arrivedProcesses.isEmpty()) {
			time++;
			continue;}
		
		ArrayList<Process> MinArrivalTime = new ArrayList<Process>();
		MinArrivalTime.add(arrivedProcesses.get(0));

		myProcesses.remove(MinArrivalTime.get(0));
		time += MinArrivalTime.get(0).getBurstTime();
		MinArrivalTime.get(0).setRemaining_burst_time(MinArrivalTime.get(0).getBurstTime());
        
		if(MinArrivalTime.get(0).getEnd_time()>= currentTime ){
			if(currentTime != 0 && queue.isEmpty() ){
				MinArrivalTime.get(0).setRemaining_burst_time(MinArrivalTime.get(0).getBurstTime()-(currentTime-MinArrivalTime.get(0).getStart_time()));
			}
		queue.add(MinArrivalTime.get(0));
		}

		if(MinArrivalTime.get(0).getEnd_time()==0 && currentTime!=0 ){
			if(currentTime != 0 && queue.isEmpty() ){
				MinArrivalTime.get(0).setRemaining_burst_time(MinArrivalTime.get(0).getBurstTime());
			}
			queue.add(MinArrivalTime.get(0));
		}
	}

		
	for (Process p : queue) {
			if(  p.getArrivalTime() > end ){
	       p.setStart_time( p.getArrivalTime());
		   end = (p.getArrivalTime()+p.getRemaining_burst_time());
		   p.setEnd_time(end);
	
			}
			else{
				p.setStart_time( end);
				end=end+p.getBurstTime();
				p.setEnd_time(end);
	
			}
		   
	  }
	  return queue;
	}


	@Override

	public double calculate_avg_wait_time(ArrayList<Process> processes){
		Queue<Process> queue= this.Schedule(processes , 0);
		double totalTime = 0;
		double totalWaiting = 0;
		int index = 0;
		for (Process process : queue) {
			if (index == 0) {
				totalTime += process.getBurstTime();
				index++;
				continue;
			}
			if (process.getArrivalTime() >= totalTime) {
				totalTime = process.getBurstTime()+ process.getArrivalTime();
			}
			else {
				totalWaiting += totalTime - process.getArrivalTime();
				totalTime += process.getBurstTime();
			}
			index++;
		}
		return (totalWaiting/queue.size());
	}
@Override
public double calculate_avg_turn_around_time(ArrayList<Process> processes) {
	Queue<Process> queue= this.Schedule(processes , 0);
	double totalTime = 0;
	double totalTurnaround = 0;
	int index = 0;
	
	for (Process process : queue) {
		if (index == 0) {
			totalTime +=process.getBurstTime();
			totalTurnaround += process.getBurstTime();
			index++;
			continue;
		}
		if (process.getArrivalTime() > totalTime) {
			totalTurnaround += process.getBurstTime();
			totalTime = process.getBurstTime() + process.getArrivalTime();
		}
		else {
			totalTurnaround += totalTime - process.getArrivalTime()+process.getBurstTime();
			totalTime +=process.getBurstTime();
		}
		index++;
	}
	return (totalTurnaround/queue.size());
}
}