package model;

/**
 * @author Matthieu Carteron
 */
public class Map_P
{
    /**
     * Retourne une requête SQL permettant d'interroger la base à propos du login et du password.
     * @param login La chaîne à décrypter.
     * @param password La clef de décryptage.
     * @return La requête SQL permettant d'interroger la base de données.
     */
    public String selectIDbyLoginPassword(final String login, final String password)
    {
        // Penser au hashage du mot de passe !
        return "";
    }
}
