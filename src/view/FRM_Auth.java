package view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import controller.WKF_Cpte;

/**
 * @author Matthieu Carteron
 */
public class FRM_Auth
{
    // Le contrôleur référencé :
    private final WKF_Cpte controller;
    
    // La frame principale :
    public final JFrame frame;
    
    // Les constantes de la fenêtre :
    private final String DIALOG_TITLE = "Authentification";
    
    /**
     * Créé la vue.
     * @param controller Référence sur le contrôleur.
     */
    public FRM_Auth(final WKF_Cpte controller)
    {
        this.controller = controller;
        this.frame = new JFrame(DIALOG_TITLE);
        
        this.InitWindow();
    }
    
    // Initialise la fenêtre :
    private void InitWindow()
    {
        // Création des panneaux :
        final JPanel panel = new JPanel();

        final JPanel pheader = new JPanel();
        final JPanel plogin = new JPanel();
        final JPanel ppassword = new JPanel();
        final JPanel pconfirm = new JPanel();
                
        // Labels :
        final JLabel lheader = new JLabel("Veuillez entrer les informations d'authentification.", SwingConstants.CENTER);
        final JLabel llogin = new JLabel("Login :");
        final JLabel lpassword = new JLabel("Mot de passe :");
        
        // Bouton de confirmation :
        final JButton confirm = new JButton("Connexion");
        
        // Champs de texte :
        final JTextField login = new JTextField();
        final JPasswordField password = new JPasswordField();
        
        // Positionnement & paramétrage des widgets :
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        pheader.setBorder(new EmptyBorder(new Insets(8, 8, 8, 8)));
        
        plogin.setLayout(new GridLayout(1, 2));
        plogin.setBorder(new EmptyBorder(new Insets(48, 64, 16, 32)));
        
        ppassword.setLayout(new GridLayout(1, 2));
        ppassword.setBorder(new EmptyBorder(new Insets(16, 64, 16, 32)));
        
        pconfirm.setLayout(new BorderLayout());
        pconfirm.setBorder(new EmptyBorder(new Insets(16, 32, 16, 32)));
        
        confirm.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // On teste les identifiants entrés :
                if (controller.pcs_authentifier(login.getText(), password.getText()))
                    controller.OnLogin();
                else
                {
                    lheader.setText("Erreur ! Login ou mot de passe invalide !");
                    lheader.setForeground(Color.red);
                }
            }
        });
        
        // Ajout des widgets :
        pheader.add(lheader);
        
        plogin.add(llogin);
        plogin.add(login);
                
        ppassword.add(lpassword);
        ppassword.add(password);
        
        pconfirm.add(confirm);
        
        panel.add(pheader);
        panel.add(plogin);
        panel.add(ppassword);
        panel.add(pconfirm);
        
        frame.add(panel);
        
        // Paramétrage de la fenêtre :
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        frame.setSize(640, 320);
        frame.setLocationRelativeTo(null);
        
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                controller.OnClose();
            }
        });
        
        frame.setVisible(true);
    }
}
