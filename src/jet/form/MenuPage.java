package jet.form;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.table.DefaultTableModel;
import jet.table.CheckBoxTableHeaderRenderer;
import jet.table.TableHeaderAlignment;
import jet.table.TableHeaderAlignment1;
import raven.popup.DefaultOption;
import raven.popup.GlassPanePopup;
import raven.popup.component.SimplePopupBorder;
import raven.toast.Notifications;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import jet.connection.DatabaseConnection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import javax.swing.Timer;
import jet.chart.ModelChart;



public class MenuPage extends javax.swing.JFrame {

    public MenuPage() {
        initComponents();
        initializeCategoryTable();
        initializeStudentTable();
        initializePaymentTable();
        updateTotalCollectedFees();
        populateReportFeeCatComboBox();
        setReportMonthToCurrent();
        init();
        
        populateRecentTable();

        Timer timer = new Timer(60000, e -> populateRecentTable()); // Refresh every 1 minute
        timer.start();
        
        updateStudentCountLabels();
        initializeCurveChart();
        curveChart.start();
    }
    
    private void updateCurveChartData() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            // Query to get total payments per month for the current year
            String sql = "SELECT MONTH(pay_date) as month, SUM(pay_amount) as total " +
                        "FROM payments " +
                        "WHERE YEAR(pay_date) = YEAR(CURRENT_DATE()) " +
                        "GROUP BY MONTH(pay_date) " +
                        "ORDER BY month";

            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            // Initialize array with zeros for all months
            double[] monthlyTotals = new double[12];

            // Fill in the actual totals from database
            while (rs.next()) {
                int month = rs.getInt("month") - 1; // Convert 1-based month to 0-based index
                double total = rs.getDouble("total");
                monthlyTotals[month] = total;
            }

