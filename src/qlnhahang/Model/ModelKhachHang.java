package qlnhahang.Model;

public class ModelKhachHang {
    private int ID_KH;
    private String name;
    private String phone;
    private String dateJoin; // Sử dụng String thay vì Date
    private long sales;
    private int points;

    public ModelKhachHang() {
    }

    public ModelKhachHang(int ID_KH, String name, String phone, String dateJoin, long sales, int points) {
        this.ID_KH = ID_KH;
        this.name = name;
        this.phone = phone;
        this.dateJoin = dateJoin;
        this.sales = sales;
        this.points = points;
    }

    public int getID_KH() {
        return ID_KH;
    }

    public void setID_KH(int ID_KH) {
        this.ID_KH = ID_KH;
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

    public String getDateJoin() {
        return dateJoin;
    }

    public void setDateJoin(String dateJoin) {
        this.dateJoin = dateJoin;
    }

    public long getSales() {
        return sales;
    }

    public void setSales(long sales) {
        this.sales = sales;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}