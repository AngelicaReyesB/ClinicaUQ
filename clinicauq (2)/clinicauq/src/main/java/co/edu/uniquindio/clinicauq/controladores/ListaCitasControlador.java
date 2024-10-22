package co.edu.uniquindio.clinicauq.controladores;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import co.edu.uniquindio.clinicauq.modelo.Clinica;
import co.edu.uniquindio.clinicauq.modelo.Cita;
import co.edu.uniquindio.clinicauq.modelo.Paciente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ListaCitasControlador {

    @FXML
    private ResourceBundle resources;
    @FXML private URL location;
    @FXML private Button btncancelarCita;
    @FXML private TableColumn<Cita, Integer> colID;
    @FXML private TableColumn<Cita, String> colFecha;
    @FXML private TableColumn<Cita, String> colPaciente;
    @FXML private TableColumn<Cita, String> colServicio;
    @FXML private TableColumn<Cita, Double> colTotal;
    @FXML private TableView<Cita> tablaCitas;

    // Lista observable de pacientes
    private ObservableList<Cita> citaObservableList;
    private final Clinica clinica;

    // Constructor
    public ListaCitasControlador() {
        clinica = Clinica.getInstance(); // Usar la instancia única de Clinica
    }

    @FXML
    void initialize() {

        citaObservableList = FXCollections.observableArrayList(clinica.listarCitas());
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colPaciente.setCellValueFactory(new PropertyValueFactory<>("paciente"));
        colServicio.setCellValueFactory(new PropertyValueFactory<>("servicio"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        tablaCitas.setItems(citaObservableList);
        cargarCitas();
    }

    // Método para recargar los pacientes en la tabla si se actualiza la lista
    public void cargarCitas() {
        try {
            List<Cita> todasLasCitas = clinica.listarCitas();
            citaObservableList.setAll(todasLasCitas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para cancelar una cita
    @FXML
    void cancelarCita(ActionEvent event) {
        // Obtener la cita seleccionada
        Cita citaSeleccionada = tablaCitas.getSelectionModel().getSelectedItem();

        if (citaSeleccionada != null) {
            clinica.cancelarCita(citaSeleccionada);
            cargarCitas();

            mostrarAlerta("Éxito", "Cita cancelada correctamente.");
        } else {
            mostrarAlerta("Error", "Debe seleccionar una cita para cancelar.");
        }
    }

    // Método para mostrar alertas
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}