package com.MetroEnterprises.shopping.Model;

public class Users {
    private String name, phone, password, image, address, new_password;
    public Users()
    {

    }

    public Users(String name, String phone, String password, String image, String address, String new_password) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.image = image;
        this.address = address;
        this.new_password = new_password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public boolean setPassword(String password) {
        this.password = password;
        return false;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public  String getNew_password() {
        return  new_password;
    }

    public  void setNew_password(String new_password){
        this.new_password = new_password;
    }
}
