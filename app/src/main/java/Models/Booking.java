package Models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Booking {
    String UserId;
    String PhoneNo;
    String Username, TotalAmount;
    String Address;

    public String getDate() {
        return Date;
    }

    String Date;
    String Time;


    ArrayList<Cart> cartItems;

    public Booking(String userId, String phoneNo, String username, String address, ArrayList<Cart> cartItems, String totalAmount) {
        UserId = userId;
        PhoneNo = phoneNo;
        Username = username;
        Address = address;
        TotalAmount=totalAmount;
        this.cartItems = cartItems;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        Calendar calendar = Calendar.getInstance();
        String todayDate = simpleDateFormat.format(calendar.getTime());

        Time =todayDate;
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(pattern);
        Date=simpleDateFormat1.format(new Date());


    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public ArrayList<Cart> getCartItems() {
        return cartItems;
    }

    public void setCartItems(ArrayList<Cart> cartItems) {
        this.cartItems = cartItems;
    }
}
