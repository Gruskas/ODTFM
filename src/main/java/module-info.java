module org.gruskas.odtmanager {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;

    opens org.gruskas.odtmanager to javafx.fxml;
    exports org.gruskas.odtmanager;
}