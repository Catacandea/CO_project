module com.benchmark {
    requires javafx.controls;
    requires javafx.fxml;
	requires jdk.compiler;

    opens com.benchmark to javafx.fxml;
    exports com.benchmark;
}