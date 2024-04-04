/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Khanh
 */
public class Nurse extends Person{
    private String department;
    private String shift;
    private double salary;

    public Nurse() {
    }

    public Nurse(String ID,  String name, int age, String gender, String address, String phone, String department, String shift, double salary) {
        super(ID, name, age, gender, address, phone);
        this.department = department;
        this.shift = shift;
        this.salary = salary;
    }


    public String getDepartment() {
        return department;
    }

    public String getShift() {
        return shift;
    }

    public double getSalary() {
        return salary;
    }


    public void setDepartment(String department) {
        this.department = department;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return super.ID + "," + super.name + "," + super.age + "," + super.gender +  "," + super.address + "," + super.phone + "," + department + "," + shift + "," + salary;
    }
    
    
}
