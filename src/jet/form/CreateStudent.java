package jet.form;

/**
 *
 * @author jetve
 */
public class CreateStudent extends javax.swing.JPanel {

    public CreateStudent() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        stuName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        stuEmail = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        stuYear = new javax.swing.JComboBox<>();

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("Student Name");

        stuName.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Email");

        stuEmail.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        stuEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuEmailActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Year Level");

        stuYear.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        stuYear.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1st Year", "2nd Year", "3rd Year", "4th Year" }));
        stuYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuYearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(stuEmail)
                    .addComponent(stuName, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addComponent(stuYear, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(50, 50, 50))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(stuName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stuEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(stuYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void stuEmailActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_stuEmailActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_stuEmailActionPerformed

    private void stuYearActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_stuYearActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_stuYearActionPerformed

    // Add these methods to get form data
    public String getStudentName() {
        return stuName.getText();
    }

    public String getEmail() {
        return stuEmail.getText();
    }

    public String getYear() {
        return stuYear.getSelectedItem().toString();
    }

    public boolean validateForm() {
        return !getStudentName().isEmpty() &&
                !getEmail().isEmpty() &&
                !getYear().isEmpty();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField stuEmail;
    private javax.swing.JTextField stuName;
    private javax.swing.JComboBox<String> stuYear;
    // End of variables declaration//GEN-END:variables
}
