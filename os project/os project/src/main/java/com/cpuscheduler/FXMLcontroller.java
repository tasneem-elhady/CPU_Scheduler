package com.cpuscheduler;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.net.URL;
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
    private TableView<processInTable> processes;
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

    ObservableList<Process> data = FXCollections.observableArrayList();
    int i = 1;

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
//          disable gantt chart pane and table
                live_pane.setDisable(false);
            }
            else {
                live_pane.setDisable(true);
            }
        });

        no_processes.textProperty().addListener((obs, oldText, newText) -> {
            RESET();
        });

        number_of_added_process.addListener((obs, oldText, newText) -> {
                if (newText.equals(Integer.parseInt(no_processes.getText()))) {
                    ProcessIDTF.clear();
                    ArrivalTimeTF.clear();
                    BurstTimeTF.clear();
                    PriorityTF.clear();
                    addingBar.setDisable(true);
                    chart_pane .setDisable(false);
                }
        });

    }

    //    on action of compute button
    @FXML
    public void setNumber()
    {
        System.out.println("number of processes is set");
        FxmlHelper.clear_table(addingBar, number_of_added_process, processes);
    }
//////////////////////////////////////////////////////////////////////////


    //    on action of compute button
    @FXML
    public void COMPUTE()
    {
        System.out.println("compute is pressed");
        switch (SchedulerType.getValue().toString()){
            case "FCFS":

                break;
            case "preemptive SJF":

                break;
            case"non-preemptive SJF":

                break;
            case "Round Robin":

                break;
            case "preemptive priority":

                break;
            case"non-preemptive priority":

                break;

        }
    }
//////////////////////////////////////////////////////////////////////////

    //    on action of generate chart button
    @FXML
    public void GENERATE()
    {
        System.out.println("Generate is pressed");
        for (int i = 0; i < data.size(); i++) {
            System.out.println(data.get(i).getProcess_ID());
            System.out.println(data.get(i).getArrivalTime());
            System.out.println(data.get(i).getBurstTime());
            System.out.println(data.get(i).getPriority());
        }
        chart_scroll_pane.setContent(chart);
        FxmlHelper.draw_process(chart, chart.getLayoutX()+ 25, 25,"p"+1,0);
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

                break;
            case "preemptive priority":
            case"non-preemptive priority":
                FxmlHelper.clear_table(addingBar, number_of_added_process, processes);
                QuantumTime.setVisible(false);
                PriorityTF.setVisible(true);
                break;
            default:
                QuantumTime.setVisible(false);
                PriorityTF.setVisible(false);
        }
    }
///////////////////////////////////////////////////////////////////////////

    //    on action of add Button
    @FXML
    public void ADD()
    {
        Alert A = new Alert(Alert.AlertType.WARNING);
        if(no_processes.getText().length()==0) {
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
            processes.getItems().add(newProcess);
            Process np = new Process(newProcess);
            data.add(np);
            number_of_added_process.setValue(number_of_added_process.getValue() + 1);
            System.out.println(number_of_added_process);
        }catch (Exception e)
        {
            A.setContentText("Please choose scheduler type ");
            A.show();
        }

    }

    //////////////////////////////////////////////////////////////////////////
//    on action of reset Button
    @FXML
    public void RESET()
    {
        FxmlHelper.clear_table(addingBar, number_of_added_process, processes);
        QuantumTime.setVisible(false);
        PriorityTF.setVisible(false);
        chart_pane.setDisable(true);
        live_pane.setDisable(true);

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
//        live_scroll_pane.setContent(live);
//
//        Timeline tl = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
//            i++;
//            FxmlHelper.draw_process(live, live.getLayoutX() + i*25, 25);
//        }));
//        tl.setCycleCount(Animation.INDEFINITE);
//        tl.play();
    }

}