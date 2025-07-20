package qlnhahang.View.Component.LoginAndRegister_Component;

import qlnhahang.Model.ModelLogin;
import qlnhahang.Model.ModelNguoiDung;
import qlnhahang.View.Swing.Button;
import qlnhahang.View.Swing.MyPasswordField;
import qlnhahang.View.Swing.MyTextField;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;

//Panel Đăng nhập/Đăng kys
public class PanelLoginAndRegister extends javax.swing.JLayeredPane {

    public ModelNguoiDung getUser() {
        return user;
    }

    public ModelLogin getDataLogin() {
        return dataLogin;
    }

    private ModelNguoiDung user;
    private ModelLogin dataLogin;
    private Icon hide;
    private Icon show;
    private char def;

    public PanelLoginAndRegister(ActionListener eventRegister, ActionListener eventLogin) {
        initComponents();
        hide = new ImageIcon(getClass().getResource("/Icons/hide.png"));
        show = new ImageIcon(getClass().getResource("/Icons/view.png"));
        initLogin(eventLogin);
        initRegister(eventRegister);
        register.setVisible(true);
        login.setVisible(false);
    }

    private void initRegister(ActionListener eventRegister) {
        register.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));

        JLabel label = new JLabel("TẠO TÀI KHOẢN");
        label.setFont(new Font("sansserif", 1, 30));
        label.setForeground(Color.decode("#F8E7D0"));
        register.add(label);

        MyTextField txtPhone = new MyTextField();
        //txtPhone.setPrefixIcon(new ImageIcon(getClass().getResource("/Icons/user (2).png")));
        txtPhone.setHint("Số điện thoại ...");
        txtPhone.setBackground(new Color(248, 231, 208));
        txtPhone.setForeground(new Color(121, 14, 14));   
        txtPhone.setCaretColor(new Color(121, 14, 14)); 
        register.add(txtPhone, "w 60%");

        MyTextField txtEmail = new MyTextField();
        //txtEmail.setPrefixIcon(new ImageIcon(getClass().getResource("/Icons/mail.png")));
        txtEmail.setHint("Email ...");
        txtEmail.setBackground(new Color(248, 231, 208));
        txtEmail.setForeground(new Color(121, 14, 14));   
        txtEmail.setCaretColor(new Color(121, 14, 14)); 
        register.add(txtEmail, "w 60%");

        MyPasswordField txtPassword = new MyPasswordField();
        def = txtPassword.getEchoChar();
        //txtPassword.setPrefixIcon(new ImageIcon(getClass().getResource("/Icons/pass.png")));
        txtPassword.setHint("Mật khẩu ...");
        txtPassword.setBackground(new Color(248, 231, 208));
        txtPassword.setForeground(new Color(121, 14, 14));   
        txtPassword.setCaretColor(new Color(121, 14, 14)); 
        txtPassword.setSuffixIcon(show);
        register.add(txtPassword, "w 60%");
        txtPassword.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (txtPassword.getSuffixIcon().equals(hide)) {
                    txtPassword.setSuffixIcon(show);
                    txtPassword.setEchoChar((char) 0);
                } else {
                    txtPassword.setSuffixIcon(hide);
                    txtPassword.setEchoChar(def);
                }
            }
        });

        Button cmd = new Button();
        cmd.setBackground(Color.decode("#F8E7D0"));
        cmd.setForeground(Color.decode("#790E0E"));
        cmd.setText("ĐĂNG KÝ");
        cmd.addActionListener(eventRegister);
        register.add(cmd, "w 40%, h 40");
        cmd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String phone = txtPhone.getText().trim();
                String email = txtEmail.getText().trim();
                String password = String.valueOf(txtPassword.getPassword());
                user = new ModelNguoiDung(0, email, password, "Khach Hang", phone);
            }
        });
    }

    private void initLogin(ActionListener eventLogin) {
        login.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));
        JLabel label = new JLabel("ĐĂNG NHẬP");
        label.setFont(new Font("sansserif", 1, 30));
        label.setForeground(Color.decode("#F8E7D0"));
        login.add(label);

        MyTextField txtEmail = new MyTextField();
        //txtEmail.setPrefixIcon(new ImageIcon(getClass().getResource("/Icons/mail.png")));
        txtEmail.setHint("Email");
        txtEmail.setBackground(new Color(248, 231, 208)); 
        txtEmail.setForeground(new Color(121, 14, 14));   
        txtEmail.setCaretColor(new Color(121, 14, 14)); 
        login.add(txtEmail, "w 60%");

        MyPasswordField txtPassword = new MyPasswordField();
        def = txtPassword.getEchoChar();
        //txtPassword.setPrefixIcon(new ImageIcon(getClass().getResource("/Icons/pass.png")));
        txtPassword.setHint("Mật khẩu");
        txtPassword.setBackground(new Color(248, 231, 208));
        txtPassword.setForeground(new Color(121, 14, 14));   
        txtPassword.setCaretColor(new Color(121, 14, 14)); 
        txtPassword.setSuffixIcon(show);
        login.add(txtPassword, "w 60%");
        txtPassword.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (txtPassword.getSuffixIcon().equals(hide)) {
                    txtPassword.setSuffixIcon(show);
                    txtPassword.setEchoChar((char) 0);
                } else {
                    txtPassword.setSuffixIcon(hide);
                    txtPassword.setEchoChar(def);
                }
            }
        });

        // JButton cmdForget = new JButton("Quên mật khẩu của bạn ?");
        // cmdForget.setForeground(Color.decode("#F8E7D0"));
        // cmdForget.setFont(new Font("sansserif", 1, 12));
        // cmdForget.setContentAreaFilled(false);
        // cmdForget.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // login.add(cmdForget);

        Button cmd = new Button();
        cmd.setBackground(Color.decode("#F8E7D0"));
        cmd.setForeground(Color.decode("#790E0E"));
        cmd.setText("ĐĂNG NHẬP");
        login.add(cmd, "gaptop 20, w 40%, h 40");
        cmd.addActionListener(eventLogin);
        cmd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = txtEmail.getText().trim();
                String password = String.valueOf(txtPassword.getPassword());
                dataLogin = new ModelLogin(email, password);
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        login = new javax.swing.JPanel();
        register = new javax.swing.JPanel();

        setLayout(new java.awt.CardLayout());

        login.setBackground(new java.awt.Color(121, 14, 14));

        javax.swing.GroupLayout loginLayout = new javax.swing.GroupLayout(login);
        login.setLayout(loginLayout);
        loginLayout.setHorizontalGroup(
            loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        loginLayout.setVerticalGroup(
            loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        add(login, "card2");

        register.setBackground(new java.awt.Color(121, 14, 14));

        javax.swing.GroupLayout registerLayout = new javax.swing.GroupLayout(register);
        register.setLayout(registerLayout);
        registerLayout.setHorizontalGroup(
            registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        registerLayout.setVerticalGroup(
            registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        add(register, "card3");
    }// </editor-fold>//GEN-END:initComponents
    //Hiển thị màn hình Đăng nhập/Đăng ký

    public void showRegister(boolean show) {
        if (show) {
            register.setVisible(true);
            login.setVisible(false);
        } else {
            register.setVisible(false);
            login.setVisible(true);
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel login;
    private javax.swing.JPanel register;
    // End of variables declaration//GEN-END:variables
}
