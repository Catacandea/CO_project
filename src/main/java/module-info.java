module com.benchmark {
    requires javafx.fxml;
    requires javafx.controls;
    requires jdk.compiler;

    opens com.benchmark to javafx.fxml;
    exports com.benchmark;
    //exports benchmark.HDD;
    //exports timing;
}