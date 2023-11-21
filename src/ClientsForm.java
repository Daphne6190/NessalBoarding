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

public class ClientsForm extends JDialog{
    private JTextField tfclientid;
    private JTable tblclient;
    private JButton btnsave;
    private JButton btnupdate;
    private JButton btndelete;
    private JButton btnsearch;
    private JTextField tfsearch;
    private JButton btnClear;
    private JTextField tfclientname;
    private JTextField tfphone;
    private JTextField tfemail;
    private JTextField tfcity;
    private JTextField tfaddress;
    private JTextField tfanimalname;
    private JScrollPane tblClient;
    private JPanel clientsPanel;
    private JButton btnBack;
    private JButton btnPatientsModule;
    private DefaultTableModel tableModel;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public void NewJFrame(){
        showTableData();
    }

    public ClientsForm(){
        setTitle("Clients Details");
        setContentPane(clientsPanel);
        setMinimumSize(new Dimension(1000, 700));
        setModal(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Client ID");
        tableModel.addColumn("Client Name");
        tableModel.addColumn("Phone Number");
        tableModel.addColumn("Email Address");
        tableModel.addColumn("City");
        tableModel.addColumn("Physical Address");
        tableModel.addColumn("Animal's Name");


        tblclient.setModel(tableModel);

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
                tfclientid.setText("");
                tfclientname.setText("");
                tfphone.setText("");
                tfemail.setText("");
                tfcity.setText("");
                tfaddress.setText("");
                tfanimalname.setText("");
            }
        });


        tblclient.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try{
                    int row = tblclient.getSelectedRow();
                    String table_click = (tblclient.getModel().getValueAt(row, 0).toString());
                    String sql = "SELECT * FROM clients WHERE client_id = '"+table_click+"'";
                    statement = connection.prepareStatement(sql);
                    resultSet = statement.executeQuery( "SELECT * FROM clients WHERE client_id = '"+table_click+"'");

                    if (resultSet.next()){
                        int client_id = resultSet.getInt("client_id");
                        tfclientid.setText(String.valueOf(client_id));

                        String client_name= resultSet.getString("client_name");
                        tfclientname.setText(client_name);

                        int phone_number = Integer.parseInt(resultSet.getString("phone_number"));
                        tfphone.setText(String.valueOf(phone_number));

                        String email_address = resultSet.getString("email_address");
                        tfemail.setText(email_address);

                        String city = resultSet.getString("city");
                        tfcity.setText(city);

                        String physical_address = resultSet.getString("physical_address");
                        tfaddress.setText(physical_address);

                        String animal_name= resultSet.getString("animal_name");
                        tfanimalname.setText(animal_name);


                    }
                }catch (SQLException ex){
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        });

        btnPatientsModule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == btnPatientsModule){
                    PatientsForm patientsPanel = new PatientsForm();
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
        ResultSet resultSet = statement.executeQuery("SELECT * FROM clients");

        // Clear existing data
        tableModel.setRowCount(0);

        // Load new data
        while (resultSet.next()) {
            int client_id= Integer.parseInt(String.valueOf(resultSet.getInt("client_id")));
            String client_name = resultSet.getString("client_name");
            int phone_number= resultSet.getInt("phone_number");
            String email_address= resultSet.getString("email_address");
            String city= resultSet.getString("city");
            String physical_address= resultSet.getString("physical_address");
            String animal_name= resultSet.getString("animal_name");

            tableModel.addRow(new Object[]{ client_id, client_name, phone_number, email_address, city, physical_address, animal_name});
        }
    }

    private void saveData() {
        try {
           int client_id = Integer.parseInt(tfclientid.getText());
            String client_name = tfclientname.getText();
            int phone_number = Integer.parseInt(tfphone.getText());
            String email_address = tfemail.getText();
            String city = tfcity.getText();
            String physical_address = tfaddress.getText();
            String animal_name = tfanimalname.getText();

            Statement statement = connection.createStatement();
            String sql = "INSERT INTO clients (client_id, client_name, phone_number, email_address, city, physical_address, animal_name)" +
                    " VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, tfclientid.getText());
            preparedStatement.setString(2, tfclientname.getText());
            preparedStatement.setString(3, tfphone.getText());
            preparedStatement.setString(4, tfemail.getText());
            preparedStatement.setString(5, tfcity.getText());
            preparedStatement.setString(6, tfaddress.getText());
            preparedStatement.setString(7, tfanimalname.getText());

            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Client Added Successfully");

            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            showTableData();
        }
    }

    private void updateData() {
        try {
            int client_id = Integer.parseInt(tfclientid.getText());
            String client_name = tfclientname.getText();
            int phone_number = Integer.parseInt(tfphone.getText());
            String email_address = tfemail.getText();
            String city = tfcity.getText();
            String physical_address = tfaddress.getText();
            String animal_name = tfanimalname.getText();


            Statement statement = connection.createStatement();
            String sql = "UPDATE clients SET client_id = '"+client_id+"', client_name='"+client_name+"', phone_number='"+phone_number+"', email_address='"+email_address+"', city='"+city+"', physical_address='"+physical_address+"', animal_name='"+animal_name+"'  WHERE client_id = '"+client_id+"' ";

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
                int client_id = Integer.parseInt(tfclientid.getText());

                Statement statement = connection.createStatement();
                String sql = "DELETE FROM clients WHERE client_id=?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, String.valueOf(client_id));
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
            String sql ="SELECT * FROM clients WHERE client_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, tfsearch.getText());

            ResultSet resultSet = preparedStatement.executeQuery();
            // Clear existing data
            tableModel.setRowCount(0);

            // Load new data
            while (resultSet.next()) {
                int client_id = Integer.parseInt(resultSet.getString("client_id"));
                tfclientid.setText(String.valueOf(client_id));

                String client_name = resultSet.getString("client_name");
                tfclientname.setText(client_name);

                int phone_number  = Integer.parseInt(resultSet.getString("phone_number"));
                tfphone.setText(String.valueOf(phone_number));

                String email_address = resultSet.getString("email_address");
                tfemail.setText(email_address);

                String city = resultSet.getString("city");
                tfcity.setText(city);

                String physical_address= resultSet.getString("physical_address");
                tfaddress.setText(physical_address);

                String animal_name = resultSet.getString("animal_name");
                tfanimalname.setText(animal_name);


                tableModel.addRow(new Object[]{client_id, client_name, phone_number, email_address, city, physical_address, animal_name});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    int q, i, id,deleteItem;
    public void showTableData(){
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nessalboardingdb", "root", "Grace/6190");
            String sql = "SELECT * FROM clients";
            statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery("SELECT * FROM clients");
            ResultSetMetaData stData = resultSet.getMetaData();
            q = stData.getColumnCount();
            DefaultTableModel tableModel = (DefaultTableModel) tblclient.getModel();
            tableModel.setRowCount(0);

            while (resultSet.next()){
                Vector columnData = new Vector();

                for (i = 1; i<=q; i++ ){
                    columnData.add(resultSet.getShort("client_id"));
                    columnData.add(resultSet.getShort("client_name"));
                    columnData.add(resultSet.getShort("phone_number"));
                    columnData.add(resultSet.getShort("email_address"));
                    columnData.add(resultSet.getShort("city"));
                    columnData.add(resultSet.getShort("physical_address"));
                    columnData.add(resultSet.getShort("animal_name"));

                }

                tableModel.addRow(columnData);
            }

            tblclient.setModel(DbUtils.resultSetToTableModel(resultSet));


        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientsForm();
            }
        });
    }


}








