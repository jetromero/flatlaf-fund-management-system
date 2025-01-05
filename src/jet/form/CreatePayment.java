package jet.form;

import com.formdev.flatlaf.FlatClientProperties;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jet.connection.DatabaseConnection;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author jetve
 */
public class CreatePayment extends javax.swing.JPanel {

    public CreatePayment() {
        initComponents();
        populatePayeeNames();
        populateFeeCats();
        
        AutoCompleteDecorator.decorate(payName);
        payName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search Payee");
        
        AutoCompleteDecorator.decorate(payCat);
        payCat.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search Fee Category");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        payFee = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        payCat = new javax.swing.JComboBox<>();
        payName = new javax.swing.JComboBox<>();

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("Payee Name");

        jLabel2.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Fee Category");

        payFee.setEditable(false);
        payFee.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        payFee.setFocusable(false);
        payFee.setRequestFocusEnabled(false);
        payFee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payFeeActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Paid Amount");

        payCat.setEditable(true);
        payCat.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        payCat.setMaximumRowCount(5);
        payCat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Search Fee Category" }));
        payCat.setSelectedIndex(-1);

        payName.setEditable(true);
        payName.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        payName.setMaximumRowCount(5);
        payName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Search Fee Category" }));
        payName.setSelectedIndex(-1);
        payName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payNameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(100, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(payFee)
                    .addComponent(payName, 0, 300, Short.MAX_VALUE)
                    .addComponent(payCat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(100, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(payName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(payCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(payFee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void payNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_payNameActionPerformed

    private void payFeeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_payFeeActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_payFeeActionPerformed

    // Add these methods to get form data
    public String getPayeeName() {
        return payName.getSelectedItem().toString();
    }

    public String getFeeCat() {
        return payCat.getSelectedItem().toString();
    }

    public String getAmount() {
        return payFee.getText();
    }

    public boolean validateForm() {
        return !getPayeeName().isEmpty() &&
                !getFeeCat().isEmpty() &&
                !getAmount().isEmpty();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JComboBox<String> payCat;
    private javax.swing.JTextField payFee;
    private javax.swing.JComboBox<String> payName;
    // End of variables declaration//GEN-END:variables

    private void populatePayeeNames() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT student_name FROM students ORDER BY student_name ASC";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            // Clear existing items
//            payName.removeAllItems();

            // Add an empty/default option if desired
//            payName.addItem("Select Student");

            // Add all student names from the database
            while (rs.next()) {
                payName.addItem(rs.getString("student_name"));
            }

            rs.close();
            pst.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Consider adding error handling/notification here
        }
    }

    private void populateFeeCats() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT cat_name FROM categories ORDER BY cat_name ASC";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            // Clear existing items
//            payCat.removeAllItems();

            // Add an empty/default option
//            payCat.addItem("Select Fee Category");

            // Add all category names from the database
            while (rs.next()) {
                payCat.addItem(rs.getString("cat_name"));
            }

            rs.close();
            pst.close();
            conn.close();

            // Add item listener to update amount when category changes
            payCat.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                        updateAmount();
                    }
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
            // Consider adding error handling/notification here
        }
    }

    private void updateAmount() {
        String selectedCategory = (String) payCat.getSelectedItem();

        // Clear amount if "Select Fee Category" is chosen
        if (selectedCategory == null || selectedCategory.equals("Select Fee Category")) {
            payFee.setText("");
            return;
        }

        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT cat_fee FROM categories WHERE cat_name = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, selectedCategory);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int amount = rs.getInt("cat_fee");
                // Format amount with peso sign and commas
                payFee.setText(String.valueOf(amount));
            }

            rs.close();
            pst.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Consider adding error handling/notification here
        }
    }

    // Add this method to the CreatePayment class
    public boolean isPaymentExists() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM payments WHERE payee_name = ? AND pay_fee = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, getPayeeName());
            pst.setString(2, getFeeCat());
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }

            rs.close();
            pst.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
