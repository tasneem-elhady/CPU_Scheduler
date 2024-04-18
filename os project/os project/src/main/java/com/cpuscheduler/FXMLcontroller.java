package com.cpuscheduler;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ResourceBundle;

public class FXMLcontroller implements Initializable {
    @FXML
    RadioButton LiveMode;
    @FXML
    private TextField no_processes;
    @FXML
    private ComboBox SchedulerType;
    @FXML
    private TextField ProcessIDTF;
    @FXML
    private TextField ArrivalTimeTF;
    @FXML
    private TextField BurstTimeTF;
    @FXML
    private TextField PriorityTF;
    @FXML
    private HBox QuantumTime;
    @FXML
    private TextField QuantumTimeTF;
    @FXML
    private Button Resetbtn;
    @FXML
    private TableView<processInTable> processesIntable;
    @FXML
    private TableColumn<processInTable, String> processIDCol;
    @FXML
    private TableColumn<processInTable, String> arrivalTimeCol;
    @FXML
    private TableColumn<processInTable, String> burstTimeCol;
    @FXML
    private TableColumn<processInTable, String> PriorityCol;
    @FXML
    private TableColumn<processInTable, String> remainingTimecol;
    @FXML
    private GridPane addingBar;
    @FXML
    private Button Computebtn;
    @FXML
    private TextField AvgWaitingTimeTF;
    @FXML
    private TextField AvgTurnaroundTimeTF;
    @FXML
    private Button generateChart;
    @FXML
    private TextField ProcessIDTFL;
//    @FXML
//    private TextField ArrivalTimeTFL;
    @FXML
    private TextField BurstTimeTFL;
    @FXML
    private TextField PriorityTFL;
    @FXML
    private Label priorityL;
    @FXML
    private Button Addbtn;
    @FXML
    private Button Runbtn;
    @FXML
    private AnchorPane chart_pane;
    @FXML
    private AnchorPane live_pane;
    @FXML
    private ScrollPane chart_scroll_pane;
    @FXML
    private ScrollPane live_scroll_pane;

    AnchorPane chart = new AnchorPane();
    AnchorPane live = new AnchorPane();
    SimpleIntegerProperty number_of_added_process = new SimpleIntegerProperty(0);

    ArrayList<Process> data = new ArrayList<>();

    Schedular schedule;

    int Live_current_time = 0;
    int Live_index = 0;
    int prev_process = -1;
    double x = live.getLayoutX();
    Queue<Process> output = new LinkedList<>();
    Queue<LiveTime> outputLiveTime = new LinkedList<>();
    ArrayList<ByTime> chosen_process = new ArrayList<>();

    Timeline tl;
    Rectangle process_block;


    boolean running = false;
    boolean added = false;

