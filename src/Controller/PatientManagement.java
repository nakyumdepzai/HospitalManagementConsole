/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import model.Nurse;
import model.Patient;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

/**
 *
 * @author Khanh
 */
public class PatientManagement {

    Validation val;
    Scanner input;
    HashMap<String, ArrayList<Patient>> patientList;
    HashMap<String, ArrayList<Nurse>> nurseList;
    ArrayList<Patient> info;
    NurseManagement nm;
    Patient patient;
    LocalDate startDate;
    LocalDate endDate;

    public PatientManagement() throws FileNotFoundException, IOException {
        val = new Validation();
        input = new Scanner(System.in);
        nurseList = new HashMap<>();
        patient = new Patient();
        nm = new NurseManagement();
        this.nurseList = nm.readFile();
        this.patientList = readFile();
    }

    public HashMap<String, ArrayList<Patient>> readFile() throws FileNotFoundException, IOException {
        String path = "src/DB/patients.dat";
        File file = new File(path);

        if (!file.exists()) {
            System.out.println("File doesn't exist!");
            return new HashMap<>();
        }

        try (FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr)) {
            patientList = new HashMap<>();

            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                String[] split = line.split(",");
                if (split.length < 9) {
                    System.out.println("Invalid line format at line " + lineNumber + ": " + line);
                    continue;
                }

                String ID = split[0].trim();
                String name = split[1].trim();
                int age = Integer.parseInt(split[2].trim());
                String gender = split[3].trim();
                String address = split[4].trim();
                String phone = split[5].trim();
                String diagnosis = split[6].trim();
                String admissionDate = split[7].trim();
                String dischargeDate = split[8].trim();
                String nurse1 = split[9].trim();
                String nurse2 = split[10].trim();
                info = new ArrayList<>();
                info.add(new Patient(ID, name, age, gender, address, phone, diagnosis, admissionDate, dischargeDate,
                        nurse1, nurse2));
                patientList.put(ID, info);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return patientList;
    }

