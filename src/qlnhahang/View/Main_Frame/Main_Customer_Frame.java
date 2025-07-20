
package qlnhahang.View.Main_Frame;

import qlnhahang.Controller.Connection.DatabaseConnection;
import qlnhahang.Controller.Event.EventMenuSelected;
import qlnhahang.Model.ModelNguoiDung;
import qlnhahang.View.Component.Customer_Component.Menu;
import qlnhahang.View.Form.Customer_Form.AccountC_Form;
import qlnhahang.View.Form.Customer_Form.Bill_Form;
import qlnhahang.View.Form.Customer_Form.FoodMenu_Form;
import qlnhahang.View.Form.MainForm;
import qlnhahang.View.Form.Customer_Form.TableMenu_Form;
import qlnhahang.View.Form.Customer_Form.Voucher_Form;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.sql.SQLException;
import net.miginfocom.swing.MigLayout;


public class Main_Customer_Frame extends javax.swing.JFrame {

    private MigLayout layout;
    private Menu menu;
    private MainForm main;
    private ModelNguoiDung user;

    public Main_Customer_Frame() throws HeadlessException {
        initComponents();
        init();
        setTitle("XIAO LONG BAO");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Icons/restaurant (1).png")));
    }
    
    public Main_Customer_Frame(ModelNguoiDung user) {
        this.user=user;
        initComponents();
        init();
        setTitle("XIAO LONG BAO");
        //setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Icons/restaurant (1).png")));
    }
    
    public void init(){
        layout = new MigLayout("fill","0[]0[100%, fill]0","0[fill, top]0");
        bg.setLayout(layout);
        menu=new Menu();
        main= new MainForm();
        menu.addEvent(new EventMenuSelected(){
            @Override
            public void menuSelected(int menuIndex, int subMenuIndex)  {
                switch (menuIndex) {
                    case 0 -> {
                        switch (subMenuIndex) {
                            case 0 -> main.showForm(new FoodMenu_Form("MonKhaiVi",user));
                            case 1 -> main.showForm(new FoodMenu_Form("MonChinh",user));
                            case 2 -> main.showForm(new FoodMenu_Form("MonCanh",user));
                            case 3 -> main.showForm(new FoodMenu_Form("DoUong",user));
                            case 4 -> main.showForm(new FoodMenu_Form("MonTrangMieng",user));
                            default -> {
                            }
                        }
                    }
                    case 1 -> {
                        switch (subMenuIndex) {
                            case 0 -> main.showForm(new TableMenu_Form("Tang 1",user));
                            case 1 -> main.showForm(new TableMenu_Form("Tang 2",user));
                            case 2 -> main.showForm(new TableMenu_Form("Tang 3",user));
                            default -> {
                            }
                        }
                    }
                    //case 2 -> main.showForm(new AboutUs_Form());
                    case 6 -> main.showForm(new AccountC_Form(user));
                    case 7 -> {
                        main.showForm(new Voucher_Form(user));
                    }
                    case 8 -> {
                        main.showForm(new Bill_Form(user));
                    }
                    case 9 -> {
                        dispose();
                        Main_LoginAndRegister.main();
                    }
                    default -> {
                    }
                }
            }
        });
        menu.initMenuItem();
        bg.add(menu,"w 265!, spany 2"); //Span Y 2cell
        bg.add(main,"w 100%, h 100%");
        //Form mặc định lúc đăng nhập
        main.showForm(new TableMenu_Form("Tang 1",user));
    }
  
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new javax.swing.JLayeredPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        bg.setBackground(new java.awt.Color(153, 153, 153));
        bg.setPreferredSize(new java.awt.Dimension(1321, 720));

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1321, Short.MAX_VALUE)
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main_Customer_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main_Customer_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main_Customer_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main_Customer_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        try {
            DatabaseConnection.getInstance().connectToDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main_Customer_Frame().setVisible(true);
            }
        });
    }
    public static void main(ModelNguoiDung user) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main_Customer_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main_Customer_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main_Customer_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main_Customer_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        try {
            DatabaseConnection.getInstance().connectToDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main_Customer_Frame(user).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLayeredPane bg;
    // End of variables declaration//GEN-END:variables
}
