import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Matiere extends  JFrame{
    private JButton saveButton;
    private JTextField txtIntitule;
    private JTextField txtCodeMatiere;
    private JTable tableMatiere;
    private JPanel mainMatierePanel;
    private JButton searchButton;
    private JTextField txtRecherche;

    Connection con;
    PreparedStatement pst;

    public Matiere() throws HeadlessException {

        connect();
        initializeUI();
        tableMatiere_load();


        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String intitule = txtIntitule.getText();
                String code = txtCodeMatiere.getText();

                if (intitule.isEmpty() || code.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs.");
                } else {
                    try {
                        String sql = "INSERT INTO Matiere (intitule, code) VALUES (?, ?)";
                        PreparedStatement pstmt = con.prepareStatement(sql);
                        pstmt.setString(1, intitule);
                        pstmt.setString(2, code);

                        int rowsAffected = pstmt.executeUpdate();
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "Matière enregistrée avec succès.");

                            txtIntitule.setText("");
                            txtCodeMatiere.setText("");

                            tableMatiere_load();
                        } else {
                            JOptionPane.showMessageDialog(null, "Erreur lors de l'enregistrement de la matière.");
                        }
                    } catch (SQLException sqle) {
                        throw new RuntimeException(sqle);
                    }
                }
            }
        });


        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String rechercheMatiere = txtRecherche.getText();

                try {
                    pst = con.prepareStatement("SELECT * FROM Matiere WHERE intitule= ? OR code = ?");
                    pst.setString(1,rechercheMatiere);
                    pst.setString(2,rechercheMatiere);
                    ResultSet rs = pst.executeQuery();
                }catch (SQLException sqll){
                    throw  new RuntimeException(sqll);
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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Matiere");
        setContentPane(mainMatierePanel);
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Matiere");
        frame.setContentPane(new Matiere().mainMatierePanel);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
//        frame.setVisible(true);
        SwingUtilities.invokeLater(()->{
            Matiere matiere = new Matiere();
            matiere.setVisible(true);
        });
    }

    public void  tableMatiere_load(){
        try{
            pst = con.prepareStatement("SELECT * FROM Matiere");
            ResultSet rs = pst.executeQuery();
            tableMatiere.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
