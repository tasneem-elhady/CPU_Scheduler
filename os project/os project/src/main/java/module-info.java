module com.cpuscheduler {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.cpuscheduler to javafx.fxml;
    exports com.cpuscheduler;
}