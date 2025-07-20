



CREATE DATABASE IF NOT EXISTS qlnhahang;
USE qlnhahang;


-- Tạo bảng NguoiDung
CREATE TABLE NguoiDung (
    ID_ND INT PRIMARY KEY,
    Email VARCHAR(50),
    Matkhau VARCHAR(20),
    Vaitro VARCHAR(20)
);

-- Tạo bảng KhachHang
CREATE TABLE KhachHang (
    ID_KH INT PRIMARY KEY,
    TenKH VARCHAR(50),
    Ngaythamgia DATE,
    Doanhso INT DEFAULT 0,
    Diemtichluy INT DEFAULT 0,
    SDT VARCHAR(20),
    ID_ND INT,
    FOREIGN KEY (ID_ND) REFERENCES NguoiDung(ID_ND)
);

-- Tạo bảng NhanVien
CREATE TABLE NhanVien (
    ID_NV INT PRIMARY KEY,
    TenNV VARCHAR(50),
    NgayVL DATE,
    SDT VARCHAR(50),
    Chucvu VARCHAR(50),
    ID_ND INT,
    ID_NQL INT,
    Tinhtrang VARCHAR(20),
    FOREIGN KEY (ID_ND) REFERENCES NguoiDung(ID_ND),
    FOREIGN KEY (ID_NQL) REFERENCES NhanVien(ID_NV)
);

-- Tạo bảng MonAn
CREATE TABLE MonAn (
    ID_MonAn INT PRIMARY KEY,
    TenMon VARCHAR(50),
    DonGia INT,
    Loai VARCHAR(50),
    TrangThai VARCHAR(30),
    HinhAnh VARCHAR(255)
);

-- Tạo bảng Ban
CREATE TABLE Ban (
    ID_Ban INT PRIMARY KEY,
    TenBan VARCHAR(50),
    Vitri VARCHAR(50),
    Trangthai VARCHAR(50)
);

-- Tạo bảng Voucher
CREATE TABLE Voucher (
    Code_Voucher VARCHAR(10) PRIMARY KEY,
    Mota VARCHAR(250),
    Phantram INT,
    LoaiMA VARCHAR(50),
    SoLuong INT,
    Diem INT
);

-- Tạo bảng HoaDon
CREATE TABLE HoaDon (
    ID_HoaDon INT PRIMARY KEY,
    ID_KH INT,
    ID_Ban INT,
    NgayHD DATE,
    TienMonAn INT,
    Code_Voucher VARCHAR(10),
    TienGiam INT,
    Tongtien INT,
    Trangthai VARCHAR(250),
    FOREIGN KEY (ID_KH) REFERENCES KhachHang(ID_KH),
    FOREIGN KEY (ID_Ban) REFERENCES Ban(ID_Ban),
    FOREIGN KEY (Code_Voucher) REFERENCES Voucher(Code_Voucher)
);

-- Tạo bảng CTHD
CREATE TABLE CTHD (
    ID_HoaDon INT,
    ID_MonAn INT,
    SoLuong INT,
    Thanhtien INT,
    PRIMARY KEY (ID_HoaDon, ID_MonAn),
    FOREIGN KEY (ID_HoaDon) REFERENCES HoaDon(ID_HoaDon),
    FOREIGN KEY (ID_MonAn) REFERENCES MonAn(ID_MonAn)
);

-- Tạo bảng NguyenLieu
CREATE TABLE NguyenLieu (
    ID_NL INT PRIMARY KEY,
    TenNL VARCHAR(50),
    Dongia INT,
    Donvitinh VARCHAR(50)
);

-- Tạo bảng Kho
CREATE TABLE Kho (
    ID_NL INT PRIMARY KEY,
    SLTon INT,
    FOREIGN KEY (ID_NL) REFERENCES NguyenLieu(ID_NL)
);

-- Tạo bảng PhieuNK
CREATE TABLE PhieuNK (
    ID_NK INT PRIMARY KEY,
    ID_NV INT,
    NgayNK DATE,
    Tongtien INT,
    FOREIGN KEY (ID_NV) REFERENCES NhanVien(ID_NV)
);

-- Tạo bảng CTNK
CREATE TABLE CTNK (
    ID_NK INT,
    ID_NL INT,
    SoLuong INT,
    Thanhtien INT,
    PRIMARY KEY (ID_NK, ID_NL),
    FOREIGN KEY (ID_NK) REFERENCES PhieuNK(ID_NK),
    FOREIGN KEY (ID_NL) REFERENCES NguyenLieu(ID_NL)
);

-- Tạo bảng PhieuXK
CREATE TABLE PhieuXK (
    ID_XK INT PRIMARY KEY,
    ID_NV INT,
    NgayXK DATE,
    FOREIGN KEY (ID_NV) REFERENCES NhanVien(ID_NV)
);

-- Tạo bảng CTXK
CREATE TABLE CTXK (
    ID_XK INT,
    ID_NL INT,
    SoLuong INT,
    PRIMARY KEY (ID_XK, ID_NL),
    FOREIGN KEY (ID_XK) REFERENCES PhieuXK(ID_XK),
    FOREIGN KEY (ID_NL) REFERENCES NguyenLieu(ID_NL)
);

-- Tao Trigger

-- Trigger BEFORE INSERT để tính ThanhTien trong CTHD
DROP TRIGGER IF EXISTS tg_cthd_thanhtien;
DELIMITER $$
CREATE TRIGGER tg_cthd_thanhtien
BEFORE INSERT ON CTHD
FOR EACH ROW
BEGIN
  DECLARE gia DECIMAL(10,2);
  SELECT DonGia INTO gia FROM MonAn WHERE ID_MonAn = NEW.ID_MonAn;
  SET NEW.ThanhTien = NEW.SoLuong * gia;
END$$
DELIMITER ;

-- Trigger AFTER INSERT để cập nhật TienMonAn và TongTien của HoaDon
DROP TRIGGER IF EXISTS tg_update_hd_after_insert_cthd;
DELIMITER $$
CREATE TRIGGER tg_update_hd_after_insert_cthd
AFTER INSERT ON CTHD
FOR EACH ROW
BEGIN
  DECLARE tongMonAn DECIMAL(10,2);
  SELECT SUM(ThanhTien) INTO tongMonAn FROM CTHD WHERE ID_HoaDon = NEW.ID_HoaDon;
  UPDATE HoaDon
  SET TienMonAn = IFNULL(tongMonAn, 0),
      TongTien = IFNULL(tongMonAn, 0) - IFNULL(TienGiam, 0)
  WHERE ID_HoaDon = NEW.ID_HoaDon;
END$$
DELIMITER ;

-- Trigger AFTER DELETE để cập nhật TienMonAn và TongTien của HoaDon
DROP TRIGGER IF EXISTS tg_update_hd_after_delete_cthd;
DELIMITER $$
CREATE TRIGGER tg_update_hd_after_delete_cthd
AFTER DELETE ON CTHD
FOR EACH ROW
BEGIN
  DECLARE tongMonAn DECIMAL(10,2);
  SELECT SUM(ThanhTien) INTO tongMonAn FROM CTHD WHERE ID_HoaDon = OLD.ID_HoaDon;
  UPDATE HoaDon
  SET TienMonAn = IFNULL(tongMonAn, 0),
      TongTien = IFNULL(tongMonAn, 0) - IFNULL(TienGiam, 0)
  WHERE ID_HoaDon = OLD.ID_HoaDon;
END$$
DELIMITER ;

