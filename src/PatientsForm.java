import net.proteanit.sql.DbUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.Vector;


public class PatientsForm extends JDialog {
    private JTextField tfpatientname;
    private JTextField tfsex;
    private JTextField tfbreed;
    private JTextField tfownername;
    private JTextField tfcolor;
    private JTextField tfproperties;
    private JTextField tfallergies;
    private JTextField tfmedicalhistory;
    private JButton btnsave;
    private JButton btnupdate;
    private JTable tblPatient;
    private JButton btndelete;
    private JButton btnsearch;
    private JTextField tfsearch;
    private JPanel patientsPanel;
    private JButton btnClear;
    private JButton btnClientsModule;
    private JButton btnBack;
    private DefaultTableModel tableModel;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public void NewJFrame(){
        showTableData();
    }

    public PatientsForm(){
        setTitle("Patients Details");
        setContentPane(patientsPanel);
        setMinimumSize(new Dimension(1000, 700));
        setModal(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Patient Name");
        tableModel.addColumn("Sex");
        tableModel.addColumn("Breed");
        tableModel.addColumn("Owner's Name");
        tableModel.addColumn("Patient's Color ");
        tableModel.addColumn("Properties");
        tableModel.addColumn("Allergies");
        tableModel.addColumn("Medical History");

        tblPatient.setModel(tableModel);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/nessalboardingdb";
            String user = "root";
            String password = "Grace/6190";
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();

            // Load data into the table
            loadData();

        } catch (Exception e) {
            e.printStackTrace();
        }


        btnsave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveData();

            }
        });
        btndelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteData();

            }
        });
        btnupdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateData();

            }
        });
        btnsearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchData();

            }
        });

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tfpatientname.setText("");
                tfsex.setText("");
                tfbreed.setText("");
                tfownername.setText("");
                tfcolor.setText("");
                tfproperties.setText("");
                tfallergies.setText("");
                tfmedicalhistory.setText("");

            }
        });


        tblPatient.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try{
                    int row = tblPatient.getSelectedRow();
                    String table_click = (tblPatient.getModel().getValueAt(row, 0).toString());
                    String sql = "SELECT * FROM patients WHERE patientname = '"+table_click+"'";
                    statement = connection.prepareStatement(sql);
                    resultSet = statement.executeQuery( "SELECT * FROM patients WHERE patientname = '"+table_click+"'");

                    if (resultSet.next()){
                        String patientname = resultSet.getString("patientname");
                        tfpatientname.setText(patientname);

                        String sex= resultSet.getString("sex");
                        tfsex.setText(sex);

                        String breed = resultSet.getString("breed");
                        tfbreed.setText(breed);

                        String ownername = resultSet.getString("ownername");
                        tfownername.setText(ownername);

                        String color = resultSet.getString("color");
                        tfcolor.setText(color);

                        String properties = resultSet.getString("properties");
                        tfproperties.setText(properties);

                        String allergies= resultSet.getString("allergies");
                        tfallergies.setText(allergies);

                        String medicalhistory = resultSet.getString("medicalhistory");
                        tfmedicalhistory.setText(medicalhistory);
                    }
                }catch (SQLException ex){
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        });

        btnClientsModule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == btnClientsModule){
                    ClientsForm clientsPanel = new ClientsForm();
                }

            }
        });
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == btnBack){
                    MainMenu menuPanel = new MainMenu();
                }

            }
        });
        setVisible(true);
    }

    private void loadData() throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM patients");

        // Clear existing data
        tableModel.setRowCount(0);

        // Load new data
        while (resultSet.next()) {
           String patientname= resultSet.getString("patientname");
            String sex = resultSet.getString("sex");
           String breed= resultSet.getString("breed");
           String ownername= resultSet.getString("ownername");
           String color= resultSet.getString("color");
           String properties= resultSet.getString("properties");
           String allergies= resultSet.getString("allergies");
           String medicalhistory= resultSet.getString("medicalhistory");

            tableModel.addRow(new Object[]{patientname, sex, breed, ownername, color, properties, allergies, medicalhistory});
        }
    }

    private void saveData() {
        try {
            String patientname = tfpatientname.getText();
            String sex = tfsex.getText();
            String breed = tfbreed.getText();
            String ownername = tfownername.getText();
            String color = tfcolor.getText();
            String properties = tfproperties.getText();
            String allergies = tfallergies.getText();
            String medicalhistory = tfmedicalhistory.getText();

            Statement statement = connection.createStatement();
            String sql = "INSERT INTO patients (patientname, sex, breed, ownername, color, properties, allergies, medicalhistory)" +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, tfpatientname.getText());
            preparedStatement.setString(2, tfsex.getText());
            preparedStatement.setString(3, tfbreed.getText());
            preparedStatement.setString(4, tfownername.getText());
            preparedStatement.setString(5, tfcolor.getText());
            preparedStatement.setString(6, tfproperties.getText());
            preparedStatement.setString(7, tfallergies.getText());
            preparedStatement.setString(8, tfmedicalhistory.getText());
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Patient Added Successfully");

            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            showTableData();
        }
    }

    private void updateData() {
        try {
            String patientname= tfpatientname.getText();
            String sex = tfsex.getText();
            String breed = tfbreed.getText();
            String ownername = tfownername.getText();
            String color = tfcolor.getText();
            String properties = tfproperties.getText();
            String allergies = tfallergies.getText();
            String medicalhistory = tfmedicalhistory.getText();

            Statement statement = connection.createStatement();
            String sql = "UPDATE patients SET patientname = '"+patientname+"', sex='"+sex+"', breed='"+breed+"', ownername='"+ownername+"', color='"+color+"', properties='"+properties+"', allergies='"+allergies+"', medicalhistory='"+medicalhistory+"' WHERE patientname = '"+patientname+"' ";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);


            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(null, "Record Updated Successfully");

            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteData() {
        int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete?", "Delete Warning!", JOptionPane.YES_NO_OPTION);
        if (p==0) {
            try {
                String patientname = tfpatientname.getText();

                Statement statement = connection.createStatement();
                String sql = "DELETE FROM patients WHERE patientname=?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, patientname);
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Delete Successful");


                loadData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void searchData() {
        try {


            Statement statement = connection.createStatement();
            String sql ="SELECT * FROM patients WHERE patientname = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, tfsearch.getText());

            ResultSet resultSet = preparedStatement.executeQuery();
            // Clear existing data
            tableModel.setRowCount(0);

            // Load new data
            while (resultSet.next()) {
                String patientname = resultSet.getString("patientname");
                tfpatientname.setText(patientname);
                String sex = resultSet.getString("sex");
                tfsex.setText(sex);
                String breed = resultSet.getString("breed");
                tfbreed.setText(breed);
                String ownername = resultSet.getString("ownername");
                tfownername.setText(ownername);
                String color = resultSet.getString("color");
                tfcolor.setText(color);
                String properties = resultSet.getString("properties");
                        tfproperties.setText(properties);
                String allergies = resultSet.getString("allergies");
                        tfallergies.setText(allergies);
                String medicalhistory = resultSet.getString("medicalhistory");
                        tfmedicalhistory.setText(medicalhistory);


                tableModel.addRow(new Object[]{patientname, sex, breed, ownername, color, properties, allergies, medicalhistory });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    int q, i, id,deleteItem;
    public void showTableData(){
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nessalboardingdb", "root", "Grace/6190");
            String sql = "SELECT * FROM patients";
            statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery("SELECT * FROM patients");
            ResultSetMetaData stData = resultSet.getMetaData();
                q = stData.getColumnCount();
            DefaultTableModel tableModel = (DefaultTableModel) tblPatient.getModel();
                    tableModel.setRowCount(0);

                    while (resultSet.next()){
                        Vector columnData = new Vector();

                        for (i = 1; i<=q; i++ ){
                            columnData.add(resultSet.getShort("patientname"));
                            columnData.add(resultSet.getShort("sex"));
                            columnData.add(resultSet.getShort("breed"));
                            columnData.add(resultSet.getShort("ownername"));
                            columnData.add(resultSet.getShort("color"));
                            columnData.add(resultSet.getShort("properties"));
                            columnData.add(resultSet.getShort("allergies"));
                            columnData.add(resultSet.getShort("medicalhistory"));
                        }

                        tableModel.addRow(columnData);
                    }

            tblPatient.setModel(DbUtils.resultSetToTableModel(resultSet));


        }catch (Exception ex){
           JOptionPane.showMessageDialog(null, ex);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PatientsForm();
            }
        });
    }


}


    
