import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Dashboard extends JFrame {

    private JMenuBar barreMenus ;
    private JMenu enseignant ;
    private JMenu anneeScolaires,matiere,classes, cours;
    private JMenuItem ouvrir, nouvelleAnneeScolaure, nouvelleMatiere, nouvelleClasse, nouvelleCours ;

    private JPanel panelEnseignant;

    Connection con;
    PreparedStatement pst;


    private JTable table_Cours;
    private JPanel panel1;

    public Dashboard() {

        initializeUI();
        connect();
        tableCours_load();


        setTitle ("Gestion d'emploi du temps") ;
        setSize (1000, 900) ;


        /* creation barre des menus */
        barreMenus = new JMenuBar() ;
        setJMenuBar(barreMenus) ;




        /* creation menu Fichier et ses options */
        enseignant = new JMenu ("Enseignant") ;
        barreMenus.add(enseignant) ;

        ouvrir = new JMenuItem ("Ouvrir") ;
        enseignant.add (ouvrir) ;



        //-------------------------------------------------------------------------------

//        add(panelEnseignant, BorderLayout.CENTER);



        panelEnseignant = new JPanel();
        panelEnseignant.setLayout(new BorderLayout());

        ouvrir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelEnseignant.removeAll();
                Enseignant newEnseignant = new Enseignant();
//                panelEnseignant.add(newEnseignant.getMainPanel(), BorderLayout.CENTER);
                newEnseignant.setVisible(true);

                panelEnseignant.revalidate(); // Rafraîchir l'affichage du panelEnseignant
                panelEnseignant.repaint();
            }
        });

        //------------------------------------------------------------



        anneeScolaires = new JMenu("Annees Scolaire");
        nouvelleAnneeScolaure = new JMenuItem("Ajouter");
        anneeScolaires.add(nouvelleAnneeScolaure);
        barreMenus.add(anneeScolaires) ;

        matiere = new JMenu("Matiere");
        nouvelleMatiere = new JMenuItem("Ajouter");
        matiere.add(nouvelleMatiere);
        barreMenus.add(matiere) ;

        classes = new JMenu("Classe");
        nouvelleClasse = new JMenuItem("Ajouter");
        classes.add(nouvelleClasse);
        barreMenus.add(classes) ;

        cours = new JMenu("Cours");
        nouvelleCours = new JMenuItem("Ajouter");
        cours.add(nouvelleCours);
        barreMenus.add(cours) ;

        nouvelleAnneeScolaure.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnneeScolaire anneeScolaire = new AnneeScolaire();
                anneeScolaire.setVisible(true);
            }
        });

        nouvelleMatiere.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Matiere matiere1 = new Matiere();
                matiere1.setVisible(true);
            }
        });

        nouvelleClasse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Classes classes1 = new Classes();
                classes1.setVisible(true);
            }
        });

        nouvelleCours.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Cours cours1 = new Cours();
                cours1.setVisible(true);
            }
        });






//        add(panelEnseignant, BorderLayout.CENTER);


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


//    public static void main(String[] args) {
//        System.out.printf("Hello and welcome!");
//
//        JFrame frame = new JFrame("DASHBOARD");
//        frame.setContentPane(new Dashboard().panel1);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
////        frame.setVisible(true);
//        SwingUtilities.invokeLater(()->{
//            Dashboard dashboard = new Dashboard();
//            dashboard.setVisible(true);
//        });
//
//    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Dashboard dashboard = new Dashboard();
            dashboard.setVisible(true);
        });
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setTitle("DASHBOARD");
        setContentPane(panel1);
        pack();
        setLocationRelativeTo(null); // Centrer la fenêtre sur l'écran
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

