package com.cpuscheduler;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class FxmlHelper {
    public static void clear_table(GridPane addingBar, SimpleIntegerProperty number_of_added_process, TableView processes, ArrayList<Process>data){
        addingBar.setDisable(false);
        number_of_added_process.set(0);
        processes.getItems().clear();
        data.clear();
    }
    public static void printQueue(Queue<Process> queue , int currentTime) {

        Queue<Process> clonedList = new LinkedList<>() ;
        for (Process p : queue) {
            clonedList.add(p);

        int queueSize = queue.size(); // Get the queue size
        int currentIndex = 0;

        while (currentIndex < queueSize) {
            Process process = clonedList.poll(); // Remove and return the head element
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
    public static void printQueue(Queue<LiveTime> queue ) {

        Queue<Process> clonedList = new LinkedList<>() ;
        for (LiveTime p : queue) {
            clonedList.add(p.getP());

            int queueSize = queue.size(); // Get the queue size
            int currentIndex = 0;

            while (currentIndex < queueSize) {
                Process process = clonedList.poll(); // Remove and return the head element
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

    public static ArrayList<Process> cloneList(ArrayList<Process> List) {
        ArrayList<Process> clonedList = new ArrayList<Process>(List.size());
        for (Process p : List) {
            clonedList.add(p);
        }
        return clonedList;
    }
    public static Schedular initializeScheduler(String type){
        switch (type){
            case "FCFS":

                break;
            case "preemptive SJF":

                return new SJF_Prem();
            case"non-preemptive SJF":
                return new SJF_Non_Prem();

            case "Round Robin":
                return new RoundRobin();
            case "preemptive priority":

                break;
            case"non-preemptive priority":
                return new priorityNonPreemptiveScheduler();

        }
        return null;
    }
    public static void draw_process(Pane pane, double x, double width, String id, int start_time){
        Rectangle process_block = new Rectangle();
        process_block.yProperty().bind(pane.heightProperty().divide(2));
        process_block.setX(x);
        process_block.setWidth(width);
        process_block.heightProperty().bind(pane.heightProperty().divide(3));
        process_block.setFill(new Color(.03, .43 , .12 ,.22));


        Line l= new Line();
        l.setEndX(x);l.setStartX(x);
        l.startYProperty().bind(process_block.yProperty());
        l.endYProperty().bind(process_block.yProperty().add(process_block.heightProperty()));
        l.setStroke(Color.BLACK);
        l.setStrokeWidth(1);

        Text label;
        label = new Text();
        label.setText(""+start_time);
        label.xProperty().bind(l.startXProperty().subtract(5));
        label.yProperty().bind(l.endYProperty().add(15));
        label.setStyle("-fx-font-size:15px;-fx-fill:black;");

        Text process_label;
        process_label = new Text();
        process_label.setText(id);
        process_label.xProperty().bind(process_block.xProperty().add(process_block.widthProperty().divide(3)));
        process_label.yProperty().bind(process_block.yProperty().add(process_block.heightProperty().divide(2)));
        process_label.setStyle("-fx-font-size:15px;-fx-fill:red;");

        pane.getChildren().add(process_block);
        pane.getChildren().add(label);
        pane.getChildren().add(process_label);
        pane.getChildren().add(l);
    }

    public static Rectangle draw_process_trial (Pane pane, double x, double width, String id) {
        Rectangle process_block = new Rectangle();
        process_block.yProperty().bind(pane.heightProperty().divide(2));
        process_block.setX(x);
        process_block.setWidth(width);
        process_block.heightProperty().bind(pane.heightProperty().divide(3));
        process_block.setFill(new Color(.03, .43, .12, .22));


        Text process_label;
        process_label = new Text();
        process_label.setText(id);
        process_label.xProperty().bind(process_block.xProperty().add(process_block.widthProperty().divide(3)));
        process_label.yProperty().bind(process_block.yProperty().add(process_block.heightProperty().divide(2)));
        process_label.setStyle("-fx-font-size:15px;-fx-fill:red;");
        pane.getChildren().add(process_block);
        pane.getChildren().add(process_label);
        return process_block;
    }
    public static void draw_line(Pane pane, Rectangle process_block, int time){
        Line l= new Line();
        double x = process_block.getX();
        l.setEndX(x);l.setStartX(x);
        l.startYProperty().bind(process_block.yProperty());
        l.endYProperty().bind(process_block.yProperty().add(process_block.heightProperty()));
        l.setStroke(Color.BLACK);
        l.setStrokeWidth(1);

        Text label;
        label = new Text();
        label.setText(""+ time);
        label.xProperty().bind(l.startXProperty().subtract(5));
        label.yProperty().bind(l.endYProperty().add(15));
        label.setStyle("-fx-font-size:15px;-fx-fill:black;");
        pane.getChildren().add(l);
        pane.getChildren().add(label);
    }

    public static void draw_chart(Queue<Process> processes,Pane pane){
        double x = pane.getLayoutX()+25;
        int end_of_process = 0;
        for (Process p :processes)
        {
            if (end_of_process != p.getStart_time()+p.getBurstTime())
            {
                Line l= new Line();
                l.setEndX(x);l.setStartX(x);
                l.startYProperty().bind(pane.heightProperty().divide(2));
                l.endYProperty().bind(l.startYProperty().add(pane.heightProperty().divide(3)));
                l.setStroke(Color.BLACK);
                l.setStrokeWidth(1);
                Text label;
                label = new Text();
                label.setText(""+end_of_process);
                label.xProperty().bind(l.startXProperty().subtract(5));
                label.yProperty().bind(l.endYProperty().add(15));
                label.setStyle("-fx-font-size:15px;-fx-fill:black;");
                pane.getChildren().add(label);
                pane.getChildren().add(l);
                x += ((p.getStart_time() - end_of_process)*25);
            }

            draw_process(pane, x, 25 * p.getBurstTime(), "P"+p.getProcess_ID(), p.getStart_time());
            end_of_process = p.getEnd_time();
            x += 25 * p.getBurstTime();
        }
        Line l= new Line();
        l.setEndX(x);l.setStartX(x);
        l.startYProperty().bind(pane.heightProperty().divide(2));
        l.endYProperty().bind(l.startYProperty().add(pane.heightProperty().divide(3)));
        l.setStroke(Color.BLACK);
        l.setStrokeWidth(1);
        Text label;
        label = new Text();
        label.setText(""+end_of_process);
        label.xProperty().bind(l.startXProperty().subtract(5));
        label.yProperty().bind(l.endYProperty().add(15));
        label.setStyle("-fx-font-size:15px;-fx-fill:black;");
        pane.getChildren().add(label);
        pane.getChildren().add(l);
    }
    public static void draw_chart_PS(Queue<Process> processes,Pane pane){
        double x = pane.getLayoutX()+25;
        int end_of_process = 0;
        for (Process p :processes)
        {
            if (end_of_process != p.getStart_time()+p.getTime_taken())
            {
                Line l= new Line();
                l.setEndX(x);l.setStartX(x);
                l.startYProperty().bind(pane.heightProperty().divide(2));
                l.endYProperty().bind(l.startYProperty().add(pane.heightProperty().divide(3)));
                l.setStroke(Color.BLACK);
                l.setStrokeWidth(1);
                Text label;
                label = new Text();
                label.setText(""+end_of_process);
                label.xProperty().bind(l.startXProperty().subtract(5));
                label.yProperty().bind(l.endYProperty().add(15));
                label.setStyle("-fx-font-size:15px;-fx-fill:black;");
                pane.getChildren().add(label);
                pane.getChildren().add(l);
                x += ((p.getStart_time() - end_of_process)*25);
            }

            draw_process(pane, x, 25 * p.getBurstTime(), "P"+p.getProcess_ID(), p.getStart_time());
            end_of_process = p.getCompleted_time();
            x += 25 * p.getBurstTime();
        }
        Line l= new Line();
        l.setEndX(x);l.setStartX(x);
        l.startYProperty().bind(pane.heightProperty().divide(2));
        l.endYProperty().bind(l.startYProperty().add(pane.heightProperty().divide(3)));
        l.setStroke(Color.BLACK);
        l.setStrokeWidth(1);
        Text label;
        label = new Text();
        label.setText(""+end_of_process);
        label.xProperty().bind(l.startXProperty().subtract(5));
        label.yProperty().bind(l.endYProperty().add(15));
        label.setStyle("-fx-font-size:15px;-fx-fill:black;");
        pane.getChildren().add(label);
        pane.getChildren().add(l);
    }
    public static void draw_chart_RR(Queue<LiveTime> processes,Pane pane){
        double x = pane.getLayoutX()+25;
        int end_of_process = 0;
        for (LiveTime p :processes)
        {
            if (end_of_process != p.getLiveTime())
            {
                Line l= new Line();
                l.setEndX(x);l.setStartX(x);
                l.startYProperty().bind(pane.heightProperty().divide(2));
                l.endYProperty().bind(l.startYProperty().add(pane.heightProperty().divide(3)));
                l.setStroke(Color.BLACK);
                l.setStrokeWidth(1);
                Text label;
                label = new Text();
                label.setText(""+end_of_process);
                label.xProperty().bind(l.startXProperty().subtract(5));
                label.yProperty().bind(l.endYProperty().add(15));
                label.setStyle("-fx-font-size:15px;-fx-fill:black;");
                pane.getChildren().add(label);
                pane.getChildren().add(l);
                x += ((p.getLiveTime() - end_of_process)*25);
            }
            draw_process(pane, x, 25 * p.getCompletedTime(), "P"+p.getP().getProcess_ID(), p.getLiveTime());
            end_of_process = p.getLiveTime() + p.getCompletedTime();
            x += 25 * p.getCompletedTime();
        }
        Line l= new Line();
        l.setEndX(x);l.setStartX(x);
        l.startYProperty().bind(pane.heightProperty().divide(2));
        l.endYProperty().bind(l.startYProperty().add(pane.heightProperty().divide(3)));
        l.setStroke(Color.BLACK);
        l.setStrokeWidth(1);
        Text label;
        label = new Text();
        label.setText(""+end_of_process);
        label.xProperty().bind(l.startXProperty().subtract(5));
        label.yProperty().bind(l.endYProperty().add(15));
        label.setStyle("-fx-font-size:15px;-fx-fill:black;");
        pane.getChildren().add(label);
        pane.getChildren().add(l);
    }

}