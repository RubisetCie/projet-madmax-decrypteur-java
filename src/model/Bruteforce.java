package model;

import javax.swing.JLabel;
import java.awt.Color;
import java.sql.SQLException;

import controller.WKF_Decrypt;

/**
 * @author Matthieu Carteron
 */
public class Bruteforce implements Runnable
{
    // Référence sur le contrôleur :
    private final WKF_Decrypt controller;
    
    // Référence sur le label permettant de renseigner sur l'état du thread :
    private final JLabel infoLabel;
    
    // Référence sur le résultat :
    private final DecryptResult result;
    
    // Les paramètres du bruteforce :
    private final String data;          // La chaîne source.
    private final String destination;   // L'adresse du fichier destination.
    private final String lkey;          // Le début de la clef.
    private final int charLeft;         // Les caractères à bruteforce.
    private final long toBruteforce;    // Le nombre de combinaisons à tester.
    private final int[] tempKey;        // Les indexes des caractères composant la clef temporaire.
    private final char[] tempKeyChar;   // Les caractères composant la clef temporaire.
    
    // Le nombre de caractères différents pour une lettre de la clef :
    private final int KEY_ALPHABET_LENGTH = 26;
    
    // L'alphabet possible pour la clef :
    // Note : on pose pour hypothèse que la clef n'est composée que de lettres de l'alphabet miniscule (26 possibilités) :
    private final char[] KEY_ALPHABET = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    
    /**
     * Créé le thread avec les paramètres de bruteforce.
     * @param controller Référence sur le contrôleur.
     * @param data La chaîne source.
     * @param destination L'adresse du fichier destination.
     * @param key Le début de la clef.
     * @param charLeft Les caractères à bruteforce.
     * @param result Référence sur le résultat à retourner.
     */
    public Bruteforce(final WKF_Decrypt controller, final String data, final String destination, final String key, final int charLeft, final DecryptResult result)
    {
        this.controller = controller;
        this.infoLabel = controller.getView().getHeaderLabel();
        this.result = result;
        
        this.data = data;
        this.destination = destination;
        this.lkey = key;
        this.charLeft = charLeft;
        this.toBruteforce = (long)Math.pow(KEY_ALPHABET_LENGTH, charLeft);
        this.tempKey = new int[charLeft];
        this.tempKeyChar = new char[charLeft];
    }
    
    // Décale la clef précédente d'une lettre vers la droite :
    private void shiftKey(final int[] key, final int l)
    {
        for (int i = 0; i < l; i++)
        {
            key[i]++;
            
            if (key[i] >= KEY_ALPHABET_LENGTH)
                key[i] = 0;
            else
                return;
        }
    }
    
    /**
     * Exécute le thread lançant le décryptage bruteforce.
     */
    @Override
    public void run()
    {
        // Déclaration des variables locales au thread :
        String decrypted = "";
        String testKey;

        // On commence par générer une chaîne de caractères qui fera office de première clef :
        for (int i = 0; i < this.charLeft; i++)
            this.tempKey[i] = KEY_ALPHABET[0];

        // On donne des infos sur l'exécution du bruteforce :
        this.infoLabel.setForeground(Color.black);
        this.infoLabel.setText("Clefs utilisés : 0 / " + toBruteforce + " (0 % testés)...");

        for (long i = 0; i < this.toBruteforce; i++)
        {
            // On décale la clef précédente d'une lettre :
            this.shiftKey(this.tempKey, this.charLeft);

            // On transforme la clef en lettres selon l'alphabet :
            for (int j = 0; j < this.charLeft; j++)
                this.tempKeyChar[j] = KEY_ALPHABET[this.tempKey[j]];

            // On combine les deux :
            testKey = this.lkey + new String(this.tempKeyChar);

            // On tente un décryptage :
            decrypted = this.controller.getDecrypter().decrypt(this.data, testKey);

            // On compare avec le dictionnaire chacun des mots pour contrôler la validité de la clef :
            try
            {
                // Si le texte est reconnu, on signale que le thread est terminé :
                if (this.controller.checkDictionary(decrypted))
                {
                    // On set la clef de décryptage dans le retour :
                    this.result.key = testKey;
                    this.result.recognized = true;
                    
                    break;
                }
            }
            catch (final SQLException e)
            {
            }
            
            // On donne des infos sur la progression du bruteforce :
            this.infoLabel.setText("Clefs utilisés : " + i + " / " + this.toBruteforce + " (" + (int)(((float)i / (float)this.toBruteforce) * 100.0f) + " % testés)...");
        }
        
        // On signale au contrôleur que le thread est terminé :
        this.controller.onFinished(this.destination, decrypted, this.result);
    }
}
