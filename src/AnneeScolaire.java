import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AnneeScolaire extends  JFrame{
    private JButton saveButton;
    private JTable tableAnneeScolaire;
    private JTextField txtCodeAnnee;
    private JTextField txtDateDebut;
    private JTextField txtDateFin;
    private JPanel mainAnneePanel;


    Connection con;
    PreparedStatement pst;


    public AnneeScolaire() throws HeadlessException {

        connect();
        initializeUI();
        tableAnneeScolaire_load();



        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Récupérer les données à partir des champs de saisie
                String codeAnnee = txtCodeAnnee.getText();
                Date dateDebut = Date.valueOf(txtDateDebut.getText());
                Date dateFin = Date.valueOf(txtDateFin.getText());

                // Vérifier si les champs sont remplis
                if (codeAnnee.isEmpty() || dateDebut == null || dateFin == null) {
                    JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs.");
                } else {
                    try {
                        // Créer une requête SQL pour insérer une nouvelle année scolaire
                        String sql = "INSERT INTO AnneeScolaire (code, date_debut, date_fin) VALUES (?, ?, ?)";
                        PreparedStatement pstmt = con.prepareStatement(sql);
                        pstmt.setString(1, codeAnnee);
                        pstmt.setDate(2, dateDebut);
                        pstmt.setDate(3, dateFin);

                        // Exécuter la requête d'insertion
                        int rowsAffected = pstmt.executeUpdate();
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "Année scolaire enregistrée avec succès.");

                            // Réinitialiser les champs de saisie
                            txtCodeAnnee.setText("");
                            txtDateDebut.setText("");
                            txtDateFin.setText("");

                            // Mettre à jour la table des années scolaires
                            tableAnneeScolaire_load();
                        } else {
                            JOptionPane.showMessageDialog(null, "Erreur lors de l'enregistrement de l'année scolaire.");
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
        setTitle("AnneeScolaire");
        setContentPane(mainAnneePanel);
        pack();
        setLocationRelativeTo(null); // Centrer la fenêtre sur l'écran
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Annee Scolaire");
        frame.setContentPane(new AnneeScolaire().mainAnneePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
//        frame.setVisible(true);
        SwingUtilities.invokeLater(()->{
            AnneeScolaire anneeScolaire = new AnneeScolaire();
            anneeScolaire.setVisible(true);
        });
    }


    public void  tableAnneeScolaire_load(){
        try{
            pst = con.prepareStatement("SELECT * FROM anneescolaire");
            ResultSet rs = pst.executeQuery();
            tableAnneeScolaire.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
