package org.example;


import java.util.Objects;

public class Order {
    private int order_id;
    private String name;
    private String phone;
    private String email;
    private String adress;

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name.length()<100 && !name.isEmpty()){
            this.name = name;
        } else throw new IllegalArgumentException("Имя покупателя не может содержать больше 100 символов или быть пустым");

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            this.phone = null;
        } else if (phone.length() < 50) {
            this.phone = phone;
        } else throw new IllegalArgumentException("Номер телефона не может содержать больше 50 символов");

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            this.email = null;
        } else if (email.length() < 50) {
            this.email = email;
        } else throw new IllegalArgumentException("Адрес электронной почты не может содержать больше 50 символов");

    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        if (adress.length()<200 && !adress.isEmpty()){
            this.adress = adress;
        } else throw new IllegalArgumentException("Адрес доставки не может содержать больше 200 символов или быть пустым");

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return order_id == order.order_id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(order_id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "order_id=" + order_id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", adress='" + adress + '\'' +
                '}';
    }
}
