package jet.form;

/**
 *
 * @author jetve
 */
public class CreateFee extends javax.swing.JPanel {

    public CreateFee() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        catName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        catFee = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        catDes = new javax.swing.JTextArea();

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("Category Name");

        catName.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Fee Amount");

        catFee.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        catFee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                catFeeActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Description");

        catDes.setColumns(20);
        catDes.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        catDes.setLineWrap(true);
        catDes.setRows(5);
        catDes.setWrapStyleWord(true);
        jScrollPane1.setViewportView(catDes);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1)
                                        .addComponent(catFee)
                                        .addComponent(catName, javax.swing.GroupLayout.DEFAULT_SIZE, 300,
                                                Short.MAX_VALUE))
                                .addGap(50, 50, 50)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(catName, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(catFee, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addComponent(jLabel3))
                                        .addGroup(layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 120,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(25, Short.MAX_VALUE)));
    }// </editor-fold>//GEN-END:initComponents

    private void catFeeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_catFeeActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_catFeeActionPerformed

    // Add these methods to get form data
    public String getCategoryName() {
        return catName.getText();
    }

    public String getFeeAmount() {
        return catFee.getText();
    }

    public String getDescription() {
        return catDes.getText();
    }

    public boolean validateForm() {
        return !getCategoryName().isEmpty() &&
                !getFeeAmount().isEmpty() &&
                !getDescription().isEmpty();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea catDes;
    private javax.swing.JTextField catFee;
    private javax.swing.JTextField catName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