-- Trigger AFTER UPDATE để cập nhật TienMonAn và TongTien của HoaDon
DROP TRIGGER IF EXISTS tg_update_hd_after_update_cthd;
DELIMITER $$
CREATE TRIGGER tg_update_hd_after_update_cthd
AFTER UPDATE ON CTHD
FOR EACH ROW
BEGIN
  DECLARE tongMonAn DECIMAL(10,2);
  SELECT SUM(ThanhTien) INTO tongMonAn FROM CTHD WHERE ID_HoaDon = NEW.ID_HoaDon;
  UPDATE HoaDon
  SET TienMonAn = IFNULL(tongMonAn, 0),
      TongTien = IFNULL(tongMonAn, 0) - IFNULL(TienGiam, 0)
  WHERE ID_HoaDon = NEW.ID_HoaDon;
END$$
DELIMITER ;


--Trigger Doanh so cua Khach hang bang tong tien cua tat ca hoa don co trang thai 'Da thanh toan' 
--cua khach hang do
-- Diem tich luy cua Khach hang duoc tinh bang 0.005% Tong tien cua hoa don (1.000.000d tuong duong 50 diem)
DELIMITER $$

CREATE TRIGGER tg_kh_doanhsovaDTL
AFTER UPDATE ON HoaDon
FOR EACH ROW
BEGIN
    IF NEW.Trangthai = 'Da thanh toan' AND OLD.Trangthai <> 'Da thanh toan' THEN
        UPDATE KhachHang 
        SET 
            Doanhso = Doanhso + NEW.Tongtien,
            Diemtichluy = Diemtichluy + ROUND(NEW.Tongtien * 0.00005)
        WHERE ID_KH = NEW.ID_KH;
    END IF;
END$$

DELIMITER ;


--Trigger khi khach hang them hoa don moi, trang thai ban chuyen tu 'Con trong' sang 'Dang dung bua'
-- Khi trang thai don hang tro thanh 'Da thanh toan' trang thai ban chuyen tu 'Dang dung bua' sang 'Con trong'

DELIMITER $$

CREATE TRIGGER tg_trangthai_ban
AFTER INSERT ON HoaDon
FOR EACH ROW
BEGIN
    IF NEW.Trangthai = 'Chua thanh toan' THEN
        UPDATE Ban SET Trangthai = 'Dang dung bua' WHERE ID_Ban = NEW.ID_Ban;
    END IF;
END$$

CREATE TRIGGER tg_trangthai_ban_update
AFTER UPDATE ON HoaDon
FOR EACH ROW
BEGIN
    IF NEW.Trangthai = 'Da thanh toan' AND OLD.Trangthai <> 'Da thanh toan' THEN
        UPDATE Ban SET Trangthai = 'Con trong' WHERE ID_Ban = NEW.ID_Ban;
    END IF;
END$$

DELIMITER ;


-- Trigger ThanhTien ở CTNK bằng SoLuong x Dongia của nguyên liệu đó
DELIMITER $$
CREATE TRIGGER tg_ctnk_thanhtien_insert
BEFORE INSERT ON CTNK
FOR EACH ROW
BEGIN
    DECLARE gia INT;
    SELECT DonGia INTO gia FROM NguyenLieu WHERE ID_NL = NEW.ID_NL;
    SET NEW.ThanhTien = NEW.SoLuong * gia;
END$$

CREATE TRIGGER tg_ctnk_thanhtien_update
BEFORE UPDATE ON CTNK
FOR EACH ROW
BEGIN
    DECLARE gia INT;
    SELECT DonGia INTO gia FROM NguyenLieu WHERE ID_NL = NEW.ID_NL;
    SET NEW.ThanhTien = NEW.SoLuong * gia;
END$$
DELIMITER ;

-- Trigger Tongtien ở PhieuNK bằng tổng ThanhTien của CTNK
DELIMITER $$
CREATE TRIGGER tg_pnk_tongtien_insert
AFTER INSERT ON CTNK
FOR EACH ROW
BEGIN
    UPDATE PhieuNK
    SET Tongtien = COALESCE((
        SELECT SUM(ThanhTien)
        FROM CTNK
        WHERE ID_NK = NEW.ID_NK
    ), 0)
    WHERE ID_NK = NEW.ID_NK;
END$$

CREATE TRIGGER tg_pnk_tongtien_update
AFTER UPDATE ON CTNK
FOR EACH ROW
BEGIN
    UPDATE PhieuNK
    SET Tongtien = COALESCE((
        SELECT SUM(ThanhTien)
        FROM CTNK
        WHERE ID_NK = NEW.ID_NK
    ), 0)
    WHERE ID_NK = NEW.ID_NK;
END$$

CREATE TRIGGER tg_pnk_tongtien_delete
AFTER DELETE ON CTNK
FOR EACH ROW
BEGIN
    UPDATE PhieuNK
    SET Tongtien = COALESCE((
        SELECT SUM(ThanhTien)
        FROM CTNK
        WHERE ID_NK = OLD.ID_NK
    ), 0)
    WHERE ID_NK = OLD.ID_NK;
END$$
DELIMITER ;

-- Trigger khi thêm CTNK tăng Số lượng tồn của nguyên liệu trong kho
DELIMITER $$
CREATE TRIGGER tg_kho_themSLTon_insert
AFTER INSERT ON CTNK
FOR EACH ROW
BEGIN
    UPDATE Kho
    SET SLTon = COALESCE(SLTon, 0) + NEW.SoLuong
    WHERE ID_NL = NEW.ID_NL;
END$$

CREATE TRIGGER tg_kho_themSLTon_delete
AFTER DELETE ON CTNK
FOR EACH ROW
BEGIN
    UPDATE Kho
    SET SLTon = COALESCE(SLTon, 0) - OLD.SoLuong
    WHERE ID_NL = OLD.ID_NL;
END$$

CREATE TRIGGER tg_kho_themSLTon_update
AFTER UPDATE ON CTNK
FOR EACH ROW
BEGIN
    UPDATE Kho
    SET SLTon = COALESCE(SLTon, 0) + NEW.SoLuong - OLD.SoLuong
    WHERE ID_NL = NEW.ID_NL;
END$$
DELIMITER ;

-- Trigger khi thêm CTXK giảm Số lượng tồn của nguyên liệu trong kho
DELIMITER $$
CREATE TRIGGER tg_kho_giamSLTon_insert
AFTER INSERT ON CTXK
FOR EACH ROW
BEGIN
    DECLARE current_slt INT;
    SELECT COALESCE(SLTon, 0) INTO current_slt FROM Kho WHERE ID_NL = NEW.ID_NL;
    
    IF current_slt < NEW.SoLuong THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Số lượng tồn kho không đủ để xuất';
    ELSE
        UPDATE Kho
        SET SLTon = current_slt - NEW.SoLuong
        WHERE ID_NL = NEW.ID_NL;
    END IF;
END$$

CREATE TRIGGER tg_kho_giamSLTon_delete
AFTER DELETE ON CTXK
FOR EACH ROW
BEGIN
    UPDATE Kho
    SET SLTon = COALESCE(SLTon, 0) + OLD.SoLuong
    WHERE ID_NL = OLD.ID_NL;
END$$

CREATE TRIGGER tg_kho_giamSLTon_update
AFTER UPDATE ON CTXK
FOR EACH ROW
BEGIN
    DECLARE current_slt INT;
    DECLARE delta_slt INT;
    SET delta_slt = NEW.SoLuong - OLD.SoLuong;
    SELECT COALESCE(SLTon, 0) INTO current_slt FROM Kho WHERE ID_NL = NEW.ID_NL;
    
    IF current_slt < delta_slt THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Số lượng tồn kho không đủ để cập nhật xuất kho';
    ELSE
        UPDATE Kho
        SET SLTon = current_slt - delta_slt
        WHERE ID_NL = NEW.ID_NL;
    END IF;
