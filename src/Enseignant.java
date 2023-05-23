import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import net.proteanit.sql.DbUtils;


public class Enseignant extends JFrame{


    private JButton saveButton;
    private JButton deleteButton;
    private JTextField txtNom;
    private JTextField txtPrenom;
    private JTextField txtTelephone;
    private JTextField txtNumMatricule;
    private JTextField txtEmail;
    private JButton updateButton;
    private JButton searchButton;
    private JTable enseignantTable;
    private JTextField txtRechercheEnseignant;
    private JPanel mainPanel;


    Connection con;
    PreparedStatement pst;

    public Enseignant() throws HeadlessException {

        connect();
        initializeUI();
        tableEnseignant_load();


        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nom, prenom,telephone,num_matricule,email;

                nom = txtNom.getText();
                prenom = txtPrenom.getText();
                telephone = txtTelephone.getText();
                num_matricule = txtNumMatricule.getText();
                email = txtEmail.getText();

                try{
                    pst = con.prepareStatement("INSERT INTO Enseignant (nom, prenom, telephone, num_matricule, email) VALUES (?,?,?,?,?)");
                    pst.setString(1,nom);
                    pst.setString(2,prenom);
                    pst.setString(3,telephone);
                    pst.setString(4,num_matricule);
                    pst.setString(5,email);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, " OK Enseignant enregistrer ! ! !");
                    tableEnseignant_load();


                    txtNom.setText("");
                    txtPrenom.setText("");
                    txtTelephone.setText("");
                    txtNumMatricule.setText("");
                    txtEmail.setText("");

                }catch (SQLException sqll){
                    throw  new RuntimeException(sqll);
                }

            }
        });


        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    String rechercheEns = txtRechercheEnseignant.getText();

                    pst = con.prepareStatement("SELECT nom,prenom,telephone,num_matricule,email FROM Enseignant WHERE nom = ? OR prenom = ? OR telephone = ?");
                    pst.setString(1,rechercheEns);
                    pst.setString(2,rechercheEns);
                    pst.setString(3,rechercheEns);
                    ResultSet rs = pst.executeQuery();

                    if (rs.next()==true){
                        String nom, prenom,telephone,num_matricule,email;

                        nom= rs.getString(1);
                        prenom= rs.getString(2);
                        telephone= rs.getString(3);
                        num_matricule= rs.getString(4);
                        email= rs.getString(5);


                        txtNom.setText(nom);
                        txtPrenom.setText(prenom);
                        txtTelephone.setText(telephone);
                        txtNumMatricule.setText(num_matricule);
                        txtEmail.setText(email);



                    }else {
                        txtNom.setText("");
                        txtPrenom.setText("");
                        txtTelephone.setText("");
                        txtNumMatricule.setText("");
                        txtEmail.setText("");

                        JOptionPane.showMessageDialog(null,"Information invalide");

                    }

                }catch (SQLException sqll){
                    throw  new RuntimeException(sqll);
                }



            }
        });


        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nom, prenom,telephone,num_matricule,email;
                String rechercheEns = txtRechercheEnseignant.getText();


                nom = txtNom.getText();
                prenom = txtPrenom.getText();
                telephone = txtTelephone.getText();
                num_matricule = txtNumMatricule.getText();
                email = txtEmail.getText();

                try {
                    pst = con.prepareStatement("UPDATE Enseignant SET nom = ?, prenom = ?, telephone = ? ,num_matricule=?,email=?   WHERE nom = ? OR prenom = ? OR telephone = ? ");
                    pst.setString(1,nom);
                    pst.setString(2,prenom);
                    pst.setString(3,telephone);
                    pst.setString(4,num_matricule);
                    pst.setString(5,email);
                    pst.setString(6,rechercheEns);
                    pst.setString(7,rechercheEns);
                    pst.setString(8,rechercheEns);




                    pst.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Mis a jour effectuer ! ! !");

                    tableEnseignant_load();


                    txtNom.setText("");
                    txtPrenom.setText("");
                    txtTelephone.setText("");
                    txtNumMatricule.setText("");
                    txtEmail.setText("");


                }catch (SQLException sqll){
                    throw  new RuntimeException(sqll);
                }

            }
        });



        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String rechercheEns = txtRechercheEnseignant.getText();

                try {
                    pst = con.prepareStatement("DELETE FROM Enseignant WHERE telephone = ?");
                    pst.setString(1, rechercheEns);
                    int rowsAffected = pst.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Enseignant supprimé avec succès");
                        tableEnseignant_load();
                        txtNom.setText("");
                        txtPrenom.setText("");
                        txtTelephone.setText("");
                        txtNumMatricule.setText("");
                        txtEmail.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Aucun enseignant trouvé avec ce numéro de téléphone");
                    }
                } catch (SQLException sqle) {
                    throw new RuntimeException(sqle);
                }
            }
        });
    }

//    public JPanel getMainPanel() {
//        return mainPanel;
//    }

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
        setTitle("Enseignant");
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null); // Centrer la fenêtre sur l'écran
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Enseignant");
        frame.setContentPane(new Enseignant().mainPanel);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
//        frame.setVisible(true);
        SwingUtilities.invokeLater(()->{
            Enseignant enseignant = new Enseignant();
            enseignant.setVisible(true);
        });
    }




    void tableEnseignant_load(){

        try{
            pst = con.prepareStatement("SELECT * FROM enseignant");
            ResultSet rs = pst.executeQuery();
            enseignantTable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
