package com.cpuscheduler;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class FXMLcontroller implements Initializable {
    @FXML
    private TextField no_processes;
    @FXML
    private ComboBox SchedulerTypeBox;
    @FXML
    private HBox QuantumTime;
    @FXML
    private TextField QuantumTimeTF;
    @FXML
    private Button Resetbtn;
    @FXML
    private Button Computebtn;
    @FXML
    private TextField AvgWaitingTimeTF;
    @FXML
    private TextField AvgTurnaroundTimeTF;
    @FXML
    private Button generateChart;
    @FXML
    private TextField ProcessIDTF;
    @FXML
    private TextField ArrivalTimeTF;
    @FXML
    private TextField BurstTimeTF;
    @FXML
    private TextField PriorityTF;
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

    @FXML
    private Text t;
    int i = 1;
//    function called first on openning scene
    public void initialize(URL url, ResourceBundle rb) {
//        chart_pane.setDisable(true);
//        live_pane.setDisable(true);
        QuantumTime.setVisible(false);
        AnchorPane pane = new AnchorPane();
        AnchorPane live = new AnchorPane();
        live_scroll_pane.setContent(live);
        Timeline tl = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            i++;
//            System.out.println(i);
            FxmlHelper.draw_process(live, live.getLayoutX() + i*25, 10);
        }));
        tl.setCycleCount(Animation.INDEFINITE);
        tl.play();
        chart_scroll_pane.setContent(pane);
        FxmlHelper.draw_process(pane, pane.getLayoutX()+ 25, 10);


    }
}