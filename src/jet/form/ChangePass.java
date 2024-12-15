package jet.form;

import java.sql.*;
import javax.swing.JOptionPane;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import jet.connection.DatabaseConnection;

/**
 *
 * @author jetve
 */
public class ChangePass extends javax.swing.JPanel {
    private String currentUsername;

    public ChangePass() {
        initComponents();
        loadUsername(); // Load the username when panel is created
    }

    // Method to load username from database
    private void loadUsername() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT username FROM users LIMIT 1";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                currentUsername = rs.getString("username");
                userName.setText(currentUsername);
            }

            rs.close();
            pst.close();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading username: " + e.getMessage());
        }
    }

    // Method to encrypt password using MD5
    private String encryptPassword(String password) {
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
            throw new RuntimeException(e);
        }
    }

    // Method to update password in database
    public boolean updatePassword() {
        // Validate passwords match
        if (!getNPass().equals(getCPass())) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!");
            return false;
        }

        // Validate password strength
        if (!isPasswordStrong(getNPass())) {
            JOptionPane.showMessageDialog(this, 
                "Password must be at least 8 characters long and contain:\n" +
                "- At least one uppercase letter\n" +
                "- At least one lowercase letter\n" +
                "- At least one number\n" +
                "- At least one special character");
            return false;
        }

        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "UPDATE users SET password = ? WHERE username = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            
            // Encrypt the new password before saving
            String encryptedPassword = encryptPassword(getNPass());
            pst.setString(1, encryptedPassword);
            pst.setString(2, currentUsername);

            int result = pst.executeUpdate();
            
            pst.close();
            conn.close();
            
            return result > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating password: " + e.getMessage());
            return false;
        }
    }

    // Method to validate password strength
    private boolean isPasswordStrong(String password) {
        // Password must be at least 8 characters long
        if (password.length() < 8) return false;

        // Check for at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) return false;

        // Check for at least one lowercase letter
        if (!password.matches(".*[a-z].*")) return false;

        // Check for at least one number
        if (!password.matches(".*\\d.*")) return false;

        // Check for at least one special character
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) return false;

        return true;
    }

    // Update the validateForm method to include password validation
    public boolean validateForm() {
        if (!getUsername().isEmpty() && !getNPass().isEmpty() && !getCPass().isEmpty()) {
            if (!getNPass().equals(getCPass())) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!");
                return false;
            }
            if (!isPasswordStrong(getNPass())) {
                JOptionPane.showMessageDialog(this, 
                    "Password must be at least 8 characters long and contain:\n" +
                    "- At least one uppercase letter\n" +
                    "- At least one lowercase letter\n" +
                    "- At least one number\n" +
                    "- At least one special character");
                return false;
            }
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        userName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        confirmPass = new javax.swing.JPasswordField();
        newPass = new javax.swing.JPasswordField();

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("Username");

        userName.setEditable(false);
        userName.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        userName.setFocusable(false);

        jLabel2.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("New Password");

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Confirm Password");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(userName, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addComponent(confirmPass)
                    .addComponent(newPass))
                .addGap(50, 50, 50))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(userName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(newPass, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(confirmPass, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Add these methods to get form data
    public String getUsername() {
        return userName.getText();
    }
    
    public String getNPass() {
        return new String(newPass.getPassword());
    }
    
    public String getCPass() {
        return new String(confirmPass.getPassword());
    }
    
//    public boolean validateForm() {
//        return !getUsername().isEmpty() && 
//               !getNPass().isEmpty() && 
//               !getCPass().isEmpty();
//    }

    // Add these setter methods to populate the form fields
    public void setUsername(String name) {
        userName.setText(name);
    }

    public void setNPAss(String npass) {
        newPass.setText(npass);
    }

    public void setCPass(String cpass) {
        confirmPass.setText(cpass);
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPasswordField confirmPass;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPasswordField newPass;
    private javax.swing.JTextField userName;
    // End of variables declaration//GEN-END:variables
}
