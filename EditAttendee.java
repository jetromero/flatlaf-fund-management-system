package jet.form;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jet.connection.DatabaseConnection;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import raven.toast.Notifications;

/**
 *
 * @author jetve
 */
public class EditAttendee extends javax.swing.JPanel {

    public EditAttendee() {
        initComponents();
        populateEventNames();
        populateAttendeeNames();
        timePicker1.setEditor(attendTimeIn);
        datePicker1.setEditorIcon(new FlatSVGIcon("jet/images/calendar.svg"));
        timePicker1.setEditorIcon(new FlatSVGIcon("jet/images/clock.svg"));
        
        AutoCompleteDecorator.decorate(attendName);
        attendName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search Attendee");
    }
   

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        timePicker1 = new raven.datetime.component.time.TimePicker();
        timePicker2 = new raven.datetime.component.time.TimePicker();
        datePicker1 = new raven.datetime.component.date.DatePicker();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        attendYear = new javax.swing.JComboBox<>();
        attendStatus = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        attendTimeIn = new javax.swing.JFormattedTextField();
        attendName = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        eventName = new javax.swing.JComboBox<>();

        timePicker1.setEditorIcon(new javax.swing.ImageIcon(getClass().getResource("/jet/images/add.png"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("Attendee Name");

        jLabel2.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Status");

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Year Level");

        attendYear.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        attendYear.setMaximumRowCount(5);
        attendYear.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1st Year", "2nd Year", "3rd Year", "4th Year" }));
        attendYear.setEnabled(false);
        attendYear.setFocusable(false);
        attendYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attendYearActionPerformed(evt);
            }
        });

        attendStatus.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        attendStatus.setMaximumRowCount(5);
        attendStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Present", "Absent" }));
        attendStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attendStatusActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("Time In");

        attendTimeIn.setPreferredSize(new java.awt.Dimension(68, 29));

        attendName.setEditable(true);
        attendName.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        attendName.setMaximumRowCount(5);
        attendName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        attendName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attendNameActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel6.setText("Event");

        eventName.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        eventName.setMaximumRowCount(5);
        eventName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        eventName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eventNameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(100, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(attendTimeIn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(attendName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(attendStatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(attendYear, 0, 300, Short.MAX_VALUE)
                    .addComponent(eventName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(100, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(50, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(eventName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(attendName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(attendYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(attendStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(attendTimeIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(50, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void attendStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attendStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_attendStatusActionPerformed

    private void attendNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attendNameActionPerformed
        Object selectedItem = attendName.getSelectedItem();
        if (selectedItem != null) {
            String selectedStudent = selectedItem.toString();
            if (!selectedStudent.equals("")) {
                populateYearLevel(selectedStudent);
            } else {
                attendYear.setSelectedItem("Select Year Level");
            }
        }
    }//GEN-LAST:event_attendNameActionPerformed

    private void eventNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eventNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_eventNameActionPerformed

    private void attendYearActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_stuYearActionPerformed

    }

    // Add these methods to get form data
    public String getEventName() {
        return eventName.getSelectedItem().toString();
    }
    
    public String getAttendeeName() {
        return attendName.getSelectedItem().toString();
    }

    public String getStatus() {
        return attendStatus.getSelectedItem().toString();
    }

    public String getYear() {
        return attendYear.getSelectedItem().toString();
    }
    
    public String getDate() {
        return java.time.LocalDate.now().toString();
    }
    
    public String getIn() {
        return attendTimeIn.getText();
    }

    public boolean validateForm() {
        return !getEventName().equals("Select Event") &&
                !getAttendeeName().isEmpty() &&
                !getStatus().equals("Select Status") &&
                !getYear().isEmpty() &&
                !getIn().isEmpty();
    }
    
    public void setEventName(String event) {
        eventName.setSelectedItem(event);
    }

    public void setAttendeeName(String name) {
        attendName.setSelectedItem(name);
    }
    
    public void setStatus(String stat) {
        attendStatus.setSelectedItem(stat);
    }
    
    public void setYear(String year) {
        attendYear.setSelectedItem(year);
    }  
    
    public void setIn(String in) {
        attendTimeIn.setText(in);
    } 
    
    private void populateEventNames() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT event_name FROM events ORDER BY event_name ASC";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            // Clear existing items
            eventName.removeAllItems();

            // Add an empty/default option if desired
            eventName.addItem("Select Event");

            // Add all student names from the database
            while (rs.next()) {
                eventName.addItem(rs.getString("event_name"));
            }

            rs.close();
            pst.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Consider adding error handling/notification here
        }
    }
    
    private void populateAttendeeNames() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT student_name FROM students ORDER BY student_name ASC";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            // Clear existing items
            attendName.removeAllItems();

            // Add all student names from the database
            while (rs.next()) {
                attendName.addItem(rs.getString("student_name"));
            }

            rs.close();
            pst.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }    
    
    private void populateYearLevel(String selectedStudent) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT year_level FROM students WHERE student_name = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, selectedStudent);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String yearLevel = rs.getString("year_level");
                attendYear.setSelectedItem(yearLevel);
            }

            rs.close();
            pst.close();
            conn.close();
        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR,
                "Error loading year level: " + e.getMessage());
        }
    }
    
    public boolean isAttendeeExists(int currentAttendeeId) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(*) as count FROM attendees WHERE event = ? AND attendee_name = ? AND attendee_id != ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, eventName.getSelectedItem().toString());
            pst.setString(2, attendName.getSelectedItem().toString());
            pst.setInt(3, currentAttendeeId);
            ResultSet rs = pst.executeQuery();

            boolean exists = false;
            if (rs.next()) {
                exists = rs.getInt("count") > 0;
            }

            rs.close();
            pst.close();
            conn.close();
            return exists;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> attendName;
    private javax.swing.JComboBox<String> attendStatus;
    private javax.swing.JFormattedTextField attendTimeIn;
    private javax.swing.JComboBox<String> attendYear;
    private raven.datetime.component.date.DatePicker datePicker1;
    private javax.swing.JComboBox<String> eventName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private raven.datetime.component.time.TimePicker timePicker1;
    private raven.datetime.component.time.TimePicker timePicker2;
    // End of variables declaration//GEN-END:variables
}