    //    function called first on openning scene
    public void initialize(URL url, ResourceBundle rb) {
        //set up the columns in the table
        processIDCol.setCellValueFactory(new PropertyValueFactory<processInTable, String>("ProcessID"));
        arrivalTimeCol.setCellValueFactory(new PropertyValueFactory<processInTable, String>("ArrivalTime"));
        burstTimeCol.setCellValueFactory(new PropertyValueFactory<processInTable, String>("BurstTime"));
        PriorityCol.setCellValueFactory(new PropertyValueFactory<processInTable, String>("Priority"));
        remainingTimecol.setCellValueFactory(new PropertyValueFactory<processInTable, String>("remainingTime"));

        RESET();

        LiveMode.selectedProperty().addListener((observable, oldValue, newValue) -> {
            //if RadioButton is selected
            if (newValue) {
                RESET();
                live_pane.setDisable(false);
                no_processes.setDisable(true);
                addingBar.setDisable(true);
                no_processes.clear();
            }
            else {
                live_pane.setDisable(true);
                no_processes.setDisable(false);
                addingBar.setDisable(false);

            }
        });

        no_processes.textProperty().addListener((obs, oldText, newText) -> {
            RESET();

        });

        number_of_added_process.addListener((obs, oldText, newText) -> {
            if (!LiveMode.isSelected() && newText.equals(Integer.parseInt(no_processes.getText()))) {
                    ProcessIDTF.clear();
                    ArrivalTimeTF.clear();
                    BurstTimeTF.clear();
                    PriorityTF.clear();
                    addingBar.setDisable(true);
                    chart_pane .setDisable(false);
                    schedule = FxmlHelper.initializeScheduler(SchedulerType.getValue().toString());
                }
        });

        SchedulerType.valueProperty().addListener((obs, oldText, newText) -> {

            System.out.println("Scheduler has changed");

            System.out.println(SchedulerType.getValue());
            if(SchedulerType.getValue()!= null) {
                switch (newText.toString()) {
                    case "Round Robin":
//                FxmlHelper.clear_table(addingBar, number_of_added_process, processes);
                        QuantumTime.setVisible(true);
                        PriorityTF.setVisible(false);
                        PriorityTFL.setVisible(false);
                        priorityL.setVisible(false);
                        schedule = FxmlHelper.initializeScheduler(SchedulerType.getValue().toString());
                        break;
                    case "preemptive priority":
                    case "non-preemptive priority":
//                        FxmlHelper.clear_table(addingBar, number_of_added_process, processesIntable, data);
                        number_of_added_process.set(0);
                        processesIntable.getItems().clear();
                        data.clear();
                        QuantumTime.setVisible(false);
                        PriorityTF.setVisible(true);
                        PriorityTFL.setVisible(true);
                        priorityL.setVisible(true);
//                        schedule = FxmlHelper.initializeScheduler(SchedulerType.getValue().toString());
                        break;
                    default:
                        QuantumTime.setVisible(false);
                        PriorityTF.setVisible(false);
                        PriorityTFL.setVisible(false);
                        priorityL.setVisible(false);
                }
            }
        });
        tl = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            System.out.println("at run ////////////////////////////////////////////////////////////////////////////////////////");
            System.out.println("current time  "+Live_current_time);
            x+=30;
            if(SchedulerType.getValue().toString().equals("non-preemptive priority")) {
                Process p = output.peek();

                if (p.getRemaining_burst_time() == 1) {
                    output.remove();
//                not sure
                    data.remove(p);
                }
                p.setRemaining_burst_time(p.getRemaining_burst_time() - 1);
//            sets remaining time value in table
                p.getProcessIndex().setRemainingTime(p.getRemaining_burst_time() + "");
//            drawing the process
                if (prev_process == p.getProcess_ID())
                    FxmlHelper.draw_process_trial(live, x, 30, "P" + p.getProcess_ID(),Live_current_time);
                else
                    FxmlHelper.draw_process(live, x, 30, "P" + p.getProcess_ID(), Live_current_time);
                prev_process = p.getProcess_ID();
//                Live_current_time++;

                FxmlHelper.printQueue(output, Live_current_time);
                System.out.println("//////////////////////////////////////////////////////////////////////////////");
            }
            else if(SchedulerType.getValue().toString().equals("Round Robin")&& chosen_process.size() != Live_index) {
                System.out.println("Live index is " + Live_index);
                System.out.println(chosen_process.size());
//                System.out.println(outputLiveTime);
                chosen_process = RoundRobin.choosenProcess(outputLiveTime);
                System.out.println(chosen_process);
//                sets remaining time value in table
                chosen_process.get(Live_index).getP().getProcessIndex().setRemainingTime(chosen_process.get(Live_index).getRemaining_time()+ "");
//            drawing the process
                if (prev_process == chosen_process.get(Live_index).getP().getProcess_ID()) {
                    process_block = FxmlHelper.draw_process_trial(live, x, 30, "P" + chosen_process.get(Live_index).getP().getProcess_ID(), Live_current_time);
                }
                else {
                    process_block = FxmlHelper.draw_process(live, x, 30, "P" + chosen_process.get(Live_index).getP().getProcess_ID(), Live_current_time);
                }
                prev_process = chosen_process.get(Live_index).getP().getProcessID();
                if(chosen_process.size() - 1 == Live_index)
                    FxmlHelper.draw_line(live, process_block,Live_index+1);
                Live_index++;
////                Live_current_time++;
//                FxmlHelper.printQueue(output, Live_current_time);
            }
            else if(SchedulerType.getValue().toString().equals("preemptive priority") && output.size() != Live_index) {

                Process p = (Process) PreemptivePriorityScheduling.getScheduledQueueP().toArray()[Live_index];

                if (prev_process == p.getProcess_ID())
                    process_block = FxmlHelper.draw_process_trial(live, x, 30, "P" + p.getProcess_ID(),Live_index);
                else
                    process_block = FxmlHelper.draw_process(live, x, 30, "P" + p.getProcess_ID(), Live_current_time);
                p.getProcessIndex().setRemainingTime( PreemptivePriorityScheduling.getScheduledQueueRBT().toArray()[Live_index]+ "");
                prev_process = p.getProcess_ID();

                if(output.size() - 1 == Live_index)
                    FxmlHelper.draw_line(live, process_block,Live_index+1);
                Live_index++;

            }
            else if(SchedulerType.getValue().toString().equals("preemptive SJF") && output.size() != Live_current_time){
                System.out.println("at second " + Live_current_time+"/////////////////////////////////////////////////////////////\n");
                Process p = (Process) output.toArray()[Live_current_time];

                if (prev_process == p.getProcess_ID())
                    process_block = FxmlHelper.draw_process_trial(live, x, 30, "P" + p.getProcess_ID(),Live_current_time);
                else
                    process_block = FxmlHelper.draw_process(live, x, 30, "P" + p.getProcess_ID(), Live_current_time);
//                p.getProcessIndex().setRemainingTime( p.getRemaining_burst_time()+ "");
                prev_process = p.getProcess_ID();

                if(output.size() - 1 == Live_current_time)
                    FxmlHelper.draw_line(live, process_block,Live_current_time + 1);
//                Live_current_time++;
            }
            else if(SchedulerType.getValue().toString().equals("FCFS") && !output.isEmpty()) {
                Process p = output.peek();

                if (p.getRemaining_burst_time() == 1) {
                    output.remove();
//                not sure
                    data.remove(p);
                }
                p.setRemaining_burst_time(p.getRemaining_burst_time() - 1);
//            sets remaining time value in table
                p.getProcessIndex().setRemainingTime(p.getRemaining_burst_time() + "");
//            drawing the process
                if (prev_process == p.getProcess_ID())
                    process_block = FxmlHelper.draw_process_trial(live, x, 30, "P" + p.getProcess_ID(),Live_current_time);
                else
                    process_block = FxmlHelper.draw_process(live, x, 30, "P" + p.getProcess_ID(), Live_current_time);
                prev_process = p.getProcess_ID();
                if(output.isEmpty())
                    FxmlHelper.draw_line(live, process_block,Live_current_time + 1);
//                Live_current_time++;
            }
            Live_current_time++;
        }));
    }

    //    on action of compute button
    @FXML
    public void setNumber()
    {
        System.out.println("number of processes is set");
        FxmlHelper.clear_table(addingBar, number_of_added_process, processesIntable,data);
    }
