import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class Classes extends JFrame{
    private JButton saveButton;
    private JTextField txtIntitule;
    private JTextField txtCodeClasse;
    private JTable tableClasses;
    private JPanel mainClassePanel;

    Connection con;
    PreparedStatement pst;

    public Classes() {


        connect();
        initializeUI();
        tableClasse_load();


        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String intitule = txtIntitule.getText();
                String code = txtCodeClasse.getText();

                if (intitule.isEmpty() || code.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs.");
                } else {
                    try {
                        String sql = "INSERT INTO Classe (intitule, code) VALUES (?, ?)";
                        PreparedStatement pstmt = con.prepareStatement(sql);
                        pstmt.setString(1, intitule);
                        pstmt.setString(2, code);

                        int rowsAffected = pstmt.executeUpdate();
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "Classe enregistrée avec succès.");

                            txtIntitule.setText("");
                            txtCodeClasse.setText("");

                            tableClasse_load();
                        } else {
                            JOptionPane.showMessageDialog(null, "Erreur lors de l'enregistrement de la classe.");
                        }
                    } catch (SQLException sqle) {
                        throw new RuntimeException(sqle);
                    }
                }
            }
        });

    }
    public  void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/projetjava","root","");
            System.out.println("Ok Connexion établie avec succès");

        }
        catch (ClassNotFoundException ex)
        {

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void initializeUI() {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setTitle("Classes");
        setContentPane(mainClassePanel);
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Classes");
        frame.setContentPane(new Classes().mainClassePanel);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
//        frame.setVisible(true);
        SwingUtilities.invokeLater(()->{
            Classes classes = new Classes();
            classes.setVisible(true);
        });
    }

    public void  tableClasse_load(){
        try{
            pst = con.prepareStatement("SELECT * FROM Classe");
            ResultSet rs = pst.executeQuery();
            tableClasses.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
