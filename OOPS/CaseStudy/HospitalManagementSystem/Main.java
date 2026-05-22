import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

// libraries for the jdbc connection
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main extends Application {

    TextArea output = new TextArea();
    Label status = new Label();

    @Override
    public void start(Stage stage) {

        TabPane tabs = new TabPane();

        tabs.getTabs().addAll(
                createPatientTab(),
                createDoctorTab(),
                createAppointmentTab()
        );

        output.setEditable(false);
        output.setPrefHeight(200);

        VBox root = new VBox(10, tabs, new Label("Output:"), output, status);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 700, 600);
        stage.setTitle("Hospital Management (JavaFX)");
        stage.setScene(scene);
        stage.show();
    }

    private Tab createPatientTab() {
        Tab tab = new Tab("Patients");

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField ageField = new TextField();
        TextField diseaseField = new TextField();

        idField.setPromptText("Patient Id");
        nameField.setPromptText("Name");
        ageField.setPromptText("Age");
        diseaseField.setPromptText("Disease");

        Button add = new Button("Add Patient");
        Button view = new Button("View Patients");

        add.setOnAction(e -> {
            try (Connection conn = JDBConn.getConnection()) {
                String sql = "insert into patients values(?,?,?,?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(idField.getText().trim()));
                ps.setString(2, nameField.getText().trim());
                ps.setInt(3, Integer.parseInt(ageField.getText().trim()));
                ps.setString(4, diseaseField.getText().trim());
                ps.executeUpdate();
                status.setText("Patient added");
            } catch (Exception ex) {
                status.setText("Error: " + ex.getMessage());
            }
        });

        view.setOnAction(e -> viewPatients());

        HBox fields = new HBox(10, idField, nameField, ageField, diseaseField);
        HBox buttons = new HBox(10, add, view);
        VBox v = new VBox(10, fields, buttons);
        v.setPadding(new Insets(10));
        tab.setContent(v);
        tab.setClosable(false);
        return tab;
    }

    private Tab createDoctorTab() {
        Tab tab = new Tab("Doctors");

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField specField = new TextField();

        idField.setPromptText("Doctor Id");
        nameField.setPromptText("Name");
        specField.setPromptText("Specialization");

        Button add = new Button("Add Doctor");
        Button view = new Button("View Doctors");

        add.setOnAction(e -> {
            try (Connection conn = JDBConn.getConnection()) {
                String sql = "insert into doctors values(?,?,?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(idField.getText().trim()));
                ps.setString(2, nameField.getText().trim());
                ps.setString(3, specField.getText().trim());
                ps.executeUpdate();
                status.setText("Doctor added");
            } catch (Exception ex) {
                status.setText("Error: " + ex.getMessage());
            }
        });

        view.setOnAction(e -> viewDoctors());

        HBox fields = new HBox(10, idField, nameField, specField);
        HBox buttons = new HBox(10, add, view);
        VBox v = new VBox(10, fields, buttons);
        v.setPadding(new Insets(10));
        tab.setContent(v);
        tab.setClosable(false);
        return tab;
    }

    private Tab createAppointmentTab() {
        Tab tab = new Tab("Appointments");

        TextField aidField = new TextField();
        TextField pidField = new TextField();
        TextField didField = new TextField();
        TextField dateField = new TextField();

        aidField.setPromptText("Appointment Id");
        pidField.setPromptText("Patient Id");
        didField.setPromptText("Doctor Id");
        dateField.setPromptText("Date (e.g., 2026-05-21)");

        Button add = new Button("Book Appointment");
        Button view = new Button("View Appointments");

        add.setOnAction(e -> {
            try (Connection conn = JDBConn.getConnection()) {
                String sql = "insert into appointments values(?,?,?,?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(aidField.getText().trim()));
                ps.setInt(2, Integer.parseInt(pidField.getText().trim()));
                ps.setInt(3, Integer.parseInt(didField.getText().trim()));
                ps.setString(4, dateField.getText().trim());
                ps.executeUpdate();
                status.setText("Appointment booked");
            } catch (Exception ex) {
                status.setText("Error: " + ex.getMessage());
            }
        });

        view.setOnAction(e -> viewAppointments());

        HBox fields = new HBox(10, aidField, pidField, didField, dateField);
        HBox buttons = new HBox(10, add, view);
        VBox v = new VBox(10, fields, buttons);
        v.setPadding(new Insets(10));
        tab.setContent(v);
        tab.setClosable(false);
        return tab;
    }

    private void viewPatients() {
        try (Connection conn = JDBConn.getConnection()) {
            String sql = "select * from patients";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("Patient Id: ").append(rs.getInt("patientId")).append('\n');
                sb.append("Name: ").append(rs.getString("name")).append('\n');
                sb.append("Age: ").append(rs.getInt("age")).append('\n');
                sb.append("Disease: ").append(rs.getString("disease")).append('\n');
                sb.append("---------------------\n");
            }
            output.setText(sb.toString());
            status.setText("Loaded patients");
        } catch (Exception ex) {
            status.setText("Error: " + ex.getMessage());
        }
    }

    private void viewDoctors() {
        try (Connection conn = JDBConn.getConnection()) {
            String sql = "select * from doctors";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("Doctor Id: ").append(rs.getInt("doctorId")).append('\n');
                sb.append("Name: ").append(rs.getString("name")).append('\n');
                sb.append("Specialization: ").append(rs.getString("specialization")).append('\n');
                sb.append("---------------------\n");
            }
            output.setText(sb.toString());
            status.setText("Loaded doctors");
        } catch (Exception ex) {
            status.setText("Error: " + ex.getMessage());
        }
    }

    private void viewAppointments() {
        try (Connection conn = JDBConn.getConnection()) {
            String sql = "select a.appointmentId,a.date,\n" +
                    "p.patientId,p.name,p.age,p.disease,\n" +
                    "d.doctorId,d.name as doctorName,d.specialization \n" +
                    "from appointments a \n" +
                    "join patients p on a.patientId = p.patientId \n" +
                    "join doctors d on a.doctorId = d.doctorId";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("Appointment Id: ").append(rs.getInt("appointmentId")).append('\n');
                sb.append("Date: ").append(rs.getString("date")).append('\n');
                sb.append("Patient Id: ").append(rs.getInt("patientId")).append('\n');
                sb.append("Patient Name: ").append(rs.getString("name")).append('\n');
                sb.append("Patient Age: ").append(rs.getInt("age")).append('\n');
                sb.append("Disease: ").append(rs.getString("disease")).append('\n');
                sb.append("Doctor Id: ").append(rs.getInt("doctorId")).append('\n');
                sb.append("Doctor Name: ").append(rs.getString("doctorName")).append('\n');
                sb.append("Specialization: ").append(rs.getString("specialization")).append('\n');
                sb.append("---------------------\n");
            }
            output.setText(sb.toString());
            status.setText("Loaded appointments");
        } catch (Exception ex) {
            status.setText("Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}