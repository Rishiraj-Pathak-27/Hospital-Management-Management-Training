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
import java.time.LocalDate;
import java.util.Locale;

public class Main extends Application {

    TextArea output = new TextArea();
    Label status = new Label();

    @Override
    public void start(Stage stage) {

        TabPane tabs = new TabPane();

        tabs.getTabs().addAll(
                createPatientTab(),
                createDoctorTab(),
            createAppointmentTab(),
            createAdmissionTab()
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
                String sql = "insert into appointments (appointmentId, patientId, doctorId, appointment_date) values(?,?,?,?)";
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

    private Tab createAdmissionTab() {
        Tab tab = new Tab("Admissions");

        TextField patientIdField = new TextField();
        TextField severityField = new TextField();
        TextField notesField = new TextField();

        patientIdField.setPromptText("Patient Id");
        severityField.setPromptText("Severity (low, medium, high, critical)");
        notesField.setPromptText("Notes");

        Button process = new Button("Process Admission");
        Button viewAdmissions = new Button("View Admissions");
        Button viewWards = new Button("View Wards");
        Button viewBeds = new Button("View Beds");

        process.setOnAction(e -> {
            try (Connection conn = JDBConn.getConnection()) {
                processAdmission(
                        conn,
                        Integer.parseInt(patientIdField.getText().trim()),
                        severityField.getText().trim(),
                        notesField.getText().trim()
                );
                status.setText("Admission processed");
            } catch (Exception ex) {
                status.setText("Error: " + ex.getMessage());
            }
        });

        viewAdmissions.setOnAction(e -> viewAdmissions());
        viewWards.setOnAction(e -> viewWards());
        viewBeds.setOnAction(e -> viewBeds());

        VBox fields = new VBox(10, patientIdField, severityField, notesField);
        HBox buttons = new HBox(10, process, viewAdmissions, viewWards, viewBeds);
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
            String sql = "select a.appointmentId,a.appointment_date,\n" +
                    "p.patientId,p.name as patientName,p.age,p.disease,\n" +
                    "d.doctorId,d.name as doctorName,d.specialization \n" +
                    "from appointments a \n" +
                    "join patients p on a.patientId = p.patientId \n" +
                    "join doctors d on a.doctorId = d.doctorId";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("Appointment Id: ").append(rs.getInt("appointmentId")).append('\n');
                sb.append("Date: ").append(rs.getString("appointment_date")).append('\n');
                sb.append("Patient Id: ").append(rs.getInt("patientId")).append('\n');
                sb.append("Patient Name: ").append(rs.getString("patientName")).append('\n');
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

    private void processAdmission(Connection conn, int patientId, String severity, String notes) throws Exception {
        String normalizedSeverity = normalizeSeverity(severity);

        if (!patientExists(conn, patientId)) {
            output.setText("Patient not found.\n");
            return;
        }

        conn.setAutoCommit(false);

        try {
            if (isConsultOnlySeverity(normalizedSeverity)) {
                recordAdmission(conn, patientId, normalizedSeverity, "CONSULT_ONLY", null, null, notes);
                conn.commit();
                output.setText("Low severity found. Consultation only. No bed allocation needed.\n");
                return;
            }

            BedAllocation allocation = findAvailableBed(conn, normalizedSeverity);

            if (allocation == null) {
                conn.rollback();
                output.setText("No available ward/bed found for this severity.\n");
                return;
            }

            markBedUnavailable(conn, allocation.bedId);
            recordAdmission(conn, patientId, normalizedSeverity, "ADMITTED", allocation.wardId, allocation.bedId, notes);
            conn.commit();

            output.setText(
                    "Patient admitted\n" +
                    "Ward: " + allocation.wardName + " (" + allocation.wardType + ")\n" +
                    "Bed: " + allocation.bedNumber + "\n"
            );
        } catch (Exception ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    private void viewAdmissions() {
        try (Connection conn = JDBConn.getConnection()) {
            String sql =
                    "select a.admissionId, a.admissionDate, a.severity, a.admissionStatus, a.notes, " +
                    "p.patientId, p.name as patientName, p.age, p.disease, " +
                    "w.wardName, w.wardType, b.bedNumber " +
                    "from admissions a " +
                    "join patients p on a.patientId = p.patientId " +
                    "left join wards w on a.wardId = w.wardId " +
                    "left join beds b on a.bedId = b.bedId " +
                    "order by a.admissionId desc";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("Admission Id: ").append(rs.getInt("admissionId")).append('\n');
                sb.append("Admission Date: ").append(rs.getString("admissionDate")).append('\n');
                sb.append("Severity: ").append(rs.getString("severity")).append('\n');
                sb.append("Status: ").append(rs.getString("admissionStatus")).append('\n');
                sb.append("Patient Id: ").append(rs.getInt("patientId")).append('\n');
                sb.append("Patient Name: ").append(rs.getString("patientName")).append('\n');
                sb.append("Age: ").append(rs.getInt("age")).append('\n');
                sb.append("Disease: ").append(rs.getString("disease")).append('\n');
                sb.append("Ward: ").append(valueOrDefault(rs.getString("wardName"), "N/A")).append('\n');
                sb.append("Bed: ").append(valueOrDefault(rs.getString("bedNumber"), "N/A")).append('\n');
                sb.append("Notes: ").append(valueOrDefault(rs.getString("notes"), "")).append('\n');
                sb.append("---------------------\n");
            }
            output.setText(sb.toString());
            status.setText("Loaded admissions");
        } catch (Exception ex) {
            status.setText("Error: " + ex.getMessage());
        }
    }

    private void viewWards() {
        try (Connection conn = JDBConn.getConnection()) {
            String sql = "select wardId, wardName, wardType, severityRank, totalBeds from wards order by severityRank, wardId";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("Ward Id: ").append(rs.getInt("wardId")).append('\n');
                sb.append("Ward Name: ").append(rs.getString("wardName")).append('\n');
                sb.append("Ward Type: ").append(rs.getString("wardType")).append('\n');
                sb.append("Severity Rank: ").append(rs.getInt("severityRank")).append('\n');
                sb.append("Total Beds: ").append(rs.getInt("totalBeds")).append('\n');
                sb.append("---------------------\n");
            }
            output.setText(sb.toString());
            status.setText("Loaded wards");
        } catch (Exception ex) {
            status.setText("Error: " + ex.getMessage());
        }
    }

    private void viewBeds() {
        try (Connection conn = JDBConn.getConnection()) {
            String sql =
                    "select b.bedId, b.bedNumber, b.isAvailable, w.wardName, w.wardType " +
                    "from beds b join wards w on b.wardId = w.wardId " +
                    "order by w.severityRank, b.bedId";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("Bed Id: ").append(rs.getInt("bedId")).append('\n');
                sb.append("Bed Number: ").append(rs.getString("bedNumber")).append('\n');
                sb.append("Ward: ").append(rs.getString("wardName")).append(" (").append(rs.getString("wardType")).append(")\n");
                sb.append("Available: ").append(rs.getBoolean("isAvailable")).append('\n');
                sb.append("---------------------\n");
            }
            output.setText(sb.toString());
            status.setText("Loaded beds");
        } catch (Exception ex) {
            status.setText("Error: " + ex.getMessage());
        }
    }

    private void recordAdmission(Connection conn, int patientId, String severity, String statusValue, Integer wardId, Integer bedId, String notes) throws Exception {
        String sql = "insert into admissions (patientId, severity, admissionStatus, wardId, bedId, admissionDate, notes) values (?,?,?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, patientId);
        ps.setString(2, severity);
        ps.setString(3, statusValue);
        if (wardId == null) {
            ps.setNull(4, java.sql.Types.INTEGER);
        } else {
            ps.setInt(4, wardId);
        }
        if (bedId == null) {
            ps.setNull(5, java.sql.Types.INTEGER);
        } else {
            ps.setInt(5, bedId);
        }
        ps.setString(6, LocalDate.now().toString());
        ps.setString(7, notes);
        ps.executeUpdate();
    }

    private BedAllocation findAvailableBed(Connection conn, String severity) throws Exception {
        String wardType = resolveWardType(severity);
        String sql =
                "select b.bedId, b.bedNumber, w.wardId, w.wardName, w.wardType " +
                "from beds b join wards w on b.wardId = w.wardId " +
                "where b.isAvailable = true and lower(w.wardType) = ? " +
                "order by b.bedId limit 1 for update";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, wardType.toLowerCase(Locale.ROOT));
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            return null;
        }
        return new BedAllocation(
                rs.getInt("wardId"),
                rs.getString("wardName"),
                rs.getString("wardType"),
                rs.getInt("bedId"),
                rs.getString("bedNumber")
        );
    }

