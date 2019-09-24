package view;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.io.File;

import controller.WKF_Decrypt;

/**
 * @author Matthieu Carteron
 */
public class FRM_Decrypt
{
    // Le contrôleur référencé :
    private final WKF_Decrypt controller;
    
    // La frame principale :
    public final JFrame frame;
    
    // Les chemins d'accès des fichiers :
    private String fileInput = "";
    private String fileOutput = "";
    
    // Les constantes de la fenêtre :
    private static final String DIALOG_TITLE = "Outil de cryptage";
    
    /**
     * Créé la vue.
     * @param controller Référence sur le contrôleur.
     */
    public FRM_Decrypt(final WKF_Decrypt controller)
    {
        this.controller = controller;
        this.frame = new JFrame(DIALOG_TITLE);
        
        this.InitWindow();
        
        // On commence par demander de choisir le fichier :
        /*String source = this.chooseSource();
        
        if (source.isEmpty())
            controller.OnCancel();
        
        // Ensuite, on demande de sélectionner la destination :
        String destination = this.chooseDestination();
        
        if (destination.isEmpty())
            controller.OnCancel();*/
    }
    
    // Initialise la fenêtre :
    private void InitWindow()
    {
        // Récupération du container :
        final Container panel = frame.getContentPane();
        
        // Labels :
        final JLabel lheader = new JLabel("Veuillez choisir les fichiers source et destination puis entrer la clef de décryptage.");
        final JLabel linput = new JLabel("Aucun fichier sélectionné.");
        final JLabel loutput = new JLabel("Aucun fichier sélectionné.");
        final JLabel lkey = new JLabel("Clef :");
        
        // Boutons :
        final JButton input = new JButton("Choisir source");
        final JButton output = new JButton("Choisir destination");
        final JButton act = new JButton("Crypter/Décrypter");
        
        // Champ de texte :
        final JTextField key = new JTextField();
        
        // Positionnement & paramétrage des widgets :
        panel.setLayout(null);
        
        lheader.setBounds(80, 24, 640, 16);
        linput.setBounds(194, 86, 480, 16);
        loutput.setBounds(194, 136, 480, 16);
        lkey.setBounds(96, 186, 480, 16);
        
        input.setBounds(32, 80, 150, 28);
        input.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String source = chooseSource();
        
                if (!source.isEmpty())
                {
                    fileInput = source;
                    linput.setText(fileInput);
                }
            }
        });
        
        output.setBounds(32, 130, 150, 28);
        output.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String destination = chooseDestination();
        
                if (!destination.isEmpty())
                {
                    fileOutput = destination;
                    loutput.setText(fileInput);
                }
            }
        });
        
        act.setBounds(120, 240, 400, 40);
        act.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // On vérifie que les paramètres sont valides pas vides :
                String k = key.getText();
                
                if (new File(fileInput).exists() && new File(fileOutput).exists() && !k.isEmpty())
                {
                    // On décrypte le fichier :
                    controller.pcs_decrypter(fileInput, fileOutput, k);
                }
                else
                {
                    lheader.setText("Erreur ! Les paramètres sont invalides !");
                    lheader.setForeground(Color.red);
                }
            }
        });
        
        key.setBounds(150, 186, 380, 16);
        
        // Ajout des widgets :
        panel.add(lheader);
        panel.add(linput);
        panel.add(loutput);
        panel.add(lkey);
        
        panel.add(input);
        panel.add(output);
        panel.add(act);
        
        panel.add(key);
        
        // Paramétrage de la fenêtre :
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        frame.setSize(640, 340);
        frame.setLocationRelativeTo(null);
        
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                frame.dispose();
                controller.OnClose();
            }
        });
        
        frame.setVisible(true);
    }
    
    private String chooseSource()
    {
        final JFileChooser fc = new JFileChooser();
        
        int returnVal = fc.showOpenDialog(null);
    
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            // On vérifie si le fichier existe :
            final File selectedFile = fc.getSelectedFile();
            
            if (selectedFile.exists())
                return selectedFile.getAbsolutePath();
        }
        
        return "";
    }
    
    private String chooseDestination()
    {
        final JFileChooser fc = new JFileChooser();
        
        int returnVal = fc.showSaveDialog(null);
        
        if (returnVal == JFileChooser.APPROVE_OPTION)
            return fc.getSelectedFile().getAbsolutePath();
        
        return "";
    }
}