//////////////////////////////////////////////////////////////////////////


    //    on action of compute button
    @FXML
    public void COMPUTE()
    {
        AvgWaitingTimeTF.setText((schedule.calculate_avg_wait_time(data)) + "");
        System.out.println(schedule.calculate_avg_wait_time(data));
        AvgTurnaroundTimeTF.setText((schedule.calculate_avg_turn_around_time(data)) + "");
        System.out.println(schedule.calculate_avg_turn_around_time(data));
    }
//////////////////////////////////////////////////////////////////////////

    //    on action of generate chart button
    @FXML
    public void GENERATE()
    {
        chart_scroll_pane.setContent(chart);
        System.out.println(data);
        ArrayList<Process> data_clone = FxmlHelper.cloneList(data);
        if(SchedulerType.getValue().toString().equals("Round Robin")) {
            int quanta = Integer.parseInt(QuantumTimeTF.getText());
//            System.out.println(quanta);
//            System.out.println(data);
            Queue<LiveTime> output;
            output = ((RoundRobin)schedule).Schedule(data_clone, quanta);
            System.out.println(output);
            FxmlHelper.draw_chart_RR(output, chart);
        }
        else if(SchedulerType.getValue().toString().equals("preemptive priority")) {
            System.out.println("preemptive priority trial////////////////////////////////////////////");
            double x = chart.getLayoutX() + 30;
            Queue<Process> output = schedule.Schedule(data_clone);
            FxmlHelper.draw_chart_PP(output,chart);
        }
        else if(SchedulerType.getValue().toString().equals("non-preemptive priority")) {
            Queue<Process> output = ((priorityNonPreemptiveScheduler)schedule).Schedule(data_clone,0);
            ((priorityNonPreemptiveScheduler)schedule).setProcessStartTimeAndEndTime(data_clone,0);
            FxmlHelper.draw_chart(output, chart);
        }
        else if(SchedulerType.getValue().toString().equals("non-preemptive SJF")) {
            Queue<Process> output = ((SJF_Non_Prem)schedule).Schedule(data_clone,0);
            FxmlHelper.draw_chart(output, chart);
        }
        else if(SchedulerType.getValue().toString().equals("preemptive SJF")) {
            double x = chart.getLayoutX() + 30;

            Queue<Process> output = schedule.Schedule(data_clone);
            System.out.println("//////////////////////////////////////////////////////////////////////\n" + output);
            FxmlHelper.draw_chart_PP(output,chart);
        }
        else if(SchedulerType.getValue().toString().equals("FCFS")) {
            Queue <Process> output = ((FCFS)schedule).Schedule(data_clone, 0);
            FxmlHelper.draw_chart(output, chart);
        }
        else
        {
            Queue<Process> output = schedule.Schedule(data_clone);
            FxmlHelper.draw_chart(output, chart);
        }
        for (Process p : data)
        {
            p.getProcessIndex().setRemainingTime("0");
        }
        Computebtn.setDisable(false);

//        System.out.println("Generate is pressed");
//        for (int i = 0; i < data.size(); i++) {
//            System.out.println(data.get(i).getProcess_ID());
//            System.out.println(data.get(i).getArrivalTime());
//            System.out.println(data.get(i).getBurstTime());
//            System.out.println(data.get(i).getPriority());
//        }

//        FxmlHelper.draw_process(chart, chart.getLayoutX()+ 25, 25,"p"+1,0);

//        ArrayList<Process> processes = new ArrayList<>();
//        processes.add(new Process(1,0,10));
//        processes.add(new Process(2,0,1));
//        processes.add(new Process(3,0,2));
//        processes.add(new Process(4,0,1));
//        processes.add(new Process(5,0,5));
//        System.out.println(processes);
//
//        Queue<LiveTime> output;
//        RoundRobin RR = new RoundRobin();
//        output = (RR).Schedule(processes, 2);
//        System.out.println(output);
//        FxmlHelper.draw_chart_RR(output, chart);

    }
