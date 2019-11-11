package com.example.tasfri2019;

public class Users {
    private String name, instansi, email, password, role;
    public Users() {
    }
    public Users(String instansi, String name, String email, String password, String role) {
        this.instansi = instansi;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Users(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstansi() {
        return instansi;
    }

    public void setInstansi(String instansi) {
        this.instansi = instansi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
