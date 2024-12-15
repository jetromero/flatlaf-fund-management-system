package jet.form;

import jet.connection.DatabaseConnection;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author jetve
 */
public class LoginPage extends javax.swing.JFrame {

    public LoginPage() {
        initComponents();
        init();
    }
    
    private void init() {
        loginBtn.putClientProperty(FlatClientProperties.STYLE, ""
                + "borderWidth:0;"
                + "focusWidth:0;");
        
        userField.putClientProperty(FlatClientProperties.STYLE, ""
                + "focusedBorderColor:#A7C957;"
                + "arc:10;");
        
        passField.putClientProperty(FlatClientProperties.STYLE, ""
                + "focusedBorderColor:#A7C957;"
                + "arc:10;");
        
        showPass.putClientProperty(FlatClientProperties.STYLE, ""
                + "icon.hoverBorderColor:#A7C957;"
                + "icon.focusColor:#A7C957;"
                + "icon.selectedBorderColor:#A7C957;"
                + "icon.checkmarkColor:#FFFFFF;"
                + "icon.selectedBackground:#A7C957");
        
        userField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter username");
        passField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter password");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        x = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        userField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        showPass = new javax.swing.JCheckBox();
        passField = new javax.swing.JPasswordField();
        loginBtn = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);
        setSize(new java.awt.Dimension(600, 400));

        x.setText("jToggleButton1");

        jPanel1.setPreferredSize(new java.awt.Dimension(300, 340));

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Sign In");

        jLabel2.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Hello, welcome back!");

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel3.setText(" Username");

        userField.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        userField.setToolTipText("Enter your username");
        userField.setMargin(new java.awt.Insets(2, 10, 2, 10));
        userField.setPreferredSize(new java.awt.Dimension(64, 38));
        userField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userFieldActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel4.setText(" Password");

        showPass.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        showPass.setForeground(new java.awt.Color(102, 102, 102));
        showPass.setText("Show Password");
        showPass.setToolTipText("");
        showPass.setActionCommand("Show password");
        showPass.setContentAreaFilled(false);
        showPass.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        showPass.setFocusPainted(false);
        showPass.setFocusable(false);
        showPass.setRequestFocusEnabled(false);
        showPass.setVerifyInputWhenFocusTarget(false);
        showPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showPassActionPerformed(evt);
            }
        });

        passField.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        passField.setToolTipText("Enter your password");
        passField.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        passField.setMargin(new java.awt.Insets(2, 10, 2, 10));
        passField.setPreferredSize(new java.awt.Dimension(90, 32));

        loginBtn.setBackground(new java.awt.Color(167, 201, 87));
        loginBtn.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        loginBtn.setForeground(new java.awt.Color(255, 255, 255));
        loginBtn.setText("LOGIN");
        loginBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loginBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        loginBtn.setMargin(new java.awt.Insets(3, 14, 3, 14));
        loginBtn.setName(""); // NOI18N
        loginBtn.setPreferredSize(new java.awt.Dimension(74, 32));
        loginBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userField, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(showPass, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passField, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(loginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(userField, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(passField, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(showPass, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(loginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(x, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(487, 487, 487)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(487, 487, 487))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(180, 180, 180)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(x)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(190, 190, 190))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void showPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showPassActionPerformed
        if (showPass.isSelected()) {
            passField.setEchoChar((char) 0);
        } else {
            passField.setEchoChar('\u2022');
        }   
    }//GEN-LAST:event_showPassActionPerformed

    private void loginBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginBtnActionPerformed
        String username = userField.getText();
        String password = new String(passField.getPassword());
        
        String hashedPassword = hashPassword(password);
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM users WHERE username = ? AND password = ? AND role = 'admin'";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, hashedPassword);
            
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                MenuPage menu = new MenuPage();
                menu.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect Username or Password!", "Invalid", JOptionPane.ERROR_MESSAGE);
            }
            
            rs.close();
            pst.close();
            conn.close();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_loginBtnActionPerformed

    private void userFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_userFieldActionPerformed

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Hashing error: " + e.getMessage());
            return null;
        }
    }

    public static void main(String args[]) {
        FlatLaf.registerCustomDefaultsSource("jet.themes");
        FlatLightLaf.setup();

        java.awt.EventQueue.invokeLater(() -> {
            new LoginPage().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton loginBtn;
    private javax.swing.JPasswordField passField;
    private javax.swing.JCheckBox showPass;
    private javax.swing.JTextField userField;
    private javax.swing.JToggleButton x;
    // End of variables declaration//GEN-END:variables
}
