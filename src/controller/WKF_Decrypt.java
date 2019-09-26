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
import model.Bruteforce;
import model.DecryptResult;

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
     * Appelé lorsque le bruteforce se termine.
     * @param destination L'adresse du fichier destination.
     * @param decrypted La chaîne de caractères décryptée.
     * @param result Référence sur le résultat à retourner.
     */
    public void onFinished(final String destination, final String decrypted, final DecryptResult result)
    {
        // On déverrouille la vue :
        this.view.setButtonsActive(true);
                
        // Enfin, on écrit le contenu dans le fichier destination :
        try
        {
            // Enfin, on écrit le contenu dans le fichier destination :
            this.files.setData(destination, decrypted);
            
            // On set le succès dans le retour :
            result.succeed = true;
            
            this.view.setResult(result);
        }
        catch (final IOException e)
        {
        }
    }
    
    /**
     * Crypte/décrypte un fichier.
     * @param source_path L'adresse du fichier source à crypter.
     * @param destination_path L'adresse du fichier destination.
     * @param key La clef de cryptage.
     * @return Le résultat du cryptage/décryptage.
     */
    public DecryptResult pcs_decrypter(final String source_path, final String destination_path, final String key)
    {
        final DecryptResult result = new DecryptResult();
        
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
                
                // On set le succès dans le retour :
                result.succeed = true;
                result.recognized = false;

                // On compare avec le dictionnaire chacun des mots pour contrôler la validité de la clef :
                try
                {
                    if (this.checkDictionary(decrypted))
                        result.recognized = true;
                }
                catch (final SQLException e)
                {
                }
                
                return result;
            }
            // Sinon, on 'bruteforce' le reste des caractères jusqu'a ce que la chaîne décryptée soit valide :
            else
            {
                // On verrouille la vue :
                this.view.setButtonsActive(false);
                
                // On créé le thread :
                Thread thread = new Thread(new Bruteforce(this, data, destination_path, key, 12 - key.length(), result));
                
                // On démarre le thread :
                thread.start();
            }
        }
        catch (final IOException e)
        {
        }
        
        return result;
    }
    
    // Compare une chaîne décryptée avec le dictionnaire en ligne :
    public boolean checkDictionary(final String data) throws SQLException
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
    
    /**
     * Permet d'obtenir la vue.
     * @return La référence sur la vue.
     */
    public FRM_Decrypt getView()
    {
        return this.view;
    }
    
    /**
     * Permet d'obtenir le composant de décryptage.
     * @return La référence sur le composant de décryptage.
     */
    public Decrypt getDecrypter()
    {
        return this.decrypt;
    }
}
