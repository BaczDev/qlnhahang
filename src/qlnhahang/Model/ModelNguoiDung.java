
package qlnhahang.Model;

// Model Người dùng của hệ thống 
public class ModelNguoiDung {

    private int userID;
    private String email;
    private String password;
    private String role;
    private String phone;

    public ModelNguoiDung() {
    }

    public ModelNguoiDung(int userID, String email, String password, String role) {
        this.userID = userID;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public ModelNguoiDung(int userID, String email, String password, String role, String phone) {
        this.userID = userID;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
