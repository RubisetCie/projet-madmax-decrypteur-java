package controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

import view.FRM_Auth;
import model.Map_P;
import model.CAD;

/**
 * @author Matthieu Carteron
 */
public class WKF_Cpte
{
    // La vue associée au contrôleur :
    private final FRM_Auth view;
    
    // Le composant de mappage de la table "personne" :
    private final Map_P map;
    
    // Le composant d'accès aux données :
    private final CAD cad;
    
    // L'instance de hashage :
    private MessageDigest md;
    
    /**
     * Instantie la vue et le modèle.
     * @param cad Référence sur le composant d'accès aux données.
     */
    public WKF_Cpte(final CAD cad)
    {
        // On instantie la vue d'authentification :
        this.view = new FRM_Auth(this);
        
        // On instantie les composants d'accès à la base de donnée :
        this.map = new Map_P();
        this.cad = cad;
        
        // On récupère l'instance de hashage MD5 :
        try
        {
            this.md = MessageDigest.getInstance("MD5");
        }
        catch (final NoSuchAlgorithmException e)
        {
            this.md = null;
        }
    }
    
    /**
     * Appelé lorsque la fenêtre est fermée.
     */
    public void OnClose()
    {
        this.view.getFrame().dispose();
        System.exit(0);
    }
    
    /**
     * Appelé lorsque l'authentification s'est déroulée correctement.
     */
    public void OnLogin()
    {
        // On dispose la fenêtre d'authentification :
        this.view.getFrame().dispose();
        
        // On instantie le contrôleur d'authentification :
        new WKF_Decrypt(cad);
    }
    
    /**
     * Contrôle l'authentification de l'utilisateur.
     * @param login L'identifiant de l'utilisateur.
     * @param password Le mot de passe associé.
     * @return Le succès de l'authentification.
     */
    public boolean pcs_authentifier(final String login, final String password)
    {
        // On hash le mot de passe en MD5 :
        this.md.update(password.getBytes());
        
        byte[] bytes = this.md.digest();
        
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < bytes.length; i++)
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        
        final String passwordMD5 = sb.toString();
        
        // On récupère la requête SQL à soumettre :
        final String sql = map.selectIDbyLoginPassword(login, passwordMD5);
        
        // On interroge la base de données sur le login/password :
        try
        {
            final ResultSet rs = cad.GetRows(sql);
            
            if (rs.next())
            {
                rs.close();
                return true;
            }
            
            rs.close();
        }
        catch (final SQLException e)
        {
        }

        return false;
    }
}