END$$
DELIMITER ;

-- Trigger khi thêm một Nguyên Liệu mới, thêm NL đó vào Kho
DELIMITER $$
CREATE TRIGGER tg_kho_themNL
AFTER INSERT ON NguyenLieu
FOR EACH ROW
BEGIN
    INSERT INTO Kho (ID_NL, SLTon)
    VALUES (NEW.ID_NL, 0);
END$$
DELIMITER ;

--Procedure
--Procudure them mot khach hang moi voi cac thong tin tenKH , NgayTG va ID_ND
DELIMITER $$

CREATE PROCEDURE KH_ThemKH (
    IN tenKH VARCHAR(100),
    IN NgayTG DATE,
    IN ID_ND INT
)
BEGIN
    DECLARE v_ID_KH INT;

    -- Tìm ID_KH nhỏ nhất chưa được sử dụng
    SELECT MIN(t1.ID_KH) + 1
    INTO v_ID_KH
    FROM KhachHang t1
    WHERE NOT EXISTS (
        SELECT 1 FROM KhachHang t2 WHERE t2.ID_KH = t1.ID_KH + 1
    );

    -- Nếu không tìm được thì dùng MAX + 1
    IF v_ID_KH IS NULL THEN
        SELECT COALESCE(MAX(ID_KH), 0) + 1 INTO v_ID_KH FROM KhachHang;
    END IF;

    -- Thêm khách hàng
    INSERT INTO KhachHang(ID_KH, TenKH, Ngaythamgia, ID_ND)
    VALUES (v_ID_KH, tenKH, NgayTG, ID_ND);
END$$

DELIMITER ;


--Procudure them mot nhan vien moi voi cac thong tin tenNV, NgayVL, SDT, Chucvu, ID_NQL, Tinhtrang
DELIMITER $$

CREATE PROCEDURE NV_ThemNV (
    IN tenNV VARCHAR(100),
    IN NgayVL DATE,
    IN SDT VARCHAR(20),
    IN Chucvu VARCHAR(50),
    IN ID_NQL INT,
    IN Tinhtrang VARCHAR(50)
)
BEGIN
    DECLARE v_ID_NV INT;

    -- Tìm ID_NV nhỏ nhất chưa được dùng
    SELECT MIN(t1.ID_NV) + 1
    INTO v_ID_NV
    FROM NhanVien t1
    WHERE NOT EXISTS (
        SELECT 1 FROM NhanVien t2 WHERE t2.ID_NV = t1.ID_NV + 1
    );

    -- Nếu không tìm được khoảng trống thì dùng MAX + 1
    IF v_ID_NV IS NULL THEN
        SELECT COALESCE(MAX(ID_NV), 0) + 1 INTO v_ID_NV FROM NhanVien;
    END IF;

    -- Thêm nhân viên
    INSERT INTO NhanVien(ID_NV, TenNV, NgayVL, SDT, Chucvu, ID_NQL, Tinhtrang)
    VALUES (v_ID_NV, tenNV, NgayVL, SDT, Chucvu, ID_NQL, Tinhtrang);
END$$

DELIMITER ;


-- Procudure xoa mot NHANVIEN voi idNV
DELIMITER $$

CREATE PROCEDURE NV_XoaNV(IN idNV INT)
BEGIN
    DECLARE v_count INT DEFAULT 0;
    DECLARE v_idNQL INT DEFAULT NULL;

    -- Kiểm tra nhân viên có tồn tại không, đồng thời lấy ID_NQL
    SELECT COUNT(*), ID_NQL
    INTO v_count, v_idNQL
    FROM NhanVien
    WHERE ID_NV = idNV;

    -- Nếu không tồn tại
    IF v_count = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Nhan vien khong ton tai';
    ELSE
        -- Nếu là quản lý chính mình thì không cho xóa
        IF idNV = v_idNQL THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Khong the xoa QUAN LY';
        ELSE
            -- Xóa CTNK liên quan
            DELETE FROM CTNK WHERE ID_NK IN (
                SELECT ID_NK FROM PhieuNK WHERE ID_NV = idNV
            );

            -- Xóa CTXK liên quan
            DELETE FROM CTXK WHERE ID_XK IN (
                SELECT ID_XK FROM PhieuXK WHERE ID_NV = idNV
            );

            -- Xóa PhieuNK và PhieuXK
            DELETE FROM PhieuNK WHERE ID_NV = idNV;
            DELETE FROM PhieuXK WHERE ID_NV = idNV;

            -- Xóa nhân viên
            DELETE FROM NhanVien WHERE ID_NV = idNV;
        END IF;
    END IF;
END$$

DELIMITER ;


-- Procudure xoa mot KHACHHANG voi idKH
DELIMITER $$

CREATE PROCEDURE KH_XoaKH(IN idKH INT)
BEGIN
    DECLARE v_count INT DEFAULT 0;

    -- Kiểm tra khách hàng có tồn tại
    SELECT COUNT(*) INTO v_count
    FROM KhachHang
    WHERE ID_KH = idKH;

    -- Nếu không tồn tại thì báo lỗi
    IF v_count = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Khach hang khong ton tai';
    ELSE
        -- Xóa chi tiết hóa đơn liên quan đến các hóa đơn của khách hàng
        DELETE FROM CTHD
        WHERE ID_HoaDon IN (
            SELECT ID_HoaDon FROM HoaDon WHERE ID_KH = idKH
        );

        -- Xóa hóa đơn của khách hàng
        DELETE FROM HoaDon WHERE ID_KH = idKH;

        -- Xóa khách hàng
        DELETE FROM KhachHang WHERE ID_KH = idKH;
    END IF;
END$$

DELIMITER ;


-- Procedure xem thong tin KHACHHANG voi thong tin idKH
DELIMITER $$

CREATE PROCEDURE KH_XemTT(IN idKH INT)
BEGIN
    DECLARE v_TenKH VARCHAR(100);
    DECLARE v_NgayTG DATE;
    DECLARE v_DoanhSo INT;
    DECLARE v_DiemTL INT;
    DECLARE v_ID_ND INT;
    DECLARE v_count INT DEFAULT 0;

    -- Kiểm tra khách hàng có tồn tại
    SELECT COUNT(*) INTO v_count
    FROM KhachHang
    WHERE ID_KH = idKH;

    IF v_count = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Khach hang khong ton tai';
    ELSE
        -- Lấy thông tin khách hàng
        SELECT TenKH, Ngaythamgia, Doanhso, Diemtichluy, ID_ND
        INTO v_TenKH, v_NgayTG, v_DoanhSo, v_DiemTL, v_ID_ND
        FROM KhachHang
        WHERE ID_KH = idKH;

        -- Xuất thông tin (dùng SELECT thay vì DBMS_OUTPUT)
        SELECT 
            idKH AS `Ma khach hang`,
            v_TenKH AS `Ten khach hang`,
            DATE_FORMAT(v_NgayTG, '%d-%m-%Y') AS `Ngay tham gia`,
            v_DoanhSo AS `Doanh so`,
            v_DiemTL AS `Diem tich luy`,
            v_ID_ND AS `Ma nguoi dung`;
    END IF;
END$$

DELIMITER ;


-- Procedure xem thong tin NHANVIEN voi thong tin idNV
DELIMITER $$

