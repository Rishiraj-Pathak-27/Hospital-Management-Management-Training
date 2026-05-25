// // package CaseStudy.HospitalManagementSystem;
// import java.util.ArrayList;


// // class patients to display the patient attributes
// class Patient{
//     private int patientId;
//     private String name;
//     private int age;
//     private String disease;

//     // getters and setters (encapsulation)

//     Patient(int patientId, String name, int age, String disease){
//         this.patientId = patientId;
//         this.name=name;
//         this.age=age;
//         this.disease=disease;
//     }

//         void displayPatient(){
//         System.out.println("Patient Id = "+this.patientId);
//         System.out.println("Patient name = "+this.name);
//         System.out.println("Patient age = "+this.age);
//         System.out.println("Disease = "+this.disease);
//         System.out.println();
    
//     }
    
// }

// // doctor attributes
// class Doctor{
//     private int doctorId;
//     private String name;
//     private String specialization;

//     Doctor(int doctorId, String name, String specialization){
//         this.doctorId = doctorId;
//         this.name = name;
//         this.specialization = specialization;
//     }

//     void displayDoctor(){
//         System.out.println("Doctor Id = "+this.doctorId);
//         System.out.println("Doctor name = "+this.name);
//         System.out.println("Specialization = "+this.specialization);
//         System.out.println();
    
//     }
// }

// // appointment attributes using patient and doctor handle
// class Appointment{
//     private int appointmentId;
//     private Patient patient;
//     private Doctor doctor;
//     private String date;

//     Appointment(int appointmentId, Patient patient, Doctor doctor, String date){
//         this.appointmentId = appointmentId;
//         this.patient = patient;
//         this.doctor = doctor;
//         this.date = date;
//     }

//     void displayAppointment(){
//         System.out.println("Appointment Id = "+this.appointmentId);
//         this.patient.displayPatient();
//         this.doctor.displayDoctor();
//         System.out.println("Date = "+this.date);
//         System.out.println();
//     }
// }


// class Hospital{
//     ArrayList<Patient> pat;
//     ArrayList<Doctor> doc;
//     ArrayList<Appointment> app;

//     Hospital(){
//         pat = new ArrayList<>();
//         doc = new ArrayList<>();
//         app = new ArrayList<>();
//     }

//     void addPatients(Patient patient){
//         pat.add(patient);
//         System.out.println("Patient Added");
//     }

//     void viewpatients(){
//         if(pat.isEmpty()){
//             System.out.println("No patients");
//         }

//         for(Patient patient:pat){
//             System.out.println("Patient = "+patient);
//         }
//     }

//     void addDoctor(Doctor doctor){
//         doc.add(doctor);
//         System.out.println("Doctor added");
//     }

//     void viewDoctor(){
//         if(doc.isEmpty()){
//             System.out.println("No doctors available");
//         }

//         for(Doctor doctor:doc){
//             System.out.println("Doctor = "+doctor);
//         }
//     }

//     void bookAppointment(Appointment appointment){
//         app.add(appointment);
//         System.out.println("Appointments booked");
//     }

//     void viewAppointment(){
//         if(app.isEmpty()){
//             System.out.println("No appointments right now");
//         }

//         for(Appointment appointment:app){
//             System.out.println("Appointments = "+appointment);
//         }
//     }
    
// }


// public class HMS{
//     public static void main(String[] args){

//         Patient p = new Patient(101,"john",20, "throat infection");
//         p.displayPatient();

//         Doctor d = new Doctor(201,"Rishiraj","Orthopedic");
//         d.displayDoctor();

//         Appointment a = new Appointment(1001,p,d,"19-05-2026");
//         a.displayAppointment();

//     }
// }

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Scanner;

class Patient {

    int patientId;
    String name;
    int age;
    String disease;

    Patient(int patientId, String name, int age, String disease) {

        this.patientId = patientId;
        this.name = name;
        this.age = age;
        this.disease = disease;
    }

    void displayPatient() {

        System.out.println("Patient Id : " + patientId);
        System.out.println("Name : " + name);
        System.out.println("Age : " + age);
        System.out.println("Disease : " + disease);
    }
}

class Doctor {

    int doctorId;
    String name;
    String specialization;

