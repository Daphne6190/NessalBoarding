import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JDialog {
    private JButton btnLogin;
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JButton btnSignuphere;
    private JPanel loginPanel;

    public LoginForm(JFrame parent) {
        super (parent);
        setTitle("Login");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(1000, 700));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = tfUsername.getText();
                String password = pfPassword.getText();

                user = getAuthenticatedUSer(username, password);
                if (user != null){
                    dispose();
                }
                else {
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Username or Password Invalid",
                            "Try Again",
                            JOptionPane.ERROR_MESSAGE);
                }
                if (e.getSource() == btnLogin){
                    MainMenu menuPanel = new MainMenu();
                }
            }
        });

        btnSignuphere.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == btnSignuphere){
                    RegistrationForm registerPanel = new RegistrationForm();
                }

            }
        });
        setVisible(true);
    }
    public User user;
    private User getAuthenticatedUSer(String username, String password){
        User user = null;
        final String DB_URL = "jdbc:mysql://localhost:3306/nessalboardingdb";
        final String USERNAME = "root";
        final String PASSWORD = "Grace/6190";

        try{

            Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM staff WHERE username=? AND password=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                user = new User ();
                user.name = resultSet.getString("name");
                user.email = resultSet.getString("email");
                user.username = resultSet.getString("username");
                user.password = resultSet.getString("password");
            }

            stmt.close();
            connection.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return user;
    }


    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm(null);
        User user = loginForm.user;
        if (user != null){
            System.out.println("Successful Authentication");
        }
        else {
            System.out.println("Authentication Cancelled");
        }
    }
}
