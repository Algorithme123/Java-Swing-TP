import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Cours extends  JFrame{
    private JComboBox cmbMatiere;
    private JComboBox cmbEnseignant;
    private JComboBox cmbClasse;
    private JComboBox cmbAnnee;
    private JPanel mainCoursPanel;
    private JButton saveButton;
    private JTable table_Cours;


    Connection con;
    PreparedStatement pst;


    public Cours() throws HeadlessException {
        connect();
        initializeUI();
        tableCours_load();


        loadMatiereData();
        loadClasseData();
        loadAnneeScolaireData();
        loadEnseignantData();




        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String matiere = cmbMatiere.getSelectedItem().toString();
                String enseignant = cmbEnseignant.getSelectedItem().toString();
                String classe = cmbClasse.getSelectedItem().toString();
                String anneeScolaire = cmbAnnee.getSelectedItem().toString();

                if (matiere.isEmpty() || enseignant.isEmpty() || classe.isEmpty() || anneeScolaire.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs.");
                } else {
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        con = DriverManager.getConnection("jdbc:mysql://localhost/projetjava","root","");
                        // Récupérer l'ID de la matière sélectionnée
                        String matiereIdQuery = "SELECT id FROM Matiere WHERE intitule = ?";
                        PreparedStatement matiereIdStmt = con.prepareStatement(matiereIdQuery);
                        matiereIdStmt.setString(1, matiere);
                        ResultSet matiereIdResult = matiereIdStmt.executeQuery();
                        int matiereId = 0;
                        if (matiereIdResult.next()) {
                            matiereId = matiereIdResult.getInt("id");
                        }

                        // Récupérer l'ID de l'enseignant sélectionné
                        String enseignantIdQuery = "SELECT id FROM Enseignant WHERE nom = ?";
                        PreparedStatement enseignantIdStmt = con.prepareStatement(enseignantIdQuery);
                        enseignantIdStmt.setString(1, enseignant);
                        ResultSet enseignantIdResult = enseignantIdStmt.executeQuery();
                        int enseignantId = 0;
                        if (enseignantIdResult.next()) {
                            enseignantId = enseignantIdResult.getInt("id");
                        }

                        // Récupérer l'ID de la classe sélectionnée
                        String classeIdQuery = "SELECT id FROM Classe WHERE intitule = ?";
                        PreparedStatement classeIdStmt = con.prepareStatement(classeIdQuery);
                        classeIdStmt.setString(1, classe);
                        ResultSet classeIdResult = classeIdStmt.executeQuery();
                        int classeId = 0;
                        if (classeIdResult.next()) {
                            classeId = classeIdResult.getInt("id");
                        }

                        // Récupérer l'ID de l'année scolaire sélectionnée
                        String anneeScolaireIdQuery = "SELECT id FROM AnneeScolaire WHERE code = ?";
                        PreparedStatement anneeScolaireIdStmt = con.prepareStatement(anneeScolaireIdQuery);
                        anneeScolaireIdStmt.setString(1, anneeScolaire);
                        ResultSet anneeScolaireIdResult = anneeScolaireIdStmt.executeQuery();
                        int anneeScolaireId = 0;
                        if (anneeScolaireIdResult.next()) {
                            anneeScolaireId = anneeScolaireIdResult.getInt("id");
                        }

                        // Enregistrer le cours dans la base de données
                        String insertQuery = "INSERT INTO Cours (id_matiere, id_enseignant, id_classe, id_annee_scolaire) VALUES (?, ?, ?, ?)";
                        PreparedStatement pstmt = con.prepareStatement(insertQuery);
                        pstmt.setInt(1, matiereId);
                        pstmt.setInt(2, enseignantId);
                        pstmt.setInt(3, classeId);
                        pstmt.setInt(4, anneeScolaireId);

                        int rowsAffected = pstmt.executeUpdate();
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "Cours enregistré avec succès.");
                            tableCours_load();


                            cmbMatiere.setSelectedIndex(0);
                            cmbEnseignant.setSelectedIndex(0);
                            cmbClasse.setSelectedIndex(0);
                            cmbAnnee.setSelectedIndex(0);

                        } else {
                            JOptionPane.showMessageDialog(null, "Erreur lors de l'enregistrement du cours.");
                        }

                        con.close();
                    } catch (ClassNotFoundException | SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });



    }

    private void loadMatiereData() {
        try {
            String query = "SELECT intitule FROM Matiere";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                cmbMatiere.addItem(rs.getString("intitule"));
            }

//            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadEnseignantData() {
        try {
            String query = "SELECT nom FROM Enseignant";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                cmbEnseignant.addItem(rs.getString("nom"));
            }

//            con.close();
        } catch ( SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadClasseData() {
        try {

            String query = "SELECT intitule FROM Classe";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                cmbClasse.addItem(rs.getString("intitule"));
            }

//            con.close();
        } catch ( SQLException ex) {
            ex.printStackTrace();
        }
    }


    private void loadAnneeScolaireData() {
        try {

            String query = "SELECT code FROM AnneeScolaire";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                cmbAnnee.addItem(rs.getString("code"));
            }

//            con.close();
        } catch ( SQLException ex) {
            ex.printStackTrace();
        }
    }


    public void tableCours_load() {
        try {
            String query = "SELECT C.id, M.intitule AS matiere, E.nom AS enseignant, Cl.intitule AS classe, AS.code AS annee_scolaire " +
                    "FROM Cours C " +
                    "INNER JOIN Matiere M ON C.id_matiere = M.id " +
                    "INNER JOIN Enseignant E ON C.id_enseignant = E.id " +
                    "INNER JOIN Classe Cl ON C.id_classe = Cl.id " +
                    "INNER JOIN AnneeScolaire `AS` ON C.id_annee_scolaire = `AS`.id";

            pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            table_Cours.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public static void main(String[] args) {
        JFrame frame = new JFrame("Cours");
        frame.setContentPane(new Cours().mainCoursPanel);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();

        SwingUtilities.invokeLater(()->{
            Cours cours = new Cours();
            cours.setVisible(true);
        });
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setTitle("Cours");
        setContentPane(mainCoursPanel);
        pack();
        setLocationRelativeTo(null);
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

}