CREATE PROCEDURE NV_XemTT(IN idNV INT)
BEGIN
    DECLARE v_TenNV VARCHAR(100);
    DECLARE v_NgayVL DATE;
    DECLARE v_SDT VARCHAR(20);
    DECLARE v_Chucvu VARCHAR(50);
    DECLARE v_ID_NQL INT;
    DECLARE v_count INT DEFAULT 0;

    -- Kiểm tra nhân viên có tồn tại
    SELECT COUNT(*) INTO v_count
    FROM NhanVien
    WHERE ID_NV = idNV;

    IF v_count = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Nhan vien khong ton tai';
    ELSE
        -- Lấy thông tin nhân viên
        SELECT TenNV, NgayVL, SDT, Chucvu, ID_NQL
        INTO v_TenNV, v_NgayVL, v_SDT, v_Chucvu, v_ID_NQL
        FROM NhanVien
        WHERE ID_NV = idNV;

        -- Trả kết quả
        SELECT 
            idNV AS `Ma nhan vien`,
            v_TenNV AS `Ten nhan vien`,
            DATE_FORMAT(v_NgayVL, '%d-%m-%Y') AS `Ngay vao lam`,
            v_SDT AS `So dien thoai`,
            v_Chucvu AS `Chuc vu`,
            v_ID_NQL AS `Ma nguoi quan ly`;
    END IF;
END$$

DELIMITER ;


-- Procedure liet ke danh sach hoa don tu ngay A den ngay B
DELIMITER $$

CREATE PROCEDURE DS_HoaDon_tuAdenB(IN fromA DATE, IN toB DATE)
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_ID_HoaDon INT;
    DECLARE v_ID_KH INT;
    DECLARE v_ID_Ban INT;
    DECLARE v_NgayHD DATE;
    DECLARE v_TienMonAn INT;
    DECLARE v_TienGiam INT;
    DECLARE v_TongTien INT;
    DECLARE v_TrangThai VARCHAR(50);

    DECLARE cur CURSOR FOR
        SELECT ID_HoaDon, ID_KH, ID_Ban, NgayHD, TienMonAn, TienGiam, TongTien, TrangThai
        FROM HoaDon
        WHERE NgayHD BETWEEN fromA AND toB;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cur;

    read_loop: LOOP
        FETCH cur INTO v_ID_HoaDon, v_ID_KH, v_ID_Ban, v_NgayHD, v_TienMonAn, v_TienGiam, v_TongTien, v_TrangThai;
        IF done THEN
            LEAVE read_loop;
        END IF;

        SELECT 
            v_ID_HoaDon AS `Ma hoa don`,
            v_ID_KH AS `Ma khach hang`,
            v_ID_Ban AS `Ma ban`,
            DATE_FORMAT(v_NgayHD, '%d-%m-%Y') AS `Ngay hoa don`,
            v_TienMonAn AS `Tien mon an`,
            v_TienGiam AS `Tien giam`,
            v_TongTien AS `Tong tien`,
            v_TrangThai AS `Trang thai`;
    END LOOP;

    CLOSE cur;
END$$

DELIMITER ;


-- Procedure liet ke danh sach phieu nhap kho tu ngay A den ngay B
DELIMITER $$

CREATE PROCEDURE DS_PhieuNK_tuAdenB(IN fromA DATE, IN toB DATE)
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_ID_NK INT;
    DECLARE v_ID_NV INT;
    DECLARE v_NgayNK DATE;
    DECLARE v_TongTien INT;

    DECLARE cur CURSOR FOR
        SELECT ID_NK, ID_NV, NgayNK, Tongtien
        FROM PhieuNK
        WHERE NgayNK BETWEEN fromA AND toB;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cur;

    read_loop: LOOP
        FETCH cur INTO v_ID_NK, v_ID_NV, v_NgayNK, v_TongTien;
        IF done THEN
            LEAVE read_loop;
        END IF;

        SELECT 
            v_ID_NK AS `Ma nhap kho`,
            v_ID_NV AS `Ma nhan vien`,
            DATE_FORMAT(v_NgayNK, '%d-%m-%Y') AS `Ngay nhap kho`,
            v_TongTien AS `Tong tien`;
    END LOOP;

    CLOSE cur;
END$$

DELIMITER ;

-- Procedure liet ke danh sach phieu xuat kho tu ngay A den ngay B
DELIMITER $$

CREATE PROCEDURE DS_PhieuXK_tuAdenB(IN fromA DATE, IN toB DATE)
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_ID_XK INT;
    DECLARE v_ID_NV INT;
    DECLARE v_NgayXK DATE;

    DECLARE cur CURSOR FOR
        SELECT ID_XK, ID_NV, NgayXK
        FROM PhieuXK
        WHERE NgayXK BETWEEN fromA AND toB;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cur;

    read_loop: LOOP
        FETCH cur INTO v_ID_XK, v_ID_NV, v_NgayXK;
        IF done THEN
            LEAVE read_loop;
        END IF;

        SELECT 
            v_ID_XK AS `Ma xuat kho`,
            v_ID_NV AS `Ma nhan vien`,
            DATE_FORMAT(v_NgayXK, '%d-%m-%Y') AS `Ngay xuat kho`;
    END LOOP;

    CLOSE cur;
END$$

DELIMITER ;


-- Procedure xem chi tiet hoa don cua 1 hoa don
DELIMITER $$

CREATE PROCEDURE HD_XemCTHD(IN idHD INT)
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_ID_MONAN INT;
    DECLARE v_SOLUONG INT;
    DECLARE v_THANHTIEN DECIMAL(10,2);

    DECLARE cur CURSOR FOR
        SELECT ID_MONAN, SOLUONG, THANHTIEN
        FROM CTHD
        WHERE ID_HOADON = idHD;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cur;

    read_loop: LOOP
        FETCH cur INTO v_ID_MONAN, v_SOLUONG, v_THANHTIEN;
        IF done THEN
            LEAVE read_loop;
        END IF;

        SELECT 
            v_ID_MONAN AS `Ma mon an`,
            v_SOLUONG AS `So luong`,
            v_THANHTIEN AS `Thanh tien`;
    END LOOP;

    CLOSE cur;
END$$

DELIMITER ;

-- Procedure voucher
-- Stored procedure Voucher_GiamSL
DELIMITER $$

CREATE PROCEDURE Voucher_GiamSL(IN code VARCHAR(50))
BEGIN
    DECLARE v_count INT DEFAULT 0;

    SELECT COUNT(*)
    INTO v_count
    FROM Voucher
    WHERE Code_Voucher = code;

    IF v_count > 0 THEN
        UPDATE Voucher
        SET SoLuong = SoLuong - 1
        WHERE Code_Voucher = code;
    ELSE
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Voucher không tồn tại';
    END IF;
END$$

DELIMITER ;

-- Stored procedure KH_TruDTL
DELIMITER $$

CREATE PROCEDURE KH_TruDTL(IN ID INT, IN diemdoi INT)
BEGIN
    DECLARE v_count INT DEFAULT 0;

    SELECT COUNT(*) INTO v_count
    FROM KhachHang
    WHERE ID_KH = ID;

    IF v_count > 0 THEN
        UPDATE KhachHang
        SET Diemtichluy = Diemtichluy - diemdoi
        WHERE ID_KH = ID;
    ELSE
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Khách hàng không tồn tại';
    END IF;
END$$

DELIMITER ;

-- Stored procedure ExchangeVoucher
DELIMITER $$

