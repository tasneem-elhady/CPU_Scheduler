package com.cpuscheduler;

import java.util.*;

public class PreemptivePriorityScheduling implements Schedular{
    private static Queue<Process> scheduledQueueP = new LinkedList<>();
    private static Queue<Integer> scheduledQueueRBT = new LinkedList<>();


    public static Queue<Integer> getScheduledQueueRBT() {
        return scheduledQueueRBT;
    }
    public static Queue<Process> getScheduledQueueP() {
        return scheduledQueueP;
    }

    //Insert a process into the heap
    static void insert(Process[] heap, Process value, int[] heapSize, int[] currentTime) {
        int start = heapSize[0];
        heap[heapSize[0]] = value;
        if (heap[heapSize[0]].getStart_time() == -1) {
            heap[heapSize[0]].setStart_time(currentTime[0]) ;
        }
        heapSize[0]++;

        // Ordering the Heap
        while (start != 0 && heap[(start - 1) / 2].getPriority() > heap[start].getPriority()) {
            Process temp = heap[(start - 1) / 2];
            heap[(start - 1) / 2] = heap[start];
            heap[start] = temp;
            start = (start - 1) / 2;
        }
    }

    // Reorder the heap based on priority
    static void order(Process[] heap, int heapSize, int start) {
        int smallest = start;
        int left = 2 * start + 1;
        int right = 2 * start + 2;
        if (left < heapSize && heap[left].getPriority() < heap[smallest].getPriority()) {
            smallest = left;
        }
        if (right < heapSize && heap[right].getPriority() < heap[smallest].getPriority()) {
            smallest = right;
        }

        // Ordering the Heap
        if (smallest != start) {
            Process temp = heap[smallest];
            heap[smallest] = heap[start];
            heap[start] = temp;
            order(heap, heapSize, smallest);
        }
    }

    // Extract the process with the highest priority from the heap
    static Process extractMinimum(Process[] heap, int[] heapSize, int[] currentTime) {
        Process minProcess = heap[0];
        if (minProcess.getresponseTime() == -1) {
            minProcess.setresponseTime( currentTime[0] - minProcess.getArrivalTime());
        }
        heapSize[0]--;
        if (heapSize[0] >= 1) {
            heap[0] = heap[heapSize[0]];
            order(heap, heapSize[0], 0);
        }
//        scheduledQueueP.add(minProcess);
        return minProcess;
    }

    // Executing the highest priority process extracted from the heap
    static void scheduling(Process[] heap, Process[] array, int n, int[] heapSize, int[] currentTime) {
        if (heapSize[0] == 0) {
            return;
        }
        Process minProcess = extractMinimum(heap, heapSize, currentTime);
        scheduledQueueP.add(minProcess);
        minProcess.setEnd_time(currentTime[0] + 1);
        minProcess.setBurstTime(minProcess.getBurstTime()-1);
        scheduledQueueRBT.add(minProcess.getBurstTime());

        //System.out.println("process id = " + minProcess.getProcess_ID() + " current time = " + currentTime[0]+ " Remaining burstTime = " + minProcess.getBurstTime());

        // If the process is not finished then insert it back into the Heap
        if (minProcess.getBurstTime() > 0) {
            insert(heap, minProcess, heapSize, currentTime);
            return;
        }

        for (int i = 0; i < n; i++) {
            if (array[i].getProcess_ID() == minProcess.getProcess_ID()) {
                array[i] = minProcess;
                break;
            }
        }
    }