    Doctor(int doctorId, String name, String specialization) {

        this.doctorId = doctorId;
        this.name = name;
        this.specialization = specialization;
    }

    void displayDoctor() {

        System.out.println("Doctor Id : " + doctorId);
        System.out.println("Name : " + name);
        System.out.println("Specialization : " + specialization);
    }
}

class Appointment {

    int appointmentId;
    Patient patient;
    Doctor doctor;
    String date;

    Appointment(int appointmentId, Patient patient, Doctor doctor, String date) {

        this.appointmentId = appointmentId;
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
    }

    void displayAppointment() {

        System.out.println("Appointment Id : " + appointmentId);
        System.out.println("Date : " + date);

        patient.displayPatient();
        doctor.displayDoctor();
    }
}

class BedAllocation {

    int wardId;
    String wardName;
    String wardType;
    int bedId;
    String bedNumber;

    BedAllocation(int wardId, String wardName, String wardType, int bedId, String bedNumber) {

        this.wardId = wardId;
        this.wardName = wardName;
        this.wardType = wardType;
        this.bedId = bedId;
        this.bedNumber = bedNumber;
    }
}

class Hospital implements AutoCloseable {

    private static final String STATUS_CONSULT_ONLY = "CONSULT_ONLY";
    private static final String STATUS_ADMITTED = "ADMITTED";

    private final Connection conn;
    private final Scanner sc = new Scanner(System.in);

    Hospital() throws SQLException {

        conn = JDBConn.getConnection();
        seedDefaultWardData();
        System.out.println("Database connected");
    }

    void run() throws SQLException {

        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("1. Add Patient");
            System.out.println("2. View Patients");
            System.out.println("3. Add Doctor");
            System.out.println("4. View Doctors");
            System.out.println("5. Book Appointment");
            System.out.println("6. View Appointments");
            System.out.println("7. Admit Patient");
            System.out.println("8. View Wards");
            System.out.println("9. View Beds");
            System.out.println("10. View Admissions");
            System.out.println("0. Exit");

            int choice = readInt("Choose an option");

            switch (choice) {
                case 1 -> addPatient();
                case 2 -> viewPatients();
                case 3 -> addDoctor();
                case 4 -> viewDoctors();
                case 5 -> bookAppointment();
                case 6 -> viewAppointments();
                case 7 -> admitPatient();
                case 8 -> viewWards();
                case 9 -> viewBeds();
                case 10 -> viewAdmissions();
                case 0 -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    void addPatient() throws SQLException {

        int id = readInt("Enter Patient Id");
        String name = readText("Enter Name");
        int age = readInt("Enter Age");
        String disease = readText("Enter Disease");

        String sql = "insert into patients (patientId, name, age, disease) values (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setInt(3, age);
            ps.setString(4, disease);
            ps.executeUpdate();
        }

        System.out.println("Patient added");
    }

    void viewPatients() throws SQLException {

        String sql = "select * from patients order by patientId";

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Patient p = new Patient(
                        rs.getInt("patientId"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("disease")
                );
                p.displayPatient();
                System.out.println();
            }
        }
    }

    void addDoctor() throws SQLException {

        int id = readInt("Enter Doctor Id");
        String name = readText("Enter Name");
        String specialization = readText("Enter Specialization");

        String sql = "insert into doctors (doctorId, name, specialization) values (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, specialization);
            ps.executeUpdate();
        }

