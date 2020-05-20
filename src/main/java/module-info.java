module com.benchmark {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.benchmark to javafx.fxml;
    exports com.benchmark;
}