CREATE PROCEDURE ExchangeVoucher(
    IN p_idHoaDon INT,
    IN p_codeVoucher VARCHAR(10)
)
BEGIN
    DECLARE v_diemdoi INT DEFAULT 0;
    DECLARE v_phantram INT DEFAULT 0;
    DECLARE v_loaiMA VARCHAR(100);
    DECLARE v_tongtien_apdung INT DEFAULT 0;
    DECLARE v_idKH INT;
    DECLARE v_count_voucher INT DEFAULT 0;
    DECLARE v_count_hoadon INT DEFAULT 0;

    START TRANSACTION;

    -- Kiểm tra voucher có tồn tại
    SELECT COUNT(*)
    INTO v_count_voucher
    FROM Voucher
    WHERE Code_Voucher = p_codeVoucher;

    IF v_count_voucher = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Voucher không tồn tại';
    END IF;

    -- Kiểm tra hóa đơn có tồn tại
    SELECT COUNT(*)
    INTO v_count_hoadon
    FROM HoaDon
    WHERE ID_HoaDon = p_idHoaDon;

    IF v_count_hoadon = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Hóa đơn không tồn tại';
    END IF;

    -- Lấy ID_KH từ HoaDon
    SELECT ID_KH
    INTO v_idKH
    FROM HoaDon
    WHERE ID_HoaDon = p_idHoaDon;

    -- Kiểm tra số lượng voucher
    IF (SELECT SoLuong FROM Voucher WHERE Code_Voucher = p_codeVoucher) <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Voucher đã hết số lượng';
    END IF;

    -- Lấy thông tin voucher
    SELECT Diem, Phantram, LoaiMA
    INTO v_diemdoi, v_phantram, v_loaiMA
    FROM Voucher
    WHERE Code_Voucher = p_codeVoucher;

    -- Kiểm tra điểm tích lũy của khách hàng
    IF (SELECT Diemtichluy FROM KhachHang WHERE ID_KH = v_idKH) < v_diemdoi THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Không đủ điểm tích lũy để đổi voucher';
    END IF;

    -- Tính tổng tiền áp dụng
    IF v_loaiMA = 'All' THEN
        SELECT TienMonAn INTO v_tongtien_apdung
        FROM HoaDon
        WHERE ID_HoaDon = p_idHoaDon;
    ELSE
        SELECT COALESCE(SUM(Thanhtien), 0)
        INTO v_tongtien_apdung
        FROM CTHD
        JOIN MonAn ON MonAn.ID_MonAn = CTHD.ID_MonAn
        WHERE ID_HoaDon = p_idHoaDon AND Loai = v_loaiMA;
    END IF;

    -- Cập nhật HoaDon
    UPDATE HoaDon
    SET Code_Voucher = p_codeVoucher,
        TienGiam = ROUND(v_tongtien_apdung * v_phantram / 100),
        Tongtien = TienMonAn - ROUND(v_tongtien_apdung * v_phantram / 100)
    WHERE ID_HoaDon = p_idHoaDon;

    -- Gọi procedure để giảm số lượng voucher
    CALL Voucher_GiamSL(p_codeVoucher);

    -- Gọi procedure để trừ điểm tích lũy
    CALL KH_TruDTL(v_idKH, v_diemdoi);

    COMMIT;
END$$

DELIMITER ;

--Fuction 
--Fuction Tinh doanh thu hoa don theo ngay
DELIMITER $$

CREATE FUNCTION DoanhThuHD_theoNgay(ngHD DATE)
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE v_Doanhthu INT DEFAULT 0;

    SELECT IFNULL(SUM(Tongtien), 0)
    INTO v_Doanhthu
    FROM HoaDon
    WHERE NgayHD = ngHD;

    RETURN v_Doanhthu;
END$$

DELIMITER ;


--Fuction Tinh chi phi nhap kho theo ngay
DELIMITER $$

CREATE FUNCTION ChiPhiNK_theoNgay(ngNK DATE)
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE v_Chiphi INT DEFAULT 0;

    SELECT IFNULL(SUM(Tongtien), 0)
    INTO v_Chiphi
    FROM PhieuNK
    WHERE NgayNK = ngNK;

    RETURN v_Chiphi;
END$$

DELIMITER ;


--Fuction Tinh doanh so trung binh cua x KHACHHANG co doanh so cao nhat
DELIMITER $$

CREATE FUNCTION DoanhsoTB_TOPxKH(x INT)
RETURNS DECIMAL(10,2)
DETERMINISTIC
BEGIN
    DECLARE v_avg DECIMAL(10,2) DEFAULT 0;

    SELECT AVG(Doanhso)
    INTO v_avg
    FROM (
        SELECT Doanhso
        FROM KhachHang
        ORDER BY Doanhso DESC
        LIMIT x
    ) AS top_kh;

    RETURN v_avg;
END$$

DELIMITER ;


--Fuction Tinh so luong KHACHANG moi trong thang chi dinh cua nam co it nhat mot hoa don co tri gia tren x vnd
DELIMITER $$

CREATE FUNCTION SL_KH_Moi(thang INT, nam INT, trigiaHD DECIMAL(10,2))
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE v_count INT DEFAULT 0;

    SELECT COUNT(*)
    INTO v_count
    FROM KhachHang KH
    WHERE MONTH(KH.Ngaythamgia) = thang 
      AND YEAR(KH.Ngaythamgia) = nam
      AND EXISTS (
          SELECT 1
          FROM HoaDon HD
          WHERE HD.ID_KH = KH.ID_KH
            AND HD.TongTien > trigiaHD
      );

    RETURN v_count;
END$$

DELIMITER ;


--Fuction Tinh tien mon an duoc giam khi them mot CTHD moi
DELIMITER $$

CREATE FUNCTION CTHD_Tinhtiengiam(Tongtien DECIMAL(10,2), Code VARCHAR(20))
RETURNS DECIMAL(10,2)
DETERMINISTIC
BEGIN
    DECLARE Tiengiam DECIMAL(10,2) DEFAULT 0;
    DECLARE v_phantram INT;

    SELECT Phantram
    INTO v_phantram
    FROM Voucher
    WHERE Code_Voucher = Code
    LIMIT 1;

    SET Tiengiam = ROUND(Tongtien * v_phantram / 100);

    RETURN Tiengiam;
END$$

DELIMITER ;






-- them data
-- Thêm dữ liệu vào bảng NguoiDung (đã bỏ trường Trangthai)
-- Nhân viên
INSERT INTO NguoiDung(ID_ND, Email, MatKhau, Vaitro)
VALUES 
(100, 'Bac@gmail.com', '123', 'Quan Ly'),
(101, 'Le@gmail.com', '123', 'Nhan Vien'),
(102, 'Dung@gmail.com', '123', 'Nhan Vien Kho'),

-- Khách hàng
(104, 'KHThaoDuong@gmail.com', '123', 'Khach Hang'),
(105, 'KHTanHieu@gmail.com', '123', 'Khach Hang'),
(106, 'KHQuocThinh@gmail.com', '123', 'Khach Hang'),
(107, 'KHNhuMai@gmail.com', '123', 'Khach Hang'),
(108, 'KHBichHao@gmail.com', '123', 'Khach Hang'),
(109, 'KHMaiQuynh@gmail.com', '123', 'Khach Hang'),
(110, 'KHMinhQuang@gmail.com', '123', 'Khach Hang'),
(111, 'KHThanhHang@gmail.com', '123', 'Khach Hang'),
(112, 'KHThanhNhan@gmail.com', '123', 'Khach Hang'),
(113, 'KHPhucNguyen@gmail.com', '123', 'Khach Hang');


