package controller;

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
     * Décrypte un fichier.
     * @param source_path L'adresse du fichier source à décrypter.
     * @param destination_path L'adresse du fichier destination.
     * @param key La clef de décryptage.
     * @return Le succès du décryptage.
     */
    public boolean pcs_decrypter(final String source_path, final String destination_path, final String key)
    {
        try
        {
            // On commence par récupérer le contenu du fichier :
            final String data = this.files.getData(source_path);
        
            // Ensuite, on décrypte les données :
            final String decrypted = this.decrypt.decrypt(data, key);

            // On compare avec le dictionnaire chacun des mots pour contrôler la validité de la clef :
            final String[] words = decrypted.split(" ");
            String sql;

            for (final String word : words)
            {
                // On récupère la requête SQL à soumettre :
                sql = map.selectWord(word);

                // On interroge la base de données sur le mot :
                cad.GetRows(sql, "");
            }

            return true;
        }
        catch (final IOException e)
        {
            return false;
        }
    }
}
