package com.cpuscheduler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class scheduler_app extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(scheduler_app.class.getResource("mainFXML.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("CPU Scheduler Simulator");
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("ICON.jpg")));
        stage.show();
    }

    public static void main(String[] args) {



        Process p1 = new Process(1, 0, 3, 1);
        Process p2 = new Process(2, 40, 5, 2);
        Process p3 = new Process(3, 30, 5, 1);
        Process p4 = new Process(4, 2, 7, 3);
        Process p5 = new Process(5, 0, 6, 2);
        ArrayList<Process> processes = new ArrayList<Process>(1);
        processes.add(p1);
        processes.add(p2);
        processes.add(p3);
        processes.add(p4);
        processes.add(p5);

        priorityNonPreemptiveScheduler par = new priorityNonPreemptiveScheduler();




        Queue<Process> queue = par.Schedule(processes, 0);
        par.setProcessStartTimeAndEndTime(processes, 0);

        System.out.println("Before adding");
        System.out.println("--------------------------------------------------------");
        printQueue(queue,0);
        System.out.println("--------------------------------------------------------");

//     System.out.println("Average turn around time " + par.calculate_avg_turn_around_time_F(processes, 0) + " time unit ");
//       System.out.println("Average waiting time " + par.calculate_avg_wait_time_F(processes, 0) + " time unit ");

        int currentTime = 5;
        Process p11 = new Process(11,5,4,1);
        processes.add(p11);
        System.out.println("After adding");
        System.out.println("--------------------------------------------------------");
        queue = par.Schedule(processes, currentTime);
        par.setProcessStartTimeAndEndTime(processes, currentTime);
        printQueue(queue,currentTime);
        System.out.println("--------------------------------------------------------");



        
        launch();
    }





     public static void printQueue(Queue<Process> queue , int currentTime) {


        int queueSize = queue.size(); // Get the queue size
        int currentIndex = 0;

        while (currentIndex < queueSize) {
            Process process = queue.poll(); // Remove and return the head element
            if (process != null) {
//                if (currentIndex ==0 ){
//                   process.setRemaining_burst_time(process.getEnd_time()-currentTime);
//                }
                System.out.println("Process:");
                System.out.println("  - Process ID: " + process.getProcessID());
                System.out.println("  - Remaining Burst Time: " + process.getRemaining_burst_time());
                System.out.println("  - Priority: " + process.getPriority());
                System.out.println("Start time " +process.getStart_time()+" End time "+process.getEnd_time());
            }
            currentIndex++;
        }
   }
    }
}
