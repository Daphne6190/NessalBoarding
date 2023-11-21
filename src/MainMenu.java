import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JDialog {
    private JButton btnPatients;
    private JButton btnClients;
    private JButton btnLogout;
    private JPanel menuPanel;

    JFrame frame = new JFrame();

    public MainMenu() {

        setTitle("Sign Up!"); //displays the title
        setContentPane(menuPanel); //displays the panel
        setMinimumSize(new Dimension(1000, 700)); //sets minimum size of the panel
        setModal(true);
        setLocationRelativeTo(null); //displays dialog in the middle of the frame
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); //terminates the dialog when the close button is clicked

        btnPatients.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == btnPatients){
                    PatientsForm patientsPanel = new PatientsForm();

                }

            }
        });
        btnClients.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == btnClients){
                    ClientsForm clientsPanel = new ClientsForm();

                }

            }
        });
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

            }
        });
        setVisible(true); //allows to view the panel
    }

    public static void main(String[]args){
        MainMenu mainMenu = new MainMenu();
    }

}
