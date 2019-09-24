package controller;

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
    
    /**
     * Instantie la vue et le modèle.
     */
    public WKF_Cpte()
    {
        // On instantie la vue d'authentification :
        this.view = new FRM_Auth(this);
        
        // On instantie les composants d'accès à la base de donnée :
        this.map = new Map_P();
        this.cad = new CAD();
    }
    
    /**
     * Appelé lorsque la fenêtre est fermée.
     */
    public void OnClose()
    {
        this.view.frame.dispose();
        System.exit(0);
    }
    
    /**
     * Appelé lorsque l'authentification s'est déroulée correctement.
     */
    public void OnLogin()
    {
        // On dispose la fenêtre d'authentification :
        this.view.frame.dispose();
        
        // On instantie le contrôleur d'authentification :
        new WKF_Decrypt();
    }
    
    /**
     * Contrôle l'authentification de l'utilisateur.
     * @param login L'identifiant de l'utilisateur.
     * @param password Le mot de passe associé.
     * @return Le succès de l'authentification.
     */
    public boolean pcs_authentifier(final String login, final String password)
    {
        // On récupère la requête SQL à soumettre :
        final String sql = map.selectIDbyLoginPassword(login, password);
        
        // On interroge la base de données sur le login/password :
        cad.GetRows(sql, "");
        
        return true;
    }
}