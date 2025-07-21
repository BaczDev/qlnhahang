package qlnhahang.Controller.Service;

import qlnhahang.Controller.Connection.DatabaseConnection;
import qlnhahang.Model.ModelLogin;
import qlnhahang.Model.ModelNguoiDung;

import java.sql.*;

public class ServiceUser {

    private final Connection con;

    public ServiceUser() {
        con = DatabaseConnection.getInstance().getConnection();
    }

    public ModelNguoiDung login(ModelLogin login) throws SQLException {
        ModelNguoiDung user = null;
        String sql = "SELECT * FROM NguoiDung WHERE Email=? AND Matkhau=? LIMIT 1";
        PreparedStatement p = con.prepareStatement(sql);
        p.setString(1, login.getEmail());
        p.setString(2, login.getPassword());
        ResultSet r = p.executeQuery();
        if (r.next()) {
            int userID = r.getInt("ID_ND");
            String email = r.getString("Email");
            String password = r.getString("Matkhau");
            String role = r.getString("Vaitro");
            user = new ModelNguoiDung(userID, email, password, role);
        }
        r.close();
        p.close();
        return user;
    }

    public void insertUser(ModelNguoiDung user) throws SQLException {
        // Kiểm tra email hợp lệ
        if (!user.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Email không hợp lệ!");
        }

        // Kiểm tra email trùng lặp
        if (checkDuplicateEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại!");
        }

        // Kiểm tra phone hợp lệ
        String phone = user.getPhone();
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập số điện thoại");
        }
        if (!phone.matches("\\d{10}")) {
            throw new IllegalArgumentException("Số điện thoại phải gồm đúng 10 chữ số!");
        }

        // Lấy ID_ND mới
        PreparedStatement p1 = con.prepareStatement("SELECT MAX(ID_ND) AS ID_ND FROM NguoiDung");
        ResultSet r = p1.executeQuery();
        int userID = 1;
        if (r.next()) {
            userID = r.getInt("ID_ND") + 1;
        }
        r.close();
        p1.close();

        // Thêm vào bảng NguoiDung
        String sql_ND = "INSERT INTO NguoiDung (ID_ND, Email, MatKhau, Vaitro) VALUES (?, ?, ?, 'Khach Hang')";
        PreparedStatement p = con.prepareStatement(sql_ND);
        p.setInt(1, userID);
        p.setString(2, user.getEmail());
        p.setString(3, user.getPassword());
        p.execute();
        p.close();

        // Cập nhật userID và role cho đối tượng user
        user.setUserID(userID);
        user.setRole("Khach Hang");

        // Tạo TenKH từ email
        String name = user.getEmail().substring(0, user.getEmail().indexOf('@'));

        // Thêm thông tin khách hàng
        insertCustomerInfo(userID, name, phone);
    }


    public boolean checkDuplicateEmail(String email) throws SQLException {
        boolean duplicate = false;
        String sql = "SELECT * FROM NguoiDung WHERE Email=? LIMIT 1";
        PreparedStatement p = con.prepareStatement(sql);
        p.setString(1, email);
        ResultSet r = p.executeQuery();
        if (r.next()) {
            duplicate = true;
        }
        r.close();
        p.close();
        return duplicate;
    }

    public void insertCustomerInfo(int userID, String name, String phone) throws SQLException {
        int id = 1;
        String sql_ID = "SELECT MAX(ID_KH) AS ID FROM KhachHang";
        PreparedStatement p_id = con.prepareStatement(sql_ID);
        ResultSet r = p_id.executeQuery();
        if (r.next()) {
            id = r.getInt("ID") + 1;
        }
        r.close();
        p_id.close();

        String sql_KH = "INSERT INTO KhachHang (ID_KH, TenKH, Ngaythamgia, ID_ND, SDT) VALUES (?, ?, CURDATE(), ?, ?)";
        PreparedStatement p2 = con.prepareStatement(sql_KH);
        p2.setInt(1, id);
        p2.setString(2, name);
        p2.setInt(3, userID);
        p2.setString(4, phone); // Lưu SDT từ tham số phone
        p2.execute();
        p2.close();
    }


    public void changePassword(int userID, String newPass) throws SQLException {
        String sql = "UPDATE NguoiDung SET MatKhau = ? WHERE ID_ND = ?";
        PreparedStatement p = con.prepareStatement(sql);
        p.setString(1, newPass);
        p.setInt(2, userID);
        p.execute();
        p.close();
    }
}
