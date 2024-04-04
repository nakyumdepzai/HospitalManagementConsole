/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.Nurse;
import model.Patient;

/**
 *
 * @author Khanh
 */
public class Validation {

    public boolean checkNurseExistID(HashMap<String, ArrayList<Nurse>> hashMap, String ID) {
        return hashMap.containsKey(ID.toUpperCase());
    }

    public boolean checkPatientExistID(HashMap<String, ArrayList<Patient>> hashMap, String ID) {
        return hashMap.containsKey(ID.toUpperCase());
    }

    public boolean checkDepartment(String department) {
        Pattern pattern = Pattern.compile(".{3,50}");
        Matcher matcher = pattern.matcher(department);
        return matcher.matches();
    }

    public boolean checkPhone(String phone) {
        Pattern pattern = Pattern.compile("^[0][0-9]{9}$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
    
    public boolean checkNurseID(String nurseID){
        Pattern pattern = Pattern.compile("^[Nn][0-9]{4}$");
        Matcher matcher = pattern.matcher(nurseID);
        return matcher.matches();
    }
          
    public boolean checkPatientID(String patientID){
        Pattern pattern = Pattern.compile("^[Pp][0-9]{4}$");
        Matcher matcher = pattern.matcher(patientID);
        return matcher.matches();
    }
    
    public double readInputDouble(Scanner input, String message) {
        double salary = 0.0;
        String enter;
        boolean isValid = false;
        do {
            System.out.print(message);
            enter = input.nextLine();
            if (enter.isEmpty()) {
                System.err.println("Can't leave this value empty!");
            } else {
                try {
                    salary = Double.parseDouble(enter);
                    isValid = true;
                } catch (NumberFormatException e) {
                    System.err.println("Invalid salary. Please enter a valid number!");
                    break;
                }
                if (salary <= 0) {
                    System.err.println("Invalid salary(must be over 0). Please input again!");
                }
            }
        } while (!isValid && enter.isEmpty() && (salary <= 0));
        return salary;
    }

    public String readInputString(Scanner input, String message) {
        String enter;
        do {
            System.out.print(message);
            enter = input.nextLine();
            if (enter.isEmpty()) {
                System.out.println("Can't leave this value empty.");
            }
        } while (enter.isEmpty());
        return enter;
    }

    public int readInputInt(Scanner input, String message) {
        int age = 0;
        String enter;
        boolean isValid = false;
        do {
            System.out.print(message);
            enter = input.nextLine();
            if (enter.isEmpty()) {
                System.err.println("Can't leave this value empty!");
            } else {
                try {
                    age = Integer.parseInt(enter);
                    isValid = true;
                } catch (NumberFormatException e) {
                    System.err.println("Invalid age. Please enter a valid number.");
                    break;
                }
                if (age <= 0) {
                    System.err.println("Invalid age(must be over 0). Please input again!");
                }
            }
        } while (enter.isEmpty() && !isValid && (age <= 0));
        return age;
    }

    public String readInputDate(Scanner input, String message) {
        String enter;
        LocalDate date = null;
        boolean validStartDate = false;
        do {
            System.out.print(message);
            enter = input.nextLine().trim();
            if (enter.isEmpty()) {
                System.err.println("Can't leave this value empty.");
                validStartDate = true;
            } else {
                try {
                    date = LocalDate.parse(enter, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    validStartDate = true;
                } catch (Exception e) {
                    System.err.println("Invalid date format. Please enter dates in dd/MM/yyyy format.");
                }
            }
        } while (!validStartDate);

        return enter;
    }

    public void countNurse(HashMap<String, Integer> map, String ID) {
        if (map.containsKey(ID)) {
            int count = map.get(ID);
            map.put(ID, count + 1);
        } else {
            map.put(ID, 1);
        }
    }
}
