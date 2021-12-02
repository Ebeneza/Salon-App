package Models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class User implements Serializable {
    String Fname, Lname, PhoneNo,Email, RegistrationDate, Address;

    @Override
    public String toString() {
        return "User{" +
                "Fname='" + Fname + '\'' +
                ", Lname='" + Lname + '\'' +
                ", PhoneNo='" + PhoneNo + '\'' +
                ", Email='" + Email + '\'' +
                ", RegistrationDate='" + RegistrationDate + '\'' +
                ", Address='" + Address + '\'' +
                '}';
    }

    public User(String fname, String lname, String phoneNo, String email, String registrationDate, String address) {
        Fname = fname;
        Lname = lname;
        PhoneNo = phoneNo;
        Email = email;
        RegistrationDate = registrationDate;
        Address = address;
    }

    public User(String fname, String lname, String phoneNo, String email, String address) {
        Fname = fname;
        Lname = lname;
        PhoneNo = phoneNo;
        Email = email;
        Address = address;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
        Calendar calendar = Calendar.getInstance();
        String todayDate = simpleDateFormat.format(calendar.getTime());
        RegistrationDate=todayDate;
    }

    public User(){

    }

    public String getFname() {
        return Fname;
    }

    public void setFname(String fname) {
        Fname = fname;
    }

    public String getLname() {
        return Lname;
    }

    public void setLname(String lname) {
        Lname = lname;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getRegistrationDate() {
        return RegistrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        RegistrationDate = registrationDate;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
