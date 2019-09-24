package view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Container;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import controller.WKF_Cpte;
import javax.swing.BoxLayout;

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
    private static final String DIALOG_TITLE = "Authentification";
    
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
        // Récupération du container :
        final Container panel = frame.getContentPane();
        
        // Labels :
        final JLabel lheader = new JLabel("Veuillez entrer les informations d'authentification.");
        final JLabel llogin = new JLabel("Login :");
        final JLabel lpassword = new JLabel("Mot de passe :");
        
        // Bouton de confirmation :
        final JButton confirm = new JButton("Connexion");
        
        // Champs de texte :
        final JTextField login = new JTextField();
        final JTextField password = new JTextField();
        
        // Positionnement & paramétrage des widgets :
        panel.setLayout(null);
        
        lheader.setBounds(160, 24, 320, 16);
        llogin.setBounds(32, 100, 480, 16);
        lpassword.setBounds(32, 140, 480, 16);
        
        confirm.setBounds(120, 200, 400, 40);
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
        
        login.setBounds(150, 100, 380, 16);
        password.setBounds(150, 140, 380, 16);
        
        // Ajout des widgets :
        panel.add(lheader);
        panel.add(llogin);
        panel.add(lpassword);
        
        panel.add(confirm);
        
        panel.add(login);
        panel.add(password);
        
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