        System.out.println("Doctor added");
    }

    void viewDoctors() throws SQLException {

        String sql = "select * from doctors order by doctorId";

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Doctor d = new Doctor(
                        rs.getInt("doctorId"),
                        rs.getString("name"),
                        rs.getString("specialization")
                );
                d.displayDoctor();
                System.out.println();
            }
        }
    }

    void bookAppointment() throws SQLException {

        int aid = readInt("Enter Appointment Id");
        int pid = readInt("Enter Patient Id");
        int did = readInt("Enter Doctor Id");
        String date = readText("Enter Date (for example 2026-05-26)");

        String sql = "insert into appointments (appointmentId, patientId, doctorId, appointment_date) values (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, aid);
            ps.setInt(2, pid);
            ps.setInt(3, did);
            ps.setString(4, date);
            ps.executeUpdate();
        }

        System.out.println("Appointment booked");
    }

    void viewAppointments() throws SQLException {

        String sql =
                "select a.appointmentId, a.appointment_date, " +
                "p.patientId, p.name as patientName, p.age, p.disease, " +
                "d.doctorId, d.name as doctorName, d.specialization " +
                "from appointments a " +
                "join patients p on a.patientId = p.patientId " +
                "join doctors d on a.doctorId = d.doctorId " +
                "order by a.appointmentId";

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Patient p = new Patient(
                        rs.getInt("patientId"),
                        rs.getString("patientName"),
                        rs.getInt("age"),
                        rs.getString("disease")
                );

                Doctor d = new Doctor(
                        rs.getInt("doctorId"),
                        rs.getString("doctorName"),
                        rs.getString("specialization")
                );

                Appointment a = new Appointment(
                        rs.getInt("appointmentId"),
                        p,
                        d,
                        rs.getString("appointment_date")
                );

                a.displayAppointment();
                System.out.println();
            }
        }
    }

    void admitPatient() throws SQLException {

        int patientId = readInt("Enter Patient Id");
        String severity = normalizeSeverity(readText("Enter Severity (low, medium, high, critical)"));
        String notes = readText("Enter Notes");

        Patient patient = getPatientById(patientId);

        if (patient == null) {
            System.out.println("Patient not found");
            return;
        }

        if (isConsultOnlySeverity(severity)) {
            recordAdmission(patientId, severity, STATUS_CONSULT_ONLY, null, null, notes);
            System.out.println("Low severity found. Consultation only. No bed allocation needed.");
            return;
        }

        BedAllocation allocation = findAvailableBed(severity);

        if (allocation == null) {
            System.out.println("No available ward/bed found for this severity");
            return;
        }

        conn.setAutoCommit(false);

        try {
            markBedUnavailable(allocation.bedId);
            recordAdmission(patientId, severity, STATUS_ADMITTED, allocation.wardId, allocation.bedId, notes);
            conn.commit();

            System.out.println("Patient admitted");
            System.out.println("Ward : " + allocation.wardName + " (" + allocation.wardType + ")");
            System.out.println("Bed : " + allocation.bedNumber);
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    void viewWards() throws SQLException {

        String sql = "select wardId, wardName, wardType, severityRank, totalBeds from wards order by severityRank, wardId";

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("Ward Id : " + rs.getInt("wardId"));
                System.out.println("Ward Name : " + rs.getString("wardName"));
                System.out.println("Ward Type : " + rs.getString("wardType"));
                System.out.println("Severity Rank : " + rs.getInt("severityRank"));
                System.out.println("Total Beds : " + rs.getInt("totalBeds"));
                System.out.println();
            }
        }
    }

    void viewBeds() throws SQLException {

        String sql =
                "select b.bedId, b.bedNumber, b.isAvailable, " +
                "w.wardId, w.wardName, w.wardType " +
                "from beds b " +
                "join wards w on b.wardId = w.wardId " +
                "order by w.severityRank, b.bedId";

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("Bed Id : " + rs.getInt("bedId"));
                System.out.println("Bed Number : " + rs.getString("bedNumber"));
                System.out.println("Ward : " + rs.getString("wardName") + " (" + rs.getString("wardType") + ")");
                System.out.println("Available : " + rs.getBoolean("isAvailable"));
                System.out.println();
            }
        }
    }

    void viewAdmissions() throws SQLException {

        String sql =
                "select a.admissionId, a.admissionDate, a.severity, a.admissionStatus, a.notes, " +
                "p.patientId, p.name as patientName, p.age, p.disease, " +
                "w.wardName, w.wardType, b.bedNumber " +
                "from admissions a " +
                "join patients p on a.patientId = p.patientId " +
                "left join wards w on a.wardId = w.wardId " +
                "left join beds b on a.bedId = b.bedId " +
                "order by a.admissionId desc";

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("Admission Id : " + rs.getInt("admissionId"));
                System.out.println("Admission Date : " + rs.getString("admissionDate"));
                System.out.println("Severity : " + rs.getString("severity"));
                System.out.println("Status : " + rs.getString("admissionStatus"));
                System.out.println("Patient Id : " + rs.getInt("patientId"));
                System.out.println("Patient Name : " + rs.getString("patientName"));
                System.out.println("Age : " + rs.getInt("age"));
                System.out.println("Disease : " + rs.getString("disease"));
                System.out.println("Ward : " + valueOrDefault(rs.getString("wardName"), "N/A"));
                System.out.println("Bed : " + valueOrDefault(rs.getString("bedNumber"), "N/A"));
                System.out.println("Notes : " + valueOrDefault(rs.getString("notes"), ""));
                System.out.println();
            }
        }
    }

    private Patient getPatientById(int patientId) throws SQLException {

        String sql = "select * from patients where patientId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return new Patient(
                        rs.getInt("patientId"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("disease")
                );
            }
        }
    }

    private BedAllocation findAvailableBed(String severity) throws SQLException {

        String wardType = resolveWardType(severity);
        String sql =
                "select b.bedId, b.bedNumber, w.wardId, w.wardName, w.wardType " +
                "from beds b " +
                "join wards w on b.wardId = w.wardId " +
                "where b.isAvailable = true and lower(w.wardType) = ? " +
                "order by b.bedId " +
                "limit 1 for update";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, wardType.toLowerCase(Locale.ROOT));

            try (ResultSet rs = ps.executeQuery()) {
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
        }
    }

    private void markBedUnavailable(int bedId) throws SQLException {

        String sql = "update beds set isAvailable = false where bedId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bedId);
            ps.executeUpdate();
        }
    }

    private void recordAdmission(
            int patientId,
            String severity,
            String status,
            Integer wardId,
            Integer bedId,
            String notes
    ) throws SQLException {

        String sql =
                "insert into admissions " +
                "(patientId, severity, admissionStatus, wardId, bedId, admissionDate, notes) " +
                "values (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setString(2, severity);
            ps.setString(3, status);

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
    }

    private void seedDefaultWardData() throws SQLException {

        if (hasRows("wards")) {
            return;
        }

        insertWardWithBeds("Critical Care Ward", "ICU", 1, 2);
        insertWardWithBeds("Emergency Ward", "Emergency", 2, 3);
        insertWardWithBeds("General Ward", "General", 3, 5);
    }

    private void insertWardWithBeds(String wardName, String wardType, int severityRank, int totalBeds) throws SQLException {

        String insertWard = "insert into wards (wardName, wardType, severityRank, totalBeds) values (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(insertWard)) {
            ps.setString(1, wardName);
            ps.setString(2, wardType);
            ps.setInt(3, severityRank);
            ps.setInt(4, totalBeds);
            ps.executeUpdate();
        }

        int wardId = getWardIdByName(wardName);

        String insertBed = "insert into beds (wardId, bedNumber, isAvailable) values (?, ?, true)";

        try (PreparedStatement ps = conn.prepareStatement(insertBed)) {
            for (int index = 1; index <= totalBeds; index++) {
                ps.setInt(1, wardId);
                ps.setString(2, wardType + "-" + index);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private int getWardIdByName(String wardName) throws SQLException {

        String sql = "select wardId from wards where wardName = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, wardName);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("Ward not found: " + wardName);
                }

                return rs.getInt("wardId");
            }
        }
    }

    private boolean hasRows(String tableName) throws SQLException {

        String sql = "select count(*) as total from " + tableName;

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            rs.next();
            return rs.getInt("total") > 0;
        }
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

    private String normalizeSeverity(String severity) {

        return severity == null ? "" : severity.trim().toLowerCase(Locale.ROOT);
    }

    private int readInt(String prompt) {

        while (true) {
            System.out.println(prompt);
            String value = sc.nextLine().trim();

            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                System.out.println("Enter a valid number");
            }
        }
    }

    private String readText(String prompt) {

        while (true) {
            System.out.println(prompt);
            String value = sc.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("Value cannot be empty");
        }
    }

    private String valueOrDefault(String value, String defaultValue) {

        return value == null || value.isBlank() ? defaultValue : value;
    }

    @Override
    public void close() throws SQLException {

        sc.close();
        conn.close();
        System.out.println("Connection closed");
    }
}

public class HMS {

    public static void main(String[] args) {

        try (Hospital hospital = new Hospital()) {
            hospital.run();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}