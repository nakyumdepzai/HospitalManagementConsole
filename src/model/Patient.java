/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author Khanh
 */
public class Patient extends Person {

    private String diagnosis;
    private String admissionDate;
    private String dischargeDate;
    private String nurse1;
    private String nurse2;

    public Patient() {
    }

    public Patient( String ID, String name, int age, String gender, String address, String phone, String diagnosis, String admissionDate, String dischargeDate, String nurse1, String nurse2) {
        super(ID, name, age, gender, address, phone);
        this.diagnosis = diagnosis;
        this.admissionDate = admissionDate;
        this.dischargeDate = dischargeDate;
        this.nurse1 = nurse1;
        this.nurse2 = nurse2;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public String getDischargeDate() {
        return dischargeDate;
    }

    public String getNurse1() {
        return nurse1;
    }

    public String getNurse2(){
        return nurse2;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    public void setDischargeDate(String dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public void setNurse1(String nurse1) {
        this.nurse1 = nurse1;
    }

    public void setNurse2(String nurse2){
        this.nurse2 = nurse2;
    }

    @Override
    public String toString() {
        return super.ID + "," + super.name + "," + super.age + "," + super.gender +  "," + super.address + "," + super.phone + "," + diagnosis + "," + admissionDate + "," + dischargeDate + "," + nurse1 + "," + nurse2;
    }
}