//////////////////////////////////////////////////////////////////////////
//
//    //    on action of generate chart button
//    @FXML
//    public void SchedulerChooser()
//    {
//    }
/////////////////////////////////////////////////////////////////////////////

    //    on action of add Button
    @FXML
    public void ADD()
    {
        Alert A = new Alert(Alert.AlertType.WARNING);
        if(no_processes.getText().length() == 0 && !LiveMode.isSelected()) {
            A.setContentText("Please choose number of processes");
            A.show();
        }
        else
        try {
            processInTable newProcess;
            switch (SchedulerType.getValue().toString()) {

                case "preemptive priority":
                case "non-preemptive priority":

                    newProcess = new processInTable(ProcessIDTF.getText(),
                            ArrivalTimeTF.getText(),
                            BurstTimeTF.getText(),
                            PriorityTF.getText());

                    break;
                default:
                    newProcess = new processInTable(ProcessIDTF.getText(),
                            ArrivalTimeTF.getText(),
                            BurstTimeTF.getText());
            }

            //Get all the items from the table as a list, then add the new process to the list
            processesIntable.getItems().add(newProcess);
            Process np = new Process(newProcess);
            data.add(np);
            number_of_added_process.setValue(number_of_added_process.getValue() + 1);
            System.out.println(number_of_added_process);
            System.out.println(data);
        }catch (Exception e)
        {
            System.out.println(e);
            A.setContentText("Please choose scheduler type ");
            A.show();
        }
    }

    //////////////////////////////////////////////////////////////////////////