-- nhan vien
-- Có tài khoản
INSERT INTO NhanVien(ID_NV, TenNV, NgayVL, SDT, Chucvu, ID_ND, ID_NQL, Tinhtrang) 
VALUES 
(100, 'Nguyen Xuan Bac', '2023-05-10', '0848044725', 'Quan ly', 100, 100, 'Dang lam viec'),
(101, 'Leu Thi Le', '2023-05-20', '0838033334', 'Tiep tan', 101, 100, 'Dang lam viec'),
(102, 'Nguyen Anh Dung', '2023-05-19', '0838033234', 'Kho', 102, 100, 'Dang lam viec');

-- Không có tài khoản
INSERT INTO NhanVien(ID_NV, TenNV, NgayVL, SDT, Chucvu, ID_NQL, Tinhtrang) 
VALUES 
(104, 'Ha Thao Duong', '2025-05-10', '0838033232', 'Phuc vu', 100, 'Dang lam viec'),
(105, 'Nguyen Quoc Thinh', '2025-05-11', '0838033734', 'Phuc vu', 100, 'Dang lam viec'),
(106, 'Truong Tan Hieu', '2025-05-12', '0838033834', 'Phuc vu', 100, 'Dang lam viec'),
(107, 'Nguyen Thai Bao', '2025-05-10', '0838093234', 'Phuc vu', 100, 'Dang lam viec'),
(108, 'Tran Nhat Khang', '2025-05-11', '0838133234', 'Thu ngan', 100, 'Dang lam viec'),
(109, 'Nguyen Ngoc Luong', '2025-05-12', '0834033234', 'Bep', 100, 'Dang lam viec');

-- khach hang
INSERT INTO KhachHang(ID_KH, TenKH, Ngaythamgia, ID_ND, SDT) 
VALUES 
(100, 'Ha Thao Duong', '2025-05-10', 104, '0123456789'),
(101, 'Truong Tan Hieu', '2025-05-10', 105, '0123456456'),
(102, 'Nguyen Quoc Thinh', '2025-05-10', 106, '0123456123'),
(103, 'Tran Nhu Mai', '2025-05-10', 107, '0123456987'),
(104, 'Nguyen Thi Bich Hao', '2025-05-10', 108, '0123456654'),
(105, 'Nguyen Mai Quynh', '2025-05-11', 109, '0123456321'),
(106, 'Hoang Minh Quang', '2025-05-11', 110, '0129876543'),
(107, 'Nguyen Thanh Hang', '2025-05-12', 111, '0987654321'),
(108, 'Nguyen Ngoc Thanh Nhan', '2025-05-11', 112, '0912345678'),
(109, 'Hoang Thi Phuc Nguyen', '2025-05-12', 113, '0987123456');

-- Them data cho bang MonAn
-- MonKhaiVi
INSERT INTO MonAn(ID_MonAn, TenMon, Dongia, Loai, TrangThai, HinhAnh) VALUES
(1, 'DUI CUU NUONG XE NHO', 250000, 'MonKhaiVi', 'Dang kinh doanh', '25.jpg'),
(2, 'BE SUON CUU NUONG ', 230000, 'MonKhaiVi', 'Dang kinh doanh', '26.jpg'),
(3, 'DUI CUU NUONG TRUNG DONG', 350000, 'MonKhaiVi', 'Dang kinh doanh', '27.jpg'),
(4, 'CUU XOC LA CA RI', 129000, 'MonKhaiVi', 'Dang kinh doanh', '28.jpg'),
(5, 'CUU KUNGBAO', 250000, 'MonKhaiVi', 'Dang kinh doanh', '29.jpg'),
(6, 'BAP CUU NUONG CAY', 250000, 'MonKhaiVi', 'Dang kinh doanh', '30.jpg');


-- MonChinh
INSERT INTO MonAn(ID_MonAn, TenMon, Dongia, Loai, TrangThai, HinhAnh) VALUES
(7, 'Cua KingCrab Duc sot', 179000, 'MonChinh', 'Dang kinh doanh', '14.jpg'),
(8, 'Mai Cua Topping Pho Mai', 169000, 'MonChinh', 'Dang kinh doanh', '15.jpg'),
(9, 'Cua sot Tu Xuyen', 179000, 'MonChinh', 'Dang kinh doanh', '16.jpg'),
(10, 'Cua Nuong Tu Nhien', 169000, 'MonChinh', 'Dang kinh doanh', '17.jpg'),
(11, 'Cua Nuong Bo Toi', 1180000, 'MonChinh', 'Dang kinh doanh', '18.jpg'),
(12, 'Com Mai Cua Chien', 1290000, 'MonChinh', 'Dang kinh doanh', '19.jpg'),
(13, 'Thit de xong hoi', 550000, 'MonChinh', 'Dang kinh doanh', '20.jpg'),
(14, 'Thit de xao rau ngo', 2390000, 'MonChinh', 'Dang kinh doanh', '21.jpg'),
(15, 'Thit de nuong tang', 180000, 'MonChinh', 'Dang kinh doanh', '22.jpg'),
(16, 'Thit de chao', 185000, 'MonChinh', 'Dang kinh doanh', '23.jpg'),
(17, 'Thit de nuong xien', 200000, 'MonChinh', 'Dang kinh doanh', '24.jpg');

-- MonCanh
INSERT INTO MonAn(ID_MonAn, TenMon, Dongia, Loai, TrangThai, HinhAnh) VALUES
(18, 'Dui de tan thuoc bac', 180000, 'MonCanh', 'Dang kinh doanh', '2.jpg'),
(19, 'Canh de ham duong quy', 190000, 'MonCanh', 'Dang kinh doanh', '3.jpg'),
(20, 'Chao de dau xanh', 230000, 'MonCanh', 'Dang kinh doanh', '4.jpg'),
(21, 'Thit de nhung me', 180000, 'MonCanh', 'Dang kinh doanh', '5.jpg'),
(22, 'Lau de nhu', 190000, 'MonCanh', 'Dang kinh doanh', '6.jpg'),
(23, 'Canh trung', 200000, 'MonCanh', 'Dang kinh doanh', '1.jpg'),
(24, 'Canh nam', 150000, 'MonCanh', 'Dang kinh doanh', '7.jpg');

-- DoUong
INSERT INTO MonAn(ID_MonAn, TenMon, Dongia, Loai, TrangThai, HinhAnh) VALUES
(25, 'SIGNATURE WINE', 3290000, 'DoUong', 'Dang kinh doanh', '8.jpg'),
(26, 'CHILEAN WINE', 3990000, 'DoUong', 'Dang kinh doanh', '9.jpg'),
(27, 'ARGENTINA WINE', 2890000, 'DoUong', 'Dang kinh doanh', '10.jpg'),
(28, 'ITALIAN WINE', 5590000, 'DoUong', 'Dang kinh doanh', '11.jpg'),
(29, 'AMERICAN WINE', 4990000, 'DoUong', 'Dang kinh doanh', '12.jpg'),
(30, 'CLASSIC COCKTAIL', 200000, 'DoUong', 'Dang kinh doanh', '13.jpg');

