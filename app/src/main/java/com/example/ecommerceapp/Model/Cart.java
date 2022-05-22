package com.example.ecommerceapp.Model;

public class Cart {
    private String pid,pname, price,quantity,discount;

    public Cart() {
    }
// quantity work bennding here...... need to work on later(constructor ,geter and setter work)
    public Cart(String pid, String pname, String price, String discount) {
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