//    on action of reset Button
    @FXML
    public void RESET()
    {
//        LiveMode.setSelected(false);
        FxmlHelper.clear_table(addingBar, number_of_added_process, processesIntable, data);
        QuantumTime.setVisible(false);
        PriorityTF.setVisible(false);
        PriorityTFL.setVisible(false);
        priorityL.setVisible(false);
        chart_pane.setDisable(true);
        live_pane.setDisable(true);
        no_processes.setDisable(false);
        addingBar.setDisable(false);
        Computebtn.setDisable(true);
        SchedulerType.getSelectionModel().clearSelection();

//        SchedulerType.setPromptText("Schedule Type");
        AvgWaitingTimeTF.clear();
        AvgTurnaroundTimeTF.clear();
        BurstTimeTFL.clear();
        PriorityTFL.clear();
        ProcessIDTFL.clear();
        live.getChildren().clear();
        System.out.println("sssss" + live_scroll_pane.heightProperty());
        live.setMinHeight(150);
        chart.getChildren().clear();
        output.clear();
        Live_current_time = 0;
        Live_index = 0;
        prev_process = -1;
        x = live.getLayoutX();
        System.out.println(live.heightProperty());
        if(running)
        {
            running = false;
            stop();
        }
        if (LiveMode.isSelected())
        {
            live_pane.setDisable(false);
            no_processes.setDisable(true);
            addingBar.setDisable(true);
            no_processes.clear();
        }
//        data.get(0).setRemainingTime("6");
//        processes.getItems().clear();
//        processes.setDisable(true);
    }