-- MonTrangMieng
INSERT INTO MonAn(ID_MonAn, TenMon, Dongia, Loai, TrangThai, HinhAnh) VALUES
(31, 'Pavlova', 650000, 'MonTrangMieng', 'Dang kinh doanh', '31.jpg'),
(32, 'Kesutera', 350000, 'MonTrangMieng', 'Dang kinh doanh', '32.jpg'),
(33, 'Cremeschnitte', 250000, 'MonTrangMieng', 'Dang kinh doanh', '33.jpg'),
(34, 'Sachertorte', 650000, 'MonTrangMieng', 'Dang kinh doanh', '34.jpg'),
(35, 'Schwarzwalder Kirschtorte', 350000, 'MonTrangMieng', 'Dang kinh doanh', '35.jpg');


-- Them data cho bang Ban
-- Tang 1
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(100,'Ban T1.1','Tang 1','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(101,'Ban T1.2','Tang 1','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(102,'Ban T1.3','Tang 1','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(103,'Ban T1.4','Tang 1','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(104,'Ban T1.5','Tang 1','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(105,'Ban T1.6','Tang 1','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(106,'Ban T1.7','Tang 1','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(107,'Ban T1.8','Tang 1','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(108,'Ban T1.9','Tang 1','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(109,'Ban T1.10','Tang 1','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(110,'Ban T1.11','Tang 1','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(111,'Ban T1.12','Tang 1','Con trong');
-- Tang 2
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(112,'Ban T2.1','Tang 2','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(113,'Ban T2.2','Tang 2','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(114,'Ban T2.3','Tang 2','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(115,'Ban T2.4','Tang 2','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(116,'Ban T2.5','Tang 2','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(117,'Ban T2.6','Tang 2','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(118,'Ban T2.7','Tang 2','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(119,'Ban T2.8','Tang 2','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(120,'Ban T2.9','Tang 2','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(121,'Ban T2.10','Tang 2','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(122,'Ban T2.11','Tang 2','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(123,'Ban T2.12','Tang 2','Con trong');
-- Tang 3
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(124,'Ban T3.1','Tang 3','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(125,'Ban T3.1','Tang 3','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(126,'Ban T3.1','Tang 3','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(127,'Ban T3.1','Tang 3','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(128,'Ban T3.1','Tang 3','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(129,'Ban T3.1','Tang 3','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(130,'Ban T3.1','Tang 3','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(131,'Ban T3.1','Tang 3','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(132,'Ban T3.1','Tang 3','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(133,'Ban T3.1','Tang 3','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(134,'Ban T3.1','Tang 3','Con trong');
insert into Ban(ID_Ban,TenBan,Vitri,Trangthai) values(135,'Ban T3.1','Tang 3','Con trong');

-- Them data cho bang Voucher
INSERT INTO Voucher(Code_Voucher, Mota, Phantram, LoaiMA, SoLuong, Diem) VALUES
('loQy', '20% off for All Menu', 20, 'All', 10, 200),
('pCfI', '30% off for All Menu', 30, 'All', 5, 300),
('ugQx', '100% off for All Menu', 100, 'All', 3, 500),
('bjff', '50% off for All Menu', 50, 'All', 5, 600),
('7hVO', '60% off for All Menu', 60, 'All', 0, 1000),
('GTsC', '40% off for All Menu', 20, 'All', 0, 400);

-- Them data cho bang Hoadon
INSERT INTO HoaDon(ID_HoaDon, ID_KH, ID_Ban, NgayHD, Trangthai) VALUES
(101, 100, 100, STR_TO_DATE('10-1-2025','%d-%m-%Y'), 'Chua thanh toan'),
(102, 104, 102, STR_TO_DATE('15-1-2025','%d-%m-%Y'), 'Chua thanh toan'),
(103, 105, 103, STR_TO_DATE('20-1-2025','%d-%m-%Y'), 'Chua thanh toan'),
(104, 101, 101, STR_TO_DATE('13-2-2025','%d-%m-%Y'), 'Chua thanh toan'),
(105, 103, 120, STR_TO_DATE('12-2-2025','%d-%m-%Y'), 'Chua thanh toan'),
(106, 104, 100, STR_TO_DATE('16-3-2025','%d-%m-%Y'), 'Chua thanh toan'),
(107, 107, 103, STR_TO_DATE('20-3-2025','%d-%m-%Y'), 'Chua thanh toan'),
(108, 108, 101, STR_TO_DATE('10-4-2025','%d-%m-%Y'), 'Chua thanh toan'),
(109, 100, 100, STR_TO_DATE('20-4-2025','%d-%m-%Y'), 'Chua thanh toan'),
(110, 103, 101, STR_TO_DATE('5-5-2025','%d-%m-%Y'), 'Chua thanh toan'),
(111, 106, 102, STR_TO_DATE('10-5-2025','%d-%m-%Y'), 'Chua thanh toan'),
(112, 108, 103, STR_TO_DATE('15-5-2025','%d-%m-%Y'), 'Chua thanh toan'),
(113, 106, 102, STR_TO_DATE('20-5-2025','%d-%m-%Y'), 'Chua thanh toan'),
(114, 108, 103, STR_TO_DATE('5-6-2025','%d-%m-%Y'), 'Chua thanh toan'),
(115, 109, 104, STR_TO_DATE('7-6-2025','%d-%m-%Y'), 'Chua thanh toan'),
(116, 100, 105, STR_TO_DATE('7-6-2025','%d-%m-%Y'), 'Chua thanh toan'),
(117, 106, 106, STR_TO_DATE('10-6-2025','%d-%m-%Y'), 'Chua thanh toan'),
(118, 102, 106, STR_TO_DATE('10-2-2025','%d-%m-%Y'), 'Chua thanh toan'),
(119, 103, 106, STR_TO_DATE('12-2-2025','%d-%m-%Y'), 'Chua thanh toan'),
(120, 104, 106, STR_TO_DATE('10-4-2025','%d-%m-%Y'), 'Chua thanh toan'),
(121, 105, 106, STR_TO_DATE('12-4-2025','%d-%m-%Y'), 'Chua thanh toan'),
(122, 107, 106, STR_TO_DATE('12-5-2025','%d-%m-%Y'), 'Chua thanh toan');

-- Them data cho bang CTHD
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (101, 1, 2);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (101, 3, 1);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (101, 10, 3);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (102, 1, 2);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (102, 2, 1);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (102, 4, 2);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (103, 12, 2);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (104, 30, 3);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (104, 35, 4);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (105, 28, 1);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (105, 18, 2);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (106, 10, 3);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (106, 25, 2);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (106, 31, 4);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (107, 32, 2);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (107, 12, 5);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (108, 12, 1);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (108, 20, 4);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (109, 15, 4);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (110, 34, 2);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (110, 23, 4);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (111, 35, 2);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (111, 17, 4);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (112, 12, 3);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (112, 20, 2);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (112, 31, 5);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (113, 10, 2);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (114, 30, 2);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (114, 32, 3);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (115, 30, 2);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (116, 27, 2);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (116, 34, 1);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (117, 17, 2);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (117, 26, 3);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (118, 34, 10);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (118, 35, 5);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (119, 23, 2);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (119, 28, 2);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (120, 31, 5);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (120, 32, 4);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (121, 13, 5);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (121, 31, 4);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (122, 33, 5);
INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES (122, 34, 6);
UPDATE HoaDon SET TrangThai = 'Da thanh toan' WHERE ID_HoaDon > 0;

-- Them data cho bang NguyenLieu
INSERT INTO NguyenLieu(ID_NL,TenNL,Dongia,Donvitinh) VALUES(100,'Thit ga',40000,'kg');
INSERT INTO NguyenLieu(ID_NL,TenNL,Dongia,Donvitinh) VALUES(101,'Thit heo',50000,'kg');
INSERT INTO NguyenLieu(ID_NL,TenNL,Dongia,Donvitinh) VALUES(102,'Thit bo',80000,'kg');
INSERT INTO NguyenLieu(ID_NL,TenNL,Dongia,Donvitinh) VALUES(103,'Tom',100000,'kg');
INSERT INTO NguyenLieu(ID_NL,TenNL,Dongia,Donvitinh) VALUES(104,'Ca hoi',500000,'kg');
INSERT INTO NguyenLieu(ID_NL,TenNL,Dongia,Donvitinh) VALUES(105,'Gao',40000,'kg');
INSERT INTO NguyenLieu(ID_NL,TenNL,Dongia,Donvitinh) VALUES(106,'Sua tuoi',40000,'l');
INSERT INTO NguyenLieu(ID_NL,TenNL,Dongia,Donvitinh) VALUES(107,'Bot mi',20000,'kg');
INSERT INTO NguyenLieu(ID_NL,TenNL,Dongia,Donvitinh) VALUES(108,'Dau ca hoi',1000000,'l');
INSERT INTO NguyenLieu(ID_NL,TenNL,Dongia,Donvitinh) VALUES(109,'Dau dau nanh',150000,'l');
INSERT INTO NguyenLieu(ID_NL,TenNL,Dongia,Donvitinh) VALUES(110,'Muoi',20000,'kg');
INSERT INTO NguyenLieu(ID_NL,TenNL,Dongia,Donvitinh) VALUES(111,'Duong',20000,'kg');
INSERT INTO NguyenLieu(ID_NL,TenNL,Dongia,Donvitinh) VALUES(112,'Hanh tay',50000,'kg');
INSERT INTO NguyenLieu(ID_NL,TenNL,Dongia,Donvitinh) VALUES(113,'Toi',30000,'kg');
INSERT INTO NguyenLieu(ID_NL,TenNL,Dongia,Donvitinh) VALUES(114,'Dam',50000,'l');
INSERT INTO NguyenLieu(ID_NL,TenNL,Dongia,Donvitinh) VALUES(115,'Thit de',130000,'kg');

-- Them data cho PhieuNK
INSERT INTO PhieuNK(ID_NK, ID_NV, NgayNK) VALUES (100, 102, STR_TO_DATE('10-01-2025', '%d-%m-%Y'));
INSERT INTO PhieuNK(ID_NK, ID_NV, NgayNK) VALUES (101, 102, STR_TO_DATE('11-02-2025', '%d-%m-%Y'));
INSERT INTO PhieuNK(ID_NK, ID_NV, NgayNK) VALUES (102, 102, STR_TO_DATE('12-02-2025', '%d-%m-%Y'));
INSERT INTO PhieuNK(ID_NK, ID_NV, NgayNK) VALUES (103, 102, STR_TO_DATE('12-03-2025', '%d-%m-%Y'));
INSERT INTO PhieuNK(ID_NK, ID_NV, NgayNK) VALUES (104, 102, STR_TO_DATE('15-03-2025', '%d-%m-%Y'));
INSERT INTO PhieuNK(ID_NK, ID_NV, NgayNK) VALUES (105, 102, STR_TO_DATE('12-04-2025', '%d-%m-%Y'));
INSERT INTO PhieuNK(ID_NK, ID_NV, NgayNK) VALUES (106, 102, STR_TO_DATE('15-04-2025', '%d-%m-%Y'));
INSERT INTO PhieuNK(ID_NK, ID_NV, NgayNK) VALUES (107, 102, STR_TO_DATE('12-05-2025', '%d-%m-%Y'));
INSERT INTO PhieuNK(ID_NK, ID_NV, NgayNK) VALUES (108, 102, STR_TO_DATE('15-05-2025', '%d-%m-%Y'));
INSERT INTO PhieuNK(ID_NK, ID_NV, NgayNK) VALUES (109, 102, STR_TO_DATE('05-06-2025', '%d-%m-%Y'));
INSERT INTO PhieuNK(ID_NK, ID_NV, NgayNK) VALUES (110, 102, STR_TO_DATE('07-06-2025', '%d-%m-%Y'));

-- Them data cho CTNK
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (100,100,10);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (100,101,20);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (100,102,15);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (101,101,10);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (101,103,20);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (101,104,10);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (101,105,10);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (101,106,20);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (101,107,5);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (101,108,5);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (102,109,10);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (102,110,20);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (102,112,15);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (102,113,15);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (102,114,15);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (103,112,15);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (103,113,15);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (103,114,15);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (104,112,15);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (104,113,15);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (105,110,15);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (106,102,25);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (106,115,25);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (107,110,35);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (107,105,25);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (108,104,25);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (108,103,15);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (108,106,30);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (109,112,15);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (109,113,15);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (109,114,15);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (110,102,15);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (110,106,25);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (110,107,15);
INSERT INTO CTNK(ID_NK,ID_NL,SoLuong) VALUES (110,110,20);

-- Them data cho PhieuXK

INSERT INTO PhieuXK(ID_XK, ID_NV, NgayXK) VALUES (100, 102, STR_TO_DATE('10-01-2025', '%d-%m-%Y'));
INSERT INTO PhieuXK(ID_XK, ID_NV, NgayXK) VALUES (101, 102, STR_TO_DATE('11-02-2025', '%d-%m-%Y'));
INSERT INTO PhieuXK(ID_XK, ID_NV, NgayXK) VALUES (102, 102, STR_TO_DATE('12-03-2025', '%d-%m-%Y'));
INSERT INTO PhieuXK(ID_XK, ID_NV, NgayXK) VALUES (103, 102, STR_TO_DATE('13-03-2025', '%d-%m-%Y'));
INSERT INTO PhieuXK(ID_XK, ID_NV, NgayXK) VALUES (104, 102, STR_TO_DATE('12-04-2025', '%d-%m-%Y'));
INSERT INTO PhieuXK(ID_XK, ID_NV, NgayXK) VALUES (105, 102, STR_TO_DATE('13-04-2025', '%d-%m-%Y'));
INSERT INTO PhieuXK(ID_XK, ID_NV, NgayXK) VALUES (106, 102, STR_TO_DATE('12-05-2025', '%d-%m-%Y'));
INSERT INTO PhieuXK(ID_XK, ID_NV, NgayXK) VALUES (107, 102, STR_TO_DATE('15-05-2025', '%d-%m-%Y'));
INSERT INTO PhieuXK(ID_XK, ID_NV, NgayXK) VALUES (108, 102, STR_TO_DATE('20-05-2025', '%d-%m-%Y'));
INSERT INTO PhieuXK(ID_XK, ID_NV, NgayXK) VALUES (109, 102, STR_TO_DATE('05-06-2025', '%d-%m-%Y'));
INSERT INTO PhieuXK(ID_XK, ID_NV, NgayXK) VALUES (110, 102, STR_TO_DATE('10-06-2025', '%d-%m-%Y'));

-- Them data cho CTXK
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (100,100,5);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (100,101,5);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (100,102,5);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (101,101,7);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (101,103,10);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (101,104,5);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (101,105,5);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (101,106,10);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (102,109,5);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (102,110,5);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (102,112,10);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (102,113,8);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (102,114,5);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (103,114,5);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (103,104,5);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (104,101,5);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (104,112,5);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (105,113,5);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (105,102,5);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (106,103,5);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (106,114,5);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (107,105,5);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (107,106,5);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (108,115,5);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (108,110,5);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (109,110,5);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (109,112,5);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (110,113,5);
INSERT INTO CTXK(ID_XK,ID_NL,SoLuong) VALUES (110,114,5);