    //the entire execution of processes based on arrival time
    static void priority(Process[] array, int n) {
        Arrays.sort(array, Comparator.comparingInt(o -> o.getArrivalTime()));

        int totalBurstTime = 0;
        int insertedProcess = 0;
        int[] heapSize = {0};
        int[] currentTime = {array[0].getArrivalTime()};

        Process[] heap = new Process[4 * n];

        // Calculating the total burst time of the processes
        for (int i = 0; i < n; i++) {
            totalBurstTime += array[i].getBurstTime();
            array[i].settempBurstTime(array[i].getBurstTime());
            // System.out.println("id:"+array[i].getProcess_ID()+"bt"+array[i].tempBurstTime);
        }

        // Inserting the processes into Heap according to arrival time
        do {
            if (insertedProcess != n) {
                for (int i = 0; i < n; i++) {
                    if (array[i].getArrivalTime() == currentTime[0]) {
                        insertedProcess++;
                        array[i].setStart_time(-1) ;
                        //array[i].setresponseTime(-1);
                        insert(heap, array[i], heapSize, currentTime);
                    }
                }
            }
            scheduling(heap, array, n, heapSize, currentTime);

            currentTime[0]++;
            if (heapSize[0] == 0 && insertedProcess == n) {
                break;
            }
        } while (true);

    }
    @Override
    public double calculate_avg_wait_time(ArrayList<Process> processes){
        Process[] array = processes.toArray(new Process[0]);
        int n=processes.size();
        int totalWaitingTime = 0;
        for (int i = 0; i < n; i++) {
            totalWaitingTime += (array[i].getEnd_time() - array[i].getStart_time() - array[i].gettempBurstTime());}
        return((double) totalWaitingTime / n);
    }
    @Override
    public double calculate_avg_turn_around_time(ArrayList<Process> processes){
        Process[] array = processes.toArray(new Process[0]);
        int n=processes.size();
        int totalBurstTime = 0;
        int totalTurnaroundTime = 0;
        for (int i = 0; i < n; i++) {
            totalTurnaroundTime += (array[i].getEnd_time() - array[i].getStart_time());
            totalBurstTime += array[i].getBurstTime();}
        return ((double)totalTurnaroundTime/n);
    }
    public Queue<Process> Schedule(ArrayList<Process> processList) {
        Process[] b = processList.toArray(new Process[0]);
        priority(b,processList.size());
        System.out.println("Average waiting time = "+calculate_avg_wait_time(processList));
        System.out.println("Average turn around time = "+calculate_avg_turn_around_time(processList));
        System.out.println("Scheduled Processes:");

        Queue<Process>ScheduleQueue=copyQueue(PreemptivePriorityScheduling.getScheduledQueueP());
        Queue<Process>ScheduleQueuereturn=copyQueue(PreemptivePriorityScheduling.getScheduledQueueP());
        Queue<Integer>scheduledQueueRBTime= copyQueue(PreemptivePriorityScheduling.getScheduledQueueRBT());

        while (!ScheduleQueue.isEmpty()) {
            Process process = ScheduleQueue.poll();
            Integer processRBT =scheduledQueueRBTime.poll();
            System.out.println("Process ["+ process.getProcess_ID()+"]\t start time= " + process.getStart_time() +"\tend time=" +process.getEnd_time() +"\t\tRemainingBurstTime=\t"+processRBT);
        }
        return ScheduleQueuereturn ;}

    public static <T> Queue<T> copyQueue(Queue<T> originalQueue) {
        Queue<T> copiedQueue = new LinkedList<>();
        for (T element : originalQueue) {
            copiedQueue.offer(element);
        }
        return copiedQueue;
    }
    //Driver code
    public static void main(String[] args) {

        ArrayList<Process> processList = new ArrayList<>();

        processList.add(new Process(1, 4, 2, 6));
        processList.add(new Process(4, 5, 1, 3));
        processList.add(new Process(2, 5, 3, 1));
        processList.add(new Process(3, 1, 4, 2));
        processList.add(new Process(5, 3, 5, 4));


        /*
        Process[] b = processList.toArray(new Process[0]);
        priority(b,processList.size());
        System.out.println("Average waiting time = "+calculate_avg_wait_time(processList));
        System.out.println("Average turn around time = "+calculate_avg_turn_around_time(processList));
        */

        /*Queue<Process> ScheduleQueue = Schedule(processList);
        Queue<Integer> scheduledQueueRBTime = copyQueue(PriorityScheduling.getScheduledQueueRBT());
        // the scheduled processes
        System.out.println("Scheduled Processes:");
        while (!ScheduleQueue.isEmpty()) {
           Process process = ScheduleQueue.poll();
           Integer processRBT =scheduledQueueRBTime.poll();
            System.out.println("mainProcess ["+ process.getProcess_ID()+"]\t start time= " + process.getStart_time() +"\tend time=" +process.getEnd_time() +"\t\tRemainingBurstTime=\t"+processRBT);
        }*/

    }
}
