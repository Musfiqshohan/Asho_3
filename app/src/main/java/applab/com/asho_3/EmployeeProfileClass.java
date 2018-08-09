package applab.com.asho_3;

import android.net.Uri;

/**
 * Created by musfiq on 7/8/18.
 */

public class EmployeeProfileClass {


    //String DOB,NID,address,email,name,phone,usefulLinks;
    public String Phone,Name,Email,Address,Dob,Usefullinks,Nid,Payment;
    public String ProfilePicture;




    public EmployeeProfileClass()
    {

    }


    public EmployeeProfileClass(String phone, String name, String email, String address, String dob, String usefullinks, String nid,  String payment, String profilePicture) {

        Phone = phone;
        Name = name;
        Email = email;
        Address = address;
        Dob = dob;
        Usefullinks = usefullinks;
        Nid = nid;
        Payment=payment;
        ProfilePicture = profilePicture;
    }


    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDob() {
        return Dob;
    }

    public void setDob(String dob) {
        Dob = dob;
    }

    public String getUsefullinks() {
        return Usefullinks;
    }

    public void setUsefullinks(String usefullinks) {
        Usefullinks = usefullinks;
    }

    public String getNid() {
        return Nid;
    }

    public void setNid(String nid) {
        Nid = nid;
    }

    public String getProfilePicture() {
        return ProfilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        ProfilePicture = profilePicture;
    }
    public String getPayment() {
        return Payment;
    }

    public void setPayment(String payment) {
        this.Payment = payment;
    }



}
