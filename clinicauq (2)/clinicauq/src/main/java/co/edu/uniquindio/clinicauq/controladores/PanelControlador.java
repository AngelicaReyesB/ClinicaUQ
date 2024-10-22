package co.edu.uniquindio.clinicauq.controladores;

import co.edu.uniquindio.clinicauq.modelo.Clinica;
import co.edu.uniquindio.clinicauq.modelo.Paciente;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.util.List;

public class PanelControlador {

    @FXML private StackPane panelPrincipal;

    public Button botonRegistrarPacientes;
    public Button botonListarPacientes;
    public Button botonCrearCita;
    public Button botonListarCita;
    private final Clinica clinica;

    public PanelControlador() {
        this.clinica = Clinica.getInstance(); // Se crea una única instancia de la clase Clinica
    }

    public void mostrarRegistroPaciente(ActionEvent actionEvent) {
        Parent node = cargarPanel("/registroPaciente.fxml");
        panelPrincipal.getChildren().setAll(node); // Reemplaza el contenido del panel principal
    }

    public void mostrarListarPacientes(ActionEvent actionEvent) {
        // Cargar la vista de lista de pacientes
        Parent node = cargarPanel("/listaPacientes.fxml");
        panelPrincipal.getChildren().setAll(node);
    }

    public void mostrarListaCitas(ActionEvent actionEvent)  {
        Parent node = cargarPanel("/listaCitas.fxml");
        panelPrincipal.getChildren().setAll(node); // Reemplaza el contenido del panel principal
    }

    public void mostrarRegistroCita(ActionEvent actionEvent) {
        Parent node = cargarPanel("/registroCita.fxml");
        panelPrincipal.getChildren().setAll(node); // Reemplaza el contenido del panel principal
    }

    private Parent cargarPanel(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            return loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
