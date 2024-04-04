package Controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Menu {

    private NurseManagement nm;
    private PatientManagement pm;

    public Menu() throws FileNotFoundException, IOException {
        nm = new NurseManagement();
        pm = new PatientManagement();
    }

    public void Menu() throws IOException {
        Scanner input = new Scanner(System.in);
        int choice = 1;
        while (true) {
            try {
                do {
                    System.out.println("I.Nurse's management");
                    System.out.println("1-Create nurse");
                    System.out.println("2-Find nurse");
                    System.out.println("3-Update nurse");
                    System.out.println("4-Delete nurse");
                    System.out.println();
                    System.out.println("II-Patient's management");
                    System.out.println("5-Add patient");
                    System.out.println("6-List patients");
                    System.out.println("7-Sort patients list");
                    System.out.println("8-Save data");
                    System.out.println("9-Load data");
                    System.out.println("10-Quit");
                    System.out.print("Enter your choice: ");
                    choice = Integer.parseInt(input.nextLine());
                    if (choice > 10 || choice < 1) {
                        System.err.println("Enter your choice from 1 - 10!");
                    }
                } while (choice > 10 || choice < 1);
                switch (choice) {
                    case 1:
                        nm.createNurse();
                        break;
                    case 2:
                        nm.findNurse();
                        break;
                    case 3:
                        nm.updateNurse();
                        break;
                    case 4:
                        nm.deleteNurse();
                        break;
                    case 5:
                        pm.addPatient();
                        break;
                    case 6:
                        pm.listPatients();
                        break;
                    case 7:
                        pm.sortedListPatients();
                        break;
                    case 8:
                        nm.savaDataNurse();
                        pm.saveDataPatient();
                        break;
                    case 9:
                        nm.loadDataNurse();
                        pm.loadDataPatient();
                        break;
                    case 10:
                        pm.quitPatient();
                        nm.quitNurse();
                        break;
                }
            } catch (NumberFormatException e) {
                System.err.println("Enter a valid number!");
            }
        }
    }
}