    public void writeFile(HashMap<String, ArrayList<Patient>> nurseList, String filename) {
        try {
            FileWriter fw = new FileWriter(filename, false);
            BufferedWriter bw = new BufferedWriter(fw);

            for (Map.Entry<String, ArrayList<Patient>> entry : patientList.entrySet()) {
                ArrayList<Patient> patients = entry.getValue();
                for (Patient nurse : patients) {
                    bw.write(nurse.toString());
                    bw.newLine();
                }
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Integer> nurseAvailable(HashMap<String, ArrayList<Patient>> patientList) {
        HashMap<String, Integer> nurseCount = new HashMap<>();

        for (Map.Entry<String, ArrayList<Patient>> entry : this.patientList.entrySet()) {
            ArrayList<Patient> patients = entry.getValue();
            for (Patient patient : patients) {
                String firstNurse = patient.getNurse1();
                String secondNurse = patient.getNurse2();
                val.countNurse(nurseCount, firstNurse);
                val.countNurse(nurseCount, secondNurse);
            }
        }
        return nurseCount;
    }

    public void addPatient() throws FileNotFoundException, IOException {
        int age;
        String ID;
        String name;
        String gender;
        String address;
        String phone;
        String diagnosis;
        String admissionDateString;
        String dischargeDateString;
        LocalDate admissionDate;
        LocalDate dischargeDate;
        String nurse1;
        String nurse2;
        boolean validPhoneNumber;
        boolean check = true;
        do {
            ArrayList<Patient> newPatientList = new ArrayList<>();
            do {
                do {
                    ID = val.readInputString(input, "Enter patients's ID(PXXXX): ");
                    if (!val.checkPatientID(ID)) {
                        System.err.println("Invalid patient's ID. Please enter again!");
                    }
                } while (!val.checkPatientID(ID));

                ID = ID.toUpperCase();
                if (val.checkPatientExistID(patientList, ID)) {
                    System.err.println("Patient's ID already exists. Please enter again.");
                }
            } while (val.checkPatientExistID(patientList, ID));
            name = val.readInputString(input, "Enter name: ");
            do {
                age = val.readInputInt(input, "Enter age: ");
            } while (age <= 0);

            gender = val.readInputString(input, "Enter gender: ");

            address = val.readInputString(input, "Enter address: ");

            do {
                phone = val.readInputString(input, "Enter phone: ");
                validPhoneNumber = val.checkPhone(phone);
                if (!validPhoneNumber) {
                    System.err.println("Invalid phone number. Must start with 0 and has exactly 10 digit numbers. Please try again.");
                }
            } while (!validPhoneNumber);

            diagnosis = val.readInputString(input, "Enter diagnosis: ");
            do {
                admissionDateString = val.readInputDate(input, "Enter admission date: ");
                admissionDate = LocalDate.parse(admissionDateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                dischargeDateString = val.readInputDate(input, "Enter discharge date: ");
                dischargeDate = LocalDate.parse(dischargeDateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                if (dischargeDate.isBefore(admissionDate)) {
                    System.err.println("Invalid date range. Discharge date should be greater than admission date!");
                }
            } while (dischargeDate.isBefore(admissionDate));

            do {
                System.out.println("Enter nurse assigned:");
                HashMap<String, Integer> nurseCount = nurseAvailable(this.patientList);
                int patientCount = 2;
                do {
                    nurse1 = val.readInputString(input, "Enter first nurse's ID: ");
                    nurse1 = nurse1.toUpperCase();
                    for (String nurseId : nurseCount.keySet()) {
                        if (nurse1.equalsIgnoreCase(nurseId)) {
                            patientCount = nurseCount.get(nurseId);
                        } else if (!nurseCount.containsKey(nurse1)) {
                            patientCount = 0;
                        }
                    }
                    if (!val.checkNurseExistID(this.nurseList, nurse1)) {
                        System.err.println("Nurse's ID doesn't exists. Please enter again.");
                    } else if (patientCount == 2) {
                        System.err.println("This nurse is not available at the moment! Please enter another nurse's ID!");
                    }
                } while (!val.checkNurseExistID(this.nurseList, nurse1) || (patientCount == 2));

                do {
                    nurse2 = val.readInputString(input, "Enter second nurse's ID: ");
                    nurse2 = nurse2.toUpperCase();
                    for (String nurseId : nurseCount.keySet()) {
                        if (nurse2.equalsIgnoreCase(nurseId)) {
                            patientCount = nurseCount.get(nurseId);
                        } else if (!nurseCount.containsKey(nurse2.toUpperCase())) {
                            patientCount = 0;
                        }
                    }
                    if (!val.checkNurseExistID(nurseList, nurse2)) {
                        System.err.println("Nurse's ID doesn't exists. Please enter again!");
                    } else if (patientCount == 2) {
                        System.err.println("This nurse is not available at the moment! Please enter another nurse's ID!");
                    }
                } while (!val.checkNurseExistID(nurseList, nurse2) || (patientCount == 2));
                if (nurse1.equalsIgnoreCase(nurse2)) {
                    System.err.println("Duplicate nurses' ID! Please enter again!");
                }
            } while (nurse1.equalsIgnoreCase(nurse2));

            newPatientList.add(new Patient(ID, name, age, gender, address, phone, diagnosis, admissionDateString,
                    dischargeDateString, nurse1, nurse2));
            patientList.put(ID, newPatientList);
            System.out.println();
            System.out.println("Continue add patient? ");
            System.out.println("Yes: y/1/t");
            System.out.println("No: n/0/f(go back to the main menu)");
            boolean validInput = false;
            while (!validInput) {
                String in = input.nextLine();
                if (in.equalsIgnoreCase("y") || in.equals("1") || in.equalsIgnoreCase("t")) {
                    validInput = true;
                } else if (in.equalsIgnoreCase("n") || in.equals("0") || in.equalsIgnoreCase("f")) {
                    validInput = true;
                    check = false;
                } else {
                    System.err.println("Invalid option. Please enter again!");
                    validInput = false;
                }
            }
        } while (check);
    }

    public void listPatients() throws FileNotFoundException, IOException {
        boolean validDate = false;

        do {
            String startDateString = val.readInputDate(input, "Enter start date: ");
            startDate = LocalDate.parse(startDateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            String endDateString = val.readInputDate(input, "Enter end date: ");
            endDate = LocalDate.parse(endDateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            if (endDate.isBefore(startDate)) {
                System.err.println("Invalid date range. End date should be greater than start date!");
                validDate = false;
            } else {
                validDate = true;
                System.out.println("LIST OF PATIENTS");
                System.out.printf("Start date: %s\n", startDateString);
                System.out.printf("End date: %s\n", endDateString);
                System.out.println(
                        "---------------------------------------------------------------------------------");
                System.out.println(
                        "| No. | Patient ID | Admission Date | Full name      |       Phone | Diagnosis  |");
                System.out.println(
                        "---------------------------------------------------------------------------------");
                int count = 0;
                LocalDate currentDate = startDate;
                while (!currentDate.isAfter(endDate)) {
                    for (Map.Entry<String, ArrayList<Patient>> entry : patientList.entrySet()) {
                        ArrayList<Patient> patients = entry.getValue();
                        for (Patient patient : patients) {
                            LocalDate admissionDate = LocalDate.parse(patient.getAdmissionDate(),
                                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                            if (admissionDate.equals(currentDate)) {
                                count++;
                                System.out.printf("|%4d | %-11s|%15s | %-15s| %11s |%12s|\n", count, patient.getID(),
                                        patient.getAdmissionDate(), patient.getName(), patient.getPhone(),
                                        patient.getDiagnosis());
                                System.out.println(
                                        "---------------------------------------------------------------------------------");
                            }
                        }
                    }
                    currentDate = currentDate.plusDays(1);
                }
                if (count == 0) {
                    System.err.println("No patient in this period of time!");
                }
            }
        } while (!validDate);
    }

    public void sortedListPatients() throws FileNotFoundException, IOException {
        boolean validTerm = false;

        do {
            String sortTerm = val.readInputString(input, "Enter sort term (patient's id, patient's name): ");
            String sortType = val.readInputString(input, "Enter sort type (asc, desc): ");
            sortTerm = sortTerm.toLowerCase();
            sortType = sortType.toLowerCase();
            Comparator<Patient> comparator = null;
            switch (sortTerm) {
                case "patient's id":
                    comparator = Comparator.comparing(Patient::getID);
                    break;
                case "patient's name":
                    comparator = Comparator.comparing(Patient::getName);
                    break;
                default:
                    System.err.println("Invalid sort option.");
                    return;
            }
            if (sortType.equals("desc")) {
                comparator = comparator.reversed();
            } else if (!sortType.equals("asc")) {
                System.out.println("Invalid sort type.");
                return;
            }

            ArrayList<Patient> allPatients = new ArrayList<>();
            for (ArrayList<Patient> patients : patientList.values()) {
                allPatients.addAll(patients);
            }
            Collections.sort(allPatients, comparator);

            System.out.println("LIST OF PATIENTS");
            System.out.println("Sorted by: " + sortTerm);
            System.out.println("Sort order: " + sortType);
            System.out.println(
                    "---------------------------------------------------------------------------------");
            System.out.println(
                    "| No. | Patient ID | Admission Date | Full name      |       Phone | Diagnosis  |");
            System.out.println(
                    "---------------------------------------------------------------------------------");
            int count = 0;
            for (Patient patient : allPatients) {
                count++;
                System.out.printf("|%4d | %-11s|%15s | %-15s| %11s |%12s|\n", count, patient.getID(),
                        patient.getAdmissionDate(), patient.getName(), patient.getPhone(),
                        patient.getDiagnosis());
                System.out.println(
                        "---------------------------------------------------------------------------------");
            }

            validTerm = true;
        } while (!validTerm);
    }

    public void saveDataPatient() throws FileNotFoundException, IOException {
        System.out.println("Successfully save the collection of patients to the binary file patients.dat. ");
        this.writeFile(patientList, "src/DB/patients.dat");
    }

    public void loadDataPatient() throws FileNotFoundException, IOException {
        System.out.println("Successfully load the collection of patients to the binary file patients.dat.");
        this.patientList = readFile();
    }

    public void quitPatient() {
        System.out.println("Do you want to quit?");
        System.out.println("Yes: y/1/t");
        System.out.println("No: n/0/f(back to main menu)");
        boolean validInput = false;
        while (!validInput) {
            String in = input.nextLine();
            if (in.equalsIgnoreCase("y") || in.equals("1") || in.equalsIgnoreCase("t")) {
                validInput = true;
                this.writeFile(patientList, "src/DB/patients.dat");
            } else if (in.equalsIgnoreCase("n") || in.equals("0") || in.equalsIgnoreCase("f")) {
                validInput = true;
            } else {
                System.err.println("Invalid option. Please enter again!");
            }
        }
    }
}
