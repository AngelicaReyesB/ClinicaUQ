module co.edu.uniquindio.clinicauq {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires org.simplejavamail.core;
    requires org.simplejavamail;

    opens co.edu.uniquindio.clinicauq.utils to javafx.fxml;
    exports co.edu.uniquindio.clinicauq.utils;
    exports co.edu.uniquindio.clinicauq.controladores;
    opens co.edu.uniquindio.clinicauq.controladores to javafx.fxml;
    exports co.edu.uniquindio.clinicauq.App;
    opens co.edu.uniquindio.clinicauq.App to javafx.fxml;
}