            // Update the curve chart with the real data
            curveChart.clear();
            for (int i = 0; i < 12; i++) {
                curveChart.addData(new ModelChart(getMonthName(i + 1), new double[]{monthlyTotals[i]}));
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, 
                "Error updating chart data: " + e.getMessage());
        }
    }

    private String getMonthName(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    // Call this method in your constructor or initialization code
    private void initializeCurveChart() {
        curveChart.addLegend("Fee Collections", new Color(163, 230, 53), new Color(26, 46, 5));
        updateCurveChartData();
    }   

    private void init() {
        dashBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                    String selectedItem = (String) dashBox.getSelectedItem();
                    // Always set back to index 0
                    dashBox.setSelectedIndex(0);

                    switch (selectedItem) {
                        case "Logout":
                            int choice = javax.swing.JOptionPane.showConfirmDialog(
                                MenuPage.this,
                                "Are you sure you want to logout?",
                                "Confirmation",
                                javax.swing.JOptionPane.YES_NO_OPTION,
                                javax.swing.JOptionPane.QUESTION_MESSAGE
                            );

                            if (choice == javax.swing.JOptionPane.YES_OPTION) {
                                new LoginPage().setVisible(true);
                                dispose();
                            }
                            break;

                        case "Manage Account":
                            ChangePass changePass = new ChangePass();
                            DefaultOption option = new DefaultOption() {
                                @Override
                                public boolean closeWhenClickOutside() {
                                    return true;
                                }
                            };
                            String[] actions = { "Cancel", "Save" };
                            GlassPanePopup.showPopup(new SimplePopupBorder(changePass, "Change Password", actions, (pc, i) -> {
                                if (i == 1) {
                                    // Save button clicked
                                    if (!changePass.validateForm()) {
                                        Notifications.getInstance().show(Notifications.Type.ERROR, "Please fill in all fields");
                                        return;
                                    }

                                    if (changePass.updatePassword()) {
                                        pc.closePopup();
                                        Notifications.getInstance().show(Notifications.Type.SUCCESS, "Password updated successfully");
                                    }
                                } else {
                                    pc.closePopup();
                                }
                            }), option);
                            break;
                    }
                }
            }
        });
        
        
        GlassPanePopup.install(this);
        Notifications.getInstance().setJFrame(this);

        logoPanel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:20;"
                + "background:$Table.background;");

        logoText.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                new FlatSVGIcon("jet/images/units_logo.svg"));

        logoText.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:20;"
                + "margin:10,10,10,10;"
                + "borderWidth:2;"
                + "borderColor:#FFFFFF");

        navPanel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:20;"
                + "background:$Table.background;");

        // ------------------------------- Category Panel
        // -------------------------------
        catPanel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:20;"
                + "background:$Table.background;");

        catTable.getTableHeader().putClientProperty(FlatClientProperties.STYLE, ""
                + "height:30;"
                + "hoverBackground:null;"
                + "pressedBackground:null;"
                + "separatorColor:$TableHeader.background;"
                + "font:bold;");

        catTable.putClientProperty(FlatClientProperties.STYLE, ""
                + "rowHeight:30;"
                + "showHorizontalLines:true;"
                + "intercellSpacing:0,1;"
                + "cellFocusColor:#A7C957;"
                + "selectionBackground:#A7C957;"
                + "selectionForeground:#FFFFFF;");

        catScroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, ""
                + "trackArc:999;"
                + "trackInsets:3,3,3,3;"
                + "thumbInsets:3,3,3,3;"
                + "background:$Table.background;");

        catLabel.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:bold +5;");

        catSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search...");
        catSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                new FlatSVGIcon("jet/images/search.svg"));
        catSearch.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10;"
                + "borderWidth:0;"
                + "innerFocusWidth:0;"
                + "margin:5,20,5,20;"
                + "background:$Panel.background");

        catTable.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxTableHeaderRenderer(catTable, 0));
        catTable.getTableHeader().setDefaultRenderer(new TableHeaderAlignment(catTable));

        catTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        // ------------------------------- Student Panel -------------------------------
        studentPanel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:20;"
                + "background:$Table.background;");

        studentTable.getTableHeader().putClientProperty(FlatClientProperties.STYLE, ""
                + "height:30;"
                + "hoverBackground:null;"
                + "pressedBackground:null;"
                + "separatorColor:$TableHeader.background;"
                + "font:bold;");

        studentTable.putClientProperty(FlatClientProperties.STYLE, ""
                + "rowHeight:30;"
                + "showHorizontalLines:true;"
                + "intercellSpacing:0,1;"
                + "cellFocusColor:#A7C957;"
                + "selectionBackground:#A7C957;"
                + "selectionForeground:#FFFFFF;");

        studentScroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, ""
                + "trackArc:999;"
                + "trackInsets:3,3,3,3;"
                + "thumbInsets:3,3,3,3;"
                + "background:$Table.background;");

        studentLabel.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:bold +5;");

        studentSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search...");
        studentSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                new FlatSVGIcon("jet/images/search.svg"));
        studentSearch.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10;"
                + "borderWidth:0;"
                + "innerFocusWidth:0;"
                + "margin:5,20,5,20;"
                + "background:$Panel.background");

        studentTable.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxTableHeaderRenderer(studentTable, 0));
        studentTable.getTableHeader().setDefaultRenderer(new TableHeaderAlignment(studentTable));

        studentTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        // ------------------------------- Payment Panel -------------------------------
        payPanel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:20;"
                + "background:$Table.background;");

        payTable.getTableHeader().putClientProperty(FlatClientProperties.STYLE, ""
                + "height:30;"
                + "hoverBackground:null;"
                + "pressedBackground:null;"
                + "separatorColor:$TableHeader.background;"
                + "font:bold;");

        payTable.putClientProperty(FlatClientProperties.STYLE, ""
                + "rowHeight:30;"
                + "showHorizontalLines:true;"
                + "intercellSpacing:0,1;"
                + "cellFocusColor:#A7C957;"
                + "selectionBackground:#A7C957;"
                + "selectionForeground:#FFFFFF;");

        payScroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, ""
                + "trackArc:999;"
                + "trackInsets:3,3,3,3;"
                + "thumbInsets:3,3,3,3;"
                + "background:$Table.background;");

        payLabel.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:bold +5;");

        totalLabel.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:bold +2;");

        totalOutput.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:bold +2;"
                + "Foreground:#A7C957;");

        paySearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search...");
        paySearch.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                new FlatSVGIcon("jet/images/search.svg"));
        paySearch.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10;"
                + "borderWidth:0;"
                + "innerFocusWidth:0;"
                + "margin:5,20,5,20;"
                + "background:$Panel.background");

        payTable.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxTableHeaderRenderer(payTable, 0));
        payTable.getTableHeader().setDefaultRenderer(new TableHeaderAlignment(payTable));

        payTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        // ------------------------------- Payment Panel -------------------------------
        reportPanel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:20;"
                + "background:$Table.background;");

        reportTable.getTableHeader().putClientProperty(FlatClientProperties.STYLE, ""
                + "height:30;"
                + "hoverBackground:null;"
                + "pressedBackground:null;"
                + "separatorColor:$TableHeader.background;"
                + "font:bold;");

        reportTable.putClientProperty(FlatClientProperties.STYLE, ""
                + "rowHeight:30;"
                + "showHorizontalLines:true;"
                + "intercellSpacing:0,1;"
                + "cellFocusColor:#A7C957;"
                + "selectionBackground:#A7C957;"
                + "selectionForeground:#FFFFFF;");

        reportScroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, ""
                + "trackArc:999;"
                + "trackInsets:3,3,3,3;"
                + "thumbInsets:3,3,3,3;"
                + "background:$Table.background;");

        reportLabel.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:bold +5;");

        // reportTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        reportTable.getTableHeader().setDefaultRenderer(new TableHeaderAlignment1(reportTable));
        
        // ------------------------------- Dashboard Panel -------------------------------
        dashPanel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:20;"
                + "background:$Table.background;");
        
        dashTable.getTableHeader().putClientProperty(FlatClientProperties.STYLE, ""
                + "height:30;"
                + "hoverBackground:null;"
                + "pressedBackground:null;"
                + "separatorColor:$TableHeader.background;"
                + "Foreground:#9CA3AF;"
                + "font:bold;");
        
        r1.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10;"
                + "background:#d9f99d;");
        
        r2.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10;"
                + "background:#d9f99d;");

        r3.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10;"
                + "background:#d9f99d;");

        r4.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10;"
                + "background:#d9f99d;");        
        
        recentPanel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:20;"
                + "background:$Panel.background;"); 

        dashTable.putClientProperty(FlatClientProperties.STYLE, ""
                + "rowHeight:30;"
                + "showHorizontalLines:true;"
                + "intercellSpacing:0,1;"
                + "cellFocusColor:#A7C957;"
                + "selectionBackground:#A7C957;"
                + "selectionForeground:#FFFFFF;");

        dashScroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, ""
                + "trackArc:999;"
                + "trackInsets:3,3,3,3;"
                + "thumbInsets:3,3,3,3;"
                + "background:$Table.background;");        
        
        totalPanel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:20;"
                + "background:#365314;");
        
        totalIcons.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                new FlatSVGIcon("jet/images/receive.svg"));
        totalIcons.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10;"
                + "borderWidth:0;"
                + "innerFocusWidth:0;"
                + "margin:5,20,5,20;"
                + "background:#365314");
        
        recordStuPanel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:20;"
                + "background:$Panel.background;"); 
        
        lineChartPanel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:20;"
                + "background:$Panel.background;"); 
        
        dashLabel.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:bold +5;");  
        
        dashStuLabel.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:bold +4;"); 
        
        recentLabel.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:bold +2;"); 
        
        dashTable.getTableHeader().setDefaultRenderer(new TableHeaderAlignment1(dashTable));
        
        rStud1.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:bold +5;"
                + "foreground:#1a2e05;"); 
        
        rStud2.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:bold +5;"
                + "foreground:#1a2e05;"); 
        
        rStud3.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:bold +5;"
                + "foreground:#1a2e05;"); 
        
        rStud4.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:bold +5;"
                + "foreground:#1a2e05;"); 
        
        chartLabel.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:bold +2;"); 
        
        dashBox.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10;"
                + "borderWidth:0;"
                + "innerFocusWidth:0;"
                + "focusColor:null;"
                + "foreground:#9CA3AF;");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        logoPanel = new javax.swing.JPanel();
        logoText = new javax.swing.JTextField();
        navPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        dashNav = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        feesNav = new javax.swing.JButton();
        catNav = new javax.swing.JButton();
        studentNav = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        reportNav = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        dashPanel = new javax.swing.JPanel();
        dashLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        totalPanel = new javax.swing.JPanel();
        totalIcons = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        dashTotal = new javax.swing.JLabel();
        recordStuPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        dashStuLabel = new javax.swing.JLabel();
        r4 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        rStud4 = new javax.swing.JLabel();
        r2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        rStud2 = new javax.swing.JLabel();
        r1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        rStud1 = new javax.swing.JLabel();
        r3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        rStud3 = new javax.swing.JLabel();
        lineChartPanel = new javax.swing.JPanel();
        panelGradient1 = new jet.panel.PanelGradient();
        chartLabel = new javax.swing.JLabel();
        curveChart = new jet.chart.CurveChart();
        recentPanel = new javax.swing.JPanel();
        dashScroll = new javax.swing.JScrollPane();
        dashTable = new javax.swing.JTable();
        jSeparator7 = new javax.swing.JSeparator();
        recentLabel = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        dashBox = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        catPanel = new javax.swing.JPanel();
        catScroll = new javax.swing.JScrollPane();
        catTable = new javax.swing.JTable();
        jSeparator2 = new javax.swing.JSeparator();
        catSearch = new javax.swing.JTextField();
        catLabel = new javax.swing.JLabel();
        catEdit = new jet.swing.ButtonAction();
        catNew = new jet.swing.ButtonAction();
        catDelete = new jet.swing.ButtonAction();
        jPanel3 = new javax.swing.JPanel();
        studentPanel = new javax.swing.JPanel();
        studentScroll = new javax.swing.JScrollPane();
        studentTable = new javax.swing.JTable();
        jSeparator3 = new javax.swing.JSeparator();
        studentSearch = new javax.swing.JTextField();
        studentLabel = new javax.swing.JLabel();
        studentEdit = new jet.swing.ButtonAction();
        studentNew = new jet.swing.ButtonAction();
        studentDelete = new jet.swing.ButtonAction();
        jPanel4 = new javax.swing.JPanel();
        payPanel = new javax.swing.JPanel();
        payScroll = new javax.swing.JScrollPane();
        payTable = new javax.swing.JTable();
        jSeparator4 = new javax.swing.JSeparator();
        paySearch = new javax.swing.JTextField();
        payLabel = new javax.swing.JLabel();
        payEdit = new jet.swing.ButtonAction();
        payNew = new jet.swing.ButtonAction();
        payDelete = new jet.swing.ButtonAction();
        jPanel5 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        reportPanel = new javax.swing.JPanel();
        reportScroll = new javax.swing.JScrollPane();
        reportTable = new javax.swing.JTable();
        jSeparator6 = new javax.swing.JSeparator();
        reportLabel = new javax.swing.JLabel();
        reportDownload = new jet.swing.ButtonAction();
        reportMonth = new javax.swing.JComboBox<>();
        reportFeeCat = new javax.swing.JComboBox<>();
        totalLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        totalOutput = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logoPanel.setBackground(new java.awt.Color(255, 255, 255));

        logoText.setBackground(new java.awt.Color(246, 246, 246));
        logoText.setFont(new java.awt.Font("SansSerif", 1, 40)); // NOI18N
        logoText.setText("UNITS");

        javax.swing.GroupLayout logoPanelLayout = new javax.swing.GroupLayout(logoPanel);
        logoPanel.setLayout(logoPanelLayout);
        logoPanelLayout.setHorizontalGroup(
            logoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(logoText, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
        );
        logoPanelLayout.setVerticalGroup(
            logoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(logoText, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
        );

        getContentPane().add(logoPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 7, 220, 110));

        navPanel.setBackground(new java.awt.Color(255, 255, 255));
        navPanel.setPreferredSize(new java.awt.Dimension(250, 720));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(156, 163, 175));
        jLabel1.setText("Main");
        jLabel1.setOpaque(true);

        dashNav.setBackground(new java.awt.Color(167, 201, 87));
        dashNav.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        dashNav.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jet/images/icons8-dashboard-24.png"))); // NOI18N
        dashNav.setText("Dashboard");
        dashNav.setBorder(feesNav.getBorder());
        dashNav.setBorderPainted(false);
        dashNav.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        dashNav.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        dashNav.setIconTextGap(10);
        dashNav.setMargin(new java.awt.Insets(3, 14, 3, 14));
        dashNav.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                dashNavMouseDragged(evt);
            }
        });
        dashNav.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                dashNavMouseWheelMoved(evt);
            }
        });
        dashNav.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dashNavActionPerformed(evt);
            }
        });

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(156, 163, 175));
        jLabel2.setText("General");
        jLabel2.setOpaque(true);

        feesNav.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        feesNav.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jet/images/icons8-peso-24.png"))); // NOI18N
        feesNav.setText("Payments");
        feesNav.setBorder(feesNav.getBorder());
        feesNav.setBorderPainted(false);
        feesNav.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        feesNav.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        feesNav.setIconTextGap(10);
        feesNav.setMargin(new java.awt.Insets(3, 14, 3, 14));
        feesNav.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                feesNavActionPerformed(evt);
            }
        });

        catNav.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        catNav.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jet/images/icons8-category-24.png"))); // NOI18N
        catNav.setText("Fees");
        catNav.setBorder(feesNav.getBorder());
        catNav.setBorderPainted(false);
        catNav.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        catNav.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        catNav.setIconTextGap(10);
        catNav.setMargin(new java.awt.Insets(3, 14, 3, 14));
        catNav.setOpaque(true);
        catNav.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                catNavActionPerformed(evt);
            }
        });

        studentNav.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        studentNav.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jet/images/icons8-student-24.png"))); // NOI18N
        studentNav.setText("Students");
        studentNav.setBorder(feesNav.getBorder());
        studentNav.setBorderPainted(false);
        studentNav.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        studentNav.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        studentNav.setIconTextGap(10);
        studentNav.setMargin(new java.awt.Insets(3, 14, 3, 14));
        studentNav.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentNavActionPerformed(evt);
            }
        });

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(156, 163, 175));
        jLabel3.setText("Support");
        jLabel3.setOpaque(true);

        reportNav.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        reportNav.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jet/images/icons8-report-24.png"))); // NOI18N
        reportNav.setText("Reports");
        reportNav.setBorder(feesNav.getBorder());
        reportNav.setBorderPainted(false);
        reportNav.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        reportNav.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        reportNav.setIconTextGap(10);
        reportNav.setMargin(new java.awt.Insets(3, 14, 3, 14));
        reportNav.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportNavActionPerformed(evt);
            }
        });

        jToggleButton1.setText("jToggleButton1");

        jLabel5.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(156, 163, 175));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("@ 2024 UNITS");

        javax.swing.GroupLayout navPanelLayout = new javax.swing.GroupLayout(navPanel);
        navPanel.setLayout(navPanelLayout);
        navPanelLayout.setHorizontalGroup(
            navPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, navPanelLayout.createSequentialGroup()
                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(94, 94, 94))
            .addGroup(navPanelLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(navPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dashNav, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(catNav, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(studentNav, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(feesNav, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(reportNav, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(navPanelLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(8, 8, 8))
            .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(navPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
            .addGroup(navPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        navPanelLayout.setVerticalGroup(
            navPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(navPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jToggleButton1))
                .addGap(8, 8, 8)
                .addComponent(dashNav, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(catNav, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(studentNav, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(feesNav, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reportNav, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 193, Short.MAX_VALUE)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jLabel5)
                .addContainerGap())
        );

        getContentPane().add(navPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 129, 220, 560));

        dashPanel.setBackground(new java.awt.Color(255, 255, 255));
        dashPanel.setPreferredSize(new java.awt.Dimension(998, 682));

        dashLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        dashLabel.setText("DASHBOARD");

        jLabel4.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(156, 163, 175));
        jLabel4.setText("Here's your analytic details");

        totalIcons.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalIconsActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(163, 229, 53));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel6.setText("Total Collected Fees");
        jLabel6.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        dashTotal.setFont(new java.awt.Font("SansSerif", 0, 36)); // NOI18N
        dashTotal.setForeground(new java.awt.Color(255, 255, 255));
        dashTotal.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        dashTotal.setText("₱ 0.00");
        dashTotal.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        dashTotal.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout totalPanelLayout = new javax.swing.GroupLayout(totalPanel);
        totalPanel.setLayout(totalPanelLayout);
        totalPanelLayout.setHorizontalGroup(
            totalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(totalIcons, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(totalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dashTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        totalPanelLayout.setVerticalGroup(
            totalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(totalIcons)
            .addGroup(totalPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel6)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(dashTotal)
                .addContainerGap())
        );

        jLabel7.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel7.setText("Recorded");
        jLabel7.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        dashStuLabel.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        dashStuLabel.setText("Students");
        dashStuLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        dashStuLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        r4.setBackground(new java.awt.Color(0, 102, 51));

        jLabel13.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("4th");

        rStud4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rStud4.setText("999");

        javax.swing.GroupLayout r4Layout = new javax.swing.GroupLayout(r4);
        r4.setLayout(r4Layout);
        r4Layout.setHorizontalGroup(
            r4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(r4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rStud4, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE))
                .addContainerGap())
        );
        r4Layout.setVerticalGroup(
            r4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addGap(0, 0, 0)
                .addComponent(rStud4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        r2.setBackground(new java.awt.Color(0, 102, 51));

        jLabel11.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("2nd");

        rStud2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rStud2.setText("999");

        javax.swing.GroupLayout r2Layout = new javax.swing.GroupLayout(r2);
        r2.setLayout(r2Layout);
        r2Layout.setHorizontalGroup(
            r2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(r2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .addComponent(rStud2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        r2Layout.setVerticalGroup(
            r2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addGap(0, 0, 0)
                .addComponent(rStud2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        r1.setBackground(new java.awt.Color(0, 102, 51));

        jLabel9.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("1st");
        jLabel9.setToolTipText("");
        jLabel9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        rStud1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        rStud1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rStud1.setText("999");

        javax.swing.GroupLayout r1Layout = new javax.swing.GroupLayout(r1);
        r1.setLayout(r1Layout);
        r1Layout.setHorizontalGroup(
            r1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(r1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rStud1, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE))
                .addContainerGap())
        );
        r1Layout.setVerticalGroup(
            r1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addGap(0, 0, 0)
                .addComponent(rStud1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        r3.setBackground(new java.awt.Color(0, 102, 51));

        jLabel12.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("3rd");

        rStud3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rStud3.setText("999");

        javax.swing.GroupLayout r3Layout = new javax.swing.GroupLayout(r3);
        r3.setLayout(r3Layout);
        r3Layout.setHorizontalGroup(
            r3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(r3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .addComponent(rStud3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        r3Layout.setVerticalGroup(
            r3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addGap(0, 0, 0)
                .addComponent(rStud3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout recordStuPanelLayout = new javax.swing.GroupLayout(recordStuPanel);
        recordStuPanel.setLayout(recordStuPanelLayout);
        recordStuPanelLayout.setHorizontalGroup(
            recordStuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recordStuPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(recordStuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(recordStuPanelLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(dashStuLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(r1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(r2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(r3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(r4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        recordStuPanelLayout.setVerticalGroup(
            recordStuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recordStuPanelLayout.createSequentialGroup()
                .addGroup(recordStuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(recordStuPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addGap(0, 0, 0)
                        .addComponent(dashStuLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(recordStuPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(r4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, recordStuPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(r2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, recordStuPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(r1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, recordStuPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(r3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        panelGradient1.setBackground(new java.awt.Color(246, 246, 246));
        panelGradient1.setColorGradient(new java.awt.Color(246, 246, 246));

        chartLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        chartLabel.setText("COLLECTED FEE DATA");
        panelGradient1.add(chartLabel);
        chartLabel.setBounds(20, 10, 210, 19);

        curveChart.setForeground(new java.awt.Color(0, 0, 0));
        curveChart.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        panelGradient1.add(curveChart);
        curveChart.setBounds(0, 14, 980, 240);

        javax.swing.GroupLayout lineChartPanelLayout = new javax.swing.GroupLayout(lineChartPanel);
        lineChartPanel.setLayout(lineChartPanelLayout);
        lineChartPanelLayout.setHorizontalGroup(
            lineChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelGradient1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        lineChartPanelLayout.setVerticalGroup(
            lineChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelGradient1, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
        );

        dashScroll.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        dashTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "PAYEE NAME", "FEE CATEGORY", "AMOUNT", "TIME ADDED"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dashScroll.setViewportView(dashTable);
        if (dashTable.getColumnModel().getColumnCount() > 0) {
            dashTable.getColumnModel().getColumn(0).setMaxWidth(50);
            dashTable.getColumnModel().getColumn(1).setPreferredWidth(150);
            dashTable.getColumnModel().getColumn(2).setPreferredWidth(150);
            dashTable.getColumnModel().getColumn(3).setPreferredWidth(100);
            dashTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        }

        jSeparator7.setForeground(new java.awt.Color(54, 83, 20));

        recentLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        recentLabel.setText("RECENT PAYMENTS");

        jLabel8.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(156, 163, 175));
        jLabel8.setText("Last 7 Days");

        javax.swing.GroupLayout recentPanelLayout = new javax.swing.GroupLayout(recentPanel);
        recentPanel.setLayout(recentPanelLayout);
        recentPanelLayout.setHorizontalGroup(
            recentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dashScroll)
            .addComponent(jSeparator7)
            .addGroup(recentPanelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(recentLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(20, 20, 20))
        );
        recentPanelLayout.setVerticalGroup(
            recentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recentPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(recentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(recentLabel)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(dashScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        dashBox.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        dashBox.setMaximumRowCount(3);
        dashBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "UNITS ADMIN", "Manage Account", "Logout" }));
        dashBox.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        dashBox.setOpaque(true);

        javax.swing.GroupLayout dashPanelLayout = new javax.swing.GroupLayout(dashPanel);
        dashPanel.setLayout(dashPanelLayout);
        dashPanelLayout.setHorizontalGroup(
            dashPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashPanelLayout.createSequentialGroup()
                .addGroup(dashPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dashPanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(dashPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dashLabel)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dashBox, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10))
                    .addGroup(dashPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(dashPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lineChartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(dashPanelLayout.createSequentialGroup()
                                .addComponent(totalPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(recordStuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(recentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(10, 10, 10))
        );
        dashPanelLayout.setVerticalGroup(
            dashPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(dashPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(dashPanelLayout.createSequentialGroup()
                        .addComponent(dashLabel)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel4))
                    .addComponent(dashBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dashPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(recordStuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(totalPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 78, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addComponent(lineChartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(recentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dashPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(dashPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab1", jPanel1);

        catPanel.setBackground(new java.awt.Color(255, 255, 255));
        catPanel.setPreferredSize(new java.awt.Dimension(998, 686));

        catScroll.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        catTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SELECT", "#", "FEE CATEGORY", "FEE AMOUNT", "DESCRIPTION", "DATE"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        catTable.getTableHeader().setReorderingAllowed(false);
        catScroll.setViewportView(catTable);
        if (catTable.getColumnModel().getColumnCount() > 0) {
            catTable.getColumnModel().getColumn(0).setMaxWidth(50);
            catTable.getColumnModel().getColumn(1).setMaxWidth(40);
            catTable.getColumnModel().getColumn(2).setPreferredWidth(100);
            catTable.getColumnModel().getColumn(3).setPreferredWidth(120);
            catTable.getColumnModel().getColumn(4).setPreferredWidth(200);
            catTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        }

        jSeparator2.setForeground(new java.awt.Color(54, 83, 20));

        catSearch.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        catSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                catSearchActionPerformed(evt);
            }
        });
        catSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                catSearchKeyReleased(evt);
            }
        });

        catLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        catLabel.setText("MANAGE FEES");

        catEdit.setText("Edit");
        catEdit.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        catEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                catEditActionPerformed(evt);
            }
        });

        catNew.setText("New");
        catNew.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        catNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                catNewActionPerformed(evt);
            }
        });

        catDelete.setText("Delete");
        catDelete.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        catDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                catDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout catPanelLayout = new javax.swing.GroupLayout(catPanel);
        catPanel.setLayout(catPanelLayout);
        catPanelLayout.setHorizontalGroup(
            catPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(catScroll)
            .addComponent(jSeparator2)
            .addGroup(catPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(catPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(catSearch)
                    .addComponent(catLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 384, Short.MAX_VALUE)
                .addComponent(catNew, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(catEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(catDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        catPanelLayout.setVerticalGroup(
            catPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(catPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(catLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(catPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(catSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(catEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(catNew, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(catDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(catScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(catPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(catPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 682, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab2", jPanel2);

        studentPanel.setBackground(new java.awt.Color(255, 255, 255));
        studentPanel.setPreferredSize(new java.awt.Dimension(998, 682));

        studentScroll.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        studentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SELECT", "#", "STUDENT NAME", "EMAIL", "YEAR LEVEL", "DATE"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        studentTable.getTableHeader().setReorderingAllowed(false);
        studentScroll.setViewportView(studentTable);
        if (studentTable.getColumnModel().getColumnCount() > 0) {
            studentTable.getColumnModel().getColumn(0).setMaxWidth(50);
            studentTable.getColumnModel().getColumn(1).setMaxWidth(40);
            studentTable.getColumnModel().getColumn(2).setPreferredWidth(150);
            studentTable.getColumnModel().getColumn(3).setPreferredWidth(250);
            studentTable.getColumnModel().getColumn(4).setPreferredWidth(100);
            studentTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        }

        jSeparator3.setForeground(new java.awt.Color(54, 83, 20));

        studentSearch.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        studentSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentSearchActionPerformed(evt);
            }
        });
        studentSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                studentSearchKeyReleased(evt);
            }
        });

        studentLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        studentLabel.setText("MANAGE STUDENTS");

        studentEdit.setText("Edit");
        studentEdit.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        studentEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentEditActionPerformed(evt);
            }
        });

        studentNew.setText("New");
        studentNew.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        studentNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentNewActionPerformed(evt);
            }
        });

        studentDelete.setText("Delete");
        studentDelete.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        studentDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout studentPanelLayout = new javax.swing.GroupLayout(studentPanel);
        studentPanel.setLayout(studentPanelLayout);
        studentPanelLayout.setHorizontalGroup(
            studentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(studentScroll)
            .addComponent(jSeparator3)
            .addGroup(studentPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(studentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(studentSearch)
                    .addComponent(studentLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 384, Short.MAX_VALUE)
                .addComponent(studentNew, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(studentEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(studentDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        studentPanelLayout.setVerticalGroup(
            studentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(studentPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(studentLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(studentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(studentSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(studentEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(studentNew, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(studentDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(studentScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(studentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(studentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab3", jPanel3);

        payPanel.setBackground(new java.awt.Color(255, 255, 255));

        payScroll.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        payTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SELECT", "#", "DATE", "PAYEE NAME", "FEE CATEGORY", "PAID AMOUNT"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        payTable.getTableHeader().setReorderingAllowed(false);
        payScroll.setViewportView(payTable);
        if (payTable.getColumnModel().getColumnCount() > 0) {
            payTable.getColumnModel().getColumn(0).setMaxWidth(50);
            payTable.getColumnModel().getColumn(0).setHeaderValue("SELECT");
            payTable.getColumnModel().getColumn(1).setMaxWidth(40);
            payTable.getColumnModel().getColumn(2).setPreferredWidth(100);
            payTable.getColumnModel().getColumn(3).setPreferredWidth(150);
            payTable.getColumnModel().getColumn(4).setPreferredWidth(100);
            payTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        }

        jSeparator4.setForeground(new java.awt.Color(54, 83, 20));

        paySearch.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        paySearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paySearchActionPerformed(evt);
            }
        });
        paySearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                paySearchKeyReleased(evt);
            }
        });

        payLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        payLabel.setText("MANAGE PAYMENTS");

        payEdit.setText("Edit");
        payEdit.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        payEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payEditActionPerformed(evt);
            }
        });

        payNew.setText("New");
        payNew.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        payNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payNewActionPerformed(evt);
            }
        });

        payDelete.setText("Delete");
        payDelete.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        payDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout payPanelLayout = new javax.swing.GroupLayout(payPanel);
        payPanel.setLayout(payPanelLayout);
        payPanelLayout.setHorizontalGroup(
            payPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(payScroll)
            .addComponent(jSeparator4)
            .addGroup(payPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(payPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(paySearch)
                    .addComponent(payLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 384, Short.MAX_VALUE)
                .addComponent(payNew, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(payEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(payDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        payPanelLayout.setVerticalGroup(
            payPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(payPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(payLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(payPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(paySearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(payEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(payNew, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(payDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(payScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(payPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(payPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        jTabbedPane1.addTab("tab4", jPanel4);

        jLabel10.setText("Payments");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(826, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addContainerGap(703, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab5", jPanel5);

        reportPanel.setBackground(new java.awt.Color(255, 255, 255));
        reportPanel.setPreferredSize(new java.awt.Dimension(998, 682));

        reportScroll.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        reportTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "PAYEE NAME", "FEE CATEGORY", "PAID AMOUNT", "DATE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        reportTable.getTableHeader().setReorderingAllowed(false);
        reportScroll.setViewportView(reportTable);
        if (reportTable.getColumnModel().getColumnCount() > 0) {
            reportTable.getColumnModel().getColumn(0).setMaxWidth(50);
            reportTable.getColumnModel().getColumn(1).setPreferredWidth(150);
            reportTable.getColumnModel().getColumn(2).setPreferredWidth(150);
            reportTable.getColumnModel().getColumn(3).setPreferredWidth(100);
            reportTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        }

        jSeparator6.setForeground(new java.awt.Color(54, 83, 20));

        reportLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        reportLabel.setText("PRINT REPORTS");

        reportDownload.setText("Download CSV");
        reportDownload.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        reportDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportDownloadActionPerformed(evt);
            }
        });

        reportMonth.setBackground(new java.awt.Color(246, 246, 246));
        reportMonth.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        reportMonth.setMaximumRowCount(5);
        reportMonth.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Month of January", "Month of February", "Month of March", "Month of April", "Month of May", "Month of June", "Month of July", "Month of August", "Month of September", "Month of October", "Month of November", "Month of December" }));
        reportMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportMonthActionPerformed(evt);
            }
        });

        reportFeeCat.setBackground(new java.awt.Color(246, 246, 246));
        reportFeeCat.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        reportFeeCat.setMaximumRowCount(5);
        reportFeeCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportFeeCatActionPerformed(evt);
            }
        });

        totalLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        totalLabel.setText("TOTAL:");

        totalOutput.setForeground(new java.awt.Color(54, 83, 20));
        totalOutput.setText("₱0");

        javax.swing.GroupLayout reportPanelLayout = new javax.swing.GroupLayout(reportPanel);
        reportPanel.setLayout(reportPanelLayout);
        reportPanelLayout.setHorizontalGroup(
            reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(reportScroll)
            .addComponent(jSeparator6)
            .addGroup(reportPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(reportLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(reportPanelLayout.createSequentialGroup()
                        .addComponent(reportMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(reportFeeCat, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 366, Short.MAX_VALUE)
                .addComponent(reportDownload, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
            .addComponent(jSeparator1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, reportPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(totalLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(totalOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        reportPanelLayout.setVerticalGroup(
            reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(reportLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(reportDownload, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reportMonth)
                    .addComponent(reportFeeCat))
                .addGap(18, 18, 18)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(reportScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addGroup(reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalLabel)
                    .addComponent(totalOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(reportPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(reportPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab6", jPanel6);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(257, -40, -1, 760));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void reportFeeCatActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_reportFeeCatActionPerformed
        populateReportTable();
    }// GEN-LAST:event_reportFeeCatActionPerformed

    private void reportMonthActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_reportMonthActionPerformed
        populateReportTable();
    }// GEN-LAST:event_reportMonthActionPerformed

    private int populateReportTable() {
        String selectedMonth = reportMonth.getSelectedItem().toString();
        String selectedFeeCategory = reportFeeCat.getSelectedItem().toString();
        int monthNumber = reportMonth.getSelectedIndex() + 1;
        int counter = 0;
        int totalAmount = 0; // Add variable to track total amount

        try {
            Connection conn = DatabaseConnection.getConnection();

            StringBuilder sql = new StringBuilder("SELECT * FROM payments WHERE 1=1");
            if (!selectedMonth.equals("Select Month")) {
                sql.append(" AND MONTH(pay_date) = ?");
            }
            if (!selectedFeeCategory.equals("Select Fee Category")) {
                sql.append(" AND pay_fee = ?");
            }
            sql.append(" ORDER BY pay_id ASC");

            PreparedStatement pst = conn.prepareStatement(sql.toString());

            int paramIndex = 1;
            if (!selectedMonth.equals("Select Month")) {
                pst.setInt(paramIndex++, monthNumber);
            }
            if (!selectedFeeCategory.equals("Select Fee Category")) {
                pst.setString(paramIndex++, selectedFeeCategory);
            }

            ResultSet rs = pst.executeQuery();
            DefaultTableModel model = (DefaultTableModel) reportTable.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[] {
                        ++counter,
                        rs.getString("payee_name"),
                        rs.getString("pay_fee"),
                        String.format("₱%,d", rs.getInt("pay_amount")),
                        rs.getTimestamp("pay_date")
                });
                totalAmount += rs.getInt("pay_amount"); // Add each payment amount to total
            }

            if (counter == 0) {
                model.addRow(new Object[] { "", "No record found.", "", "", "" });
            }

            // Update the total label with formatted amount
            totalOutput.setText(String.format("₱%,d", totalAmount));

            rs.close();
            pst.close();
            conn.close();

        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Error loading reports: " + e.getMessage());
        }

        return counter;
    }

    private void initializeReportTable() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        reportTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(javax.swing.JLabel.LEFT);
        reportTable.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);

        reportTable.getTableHeader().setReorderingAllowed(false);
        populateReportTable();
    }

    public void dashNavMouseDragged(java.awt.event.MouseEvent evt) {

    }
    
    public void totalIconsActionPerformed(java.awt.event.ActionEvent evt) {

    }

    public void dashNavMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {

    }

    private void reportDownloadActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_reportDownloadActionPerformed
        exportTableToCSV();
    }// GEN-LAST:event_reportDownloadActionPerformed

    private void exportTableToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Report");
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String defaultFileName = "UNITS_Report_" + dateFormat.format(new Date()) + ".csv";
        fileChooser.setSelectedFile(new File(defaultFileName));
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files (*.csv)", "csv");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            if (!fileToSave.getAbsolutePath().toLowerCase().endsWith(".csv")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
            }

            try {
                FileWriter csvWriter = new FileWriter(fileToSave);
                DefaultTableModel model = (DefaultTableModel) reportTable.getModel();
                
                // Write title with selected month
                String selectedMonth = reportMonth.getSelectedItem().toString().replace("Month of ", "");
                csvWriter.append("Collected Fees on the Month of " + selectedMonth + "\n");

                // Write header
                csvWriter.append("#,PAYEE SURNAME,PAYEE FIRSTNAME,FEE CATEGORY,PAID AMOUNT,DATE\n");

                // Write rows
                for (int i = 0; i < model.getRowCount(); i++) {
                    // Skip if it's the "No record found" row
                    if (model.getValueAt(i, 1).toString().equals("No record found.")) {
                        continue;
                    }

                    // Write row number
                    csvWriter.append(model.getValueAt(i, 0).toString()).append(",");
                    
                    // Write payee name
                    csvWriter.append(model.getValueAt(i, 1).toString()).append(",");
                    
                    // Write fee category
                    csvWriter.append(model.getValueAt(i, 2).toString()).append(",");
                    
                    // Write paid amount (remove peso sign and commas)
                    String amount = model.getValueAt(i, 3).toString()
                        .replace("₱", "")
                        .replace(",", "");
                    csvWriter.append(amount).append(",");
                    
                    // Write date in MM/dd/yyyy format
                    try {
                        String dateStr = model.getValueAt(i, 4).toString();
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                        SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
                        Date date = inputFormat.parse(dateStr);
                        csvWriter.append(outputFormat.format(date));
                    } catch (ParseException e) {
                        csvWriter.append(model.getValueAt(i, 4).toString());
                    }
                    csvWriter.append("\n");
                }

                // Write total
                csvWriter.append("\n,,,Total,");
                String total = totalOutput.getText()
                    .replace("₱", "")
                    .replace(",", "");
                csvWriter.append(total).append("\n");

                csvWriter.flush();
                csvWriter.close();

                Notifications.getInstance().show(Notifications.Type.SUCCESS, 
                    "Report saved successfully to: " + fileToSave.getAbsolutePath());

            } catch (IOException e) {
                Notifications.getInstance().show(Notifications.Type.ERROR, 
                    "Error saving report: " + e.getMessage());
            }
        }
    }

    private void accountNavActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_accountNavActionPerformed
    }// GEN-LAST:event_accountNavActionPerformed

    private void studentSearchActionPerformed(java.awt.event.ActionEvent evt) {

    }

    private void studentEditActionPerformed(java.awt.event.ActionEvent evt) {
        List<Integer> selectedIds = getSelectedStudentIds();

        if (selectedIds.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Please select a student to edit");
            return;
        }

        if (selectedIds.size() > 1) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Please select only one student to edit");
            return;
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM students WHERE student_id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, selectedIds.get(0));
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                EditStudent edit = new EditStudent();
                // Set the data to the form
                edit.setStudentName(rs.getString("student_name"));
                edit.setEmail(rs.getString("student_email"));
                edit.setYear(rs.getString("year_level"));

                DefaultOption option = new DefaultOption() {
                    @Override
                    public boolean closeWhenClickOutside() {
                        return true;
                    }
                };

                final Connection finalConn = conn;
                final int studentId = selectedIds.get(0);
                String actions[] = new String[] { "Cancel", "Update" };
                GlassPanePopup.showPopup(new SimplePopupBorder(edit, "Update Student", actions, (pc, i) -> {
                    if (i == 1) {
                        // Update button clicked
                        if (!edit.validateForm()) {
                            Notifications.getInstance().show(Notifications.Type.ERROR, "Please fill in all fields");
                            return;
                        }

                        try {
                            // Validate fee amount is a number

                            String updateSql = "UPDATE students SET student_name=?, student_email=?, year_level=? WHERE student_id=?";
                            PreparedStatement updatePst = finalConn.prepareStatement(updateSql);
                            updatePst.setString(1, edit.getStudentName());
                            updatePst.setString(2, edit.getEmail());
                            updatePst.setString(3, edit.getYear());
                            updatePst.setInt(4, studentId);

                            updatePst.executeUpdate();
                            updatePst.close();

                            // Close popup first
                            pc.closePopup();

                            // Then refresh table and show notification
                            refreshStudentTable();
                            Notifications.getInstance().show(Notifications.Type.SUCCESS,
                                    "Student updated successfully");

                        } catch (SQLException e) {
                            Notifications.getInstance().show(Notifications.Type.ERROR,
                                    "Error updating student: " + e.getMessage());
                        } finally {
                            try {
                                finalConn.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        pc.closePopup();
                        try {
                            finalConn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }), option);
            }

            rs.close();
            pst.close();

        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Error loading student: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }// GEN-LAST:event_catEdit1ActionPerformed

    private void studentNewActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_catNew1ActionPerformed
        CreateStudent create = new CreateStudent();
        DefaultOption option = new DefaultOption() {
            @Override
            public boolean closeWhenClickOutside() {
                return true;
            }
        };
        String actions[] = new String[] { "Cancel", "Save" };
        GlassPanePopup.showPopup(new SimplePopupBorder(create, "New Student", actions, (pc, i) -> {
            if (i == 1) {
                // Save button clicked
                if (!create.validateForm()) {
                    Notifications.getInstance().show(Notifications.Type.ERROR, "Please fill in all fields");
                    return;
                }

                try {
                    // Validate fee amount is a number

                    Connection conn = DatabaseConnection.getConnection();
                    String sql = "INSERT INTO students (student_name, student_email, year_level) VALUES (?, ?, ?)";

                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, create.getStudentName());
                    pst.setString(2, create.getEmail());
                    pst.setString(3, create.getYear());

                    pst.executeUpdate();

                    // Refresh the table
                    refreshStudentTable();

                    // Show success notification
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, "Student added successfully");

                    // Close popup
                    pc.closePopup();

                } catch (SQLException e) {
                    Notifications.getInstance().show(Notifications.Type.ERROR,
                            "Error saving category: " + e.getMessage());
                }
            } else {
                pc.closePopup();
            }
        }), option);
    }// GEN-LAST:event_catNew1ActionPerformed

    private void studentDeleteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_catDelete1ActionPerformed
        List<Integer> selectedIds = getSelectedStudentIds();

        if (selectedIds.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Please select students to delete");
            return;
        }

        // Show confirmation dialog
        int choice = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete " + selectedIds.size() + " selected " +
                        (selectedIds.size() == 1 ? "student" : "students") + "?",
                "Confirm Delete",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE);

        if (choice == javax.swing.JOptionPane.YES_OPTION) {
            try {
                Connection conn = DatabaseConnection.getConnection();

                // Create SQL statement with multiple IDs
                StringBuilder sql = new StringBuilder("DELETE FROM students WHERE student_id IN (");
                for (int i = 0; i < selectedIds.size(); i++) {
                    sql.append(i == 0 ? "?" : ", ?");
                }
                sql.append(")");

                PreparedStatement pst = conn.prepareStatement(sql.toString());

                // Set the parameters
                for (int i = 0; i < selectedIds.size(); i++) {
                    pst.setInt(i + 1, selectedIds.get(i));
                }

                // Execute the delete
                int deletedRows = pst.executeUpdate();

                pst.close();
                conn.close();

                // Refresh the table
                refreshStudentTable();

                // Show success notification
                Notifications.getInstance().show(Notifications.Type.SUCCESS,
                        deletedRows + (deletedRows == 1 ? " student" : " students") + " deleted successfully");

            } catch (SQLException e) {
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        "Error deleting students: " + e.getMessage());
            }
        }
    }// GEN-LAST:event_catDelete1ActionPerformed

    private void studentSearchKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_studentSearchKeyReleased
        String searchText = studentSearch.getText();
        if (searchText.trim().isEmpty()) {
            refreshStudentTable();
        } else {
            searchStudent(searchText);
        }
    }// GEN-LAST:event_studentSearchKeyReleased

    private void catSearchKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_catSearchKeyReleased
        String searchText = catSearch.getText();
        if (searchText.trim().isEmpty()) {
            refreshCategoryTable();
        } else {
            searchCategory(searchText);
        }
    }// GEN-LAST:event_catSearchKeyReleased

    private void paySearchKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_paySearchKeyReleased
        String searchText = paySearch.getText();
        if (searchText.trim().isEmpty()) {
            refreshPaymentTable();
        } else {
            searchPay(searchText);
        }
    }

    private void searchStudent(String searchText) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM students WHERE " +
                    "CONVERT(student_id, CHAR) LIKE ? OR " + // Search by ID
                    "LOWER(student_name) LIKE LOWER(?) OR " +
                    "LOWER(student_email) LIKE LOWER(?) OR " +
                    "LOWER(year_level) LIKE LOWER(?) OR " +
                    "DATE_FORMAT(student_date, '%Y-%m-%d %H:%i:%s') LIKE ? " + // Search by date
                    "ORDER BY student_id ASC";
            PreparedStatement pst = conn.prepareStatement(sql);
            String searchPattern = "%" + searchText + "%";
            // Set all parameters to the search pattern
            for (int i = 1; i <= 5; i++) {
                pst.setString(i, searchPattern);
            }
            ResultSet rs = pst.executeQuery();
            DefaultTableModel model = (DefaultTableModel) studentTable.getModel();
            model.setRowCount(0); // Clear existing rows
            int counter = 1;
            while (rs.next()) {
                model.addRow(new Object[] {
                        false, // Checkbox column
                        counter++, // Row number
                        rs.getString("student_name"),
                        rs.getString("student_email"),
                        rs.getString("year_level"),
                        rs.getTimestamp("student_date")
                });
            }
            rs.close();
            pst.close();
            conn.close();
        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Error searching: " + e.getMessage());
        }
    }

    private void catSearchActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_catSearchActionPerformed

    }

    private void catDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        List<Integer> selectedIds = getSelectedCategoryIds();

        if (selectedIds.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Please select categories to delete");
            return;
        }

        // Show confirmation dialog
        int choice = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete " + selectedIds.size() + " selected " +
                        (selectedIds.size() == 1 ? "category" : "categories") + "?",
                "Confirm Delete",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE);

        if (choice == javax.swing.JOptionPane.YES_OPTION) {
            try {
                Connection conn = DatabaseConnection.getConnection();

                // Create SQL statement with multiple IDs
                StringBuilder sql = new StringBuilder("DELETE FROM categories WHERE cat_id IN (");
                for (int i = 0; i < selectedIds.size(); i++) {
                    sql.append(i == 0 ? "?" : ", ?");
                }
                sql.append(")");

                PreparedStatement pst = conn.prepareStatement(sql.toString());

                // Set the parameters
                for (int i = 0; i < selectedIds.size(); i++) {
                    pst.setInt(i + 1, selectedIds.get(i));
                }

                // Execute the delete
                int deletedRows = pst.executeUpdate();

                pst.close();
                conn.close();

                // Refresh the table
                refreshCategoryTable();

                // Show success notification
                Notifications.getInstance().show(Notifications.Type.SUCCESS,
                        deletedRows + (deletedRows == 1 ? " category" : " categories") + " deleted successfully");

            } catch (SQLException e) {
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        "Error deleting categories: " + e.getMessage());
            }
        }
    }

    private void catNewActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_catNewActionPerformed
        CreateFee create = new CreateFee();
        DefaultOption option = new DefaultOption() {
            @Override
            public boolean closeWhenClickOutside() {
                return true;
            }
        };
        String actions[] = new String[] { "Cancel", "Save" };
        GlassPanePopup.showPopup(new SimplePopupBorder(create, "Create Fee Category", actions, (pc, i) -> {
            if (i == 1) {
                // Save button clicked
                if (!create.validateForm()) {
                    Notifications.getInstance().show(Notifications.Type.ERROR, "Please fill in all fields");
                    return;
                }

                try {
                    // Validate fee amount is a number
                    int feeAmount = Integer.parseInt(create.getFeeAmount());

                    Connection conn = DatabaseConnection.getConnection();
                    String sql = "INSERT INTO categories (cat_name, cat_fee, cat_des) VALUES (?, ?, ?)";

                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, create.getCategoryName());
                    pst.setInt(2, feeAmount);
                    pst.setString(3, create.getDescription()); // Now this will work with VARCHAR/TEXT

                    pst.executeUpdate();

                    // Refresh the table
                    refreshCategoryTable();

                    // Show success notification
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, "Category added successfully");

                    // Close popup
                    pc.closePopup();

                } catch (SQLException e) {
                    Notifications.getInstance().show(Notifications.Type.ERROR,
                            "Error saving category: " + e.getMessage());
                } catch (NumberFormatException e) {
                    Notifications.getInstance().show(Notifications.Type.ERROR, "Fee amount must be a number");
                }
            } else {
                pc.closePopup();
            }
        }), option);
    }// GEN-LAST:event_catNewActionPerformed

    private void reportNavActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_reportNavActionPerformed
        initializeReportTable();
        setReportMonthToCurrent();
        
        jTabbedPane1.setSelectedIndex(5);
        dashNav.setBackground(new java.awt.Color(255, 255, 255));
        feesNav.setBackground(new java.awt.Color(255, 255, 255));
        catNav.setBackground(new java.awt.Color(255, 255, 255));
        studentNav.setBackground(new java.awt.Color(255, 255, 255));
        reportNav.setBackground(new java.awt.Color(167, 201, 87));
        
        initializeCategoryTable();
        initializeStudentTable();
        initializePaymentTable();


        int recordCount = populateReportTable();
        if (recordCount == 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "No records found.");
        }
    }

    private void studentNavActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_studentNavActionPerformed
        jTabbedPane1.setSelectedIndex(2);
        dashNav.setBackground(new java.awt.Color(255, 255, 255));
        feesNav.setBackground(new java.awt.Color(255, 255, 255));
        catNav.setBackground(new java.awt.Color(255, 255, 255));
        studentNav.setBackground(new java.awt.Color(167, 201, 87));
        reportNav.setBackground(new java.awt.Color(255, 255, 255));
        
        initializeCategoryTable();
        initializePaymentTable();
        initializeReportTable();
    }

    private void catNavActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_catNavActionPerformed
        jTabbedPane1.setSelectedIndex(1);
        dashNav.setBackground(new java.awt.Color(255, 255, 255));
        feesNav.setBackground(new java.awt.Color(255, 255, 255));
        catNav.setBackground(new java.awt.Color(167, 201, 87));
        studentNav.setBackground(new java.awt.Color(255, 255, 255));
        reportNav.setBackground(new java.awt.Color(255, 255, 255));
        
        initializeStudentTable();
        initializePaymentTable();
        initializeReportTable();
    }

    private void feesNavActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_feesNavActionPerformed
        jTabbedPane1.setSelectedIndex(3);
        dashNav.setBackground(new java.awt.Color(255, 255, 255));
        feesNav.setBackground(new java.awt.Color(167, 201, 87));
        catNav.setBackground(new java.awt.Color(255, 255, 255));
        studentNav.setBackground(new java.awt.Color(255, 255, 255));
        reportNav.setBackground(new java.awt.Color(255, 255, 255));
        
        initializeCategoryTable();
        initializeStudentTable();
        initializeReportTable();
    }

    private void outNavActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void dashNavActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_dashNavActionPerformed
        jTabbedPane1.setSelectedIndex(0);
        dashNav.setBackground(new java.awt.Color(167, 201, 87));
        feesNav.setBackground(new java.awt.Color(255, 255, 255));
        catNav.setBackground(new java.awt.Color(255, 255, 255));
        studentNav.setBackground(new java.awt.Color(255, 255, 255));
        reportNav.setBackground(new java.awt.Color(255, 255, 255));
        
        curveChart.start();
        updateCurveChartData();
        updateTotalCollectedFees();
        populateRecentTable();
        
        initializeCategoryTable();
        initializeStudentTable();
        initializePaymentTable();
        initializeReportTable();
    }

    public static void main(String args[]) {
        // FlatRobotoFont.install();
        // UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN,
        // 13));
        FlatLaf.registerCustomDefaultsSource("jet.themes");
        FlatLightLaf.setup();

        java.awt.EventQueue.invokeLater(() -> {
            new MenuPage().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private jet.swing.ButtonAction catDelete;
    private jet.swing.ButtonAction catEdit;
    private javax.swing.JLabel catLabel;
    private javax.swing.JButton catNav;
    private jet.swing.ButtonAction catNew;
    private javax.swing.JPanel catPanel;
    private javax.swing.JScrollPane catScroll;
    private javax.swing.JTextField catSearch;
    private javax.swing.JTable catTable;
    private javax.swing.JLabel chartLabel;
    private jet.chart.CurveChart curveChart;
    private javax.swing.JComboBox<String> dashBox;
    private javax.swing.JLabel dashLabel;
    private javax.swing.JButton dashNav;
    private javax.swing.JPanel dashPanel;
    private javax.swing.JScrollPane dashScroll;
    private javax.swing.JLabel dashStuLabel;
    private javax.swing.JTable dashTable;
    private javax.swing.JLabel dashTotal;
    private javax.swing.JButton feesNav;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JPanel lineChartPanel;
    private javax.swing.JPanel logoPanel;
    private javax.swing.JTextField logoText;
    private javax.swing.JPanel navPanel;
    private jet.panel.PanelGradient panelGradient1;
    private jet.swing.ButtonAction payDelete;
    private jet.swing.ButtonAction payEdit;
    private javax.swing.JLabel payLabel;
    private jet.swing.ButtonAction payNew;
    private javax.swing.JPanel payPanel;
    private javax.swing.JScrollPane payScroll;
    private javax.swing.JTextField paySearch;
    private javax.swing.JTable payTable;
    private javax.swing.JPanel r1;
    private javax.swing.JPanel r2;
    private javax.swing.JPanel r3;
    private javax.swing.JPanel r4;
    private javax.swing.JLabel rStud1;
    private javax.swing.JLabel rStud2;
    private javax.swing.JLabel rStud3;
    private javax.swing.JLabel rStud4;
    private javax.swing.JLabel recentLabel;
    private javax.swing.JPanel recentPanel;
    private javax.swing.JPanel recordStuPanel;
    private jet.swing.ButtonAction reportDownload;
    private javax.swing.JComboBox<String> reportFeeCat;
    private javax.swing.JLabel reportLabel;
    private javax.swing.JComboBox<String> reportMonth;
    private javax.swing.JButton reportNav;
    private javax.swing.JPanel reportPanel;
    private javax.swing.JScrollPane reportScroll;
    private javax.swing.JTable reportTable;
    private jet.swing.ButtonAction studentDelete;
    private jet.swing.ButtonAction studentEdit;
    private javax.swing.JLabel studentLabel;
    private javax.swing.JButton studentNav;
    private jet.swing.ButtonAction studentNew;
    private javax.swing.JPanel studentPanel;
    private javax.swing.JScrollPane studentScroll;
    private javax.swing.JTextField studentSearch;
    private javax.swing.JTable studentTable;
    private javax.swing.JTextField totalIcons;
    private javax.swing.JLabel totalLabel;
    private javax.swing.JLabel totalOutput;
    private javax.swing.JPanel totalPanel;
    // End of variables declaration//GEN-END:variables

    private void initializeCategoryTable() {
        refreshCategoryTable();
    }

    private void refreshCategoryTable() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM categories ORDER BY cat_id ASC";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = (DefaultTableModel) catTable.getModel();
            model.setRowCount(0); // Clear existing rows

            int counter = 1;
            while (rs.next()) {
                model.addRow(new Object[] {
                        false, // Checkbox column
                        counter++, // Row number
                        rs.getString("cat_name"),
                        String.format("₱%,d", rs.getInt("cat_fee")), // Format fee with peso sign and commas
                        rs.getString("cat_des"),
                        rs.getTimestamp("cat_date")
                });
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Error refreshing table: " + e.getMessage());
        }
    }

    private void initializeStudentTable() {
        studentTable.getColumnModel().getColumn(1).setMaxWidth(40); // Set max width for # column
        studentTable.getTableHeader().setDefaultRenderer(new TableHeaderAlignment(studentTable));
        refreshStudentTable();
    }

    private void refreshStudentTable() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM students ORDER BY student_id ASC";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = (DefaultTableModel) studentTable.getModel();
            model.setRowCount(0); // Clear existing rows

            int counter = 1;
            while (rs.next()) {
                model.addRow(new Object[] {
                        false, // Checkbox column
                        counter++, // Row number
                        rs.getString("student_name"),
                        rs.getString("student_email"),
                        rs.getString("year_level"),
                        rs.getTimestamp("student_date")
                });
            }

            rs.close();
            pst.close();
            conn.close();
            
            updateStudentCountLabels();

        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Error refreshing table: " + e.getMessage());
        }
    }

    // Add this method to handle search functionality
    private void searchCategory(String searchText) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            // Updated SQL to include ID and date search
            String sql = "SELECT * FROM categories WHERE " +
                    "CONVERT(cat_id, CHAR) LIKE ? OR " + // Search by ID
                    "LOWER(cat_name) LIKE LOWER(?) OR " +
                    "LOWER(cat_des) LIKE LOWER(?) OR " +
                    "CONVERT(cat_fee, CHAR) LIKE ? OR " +
                    "DATE_FORMAT(cat_date, '%Y-%m-%d %H:%i:%s') LIKE ? " + // Search by date
                    "ORDER BY cat_id ASC";

            PreparedStatement pst = conn.prepareStatement(sql);
            String searchPattern = "%" + searchText + "%";
            // Set all parameters to the search pattern
            for (int i = 1; i <= 5; i++) {
                pst.setString(i, searchPattern);
            }

            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = (DefaultTableModel) catTable.getModel();
            model.setRowCount(0); // Clear existing rows

            int counter = 1;
            while (rs.next()) {
                model.addRow(new Object[] {
                        false, // Checkbox column
                        counter++, // Row number
                        rs.getString("cat_name"),
                        String.format("₱%,d", rs.getInt("cat_fee")),
                        rs.getString("cat_des"),
                        rs.getTimestamp("cat_date")
                });
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Error searching: " + e.getMessage());
        }
    }

    private void catEditActionPerformed(java.awt.event.ActionEvent evt) {
        List<Integer> selectedIds = getSelectedCategoryIds();

        if (selectedIds.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Please select a category to edit");
            return;
        }

        if (selectedIds.size() > 1) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Please select only one category to edit");
            return;
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM categories WHERE cat_id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, selectedIds.get(0));
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                EditFee edit = new EditFee();
                // Set the data to the form
                edit.setCategoryName(rs.getString("cat_name"));
                edit.setFeeAmount(String.valueOf(rs.getInt("cat_fee")));
                edit.setDescription(rs.getString("cat_des"));

                DefaultOption option = new DefaultOption() {
                    @Override
                    public boolean closeWhenClickOutside() {
                        return true;
                    }
                };

                final Connection finalConn = conn;
                final int categoryId = selectedIds.get(0);
                String actions[] = new String[] { "Cancel", "Update" };
                GlassPanePopup.showPopup(new SimplePopupBorder(edit, "Edit Fee Category", actions, (pc, i) -> {
                    if (i == 1) {
                        // Update button clicked
                        if (!edit.validateForm()) {
                            Notifications.getInstance().show(Notifications.Type.ERROR, "Please fill in all fields");
                            return;
                        }

                        try {
                            // Validate fee amount is a number
                            int feeAmount = Integer.parseInt(edit.getFeeAmount());

                            String updateSql = "UPDATE categories SET cat_name=?, cat_fee=?, cat_des=? WHERE cat_id=?";
                            PreparedStatement updatePst = finalConn.prepareStatement(updateSql);
                            updatePst.setString(1, edit.getCategoryName());
                            updatePst.setInt(2, feeAmount);
                            updatePst.setString(3, edit.getDescription());
                            updatePst.setInt(4, categoryId);

                            updatePst.executeUpdate();
                            updatePst.close();

                            // Close popup first
                            pc.closePopup();

                            // Then refresh table and show notification
                            refreshCategoryTable();
                            Notifications.getInstance().show(Notifications.Type.SUCCESS,
                                    "Category updated successfully");

                        } catch (SQLException e) {
                            Notifications.getInstance().show(Notifications.Type.ERROR,
                                    "Error updating category: " + e.getMessage());
                        } catch (NumberFormatException e) {
                            Notifications.getInstance().show(Notifications.Type.ERROR, "Fee amount must be a number");
                        } finally {
                            try {
                                finalConn.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        pc.closePopup();
                        try {
                            finalConn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }), option);
            }

            rs.close();
            pst.close();

        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Error loading category: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private List<Integer> getSelectedCategoryIds() {
        List<Integer> selectedIds = new ArrayList<>();
        DefaultTableModel model = (DefaultTableModel) catTable.getModel();

        // Iterate through all rows in the table
        for (int i = 0; i < model.getRowCount(); i++) {
            // Check if the checkbox in the first column is selected
            Boolean isSelected = (Boolean) model.getValueAt(i, 0);
            if (isSelected) {
                try {
                    // Get the category ID from the database using the row data
                    Connection conn = DatabaseConnection.getConnection();
                    String categoryName = (String) model.getValueAt(i, 2); // Category name is in column 3

                    String sql = "SELECT cat_id FROM categories WHERE cat_name = ?";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, categoryName);

                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        selectedIds.add(rs.getInt("cat_id"));
                    }

                    rs.close();
                    pst.close();
                    conn.close();

                } catch (SQLException e) {
                    Notifications.getInstance().show(Notifications.Type.ERROR,
                            "Error getting category ID: " + e.getMessage());
                }
            }
        }

        return selectedIds;
    }

    private List<Integer> getSelectedStudentIds() {
        List<Integer> selectedIds = new ArrayList<>();
        DefaultTableModel model = (DefaultTableModel) studentTable.getModel();

        // Iterate through all rows in the table
        for (int i = 0; i < model.getRowCount(); i++) {
            // Check if the checkbox in the first column is selected
            Boolean isSelected = (Boolean) model.getValueAt(i, 0);
            if (isSelected) {
                try {
                    // Get the category ID from the database using the row data
                    Connection conn = DatabaseConnection.getConnection();
                    String studentName = (String) model.getValueAt(i, 2); // Category name is in column 3

                    String sql = "SELECT student_id FROM students WHERE student_name = ?";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, studentName);

                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        selectedIds.add(rs.getInt("student_id"));
                    }

                    rs.close();
                    pst.close();
                    conn.close();

                } catch (SQLException e) {
                    Notifications.getInstance().show(Notifications.Type.ERROR,
                            "Error getting category ID: " + e.getMessage());
                }
            }
        }

        return selectedIds;
    }

    private void payEditActionPerformed(java.awt.event.ActionEvent evt) {
        List<Integer> selectedIds = getSelectedPaymentIds();
        if (selectedIds.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Please select a payment to edit");
            return;
        }
        if (selectedIds.size() > 1) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Please select only one payment to edit");
            return;
        }

        int paymentId = selectedIds.get(0);
        EditPayment edit = new EditPayment();

        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM payments WHERE pay_id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, paymentId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                edit.setPayeeName(rs.getString("payee_name"));
                edit.setFeeCat(rs.getString("pay_fee"));
                edit.setAmount(rs.getString("pay_amount"));
            }

            rs.close();
            pst.close();

            Connection finalConn = conn;
            DefaultOption option = new DefaultOption();
            String[] actions = { "Cancel", "Update" };

            GlassPanePopup.showPopup(new SimplePopupBorder(edit, "Edit Payment", actions, (pc, i) -> {
                if (i == 1) {
                    if (!edit.validateForm()) {
                        Notifications.getInstance().show(Notifications.Type.ERROR, "Please fill in all fields");
                        return;
                    }

                    // Check if the new combination already exists BEFORE attempting update
                    if (edit.isPaymentExists(paymentId)) {
                        Notifications.getInstance().show(Notifications.Type.WARNING,
                                "A payment with this student and fee category already exists!");
                        return; // Important: Return here to prevent the update
                    }

                    try {
                        String updateSql = "UPDATE payments SET payee_name=?, pay_fee=?, pay_amount=? WHERE pay_id=?";
                        PreparedStatement updatePst = finalConn.prepareStatement(updateSql);
                        updatePst.setString(1, edit.getPayeeName());
                        updatePst.setString(2, edit.getFeeCat());
                        updatePst.setString(3, edit.getAmount());
                        updatePst.setInt(4, paymentId);

                        updatePst.executeUpdate();
                        updatePst.close();

                        pc.closePopup();
                        refreshPaymentTable();
                        Notifications.getInstance().show(Notifications.Type.SUCCESS,
                                "Payment updated successfully");

                    } catch (SQLException e) {
                        Notifications.getInstance().show(Notifications.Type.ERROR,
                                "Error updating payment: " + e.getMessage());
                    }
                } else {
                    pc.closePopup();
                }
            }), option);

        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    "Error loading payment: " + e.getMessage());
        }
    }

    private List<Integer> getSelectedPaymentIds() {
        List<Integer> selectedIds = new ArrayList<>();
        DefaultTableModel model = (DefaultTableModel) payTable.getModel();

        // Iterate through all rows in the table
        for (int i = 0; i < model.getRowCount(); i++) {
            // Check if the checkbox in the first column is selected
            Boolean isSelected = (Boolean) model.getValueAt(i, 0);
            if (isSelected) {
                try {
                    // Get the category ID from the database using the row data
                    Connection conn = DatabaseConnection.getConnection();
                    String payeeName = (String) model.getValueAt(i, 3); // Payee name is in column 4 (index 3)
                    String feeCat = (String) model.getValueAt(i, 4); // Fee category is in column 5 (index 4)

                    String sql = "SELECT pay_id FROM payments WHERE payee_name = ? AND pay_fee = ?";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, payeeName);
                    pst.setString(2, feeCat);

                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        selectedIds.add(rs.getInt("pay_id"));
                    }

                    rs.close();
                    pst.close();
                    conn.close();

                } catch (SQLException e) {
                    Notifications.getInstance().show(Notifications.Type.ERROR,
                            "Error getting payment ID: " + e.getMessage());
                }
            }
        }

        return selectedIds;
    }

    private void payNewActionPerformed(java.awt.event.ActionEvent evt) {
        CreatePayment create = new CreatePayment();
        DefaultOption option = new DefaultOption() {
            @Override
            public boolean closeWhenClickOutside() {
                return true;
            }
        };
        String actions[] = new String[] { "Cancel", "Save" };
        GlassPanePopup.showPopup(new SimplePopupBorder(create, "New Payment", actions, (pc, i) -> {
            if (i == 1) {
                // Save button clicked
                if (!create.validateForm()) {
                    Notifications.getInstance().show(Notifications.Type.ERROR, "Please fill in all fields");
                    return;
                }

                // Check if payment already exists
                if (create.isPaymentExists()) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, "This payment already exists");
                    return;
                }

                try {
                    Connection conn = DatabaseConnection.getConnection();
                    String sql = "INSERT INTO payments (payee_name, pay_fee, pay_amount) VALUES (?, ?, ?)";

                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, create.getPayeeName());
                    pst.setString(2, create.getFeeCat());
                    pst.setString(3, create.getAmount());

                    pst.executeUpdate();

                    // Refresh the table
                    refreshPaymentTable();

                    // Show success notification
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, "Payment added successfully");

                    // Close popup
                    pc.closePopup();

                } catch (SQLException e) {
                    Notifications.getInstance().show(Notifications.Type.ERROR,
                            "Error saving payment: " + e.getMessage());
                }
            } else {
                pc.closePopup();
            }
        }), option);
    }

    private void payDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        List<Integer> selectedIds = getSelectedPaymentIds();

        if (selectedIds.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Please select payments to delete");
            return;
        }

        // Show confirmation dialog
        int choice = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete " + selectedIds.size() + " selected " +
                        (selectedIds.size() == 1 ? "payment" : "payments") + "?",
                "Confirm Delete",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE);

        if (choice == javax.swing.JOptionPane.YES_OPTION) {
            try {
                Connection conn = DatabaseConnection.getConnection();

                // Create SQL statement with multiple IDs
                StringBuilder sql = new StringBuilder("DELETE FROM payments WHERE pay_id IN (");
                for (int i = 0; i < selectedIds.size(); i++) {
                    sql.append(i == 0 ? "?" : ", ?");
                }
                sql.append(")");

                PreparedStatement pst = conn.prepareStatement(sql.toString());

                // Set the parameters
                for (int i = 0; i < selectedIds.size(); i++) {
                    pst.setInt(i + 1, selectedIds.get(i));
                }

                // Execute the delete
                int deletedRows = pst.executeUpdate();

                pst.close();
                conn.close();

                // Refresh the table
                refreshPaymentTable();

                // Show success notification
                Notifications.getInstance().show(Notifications.Type.SUCCESS,
                        deletedRows + (deletedRows == 1 ? " payment" : " payments") + " deleted successfully");

            } catch (SQLException e) {
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        "Error deleting payments: " + e.getMessage());
            }
        }
    }

    private void paySearchActionPerformed(java.awt.event.ActionEvent evt) {

    }

    private void searchPay(String searchText) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM payments WHERE " +
                    "CONVERT(pay_id, CHAR) LIKE ? OR " + // Search by ID
                    "LOWER(payee_name) LIKE LOWER(?) OR " +
                    "LOWER(pay_fee) LIKE LOWER(?) OR " +
                    "CONVERT(pay_amount, CHAR) LIKE ? OR " +
                    "DATE_FORMAT(pay_date, '%Y-%m-%d %H:%i:%s') LIKE ? " + // Search by date
                    "ORDER BY pay_id ASC";
            PreparedStatement pst = conn.prepareStatement(sql);
            String searchPattern = "%" + searchText + "%";
            // Set all parameters to the search pattern
            for (int i = 1; i <= 5; i++) {
                pst.setString(i, searchPattern);
            }
            ResultSet rs = pst.executeQuery();
            DefaultTableModel model = (DefaultTableModel) payTable.getModel();
            model.setRowCount(0); // Clear existing rows
            int counter = 1;
            while (rs.next()) {
                model.addRow(new Object[] {
                        false, // Checkbox column
                        counter++, // Row number
                        rs.getTimestamp("pay_date"),
                        rs.getString("payee_name"),
                        rs.getString("pay_fee"),
                        String.format("₱%,d", rs.getInt("pay_amount"))

                });
            }
            rs.close();
            pst.close();
            conn.close();
        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Error searching: " + e.getMessage());
        }
    }

    private void initializePaymentTable() {
        refreshPaymentTable();
    }

    private void refreshPaymentTable() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM payments ORDER BY pay_id ASC";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = (DefaultTableModel) payTable.getModel();
            model.setRowCount(0); // Clear existing rows

            int counter = 1;
            while (rs.next()) {
                model.addRow(new Object[] {
                        false, // Checkbox column
                        counter++, // Row number
                        rs.getTimestamp("pay_date"),
                        rs.getString("payee_name"),
                        rs.getString("pay_fee"),
                        String.format("₱%,d", rs.getInt("pay_amount"))
                });
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Error refreshing table: " + e.getMessage());
        }
    }

    private void populateReportFeeCatComboBox() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT cat_name FROM categories ORDER BY cat_name ASC";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            reportFeeCat.removeAllItems(); // Clear existing items

            reportFeeCat.addItem("Select Fee Category");

            while (rs.next()) {
                reportFeeCat.addItem(rs.getString("cat_name"));
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Error loading categories: " + e.getMessage());
        }
    }

    private void setReportMonthToCurrent() {
        java.time.Month currentMonth = java.time.LocalDate.now().getMonth();
        reportMonth.setSelectedIndex(currentMonth.getValue() - 1); // Months are 1-based in LocalDate
    }
    
    private void populateRecentTable() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT *, TIMESTAMPDIFF(SECOND, pay_date, NOW()) as seconds_ago " +
                        "FROM payments " +
                        "WHERE pay_date >= DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY) " +
                        "ORDER BY pay_date DESC";

            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = (DefaultTableModel) dashTable.getModel();
            model.setRowCount(0);

            int counter = 1;
            while (rs.next()) {
                model.addRow(new Object[] {
                    counter++,
                    rs.getString("payee_name"),
                    rs.getString("pay_fee"),
                    String.format("₱%,d", rs.getInt("pay_amount")),
                    formatRelativeTime(rs.getLong("seconds_ago"))
                });
            }

            if (counter == 1) {
                model.addRow(new Object[] { "", "No payments in the last 7 days", "", "", "" });
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, 
                "Error loading recent payments: " + e.getMessage());
        }
    }

    private String formatRelativeTime(long secondsAgo) {
        if (secondsAgo < 60) {
            return secondsAgo + " second" + (secondsAgo > 1 ? "s" : "") + " ago";
        }

        long minutesAgo = secondsAgo / 60;
        if (minutesAgo < 60) {
            return minutesAgo + " minute" + (minutesAgo > 1 ? "s" : "") + " ago";
        }

        long hoursAgo = minutesAgo / 60;
        if (hoursAgo < 24) {
//            return hoursAgo + " hour ago";
            return hoursAgo + " hour" + (hoursAgo > 1 ? "s" : "") + " ago";
        }

        long daysAgo = hoursAgo / 24;
        if (daysAgo >= 7) {
            return "7 days ago";
        }
        return daysAgo + " day" + (daysAgo > 1 ? "s" : "") + " ago";
    }
    
    private void updateTotalCollectedFees() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT SUM(pay_amount) AS total FROM payments";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int total = rs.getInt("total");
                dashTotal.setText(String.format("₱%,.2f", (double) total));
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, 
                "Error calculating total fees: " + e.getMessage());
        }
    } 
    
    private void updateStudentCountLabels() {
        try {
            Connection conn = DatabaseConnection.getConnection();

            // Query to count students per year level
            String sql = "SELECT year_level, COUNT(*) as count FROM students GROUP BY year_level";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            // Initialize counts to 0
            int[] counts = new int[4];

            // Update counts based on query results
            while (rs.next()) {
                String yearLevel = rs.getString("year_level");
                int count = rs.getInt("count");

                // Convert year level to array index (1st -> 0, 2nd -> 1, etc)
                int index = Integer.parseInt(yearLevel.substring(0, 1)) - 1;
                if (index >= 0 && index < 4) {
                    counts[index] = count;
                }
            }

            // Update the labels
            rStud1.setText(String.valueOf(counts[0]));
            rStud2.setText(String.valueOf(counts[1]));
            rStud3.setText(String.valueOf(counts[2]));
            rStud4.setText(String.valueOf(counts[3]));

            rs.close();
            pst.close();
            conn.close();

        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, 
                "Error updating student counts: " + e.getMessage());
        }
    }
}
