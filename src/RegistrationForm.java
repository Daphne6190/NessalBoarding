import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class RegistrationForm extends JDialog{
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JButton btnSignup;
    private JPanel registerPanel;
    private JButton btnLogin;

    JFrame frame = new JFrame();

    public RegistrationForm() {

        setTitle("Sign Up!");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(1000, 700));
        setModal(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnSignup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
                if (e.getSource() == btnSignup){
                    MainMenu menuPanel = new MainMenu();
                }
            }
        });

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == btnLogin){
                    LoginForm loginPanel = new LoginForm(null);
                }

            }
        });
        setVisible(true);
    }

    private void registerUser() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String username = tfUsername.getText();
        String password =  String.valueOf(pfPassword.getPassword());

        if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "Please fill in all the fields",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
           return;
        }

        user = addUserToDatabase(name, email, username, password);
        if (user != null){
            dispose();
        }
    }
    public User user;
    private User addUserToDatabase(String name, String email, String username, String password) {
        User user = null;
        final String DB_URL = "jdbc:mysql://localhost:3306/nessalboardingdb";
        final String USERNAME = "root";
        final String PASSWORD = "Grace/6190";

        try{
            Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = connection.createStatement();
            String sql = "INSERT INTO staff (name, email, username, password)" +
                    "VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, password);

            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0){
                user = new User ();
                user.name = name;
                user.email = email;
                user.username = username;
                user.password = password;
            }

            stmt.close();
            connection.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return user;
    }

    public static void main(String[] args) {
        RegistrationForm myForm = new RegistrationForm();
        User user = myForm.user;
        if (user != null){
            System.out.println("Successfull registration");
        }


        }
    }