    private void markBedUnavailable(Connection conn, int bedId) throws Exception {
        String sql = "update beds set isAvailable = false where bedId = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, bedId);
        ps.executeUpdate();
    }

    private boolean patientExists(Connection conn, int patientId) throws Exception {
        String sql = "select 1 from patients where patientId = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, patientId);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    private String normalizeSeverity(String severity) {
        return severity == null ? "" : severity.trim().toLowerCase(Locale.ROOT);
    }

    private boolean isConsultOnlySeverity(String severity) {
        return severity.contains("low") || severity.contains("mild") || severity.contains("minor");
    }

    private String resolveWardType(String severity) {
        if (severity.contains("critical")) {
            return "ICU";
        }
        if (severity.contains("high") || severity.contains("severe")) {
            return "Emergency";
        }
        return "General";
    }

    private String valueOrDefault(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value;
    }

    public static void main(String[] args) {
        launch();
    }

    private static class BedAllocation {
        final int wardId;
        final String wardName;
        final String wardType;
        final int bedId;
        final String bedNumber;

        BedAllocation(int wardId, String wardName, String wardType, int bedId, String bedNumber) {
            this.wardId = wardId;
            this.wardName = wardName;
            this.wardType = wardType;
            this.bedId = bedId;
            this.bedNumber = bedNumber;
        }
    }
}