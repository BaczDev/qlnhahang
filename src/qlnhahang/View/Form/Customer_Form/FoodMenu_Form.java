package qlnhahang.View.Form.Customer_Form;

import qlnhahang.Controller.Service.ServiceCustomer;
import qlnhahang.Model.ModelMonAn;
import qlnhahang.Model.ModelKhachHang;
import qlnhahang.Model.ModelHoaDon;
import qlnhahang.Model.ModelNguoiDung;
import qlnhahang.View.Component.Customer_Component.CardMonAn;
import qlnhahang.View.Dialog.MS_PayBill;
import qlnhahang.View.Dialog.MS_Warning;
import qlnhahang.View.Main_Frame.Main_Customer_Frame;
import qlnhahang.View.Swing.CustomScrollBar.ScrollBarCustom;
import qlnhahang.View.Swing.WrapLayout;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

public class FoodMenu_Form extends javax.swing.JPanel {

    private final String type;
    private final ServiceCustomer service;
    private ArrayList<ModelMonAn> list;
    private final ModelNguoiDung user;
    private ModelKhachHang customer;
    private ModelHoaDon HoaDon;
    private final MS_Warning warning;
    private MS_PayBill obj;

    public FoodMenu_Form(String type, ModelNguoiDung user) {
        this.type = type;
        this.user = user;
        service = new ServiceCustomer();
        warning = new MS_Warning(Main_Customer_Frame.getFrames()[0], true);
        obj = new MS_PayBill(Main_Customer_Frame.getFrames()[0], true);
        initComponents();    
        init();
        //Kiểm tra Khách hàng đã đặt bàn trước khi gọi món hay chưa
        if (HoaDon == null) {
            warning.WarningBook();
        } else {
            txtTableName.setText(HoaDon.getIdBan() + "");
        }
        
    }

    public void init() {
        try {
            panel.setLayout(new WrapLayout(WrapLayout.LEADING, 20, 20));
            txtSearch.setHint("Tìm kiếm món ăn . . .");
            jScrollPane1.setVerticalScrollBar(new ScrollBarCustom());
            customer = service.getCustomer(user.getUserID());
            
            //Tìm thông tin Hóa Đơn mà Khách Hàng vừa tạo
            HoaDon = service.FindHoaDon(customer);
            
            //Thêm data cho Menu
            initMenuFood();
            
            
            //Set Data cho Tiêu đề Menu
            switch (type) {
                case "MonKhaiVi" -> {
                    lbTitle.setText("Menu - Món Khai Vị");
                    //lbTitle.setIcon(new ImageIcon(getClass().getResource("/Icons/MenuBar/aries.png")));
                }
                case "MonChinh" -> {
                    lbTitle.setText("Menu - Món Chính");
                    //lbTitle.setIcon(new ImageIcon(getClass().getResource("/Icons/MenuBar/taurus.png")));
                }
                case "MonCanh" -> {
                    lbTitle.setText("Menu - Món Canh");
                    //lbTitle.setIcon(new ImageIcon(getClass().getResource("/Icons/MenuBar/gemini.png")));
                }
                case "DoUong" -> {
                    lbTitle.setText("Menu - Đồ Uống");
                    //lbTitle.setIcon(new ImageIcon(getClass().getResource("/Icons/MenuBar/cancer.png")));
                }
                case "MonTrangMieng" -> {
                    lbTitle.setText("Menu - Món Tráng Miệng");
                    //lbTitle.setIcon(new ImageIcon(getClass().getResource("/Icons/MenuBar/leo.png")));
                }
                default -> {
                }
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(FoodMenu_Form.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initMenuFood() {
        try {
            list = service.MenuFood(type);
            for (ModelMonAn data : list) {
                panel.add(new CardMonAn(data, HoaDon));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void searchFood(String txt) {
        panel.removeAll();
        for (ModelMonAn data : list) {
            if (data.getTitle().toLowerCase().contains(txt.toLowerCase())) {
                panel.add(new CardMonAn(data, HoaDon));
            }
        }
        panel.repaint();
        panel.revalidate();
    }

    public void initMenuFoodOrderby(String txt) {
        try {
            list = service.MenuFoodOrder(type, txt);
            panel.removeAll();
            for (ModelMonAn data : list) {
                panel.add(new CardMonAn(data, HoaDon));
            }

        } catch (SQLException ex) {
            Logger.getLogger(FoodMenu_Form.class.getName()).log(Level.SEVERE, null, ex);
        }
        panel.repaint();
        panel.revalidate();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        panel = new javax.swing.JPanel();
        lbTitle = new javax.swing.JLabel();
        txtSearch = new qlnhahang.View.Swing.MyTextField();
        cmdShowBill = new qlnhahang.View.Swing.Button();
        lbTable = new javax.swing.JLabel();
        txtTableName = new qlnhahang.View.Swing.MyTextField();
        jSeparator2 = new javax.swing.JSeparator();

        setBackground(new java.awt.Color(247, 247, 247));

        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panel.setBackground(new java.awt.Color(215, 221, 232));

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 963, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 542, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(panel);

        lbTitle.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbTitle.setForeground(new java.awt.Color(108, 91, 123));
        lbTitle.setText("Menu/Arias - Bạch Dương");
        lbTitle.setIconTextGap(10);
        lbTitle.setInheritsPopupMenu(false);

        txtSearch.setPrefixIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/loupe (1).png"))); // NOI18N
        txtSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtSearchMouseEntered(evt);
            }
        });
        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        cmdShowBill.setBackground(new java.awt.Color(108, 91, 123));
        cmdShowBill.setForeground(new java.awt.Color(255, 255, 255));
        cmdShowBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/clipboard.png"))); // NOI18N
        cmdShowBill.setText("XEM HÓA ĐƠN");
        cmdShowBill.setFocusable(false);
        cmdShowBill.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cmdShowBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowBillActionPerformed(evt);
            }
        });

        lbTable.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbTable.setForeground(new java.awt.Color(89, 89, 89));
        lbTable.setText("MÃ BÀN CỦA BẠN");

        txtTableName.setEditable(false);
        txtTableName.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTableName.setText("Chưa đặt bàn");
        txtTableName.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N

        jSeparator2.setBackground(new java.awt.Color(76, 76, 76));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbTitle)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbTable)
                        .addGap(38, 38, 38)
                        .addComponent(txtTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmdShowBill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmdShowBill, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbTable, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTableName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSearchMouseEntered
        searchFood(txtSearch.getText().trim());
    }//GEN-LAST:event_txtSearchMouseEntered

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        searchFood(txtSearch.getText().trim());
    }//GEN-LAST:event_txtSearchActionPerformed

    private void cmdShowBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowBillActionPerformed
        try {
            HoaDon=service.FindHoaDon(customer);
            obj.showBill(HoaDon);
        } catch (SQLException ex) {
            Logger.getLogger(FoodMenu_Form.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cmdShowBillActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private qlnhahang.View.Swing.Button cmdShowBill;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lbTable;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JPanel panel;
    private qlnhahang.View.Swing.MyTextField txtSearch;
    private qlnhahang.View.Swing.MyTextField txtTableName;
    // End of variables declaration//GEN-END:variables
}
