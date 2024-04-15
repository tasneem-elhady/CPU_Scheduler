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
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
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
    @FXML
    private TextField ArrivalTimeTFL;
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

    int i = 1;
    double x = live.getLayoutX();

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

    }

    //    on action of compute button
    @FXML
    public void setNumber()
    {
        System.out.println("number of processes is set");
        FxmlHelper.clear_table(addingBar, number_of_added_process, processesIntable);
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

    //    on action of generate chart button
    @FXML
    public void SchedulerChooser()
    {
        System.out.println("Scheduler has changed");
        chart.getChildren().clear();
        AvgTurnaroundTimeTF.clear();
        AvgWaitingTimeTF.clear();
//        System.out.println(SchedulerType.getValue());
        switch (SchedulerType.getValue().toString()){
            case "Round Robin":
//                FxmlHelper.clear_table(addingBar, number_of_added_process, processes);
                QuantumTime.setVisible(true);
                PriorityTF.setVisible(false);
                PriorityTFL.setVisible(false);
                priorityL.setVisible(false);

                break;
            case "preemptive priority":
            case"non-preemptive priority":
                FxmlHelper.clear_table(addingBar, number_of_added_process, processesIntable);
                QuantumTime.setVisible(false);
                PriorityTF.setVisible(true);
                PriorityTFL.setVisible(true);
                priorityL.setVisible(true);
                break;
            default:
                QuantumTime.setVisible(false);
                PriorityTF.setVisible(false);
                PriorityTFL.setVisible(false);
                priorityL.setVisible(false);
        }
    }
///////////////////////////////////////////////////////////////////////////

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
                    if(LiveMode.isSelected())
                    {
                        newProcess = new processInTable(ProcessIDTFL.getText(),
                            ArrivalTimeTFL.getText(),
                            BurstTimeTFL.getText(),
                            PriorityTFL.getText());
                    }
                    else {
                        newProcess = new processInTable(ProcessIDTF.getText(),
                                ArrivalTimeTF.getText(),
                                BurstTimeTF.getText(),
                                PriorityTF.getText());
                    }

                    break;
                default:
                    if(LiveMode.isSelected())
                    {
                        newProcess = new processInTable(ProcessIDTFL.getText(),
                                ArrivalTimeTFL.getText(),
                                BurstTimeTFL.getText());
                    }
                    else {
                        newProcess = new processInTable(ProcessIDTF.getText(),
                                ArrivalTimeTF.getText(),
                                BurstTimeTF.getText());
                    }
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
        FxmlHelper.clear_table(addingBar, number_of_added_process, processesIntable);
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
        SchedulerType.setValue("Scheduler Type");
        AvgWaitingTimeTF.clear();
        AvgTurnaroundTimeTF.clear();
        live.getChildren().clear();
        chart.getChildren().clear();
//        data.get(0).setRemainingTime("6");
//        processes.getItems().clear();
//        processes.setDisable(true);
    }
//////////////////////////////////////////////////////////////////////////

    //    on action of Run button
    @FXML
    public void RUN()
    {
        System.out.println("Run is pressed");
        live_scroll_pane.setContent(live);
        Timeline tl = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            i++;
            x+=30;
           FxmlHelper.draw_process(live,x , 30,"PP",i);

        }));
        tl.setCycleCount(Animation.INDEFINITE);
        tl.play();
    }
//    @FXML
//    public void ADD_LIVE()
//    {
//        Process newProcess;
//        try {
//            switch (SchedulerType.getValue().toString()) {
//
//                case "preemptive priority":
//                case "non-preemptive priority":
//                    newProcess = new Process(Integer.parseInt(ProcessIDTFL.getText()),
//                            Integer.parseInt(ArrivalTimeTFL.getText()),
//                            Integer.parseInt(BurstTimeTFL.getText()),
//                            Integer.parseInt(PriorityTFL.getText()));
//                    break;
//                default:
//                    newProcess = new Process(Integer.parseInt(ProcessIDTFL.getText()),
//                            Integer.parseInt(ArrivalTimeTFL.getText()),
//                            Integer.parseInt(BurstTimeTFL.getText()));
//            }
//            data.add(newProcess);
//            System.out.println(data);
//        }catch (Exception e){
//            A.setContentText("Please choose scheduler type ");
//            A.show();
//        }
//    }
}