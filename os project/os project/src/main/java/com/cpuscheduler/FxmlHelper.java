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

public class FxmlHelper {
    public static void clear_table(GridPane addingBar, SimpleIntegerProperty number_of_added_process, TableView processes){
        addingBar.setDisable(false);
        number_of_added_process.set(0);
        processes.getItems().clear();
    }
     public static void draw_chart(Queue<Process> processes,Pane pane){
        double x = pane.getLayoutX()+25;
        int end_of_process = 0;
        for (Process p :processes)
        {
            if (end_of_process != p.getStart_time())
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
    }
}
