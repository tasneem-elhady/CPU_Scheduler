package com.cpuscheduler;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class FxmlHelper {
    public static void draw_process(Pane pane, double x, double width){
        Rectangle process_block = new Rectangle();
        process_block.yProperty().bind(pane.heightProperty().divide(2));
        process_block.setX(x);
        process_block.setWidth(10 * width);
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
        label.setText("0");
        label.xProperty().bind(l.startXProperty().subtract(5));
        label.yProperty().bind(l.endYProperty().add(15));
        label.setStyle("-fx-font-size:15px;-fx-fill:black;");

        Text process_label;
        process_label = new Text();
        process_label.setText("P1");
        process_label.xProperty().bind(process_block.xProperty().add(process_block.widthProperty().divide(3)));
        process_label.yProperty().bind(process_block.yProperty().add(process_block.heightProperty().divide(2)));
        process_label.setStyle("-fx-font-size:15px;-fx-fill:red;");

        pane.getChildren().add(process_block);
        pane.getChildren().add(label);
        pane.getChildren().add(process_label);
        pane.getChildren().add(l);
    }
}