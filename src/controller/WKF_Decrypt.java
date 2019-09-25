package controller;

//import javax.swing.SwingUtilities;
//import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;

import view.FRM_Decrypt;
import model.Files;
import model.Decrypt;
import model.Map_Dic;
import model.CAD;

/**
 * @author Matthieu Carteron
 */
public class WKF_Decrypt
{
    // La vue associée au contrôleur :
    private final FRM_Decrypt view;
    
    // Le composant permettant d'intéragir avec le fichier :
    private final Files files;
    
    // Le composant permettant de décrypter la chaîne de caractères :
    private final Decrypt decrypt;
    
    // Le composant de mappage de la table "personne" :
    private final Map_Dic map;
    
    // Le composant d'accès aux données :
    private final CAD cad;
    
    // Le ratio pour la validité du décryptage :
    private final float CORRECT_RATIO = 0.5f;
    
    // Le nombre de caractères différents pour une lettre de la clef :
    private final int KEY_ALPHABET_LENGTH = 26;
    
    // L'alphabet possible pour la clef :
    // Note : on pose pour hypothèse que la clef n'est composée que de lettres de l'alphabet miniscule (26 possibilités) :
    private final char[] KEY_ALPHABET = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    
    /**
     * Instantie la vue et le modèle.
     * @param cad Référence sur le composant d'accès aux données.
     */
    public WKF_Decrypt(final CAD cad)
    {
        // On instantie la vue d'authentification :
        this.view = new FRM_Decrypt(this);
        
        // On instantie les composants d'accès à la base de donnée :
        this.files = new Files();
        this.decrypt = new Decrypt();
        this.map = new Map_Dic();
        this.cad = cad;
    }
    
    /**
     * Appelé lorsque la fenêtre est fermée.
     */
    public void OnClose()
    {
        System.exit(0);
    }
    
    /**
     * Crypte/décrypte un fichier.
     * @param source_path L'adresse du fichier source à crypter.
     * @param destination_path L'adresse du fichier destination.
     * @param key La clef de cryptage.
     * @return Le succès du cryptage/décryptage.
     */
    public int pcs_decrypter(final String source_path, final String destination_path, final String key)
    {
        try
        {
            // On commence par récupérer le contenu du fichier :
            final String data = this.files.getData(source_path);
            //final String data = "";

            // Si la clef est complète (12 caractères), on ne tente qu'un seul décryptage :
            if (key.length() == 12)
            {
                // On décrypte les données :
                final String decrypted = this.decrypt.decrypt(data, key);

                // Enfin, on écrit le contenu dans le fichier destination :
                this.files.setData(destination_path, decrypted);

                // On compare avec le dictionnaire chacun des mots pour contrôler la validité de la clef :
                try
                {
                    if (this.checkDictionary(decrypted))
                        return 2;
                    else
                        return 1;
                }
                catch (final SQLException e)
                {
                    return 1;
                }
            }
            // Sinon, on 'bruteforce' le reste des caractères jusqu'a ce que la chaîne décryptée soit valide :
            else
            {
                final int charLeft = 12 - key.length();
                final long toBruteforce = (long)Math.pow(KEY_ALPHABET_LENGTH, charLeft);
                final int[] tempKey = new int[charLeft];
                final char[] tempKeyChar = new char[charLeft];

                String decrypted;
                String testKey;

                // On commence par générer une chaîne de caractères qui fera office de première clef :
                for (int i = 0; i < charLeft; i++)
                    tempKey[i] = KEY_ALPHABET[0];
                
                /*this.view.lheader.setForeground(Color.black);
                this.view.lheader.setText("Clefs utilisés : 0 / " + toBruteforce + " (0 % testés)...");
                
                SwingUtilities.updateComponentTreeUI(this.view.frame);*/

                for (long i = 0; i < toBruteforce; i++)
                {
                    // On décale la clef précédente d'une lettre :
                    this.shiftKey(tempKey, charLeft);

                    // On transforme la clef en lettres selon l'alphabet :
                    for (int j = 0; j < charLeft; j++)
                        tempKeyChar[j] = KEY_ALPHABET[tempKey[j]];

                    // On combine les deux :
                    testKey = key + new String(tempKeyChar);

                    decrypted = this.decrypt.decrypt(data, testKey);

                    // On compare avec le dictionnaire chacun des mots pour contrôler la validité de la clef :
                    try
                    {
                        if (this.checkDictionary(decrypted))
                        {
                            // Enfin, on écrit le contenu dans le fichier destination :
                            this.files.setData(destination_path, decrypted);

                            return 2;
                        }
                    }
                    catch (final SQLException e)
                    {
                    }
                    
/*                    this.view.lheader.setText("Clefs utilisés : " + i + " / " + toBruteforce + " (" + (i / toBruteforce) * 100 + " % testés)...");
                    
                    SwingUtilities.updateComponentTreeUI(this.view.frame);
                    
                    this.view.frame.invalidate();
                    this.view.frame.validate();
                    this.view.frame.repaint();*/
                }
            }
        }
        catch (final IOException e)
        {
        }
        
        return 0;
    }
    
    // Compare une chaîne décryptée avec le dictionnaire en ligne :
    private boolean checkDictionary(final String data) throws SQLException
    {
        final String[] words = data.split(" |\\.|\n");
        
        ResultSet rs;
        String sql;
        
        int correct = 0;

        for (final String word : words)
        {
            // On récupère la requête SQL à soumettre :
            sql = map.selectWord(word);

            // On interroge la base de données sur le mot :
            rs = cad.GetRows(sql);

            // Si le mot est valide, on incrémente le compteur :
            if (rs.next())
                correct++;

            rs.close();
        }

        // Enfin, on compare le nombre de mots corrects par rapport au nombre de mots :
        if ((float)correct / (float)words.length >= CORRECT_RATIO)
        {
            return true;
        }
        
        return false;
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
}
