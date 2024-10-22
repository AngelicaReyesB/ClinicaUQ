    package co.edu.uniquindio.clinicauq.controladores;
    import java.net.URL;
    import java.util.List;
    import java.util.ResourceBundle;
    import co.edu.uniquindio.clinicauq.modelo.Clinica;
    import co.edu.uniquindio.clinicauq.modelo.Paciente;
    import javafx.beans.property.SimpleStringProperty;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.fxml.FXML;
    import javafx.scene.control.Button;
    import javafx.scene.control.TableColumn;
    import javafx.scene.control.TableView;
    import javafx.scene.control.cell.PropertyValueFactory;

    public class ListaPacientesControlador {

        @FXML private TableView<Paciente> tablaPacientes;
        @FXML private TableColumn<Paciente, String> colNombre;
        @FXML private TableColumn<Paciente, String> colCedula;
        @FXML private TableColumn<Paciente, String> colCorreo;
        @FXML private TableColumn<Paciente, String> colTelefono;
        @FXML private TableColumn<Paciente, String> colSuscripcion;

        // Lista observable de pacientes
        private ObservableList<Paciente> pacienteObservableList;
        private final Clinica clinica;

        // Constructor
        public ListaPacientesControlador() {
            clinica = Clinica.getInstance();
        }

        @FXML
        public void initialize() {
            pacienteObservableList = FXCollections.observableArrayList(clinica.listarPacientes());

            colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
            colCedula.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCedula()));
            colCorreo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCorreo()));
            colTelefono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelefono()));
            colSuscripcion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSuscripcion().getTipo()));


            // Asignar la lista de pacientes a la tabla
            tablaPacientes.setItems(pacienteObservableList);
            cargarPacientes();
        }

        // Método para recargar los pacientes en la tabla si se actualiza la lista
        public void cargarPacientes() {
            try {
                List<Paciente> todosLosPacientes = clinica.listarPacientes();
                pacienteObservableList.setAll(todosLosPacientes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }