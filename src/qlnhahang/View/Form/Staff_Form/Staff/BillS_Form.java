package qlnhahang.View.Form.Staff_Form.Staff;

import qlnhahang.Controller.Service.ServiceCustomer;
import qlnhahang.Controller.Service.ServiceStaff;
import qlnhahang.Model.ModelBan;
import qlnhahang.Model.ModelCTHD;
import qlnhahang.Model.ModelHoaDon;
import qlnhahang.Model.ModelNguoiDung;
import qlnhahang.Model.ModelNhanVien;
import qlnhahang.View.Dialog.MS_PaymentSuccess;
import qlnhahang.View.Dialog.MS_Success;
import qlnhahang.View.Form.MainForm;
import qlnhahang.View.Main_Frame.Main_Admin_Frame;
import qlnhahang.View.Swing.CustomScrollBar.ScrollBarCustom;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class BillS_Form extends javax.swing.JPanel {

    private ServiceStaff serviceS;
    private ServiceCustomer serviceC;
    private final ModelNguoiDung user;
    private final ModelNhanVien staff;
    private final ModelBan table;
    private final ModelHoaDon bill;
    private final MainForm main;
    private ArrayList<ModelCTHD> cthd;
    private DecimalFormat df;
    private MS_Success obj;
    private MS_PaymentSuccess conf;

    public BillS_Form(ModelNguoiDung user, ModelNhanVien staff, ModelBan table, ModelHoaDon bill, MainForm main) {
        this.user = user;
        this.staff = staff;
        this.table = table;
        this.bill = bill;
        this.main = main;
        initComponents();
        init();
    }

    public void init() {
        serviceS = new ServiceStaff();
        serviceC = new ServiceCustomer();
        jScrollPane1.setVerticalScrollBar(new ScrollBarCustom());
        jScrollPane1.getViewport().setBackground(Color.WHITE);
        df = new DecimalFormat("##,###,###");
        obj=new MS_Success(Main_Admin_Frame.getFrames()[0], true);
        conf=new MS_PaymentSuccess(Main_Admin_Frame.getFrames()[0], true);
        //Thêm data cho CTHD 
        initTable();
        //Thêm data cho Tiền hóa đơn
        initCash();

        txtidHD.setText(bill.getIdHoaDon() + "");
        txtidKH.setText(bill.getIdKH() + "");
        lbNgHD.setText("Ngày: " + bill.getNgayHD());
    }

    public void initTable() {
        try {
            //Lấy danh sách CTHD từ mã hóa đơn
            cthd = serviceC.getCTHD(bill.getIdHoaDon());
            for (ModelCTHD data : cthd) {
                tableCTHD.addRow(new Object[]{data.getTenMonAn(), data.getSoluong(), df.format(data.getThanhTien()) + "d"});
            }
        } catch (SQLException ex) {
            Logger.getLogger(BillS_Form.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initCash() {
        txtTienmonan.setText(df.format(bill.getTienMonAn()) + "d");
        txtTiengiam.setText(df.format(bill.getTienGiam()) + "d");
        txtTongtien.setText(df.format(bill.getTongtien()) + "d");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new qlnhahang.View.Swing.PanelRound();
        lbTitle = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        cmdOK = new qlnhahang.View.Swing.ButtonOutLine();
        cmdCancel = new qlnhahang.View.Swing.ButtonOutLine();
        lbNgHD = new javax.swing.JLabel();
        txtidHD = new qlnhahang.View.Swing.MyTextField();
        lbidHD = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        cmdExportBill = new qlnhahang.View.Swing.Button();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableCTHD = new qlnhahang.View.Swing.Table();
        lbTienmonan = new javax.swing.JLabel();
        txtTienmonan = new qlnhahang.View.Swing.MyTextField();
        lbTiengiam = new javax.swing.JLabel();
        txtTiengiam = new qlnhahang.View.Swing.MyTextField();
        lbTongtien = new javax.swing.JLabel();
        txtTongtien = new qlnhahang.View.Swing.MyTextField();
        lbTienKH = new javax.swing.JLabel();
        txtTienKH = new qlnhahang.View.Swing.MyTextField();
        lbTientra = new javax.swing.JLabel();
        txtTientra = new qlnhahang.View.Swing.MyTextField();
        lbidKH = new javax.swing.JLabel();
        txtidKH = new qlnhahang.View.Swing.MyTextField();

        setBackground(new java.awt.Color(222, 222, 222));

        bg.setBackground(new java.awt.Color(247, 247, 247));

        lbTitle.setBackground(new java.awt.Color(255, 255, 255));
        lbTitle.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        lbTitle.setForeground(new java.awt.Color(108, 91, 123));
        lbTitle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/search.png"))); // NOI18N
        lbTitle.setText("Thông tin Hóa Đơn");
        lbTitle.setIconTextGap(10);

        jSeparator1.setBackground(new java.awt.Color(76, 76, 76));

        cmdOK.setBackground(new java.awt.Color(17, 153, 142));
        cmdOK.setForeground(new java.awt.Color(108, 91, 123));
        cmdOK.setText("Xác nhận Thanh Toán");
        cmdOK.setFocusable(false);
        cmdOK.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        cmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOKActionPerformed(evt);
            }
        });

        cmdCancel.setBackground(new java.awt.Color(237, 33, 58));
        cmdCancel.setForeground(new java.awt.Color(108, 91, 123));
        cmdCancel.setText("Hủy");
        cmdCancel.setFocusable(false);
        cmdCancel.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        lbNgHD.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbNgHD.setForeground(new java.awt.Color(89, 89, 89));
        lbNgHD.setText("Ngày ");

        txtidHD.setEditable(false);
        txtidHD.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtidHD.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N

        lbidHD.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbidHD.setForeground(new java.awt.Color(89, 89, 89));
        lbidHD.setText("Mã Hóa Đơn");

        jSeparator3.setBackground(new java.awt.Color(76, 76, 76));
        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        cmdExportBill.setBackground(new java.awt.Color(108, 91, 123));
        cmdExportBill.setForeground(new java.awt.Color(255, 255, 255));
        cmdExportBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/receipt.png"))); // NOI18N
        cmdExportBill.setText("XUẤT HÓA ĐƠN");
        cmdExportBill.setFocusable(false);
        cmdExportBill.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cmdExportBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExportBillActionPerformed(evt);
            }
        });

        tableCTHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mon An", "So luong", "Thanh tien"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableCTHD.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jScrollPane1.setViewportView(tableCTHD);
        if (tableCTHD.getColumnModel().getColumnCount() > 0) {
            tableCTHD.getColumnModel().getColumn(0).setMinWidth(300);
            tableCTHD.getColumnModel().getColumn(0).setMaxWidth(300);
        }

        lbTienmonan.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbTienmonan.setForeground(new java.awt.Color(89, 89, 89));
        lbTienmonan.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTienmonan.setText("Tiền món ăn");

        txtTienmonan.setEditable(false);
        txtTienmonan.setBackground(new java.awt.Color(175, 185, 203));
        txtTienmonan.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTienmonan.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N

        lbTiengiam.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbTiengiam.setForeground(new java.awt.Color(89, 89, 89));
        lbTiengiam.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTiengiam.setText("Tiền giảm giá");

        txtTiengiam.setEditable(false);
        txtTiengiam.setBackground(new java.awt.Color(175, 185, 203));
        txtTiengiam.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTiengiam.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N

        lbTongtien.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbTongtien.setForeground(new java.awt.Color(89, 89, 89));
        lbTongtien.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTongtien.setText("Tổng tiền");

        txtTongtien.setEditable(false);
        txtTongtien.setBackground(new java.awt.Color(175, 185, 203));
        txtTongtien.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTongtien.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N

        lbTienKH.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbTienKH.setForeground(new java.awt.Color(89, 89, 89));
        lbTienKH.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTienKH.setText("Tiền khách đưa ");

        txtTienKH.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTienKH.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        txtTienKH.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTienKHFocusLost(evt);
            }
        });
        txtTienKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTienKHActionPerformed(evt);
            }
        });
        txtTienKH.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTienKHKeyTyped(evt);
            }
        });

        lbTientra.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbTientra.setForeground(new java.awt.Color(89, 89, 89));
        lbTientra.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTientra.setText("Tiền trả lại");

        txtTientra.setEditable(false);
        txtTientra.setBackground(new java.awt.Color(175, 185, 203));
        txtTientra.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTientra.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N

        lbidKH.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbidKH.setForeground(new java.awt.Color(89, 89, 89));
        lbidKH.setText("Mã Khách Hàng");

        txtidKH.setEditable(false);
        txtidKH.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtidKH.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(jScrollPane1)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addComponent(lbidHD)
                        .addGap(30, 30, 30)
                        .addComponent(txtidHD, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addComponent(lbidKH)
                        .addGap(30, 30, 30)
                        .addComponent(txtidKH, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(cmdExportBill, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(bgLayout.createSequentialGroup()
                        .addComponent(lbTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbNgHD))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lbTienmonan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbTiengiam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbTongtien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbTientra, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbTienKH, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTientra, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTienKH, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTongtien, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTiengiam, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTienmonan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cmdOK, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(149, 149, 149)
                        .addComponent(cmdCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(158, 158, 158)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbTitle)
                    .addComponent(lbNgHD, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtidKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbidKH, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtidHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbidHD, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmdExportBill, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTienmonan, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbTienmonan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTiengiam, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbTiengiam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTongtien, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbTongtien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTienKH, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbTienKH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTientra, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbTientra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdOK, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmdOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOKActionPerformed
        try {
            //Khi NV bấm xác nhận thanh toán, thay đổi trạng thái hóa đơn từ Chưa thanh toán thành đã thanh toán
            serviceS.UpdateHoaDonStatus(bill.getIdHoaDon());
            conf.ConfirmPaymentSuccess(bill.getIdHoaDon());
            main.showForm(new TableMenuS_Form("Tang 1", user, main));
            
        } catch (SQLException ex) {
            Logger.getLogger(BillS_Form.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cmdOKActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        main.showForm(new OrderFood_Form(user, staff, table, main));
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void txtTienKHKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTienKHKeyTyped
        char c = evt.getKeyChar();
        if (!((c >= '0') && (c <= '9')
                || (c == KeyEvent.VK_BACK_SPACE)
                || (c == KeyEvent.VK_DELETE))) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_txtTienKHKeyTyped

    private void txtTienKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTienKHActionPerformed
        txtTientra.setText(df.format(Integer.parseInt(txtTienKH.getText()) - bill.getTongtien()) + "d");
    }//GEN-LAST:event_txtTienKHActionPerformed

    private void txtTienKHFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTienKHFocusLost
        txtTientra.setText(df.format(Integer.parseInt(txtTienKH.getText()) - bill.getTongtien()) + "d");
    }//GEN-LAST:event_txtTienKHFocusLost

    private void cmdExportBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExportBillActionPerformed
        // Tạo document mới
PDDocument invc = new PDDocument();

// Tạo trang trắng
PDPage newpage = new PDPage();
invc.addPage(newpage);

String title = "XIAO LONG BAO";
String subtitle = "HÓA ĐƠN THANH TOÁN";
String footer = "XIN CẢM ƠN QUÝ KHÁCH";
String tenKH = "";

try {
    tenKH = serviceS.getTenKH(bill.getIdKH());
} catch (SQLException ex) {
    Logger.getLogger(BillS_Form.class.getName()).log(Level.SEVERE, null, ex);
}

DefaultTableModel model = (DefaultTableModel) tableCTHD.getModel();

PDPage mypage = invc.getPage(0);

try {
    // Load font Unicode Times New Roman
    PDFont font = PDType0Font.load(invc, new File("C:/Windows/Fonts/times.ttf"));
    PDFont fontBold = PDType0Font.load(invc, new File("C:/Windows/Fonts/timesbd.ttf"));

    PDPageContentStream cs = new PDPageContentStream(invc, mypage);
    
    // Lấy chiều rộng trang
    float pageWidth = mypage.getMediaBox().getWidth();

    // ==== Căn giữa TITLE ====
    float titleFontSize = 22;
    float titleWidth = fontBold.getStringWidth(title) / 1000 * titleFontSize;
    float titleX = (pageWidth - titleWidth) / 2;

    cs.beginText();
    cs.setFont(fontBold, titleFontSize);
    cs.newLineAtOffset(titleX, 750);
    cs.showText(title);
    cs.endText();

    // ==== Căn giữa SUBTITLE ====
    float subtitleFontSize = 18;
    float subtitleWidth = font.getStringWidth(subtitle) / 1000 * subtitleFontSize;
    float subtitleX = (pageWidth - subtitleWidth) / 2;

    cs.beginText();
    cs.setFont(font, subtitleFontSize);
    cs.newLineAtOffset(subtitleX, 720);
    cs.showText(subtitle);
    cs.endText();

    // Thông tin khách hàng
    cs.beginText();
    cs.setFont(font, 14);
    cs.setLeading(20f);
    cs.newLineAtOffset(60, 670);
    cs.showText("Mã Hóa Đơn: ");
    cs.newLine();
    cs.showText("Ngày: ");
    cs.newLine();
    cs.showText("Khách Hàng: ");
    cs.endText();

    cs.beginText();
    cs.setFont(font, 14);
    cs.setLeading(20f);
    cs.newLineAtOffset(170, 670);
    cs.showText(bill.getIdHoaDon() + "");
    cs.newLine();
    cs.showText(bill.getNgayHD());
    cs.newLine();
    cs.showText(tenKH);
    cs.endText();

    // Vẽ bảng chi tiết hóa đơn
    float tableStartY = 600;
    float rowHeight = 20;
    int numCols = model.getColumnCount();
    int numRows = model.getRowCount();
    float[] colWidths = new float[numCols];
    colWidths[0] = 200;
    for (int i = 1; i < numCols; i++) colWidths[i] = 100;

    float x = 60;
    float y = tableStartY;

    // Header
    for (int col = 0; col < numCols; col++) {
        cs.beginText();
        cs.setFont(fontBold, 13);
        cs.newLineAtOffset(x, y);
        cs.showText(model.getColumnName(col));
        cs.endText();
        x += colWidths[col];
    }

    // Dữ liệu
    for (int row = 0; row < numRows; row++) {
        x = 60;
        y -= rowHeight;
        for (int col = 0; col < numCols; col++) {
            cs.beginText();
            cs.setFont(font, 12);
            cs.newLineAtOffset(x, y);
            cs.showText(model.getValueAt(row, col).toString());
            cs.endText();
            x += colWidths[col];
        }
    }

    // Thông tin thanh toán
    float startYPayment = y - 40;

    cs.beginText();
    cs.setFont(font, 14);
    cs.setLeading(20f);
    cs.newLineAtOffset(280, startYPayment);
    cs.showText("Tiền món ăn: ");
    cs.newLine();
    cs.showText("Tiền giảm giá: ");
    cs.newLine();
    cs.showText("Tổng tiền: ");
    cs.newLine();
    cs.showText("Tiền khách đưa: ");
    cs.newLine();
    cs.showText("Tiền trả lại: ");
    cs.endText();

    cs.beginText();
    cs.setFont(font, 14);
    cs.setLeading(20f);
    cs.newLineAtOffset(420, startYPayment);
    cs.showText(txtTienmonan.getText());
    cs.newLine();
    cs.showText(txtTiengiam.getText());
    cs.newLine();
    cs.showText(txtTongtien.getText());
    cs.newLine();
    cs.showText(df.format(Integer.parseInt(txtTienKH.getText())) + "đ");
    cs.newLine();
    cs.showText(txtTientra.getText());
    cs.endText();

    // Footer
    cs.beginText();
    cs.setFont(fontBold, 16);
    cs.newLineAtOffset(180, startYPayment - 100);
    cs.showText(footer);
    cs.endText();

    cs.close();

    // Lưu file
    String fileName = ".\\src\\ExportBill\\HoaDon_ID-" + bill.getIdHoaDon() + ".pdf";
    invc.save(fileName);
    File file = new File(fileName);
    obj.ExportFileSuccess(file.getAbsolutePath());

} catch (IOException ex) {
    Logger.getLogger(BillS_Form.class.getName()).log(Level.SEVERE, null, ex);
}

    }//GEN-LAST:event_cmdExportBillActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private qlnhahang.View.Swing.PanelRound bg;
    private qlnhahang.View.Swing.ButtonOutLine cmdCancel;
    private qlnhahang.View.Swing.Button cmdExportBill;
    private qlnhahang.View.Swing.ButtonOutLine cmdOK;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lbNgHD;
    private javax.swing.JLabel lbTienKH;
    private javax.swing.JLabel lbTiengiam;
    private javax.swing.JLabel lbTienmonan;
    private javax.swing.JLabel lbTientra;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JLabel lbTongtien;
    private javax.swing.JLabel lbidHD;
    private javax.swing.JLabel lbidKH;
    private qlnhahang.View.Swing.Table tableCTHD;
    private qlnhahang.View.Swing.MyTextField txtTienKH;
    private qlnhahang.View.Swing.MyTextField txtTiengiam;
    private qlnhahang.View.Swing.MyTextField txtTienmonan;
    private qlnhahang.View.Swing.MyTextField txtTientra;
    private qlnhahang.View.Swing.MyTextField txtTongtien;
    private qlnhahang.View.Swing.MyTextField txtidHD;
    private qlnhahang.View.Swing.MyTextField txtidKH;
    // End of variables declaration//GEN-END:variables
}
