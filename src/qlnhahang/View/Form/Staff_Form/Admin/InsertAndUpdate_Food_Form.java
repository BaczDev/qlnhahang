package qlnhahang.View.Form.Staff_Form.Admin;

import qlnhahang.Controller.Service.ServiceAdmin;
import qlnhahang.Model.ModelMonAn;
import qlnhahang.View.Dialog.MS_Admin_Confirm;
import qlnhahang.View.Dialog.MS_Success;
import qlnhahang.View.Dialog.MS_Warning;
import qlnhahang.View.Form.MainForm;
import qlnhahang.View.Main_Frame.Main_Admin_Frame;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import qlnhahang.View.Swing.ButtonOutLine;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class InsertAndUpdate_Food_Form extends javax.swing.JPanel {

    private ServiceAdmin service;
    private final MainForm main;
    private ModelMonAn data;
    private boolean insert;  //Thêm hay sửa
    private MS_Warning obj;
    private MS_Admin_Confirm confirm;
    private ButtonOutLine cmdChooseImage;

    public InsertAndUpdate_Food_Form(ModelMonAn data, MainForm main) {
        this.data = data;
        this.main = main;

        initComponents();
        init();
    }

    public void init() {
        service = new ServiceAdmin();
        obj = new MS_Warning(Main_Admin_Frame.getFrames()[0], true);
        confirm = new MS_Admin_Confirm(Main_Admin_Frame.getFrames()[0], true);

        cmdChooseImage = new ButtonOutLine();
        cmdChooseImage.setBackground(new java.awt.Color(17, 153, 142));
        cmdChooseImage.setForeground(new java.awt.Color(108, 91, 123));
        cmdChooseImage.setText("Chọn ảnh");
        cmdChooseImage.setFocusable(false);
        cmdChooseImage.setFont(new java.awt.Font("Segoe UI", 0, 16));

        if (data == null) {
            insert = true;
            lbTitle.setText("Thêm MÓN ĂN");
            pic.setIcon(new ImageIcon(getClass().getResource("/Icons/MonAn/Unknown/unknown.jpg")));
            data = new ModelMonAn();
            initID();
            cmdStop.setVisible(false);
        } else {
            insert = false;
            lbTitle.setText("Sửa THÔNG TIN");
            // Tải lại dữ liệu từ cơ sở dữ liệu để đảm bảo HinhAnh mới nhất
            try {
                data = service.getMonAnById(data.getId()); // Giả sử có phương thức này
            } catch (SQLException e) {
                System.out.println("Lỗi tải dữ liệu món ăn: " + e.getMessage());
            }
            if (data.getHinhAnh() != null && !data.getHinhAnh().isEmpty()) {
                data.loadIconFromHinhAnh(data.getType());
            }
            pic.setIcon(data.getIcon() != null ? data.getIcon() : new ImageIcon(getClass().getResource("/Icons/MonAn/Unknown/unknown.jpg")));
            if (data.getState().equals("Dang kinh doanh")) {
                cmdStop.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        confirm.ConfirmStop(data);
                        main.showForm(new MenuManagement_Form(main));
                    }
                });
            } else if (data.getState().equals("Ngung kinh doanh")) {
                cmdStop.setIcon(new ImageIcon(getClass().getResource("/Icons/selling.png")));
                cmdStop.setText("KINH DOANH LẠI");
                cmdStop.setBackground(Color.decode("#DA4453"));
                cmdStop.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        confirm.ConfirmBack(data);
                        main.showForm(new MenuManagement_Form(main));
                    }
                });
            }
            cmdStop.setVisible(true);
            initData();
        }

        cbboxLoai.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = cbboxLoai.getSelectedItem().toString();
                data.setType(selectedType);
                System.out.println("Đã cập nhật type: " + selectedType);
            }
        });

        cmdChooseImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseImage();
            }
        });

        bg.add(cmdChooseImage);
        cmdChooseImage.setBounds(690, 430, 120, 38);
    }
    

    public void initID() {
        try {
            data.setId(service.getNextID_MA());
            txtmaMA.setText(data.getId() + "");

        } catch (SQLException ex) {

        }
    }

    public void initData() {
        txtmaMA.setText(data.getId()+"");
        txttenMA.setText(data.getTitle());
        txtgia.setText(data.getValue()+"");
        cbboxLoai.setSelectedItem(data.getType());
    }
    
    private void chooseImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
            if (icon.getImageLoadStatus() == java.awt.MediaTracker.ERRORED || icon.getImage() == null) {
                return;
            }
            int width = pic.getWidth();
            int height = pic.getHeight();
            if (width <= 0 || height <= 0) {
                pic.setSize(335, 366);
                width = 335;
                height = 366;
            }
            if (icon.getIconWidth() > width || icon.getIconHeight() > height) {
                icon = new ImageIcon(icon.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH));
            }
            pic.setIcon(icon);
            data.setIcon(icon);
            String fileName = selectedFile.getName();
            data.setHinhAnh(fileName);
            String type = data.getType();
            if (type == null || type.trim().isEmpty()) {
                //obj.Warning("Vui lòng chọn loại món ăn trước khi thêm ảnh!");
                return;
            }
            Path source = selectedFile.toPath();
            Path destination = Paths.get("src/main/resources/Icons/MonAn/" + type + "/" + fileName);
            try {
                Files.createDirectories(destination.getParent());
                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Đã sao chép: " + destination.toString());
                // Tải lại icon từ đường dẫn mới
                data.loadIconFromHinhAnh(type);
                pic.setIcon(data.getIcon()); // Cập nhật lại hình ảnh trên form
            } catch (IOException e) {
                System.out.println("Lỗi sao chép: " + e.getMessage());
            }
            pic.repaint();
            bg.revalidate();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new qlnhahang.View.Swing.PanelRound();
        pic = new qlnhahang.View.Swing.ImageAvatar();
        lbmaMA = new javax.swing.JLabel();
        lbTitle = new javax.swing.JLabel();
        lbtenMA = new javax.swing.JLabel();
        txttenMA = new qlnhahang.View.Swing.MyTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        lbloai = new javax.swing.JLabel();
        txtmaMA = new qlnhahang.View.Swing.MyTextField();
        cbboxLoai = new javax.swing.JComboBox<>();
        cmdOK = new qlnhahang.View.Swing.ButtonOutLine();
        cmdCancel = new qlnhahang.View.Swing.ButtonOutLine();
        lbDonGia = new javax.swing.JLabel();
        txtgia = new qlnhahang.View.Swing.MyTextField();
        cmdStop = new qlnhahang.View.Swing.Button();

        setBackground(new java.awt.Color(222, 222, 222));

        bg.setBackground(new java.awt.Color(255, 255, 255));

        pic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/MonAn/Unknown/unknown.jpg"))); // NOI18N

        lbmaMA.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbmaMA.setForeground(new java.awt.Color(89, 89, 89));
        lbmaMA.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbmaMA.setText("Mã Món Ăn");

        lbTitle.setBackground(new java.awt.Color(255, 255, 255));
        lbTitle.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        lbTitle.setForeground(new java.awt.Color(108, 91, 123));
        lbTitle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/online-order.png"))); // NOI18N
        lbTitle.setText("Thêm MÓN ĂN");
        lbTitle.setIconTextGap(10);

        lbtenMA.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbtenMA.setForeground(new java.awt.Color(89, 89, 89));
        lbtenMA.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbtenMA.setText("Tên Món Ăn");

        txttenMA.setForeground(new java.awt.Color(93, 98, 101));
        txttenMA.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txttenMA.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N

        jSeparator1.setBackground(new java.awt.Color(76, 76, 76));

        jSeparator2.setBackground(new java.awt.Color(76, 76, 76));

        lbloai.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbloai.setForeground(new java.awt.Color(89, 89, 89));
        lbloai.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbloai.setText("Loại Món");

        txtmaMA.setEditable(false);
        txtmaMA.setBackground(new java.awt.Color(175, 185, 203));
        txtmaMA.setForeground(new java.awt.Color(93, 98, 101));
        txtmaMA.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtmaMA.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N

        cbboxLoai.setBackground(new java.awt.Color(215, 221, 232));
        cbboxLoai.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        cbboxLoai.setForeground(new java.awt.Color(83, 105, 118));
        cbboxLoai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "MonKhaiVi", "MonChinh", "MonCanh", "DoUong", "MonTrangMieng" }));
        cbboxLoai.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 2));
        cbboxLoai.setFocusable(false);

        cmdOK.setBackground(new java.awt.Color(17, 153, 142));
        cmdOK.setForeground(new java.awt.Color(108, 91, 123));
        cmdOK.setText("Xác nhận");
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

        lbDonGia.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbDonGia.setForeground(new java.awt.Color(89, 89, 89));
        lbDonGia.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbDonGia.setText("Đơn Giá");

        txtgia.setForeground(new java.awt.Color(93, 98, 101));
        txtgia.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtgia.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        txtgia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtgiaKeyTyped(evt);
            }
        });

        cmdStop.setBackground(new java.awt.Color(108, 91, 123));
        cmdStop.setForeground(new java.awt.Color(255, 255, 255));
        cmdStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/no-food.png"))); // NOI18N
        cmdStop.setText("NGƯNG KINH DOANH");
        cmdStop.setFocusable(false);
        cmdStop.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        cmdStop.setIconTextGap(10);

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addComponent(lbTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmdStop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator2)
                    .addComponent(jSeparator1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                        .addComponent(pic, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbtenMA, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                            .addComponent(lbmaMA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbDonGia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbloai, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(30, 30, 30)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtgia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtmaMA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txttenMA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbboxLoai, 0, 250, Short.MAX_VALUE))))
                .addGap(60, 60, 60))
            .addGroup(bgLayout.createSequentialGroup()
                .addGap(231, 231, 231)
                .addComponent(cmdOK, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(148, 148, 148)
                .addComponent(cmdCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbTitle)
                    .addComponent(cmdStop, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbmaMA, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtmaMA, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbtenMA, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txttenMA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbDonGia, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtgia, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbboxLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbloai, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(bgLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(pic, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdOK, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40))
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
                .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmdOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOKActionPerformed
            if ("".equals(txttenMA.getText().trim()) || "".equals(txtgia.getText().trim())) {
                obj.WarningLackofInfo(); // Hiển thị thông báo thiếu thông tin
            } else {
                data.setTitle(txttenMA.getText().trim());
                data.setValue(Integer.parseInt(txtgia.getText().trim()));
                data.setType(cbboxLoai.getSelectedItem().toString());

                if (insert) {
                    try {
                        service.insertMA(data);
                        MS_Success success = new MS_Success((java.awt.Frame) 
                                javax.swing.SwingUtilities.getWindowAncestor(this), true);
                        success.showAddFoodSuccess();
                    } catch (SQLException ex) {
                        Logger.getLogger(InsertAndUpdate_Food_Form.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    try {
                        service.UpdateMonAn(data);
                        MS_Success success = new MS_Success((java.awt.Frame) 
                                javax.swing.SwingUtilities.getWindowAncestor(this), true);
                        success.showUpdateFoodSuccess();
                    } catch (SQLException ex) {
                        Logger.getLogger(InsertAndUpdate_Food_Form.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                main.showForm(new MenuManagement_Form(main));
            }

        
    }//GEN-LAST:event_cmdOKActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        main.showForm(new MenuManagement_Form(main));
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void txtgiaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtgiaKeyTyped
        char c = evt.getKeyChar();
        if (!((c >= '0') && (c <= '9')
                || (c == KeyEvent.VK_BACK_SPACE)
                || (c == KeyEvent.VK_DELETE))) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_txtgiaKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private qlnhahang.View.Swing.PanelRound bg;
    private javax.swing.JComboBox<String> cbboxLoai;
    private qlnhahang.View.Swing.ButtonOutLine cmdCancel;
    private qlnhahang.View.Swing.ButtonOutLine cmdOK;
    private qlnhahang.View.Swing.Button cmdStop;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lbDonGia;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JLabel lbloai;
    private javax.swing.JLabel lbmaMA;
    private javax.swing.JLabel lbtenMA;
    private qlnhahang.View.Swing.ImageAvatar pic;
    private qlnhahang.View.Swing.MyTextField txtgia;
    private qlnhahang.View.Swing.MyTextField txtmaMA;
    private qlnhahang.View.Swing.MyTextField txttenMA;
    // End of variables declaration//GEN-END:variables
}
