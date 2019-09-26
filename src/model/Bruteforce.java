package model;

import java.sql.SQLException;

import controller.WKF_Decrypt;

/**
 * @author Matthieu Carteron
 */
public class Bruteforce implements Runnable
{
    // Référence sur le contrôleur :
    private final WKF_Decrypt controller;
    
    // Les paramètres du bruteforce :
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
     * @param key Le début de la clef.
     * @param charLeft Les caractères à bruteforce.
     */
    public Bruteforce(final WKF_Decrypt controller, final String key, final int charLeft)
    {
        this.controller = controller;
        
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
        String decrypted;
        String testKey;

        // On commence par générer une chaîne de caractères qui fera office de première clef :
        for (int i = 0; i < this.charLeft; i++)
            this.tempKey[i] = KEY_ALPHABET[0];

        /*this.view.lheader.setForeground(Color.black);
        this.view.lheader.setText("Clefs utilisés : 0 / " + toBruteforce + " (0 % testés)...");

        SwingUtilities.updateComponentTreeUI(this.view.frame);*/

        for (long i = 0; i < this.toBruteforce; i++)
        {
            // On décale la clef précédente d'une lettre :
            this.shiftKey(this.tempKey, this.charLeft);

            // On transforme la clef en lettres selon l'alphabet :
            for (int j = 0; j < this.charLeft; j++)
                this.tempKeyChar[j] = KEY_ALPHABET[this.tempKey[j]];

            // On combine les deux :
            testKey = this.lkey + new String(this.tempKeyChar);

            decrypted = this.controller.getDecrypter().decrypt(data, this.testKey);

            // On compare avec le dictionnaire chacun des mots pour contrôler la validité de la clef :
            try
            {
                if (this.controller.checkDictionary(decrypted))
                {
                    // Enfin, on écrit le contenu dans le fichier destination :
                    this.controller.getFiles().setData(destination_path, decrypted);

                    // On set le succès dans le retour :
                    result.succeed = true;
                    result.key = testKey;

                    return result;
                }
            }
            catch (final SQLException e)
            {
            }
        }
    }
}