//////////////////////////////////////////////////////////////////////////

    //    on action of Run button
    @FXML
    public void RUN()
    {
        live_scroll_pane.setContent(live);
        if(!running) {

            System.out.println("Run is pressed");
            running = true;
//        priority non-preemptive live scheduling trial

            schedule = FxmlHelper.initializeScheduler(SchedulerType.getValue().toString());
            System.out.println(SchedulerType.getValue().toString());
            if (SchedulerType.getValue().toString().equals("non-preemptive priority")) {
                output = ((priorityNonPreemptiveScheduler) schedule).Schedule(data, 0);
                ((priorityNonPreemptiveScheduler) schedule).setProcessStartTimeAndEndTime(data, 0);
                System.out.println(output);
            }

            else if(SchedulerType.getValue().toString().equals("Round Robin")) {
//                System.out.println(SchedulerType.getValue().toString());
//                int quanta = Integer.parseInt(QuantumTimeTF.getText());
////            System.out.println(quanta);
////            System.out.println(data);
////                Queue<LiveTime> output;
//                outputLiveTime = ((RoundRobin)schedule).Schedule(data, quanta);
//                System.out.println(output);
                int quanta;
                try
                {
                    quanta = Integer.parseInt(QuantumTimeTF.getText());
                }catch (Exception e)
                {
                    quanta = 1;
                }
                outputLiveTime = ((RoundRobin)schedule).Schedule(data, quanta);
                chosen_process = RoundRobin.choosenProcess(outputLiveTime);

            }
            else if(SchedulerType.getValue().toString().equals("preemptive priority")) {
                System.out.println(SchedulerType.getValue().toString());
                output = schedule.Schedule(data);
//                output = PreemptivePriorityScheduling.getScheduledQueueP();
            }
            else if(SchedulerType.getValue().toString().equals("preemptive SJF")) {
                System.out.println(SchedulerType.getValue().toString());
                output = schedule.Schedule(data);
//                output = PreemptivePriorityScheduling.getScheduledQueueP();
            }
            else if(SchedulerType.getValue().toString().equals("FCFS")) {
                output = ((FCFS) schedule).Schedule(data, 0);
                System.out.println(output);
            }
        }

//        copied to initialize method


//        Timeline tl = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
//
//            x+=30;
//            Process p = output.peek();
//
//            if (p.getRemaining_burst_time() == 1) {
//                output.remove();
//            }
//            p.setRemaining_burst_time(p.getRemaining_burst_time() - 1);
////            sets remaining time value in table
//            p.getProcessIndex().setRemainingTime(p.getRemaining_burst_time()+"");
//
//            if(prev_process == p.getProcess_ID())
//                FxmlHelper.draw_process_trial(live,x , 30,"P" + p.getProcess_ID());
//            else
//                FxmlHelper.draw_process(live,x , 30,"P" + p.getProcess_ID(),Live_current_time);
//            prev_process = p.getProcess_ID();
//            Live_current_time++;
//        }));
        tl.setCycleCount(Animation.INDEFINITE);
        tl.play();
    }

    @FXML
    public void stop()
    {
        tl.stop();
    }
    @FXML
    public void ADD_LIVE()
    {
        Alert A = new Alert(Alert.AlertType.WARNING);
        try {
            processInTable newProcess;
            switch (SchedulerType.getValue().toString()) {

                case "preemptive priority":
                case "non-preemptive priority":
                    newProcess = new processInTable(ProcessIDTFL.getText(),
                            Live_current_time +"",
                            BurstTimeTFL.getText(),
                            PriorityTFL.getText());
                    break;
                default:

                    newProcess = new processInTable(ProcessIDTFL.getText(),
                            Live_current_time+"",
                            BurstTimeTFL.getText());
            }

            //Get all the items from the table as a list, then add the new process to the list
            processesIntable.getItems().add(newProcess);
            Process np = new Process(newProcess);
            data.add(np);
//            number_of_added_process.setValue(number_of_added_process.getValue() + 1);
//            System.out.println(number_of_added_process);
            System.out.println(data);
            if(running)
            {
                if(SchedulerType.getValue().toString().equals("non-preemptive priority")) {
                    output = ((priorityNonPreemptiveScheduler) schedule).Schedule(data, Live_current_time);
                    ((priorityNonPreemptiveScheduler) schedule).setProcessStartTimeAndEndTime(data, Live_current_time);
                    System.out.println("at Live//////////////////////////////////////////////////////////////////////////");

                    FxmlHelper.printQueue(output, 0);
                    System.out.println("///////////////////////////////////////////////////////////////////////////////");
                }
                else if(SchedulerType.getValue().toString().equals("FCFS")) {
                    output = ((FCFS)schedule).Schedule(data,Live_current_time);
                }
                else if(SchedulerType.getValue().toString().equals("Round Robin")) {
                    int quanta;
                    try
                    {
                         quanta = Integer.parseInt(QuantumTimeTF.getText());
                    }
                    catch (Exception e)
                    {
                        quanta = 1;
                    }
                    System.out.println(outputLiveTime);
                    outputLiveTime = ((RoundRobin)schedule).Schedule(data, quanta);
                    chosen_process = RoundRobin.choosenProcess(outputLiveTime);
                    System.out.println(outputLiveTime);
                }
                else if(SchedulerType.getValue().toString().equals("non-preemptive SJF")){
                    output = ((SJF_Non_Prem) schedule).Schedule(data, Live_current_time);
                    System.out.println("at Live//////////////////////////////////////////////////////////////////////////");
                    FxmlHelper.printQueue(output, 0);
                    System.out.println("///////////////////////////////////////////////////////////////////////////////");
                }
                else
                {
                    System.out.println("entered successfulyyy /////////////////////////////////////////////////////////////");
                    System.out.println(data);
                    output = schedule.Schedule(data);
                    System.out.println(output);
                    System.out.println("called successfulyyy /////////////////////////////////////////////////////////////");

//                    output = PreemptivePriorityScheduling.getScheduledQueueP();
                }
//                added = true;
            }
        }catch (Exception e)
        {
            System.out.println(e);
            A.setContentText("Please choose scheduler type ");
            A.show();
        }
    }
}
