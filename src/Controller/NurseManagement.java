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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Iterator;

import model.Nurse;

/**
 *
 * @author Khanh
 */
public class NurseManagement {

    Validation val;
    Scanner input;
    ArrayList<Nurse> info;
    HashMap<String, ArrayList<Nurse>> nurseList;
    Nurse nurse;

    public NurseManagement() throws FileNotFoundException, IOException {
        val = new Validation();
        input = new Scanner(System.in);
        nurse = new Nurse();
        this.nurseList = readFile();
    }

    public HashMap<String, ArrayList<Nurse>> readFile() throws FileNotFoundException, IOException {
        String path = "src/DB/nurses.dat";
        File file = new File(path);

        if (!file.exists()) {
            System.out.println("File doesn't exist!");
            return new HashMap<>();
        }

        try (FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr)) {
            nurseList = new HashMap<>();

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
                String department = split[6].trim();
                String shift = split[7].trim();
                double salary = Double.parseDouble(split[8].trim());

                info = new ArrayList<>();
                info.add(new Nurse(ID, name, age, gender, address, phone, department, shift, salary));
                nurseList.put(ID, info);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return nurseList;
    }

    public void writeFile(HashMap<String, ArrayList<Nurse>> nurseList, String filename) {
        try {
            FileWriter fw = new FileWriter(filename, false);
            BufferedWriter bw = new BufferedWriter(fw);

            for (Map.Entry<String, ArrayList<Nurse>> entry : nurseList.entrySet()) {
                ArrayList<Nurse> nurses = entry.getValue();
                for (Nurse nurse : nurses) {
                    bw.write(nurse.toString());
                    bw.newLine();
                }
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Integer> nurseAvailable(String filePath) {
        HashMap<String, Integer> nurseCount = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(",");
                String firseNurse = split[split.length - 2].trim();
                String secondNurse = split[split.length - 1].trim();

                val.countNurse(nurseCount, firseNurse);
                val.countNurse(nurseCount, secondNurse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nurseCount;
    }

    public void createNurse() throws FileNotFoundException, IOException {
        String ID;
        String name;
        int age;
        String gender;
        String address;
        String phone;
        String department;
        String shift;
        double salary;
        boolean validPhoneNumber;
        boolean validDepartment;
        boolean check = true;
        info = new ArrayList<>();

        do {
            do {
                do {
                    System.out.print("Enter nurse's ID(NXXXX): ");
                    ID = input.nextLine();
                    if (!val.checkNurseID(ID)) {
                        System.err.println("Invalid nurse's ID. Please enter again!");
                    }
                } while (!val.checkNurseID(ID));
                ID = ID.toUpperCase();
                if (val.checkNurseExistID(nurseList, ID)) {
                    System.out.println("Nurse's ID already exists. Please enter again.");
                }
            } while (val.checkNurseExistID(nurseList, ID));
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

            do {
                department = val.readInputString(input, "Enter department: ");
                validDepartment = val.checkDepartment(department);
                if (!validDepartment) {
                    System.err.println("Invalid department(must be from 3 to 50 chacrater). Please try again.");

                }
            } while (!validDepartment);

            shift = val.readInputString(input, "Enter shift: ");

            do {
                salary = val.readInputDouble(input, "Enter salary: ");
            } while (salary <= 0);

            info.add(new Nurse(ID, name, age, gender, address, phone, department, shift, salary));
            nurseList.put(ID, info);
            System.out.println();
            System.out.println("Continue create nurse? ");
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

    public void findNurse() throws FileNotFoundException, IOException {
        int count = 0;

        String name = val.readInputString(input, "Enter nurse's full name or part name: ");
        for (Map.Entry<String, ArrayList<Nurse>> entry : nurseList.entrySet()) {
            ArrayList<Nurse> nurses = entry.getValue();
            for (Nurse nurse : nurses) {
                if (nurse.getName().toLowerCase().contains(name.toLowerCase())) {
                    System.out.println(nurse);
                    count++;
                }
            }
        }
        if (count == 0) {
            System.err.println("The nurse does not exist");
        } else {
            System.out.println("Found: " + count + " nurse(s)");
        }
        System.out.println();
    }

    public void updateNurse() throws FileNotFoundException, IOException {
        boolean found = false;
        String ID;

        do {
            do {
                ID = val.readInputString(input, "Enter nurse's ID(NXXXX): ");
                if (!val.checkNurseID(ID)) {
                    System.err.println("Invalid nurse's ID. Please enter again!");
                }
            } while (!val.checkNurseID(ID));

            for (Map.Entry<String, ArrayList<Nurse>> entry : nurseList.entrySet()) {
                String key = entry.getKey();
                ArrayList<Nurse> nurses = entry.getValue();

                for (int i = 0; i < nurses.size(); i++) {
                    Nurse nurse = nurses.get(i);
                    if (nurse.getID().equalsIgnoreCase(ID)) {
                        found = true;
                        System.out.println("Update new information:");
                        System.out.println("\n");
                        String name = val.readInputString(input, "Enter name: ");
                        int age;
                        do {
                            age = val.readInputInt(input, "Enter age: ");
                        } while (age <= 0);

                        String gender = val.readInputString(input, "Enter gender: ");
                        String address = val.readInputString(input, "Enter address: ");

                        boolean validPhoneNumber;
                        String phone;
                        do {
                            phone = val.readInputString(input, "Enter phone: ");
                            validPhoneNumber = val.checkPhone(phone);
                            if (!validPhoneNumber) {
                                System.err.println("Invalid phone number. Must start with 0 and has exactly 10 digit numbers. Please try again.");
                            }
                        } while (!validPhoneNumber);

                        boolean validDepartment;
                        String department;
                        do {
                            department = val.readInputString(input, "Enter department: ");
                            validDepartment = val.checkDepartment(department);
                            if (!validDepartment) {
                                System.err.println("Invalid department (must be from 3 to 50 characters). Please try again.");
                            }
                        } while (!validDepartment);

                        String shift = val.readInputString(input, "Enter shift: ");

                        double salary;
                        do {
                            salary = val.readInputDouble(input, "Enter salary: ");
                        } while (salary <= 0);

                        nurses.set(i, new Nurse(ID, name, age, gender, address, phone, department, shift, salary));
                        nurseList.put(key, nurses);
                        break;
                    }
                }
                if (found) {
                    break;
                }
            }
            if (!found) {
                System.err.println("Fail! The nurse does not exist! Please enter again!");
            }
        } while (!found);

        System.out.println("Update successfully!");
        System.out.println("");
    }

    public void deleteNurse() throws FileNotFoundException, IOException {
        boolean nurseExists = false;
        boolean validInput = false;
        String ID;
        ArrayList<String> nursesToRemove = new ArrayList<>();

        do {
            do {
                ID = val.readInputString(input, "Enter nurse's ID(NXXXX): ");
                if (!val.checkNurseID(ID)) {
                    System.err.println("Invalid nurse's ID. Please enter again!");
                }
            } while (!val.checkNurseID(ID));

            for (Map.Entry<String, ArrayList<Nurse>> entry : nurseList.entrySet()) {
                String key = entry.getKey();
                ArrayList<Nurse> nurses = entry.getValue();
                Iterator<Nurse> iterator = nurses.iterator();
                while (iterator.hasNext()) {
                    Nurse nurse = iterator.next();
                    if (nurse.getID().equalsIgnoreCase(ID)) {
                        nurseExists = true;
                        HashMap<String, Integer> nurseCount = nurseAvailable("src//patients.dat");
                        int patientCount = nurseCount.getOrDefault(nurse.getID(), 0);
                        if (patientCount > 0) {
                            System.err.println("This nurse has tasks available at the moment! Cannot delete!! Please enter another nurse's ID!");
                        } else {
                            System.out.println("Delete this nurse?");
                            System.out.println("Yes: y/1/t");
                            System.out.println("No: n/0/f");

                            while (!validInput) {
                                String in = input.nextLine();
                                if (in.equalsIgnoreCase("y") || in.equals("1") || in.equalsIgnoreCase("t")) {
                                    validInput = true;
                                    nursesToRemove.add(key);
                                    System.out.println("Update successfully!");
                                    break;
                                } else if (in.equalsIgnoreCase("n") || in.equals("0") || in.equalsIgnoreCase("f")) {
                                    System.out.println("Action cancelled!");
                                    validInput = true;
                                    break;
                                } else {
                                    validInput = false;
                                    System.err.println("Invalid option. Please enter again!");
                                }
                            }
                        }
                        break;
                    }
                }
            }
            if (!nurseExists) {
                System.err.println("Fail! The nurse does not exist! Please enter again!");
            }
        } while (!validInput || !nurseExists);

        for (String key : nursesToRemove) {
            nurseList.remove(key);
        }
    }

    public void savaDataNurse() throws FileNotFoundException, IOException {
        System.out.println("Successfully save the collection of nurses to the binary file nurses.dat.");
        this.writeFile(nurseList, "src/DB/nurses.dat");
    }

    public void loadDataNurse() throws FileNotFoundException, IOException {
        System.out.println("Successfully load the collection of nurses from the binary file nurses.dat ");
        this.nurseList = readFile();
    }

    public void quitNurse() throws FileNotFoundException, IOException {
        this.writeFile(nurseList, "src/DB/nurses.dat");
        System.out.println("Successfully save data to patients.dat!");
        System.out.println("Successfully save data to nurses.dat!");
        System.out.println("Quit successfully!");
        System.exit(0);
    }
}
