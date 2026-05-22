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

import java.sql.*;
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

    Appointment(
            int appointmentId,
            Patient patient,
            Doctor doctor,
            String date
    ) {

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

class Hospital {

    Connection conn;
    Scanner sc = new Scanner(System.in);

    Hospital() throws SQLException {

        conn = JDBConn.getConnection();

        System.out.println("Database Connected");
    }

    // patient function

    void addPatient() throws SQLException {

        System.out.println("Enter Patient Id");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter Name");
        String name = sc.nextLine();

        System.out.println("Enter Age");
        int age = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter Disease");
        String disease = sc.nextLine();

        String sql =
                "insert into patients values(?,?,?,?)";

        PreparedStatement ps =
                conn.prepareStatement(sql);

        ps.setInt(1, id);
        ps.setString(2, name);
        ps.setInt(3, age);
        ps.setString(4, disease);

        ps.executeUpdate();

        System.out.println("Patient Added");
    }

    void viewPatients() throws SQLException {

        String sql = "select * from patients";

        Statement st =
                conn.createStatement();

        ResultSet rs =
                st.executeQuery(sql);

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

    // doctor function

    void addDoctor() throws SQLException {

        System.out.println("Enter Doctor Id");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter Name");
        String name = sc.nextLine();

        System.out.println("Enter Specialization");
        String specialization = sc.nextLine();

        String sql =
                "insert into doctors values(?,?,?)";

        PreparedStatement ps =
                conn.prepareStatement(sql);

        ps.setInt(1, id);
        ps.setString(2, name);
        ps.setString(3, specialization);

        ps.executeUpdate();

        System.out.println("Doctor Added");
    }

    void viewDoctors() throws SQLException {

        String sql = "select * from doctors";

        Statement st =
                conn.createStatement();

        ResultSet rs =
                st.executeQuery(sql);

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

    // appointment function

    void bookAppointment() throws SQLException {

        System.out.println("Enter Appointment Id");
        int aid = sc.nextInt();

        System.out.println("Enter Patient Id");
        int pid = sc.nextInt();

        System.out.println("Enter Doctor Id");
        int did = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter Date");
        String date = sc.nextLine();

        String sql =
                "insert into appointments values(?,?,?,?)";

        PreparedStatement ps =
                conn.prepareStatement(sql);

        ps.setInt(1, aid);
        ps.setInt(2, pid);
        ps.setInt(3, did);
        ps.setString(4, date);

        ps.executeUpdate();

        System.out.println("Appointment Booked");
    }

    void viewAppointments() throws SQLException {

        String sql =
                "select a.appointmentId,a.date," +
                "p.patientId,p.name,p.age,p.disease," +
                "d.doctorId,d.name as doctorName,d.specialization " +
                "from appointments a " +
                "join patients p on a.patientId = p.patientId " +
                "join doctors d on a.doctorId = d.doctorId";

        Statement st =
                conn.createStatement();

        ResultSet rs =
                st.executeQuery(sql);

        while (rs.next()) {

            Patient p = new Patient(
                    rs.getInt("patientId"),
                    rs.getString("name"),
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
                    rs.getString("date")
            );

            a.displayAppointment();

            System.out.println();
        }
    }

    void closeConnection() throws SQLException {

        conn.close();

        System.out.println("Connection Closed");
    }
}

public class HMS {

    public static void main(String[] args) {

        try {

            Hospital hospital = new Hospital();

            hospital.addPatient();
            hospital.viewPatients();

            hospital.addDoctor();
            hospital.viewDoctors();

            hospital.bookAppointment();
            hospital.viewAppointments();

            hospital.closeConnection();

        } catch (Exception e) {

            System.out.println(e);
        }
    }
}