package view;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
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
import java.io.File;

import controller.WKF_Decrypt;
import model.KeyTextField;

/**
 * @author Matthieu Carteron
 */
public class FRM_Decrypt
{
    // Le contrôleur référencé :
    private final WKF_Decrypt controller;
    
    // La frame principale :
    public final JFrame frame;
    
    // Le label pour afficher les informations du processus :
    public JLabel lheader;
    
    // Les chemins d'accès des fichiers :
    private String fileInput = "";
    private String fileOutput = "";
    
    // Les constantes de la fenêtre :
    private final String DIALOG_TITLE = "Outil de cryptage";
    
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
        // Création des panneaux :
        final JPanel panel = new JPanel();
        
        final JPanel pheader = new JPanel();
        final JPanel pinput = new JPanel();
        final JPanel pinputb = new JPanel();
        final JPanel pinputl = new JPanel();
        final JPanel poutput = new JPanel();
        final JPanel poutputb = new JPanel();
        final JPanel poutputl = new JPanel();
        final JPanel pkey = new JPanel();
        final JPanel pconfirm = new JPanel();
        
        // Labels :
        this.lheader = new JLabel("Veuillez choisir les fichiers source et destination puis entrer la clef de décryptage.", SwingConstants.CENTER);
        final JLabel linput = new JLabel("Aucun fichier sélectionné.");
        final JLabel loutput = new JLabel("Aucun fichier sélectionné.");
        final JLabel lkey = new JLabel("Clef : 0 / 12 caractères (à Bruteforce : 12) :");
        
        // Boutons :
        final JButton input = new JButton("Choisir source");
        final JButton output = new JButton("Choisir destination");
        final JButton act = new JButton("Crypter/Décrypter");
        
        // Champ de texte :
        final JTextField key = new JTextField();
        final KeyTextField keyfield = new KeyTextField(lkey, 12);   // Champ de texte custom !
        
        // Positionnement & paramétrage des widgets :
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        pheader.setBorder(new EmptyBorder(new Insets(8, 8, 8, 8)));
        
        pinput.setLayout(new GridLayout(1, 2));
        pinput.setBorder(new EmptyBorder(new Insets(32, 32, 16, 16)));
        
        pinputb.setLayout(new BorderLayout());
        pinputb.setBorder(new EmptyBorder(new Insets(2, 8, 2, 8)));
        
        pinputl.setLayout(new BorderLayout());
        pinputl.setBorder(new EmptyBorder(new Insets(8, 8, 8, 8)));
        
        poutput.setLayout(new GridLayout(1, 2));
        poutput.setBorder(new EmptyBorder(new Insets(16, 32, 16, 16)));
        
        poutputb.setLayout(new BorderLayout());
        poutputb.setBorder(new EmptyBorder(new Insets(2, 8, 2, 8)));
        
        poutputl.setLayout(new BorderLayout());
        poutputl.setBorder(new EmptyBorder(new Insets(8, 8, 8, 8)));
        
        pkey.setLayout(new GridLayout(1, 2));
        pkey.setBorder(new EmptyBorder(new Insets(16, 64, 16, 32)));
        key.setDocument(keyfield);
        
        pconfirm.setLayout(new BorderLayout());
        pconfirm.setBorder(new EmptyBorder(new Insets(8, 64, 16, 64)));
        
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
        
        output.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String destination = chooseDestination();
        
                if (!destination.isEmpty())
                {
                    fileOutput = destination;
                    loutput.setText(fileOutput);
                }
            }
        });
        
        act.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // On vérifie que les paramètres sont valides pas vides :
                String k = key.getText();
                
                if (new File(fileInput).exists() && !fileOutput.isEmpty() && !k.isEmpty())
                {
                    // On décrypte le fichier :
                    switch (controller.pcs_decrypter(fileInput, fileOutput, k))
                    {
                        case 0 :
                            lheader.setText("L'opération a échouée.");
                            lheader.setForeground(Color.red);
                            break;
                        case 1 :
                            lheader.setText("L'opération s'est déroulée correctement mais le décryptage pourrait être invalide.");
                            lheader.setForeground(Color.orange);
                            break;
                        case 2 :
                            lheader.setText("Le décryptage s'est déroulé avec succès.");
                            lheader.setForeground(Color.green);
                            break;
                    }
                }
                else
                {
                    lheader.setText("Erreur ! Les paramètres sont invalides !");
                    lheader.setForeground(Color.red);
                }
            }
        });
        
        // Ajout des widgets :
        pheader.add(lheader);
        
        pinput.add(pinputb);
        pinput.add(pinputl);
        
        pinputb.add(input);
        
        pinputl.add(linput);
        
        poutput.add(poutputb);
        poutput.add(poutputl);
        
        poutputb.add(output);
        
        poutputl.add(loutput);
        
        pkey.add(lkey);
        pkey.add(key);
        
        pconfirm.add(act);
        
        panel.add(pheader);
        panel.add(pinput);
        panel.add(poutput);
        panel.add(pkey);
        panel.add(pconfirm);
        
        frame.add(panel);
        
        // Paramétrage de la fenêtre :
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        frame.setSize(640, 360);
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
