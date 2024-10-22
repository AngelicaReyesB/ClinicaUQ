    package co.edu.uniquindio.clinicauq.controladores;

    import co.edu.uniquindio.clinicauq.modelo.Clinica;
    import co.edu.uniquindio.clinicauq.modelo.Paciente;
    import co.edu.uniquindio.clinicauq.modelo.factory.Suscripcion;
    import co.edu.uniquindio.clinicauq.modelo.factory.SuscripcionFactory;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.fxml.Initializable;
    import javafx.scene.control.*;
    import javafx.stage.Stage;

    import java.net.URL;
    import java.util.List;
    import java.util.ResourceBundle;

    public class RegistroPacienteControlador implements Initializable {

        @FXML private TextField cedulatxt;
        @FXML private TextField correotxt;
        @FXML private TextField nombretxt;
        @FXML private TextField telefonotxt;
        @FXML private ComboBox<String> tipoSuscripcionComboBox;

        private ObservableList<Paciente> pacienteObservableList;
        private Clinica clinica;

        private void configurarSuscripciones() {
            tipoSuscripcionComboBox.setItems(FXCollections.observableArrayList(List.of("Basica", "Premium")));
        }

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            configurarSuscripciones();
            clinica = Clinica.getInstance();
        }

        @FXML
        void registrarPaciente(ActionEvent event) {
            try {
                String tipoSuscripcionSeleccionada = tipoSuscripcionComboBox.getValue();

                if (tipoSuscripcionSeleccionada == null || tipoSuscripcionSeleccionada.isEmpty()) {
                    throw new IllegalArgumentException("Debe seleccionar un tipo de suscripción.");
                }

                Suscripcion suscripcion = SuscripcionFactory.crearSuscripcion(tipoSuscripcionSeleccionada);

                clinica.registrarPaciente(
                        cedulatxt.getText(),
                        nombretxt.getText(),
                        telefonotxt.getText(),
                        correotxt.getText(),
                        suscripcion
                );

                crearAlerta("El paciente ha sido registrado correctamente.", Alert.AlertType.INFORMATION);

            } catch (IllegalArgumentException e) {
                crearAlerta(e.getMessage(), Alert.AlertType.ERROR);
            } catch (Exception e) {
                crearAlerta("Error al registrar el paciente: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }


        public void crearAlerta(String mensaje, Alert.AlertType tipo) {
            Alert alert = new Alert(tipo);
            alert.setTitle("Alerta");
            alert.setHeaderText(null);
            alert.setContentText(mensaje);
            alert.showAndWait();
        }

        public void cerrarVentana() {
            Stage stage = (Stage) cedulatxt.getScene().getWindow();
            stage.close();
        }
    }
