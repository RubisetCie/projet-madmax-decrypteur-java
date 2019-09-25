package model;

/**
 * @author Matthieu Carteron
 */
public class Map_Dic
{
    /**
     * Retourne une requête SQL permettant d'interroger la base à propos d'un mot du dictionnaire.
     * @param word Le mot recherché.
     * @return La requête SQL permettant d'interroger la base de données.
     */
    public String selectWord(final String word)
    {
        return "SELECT * FROM `dictionary` WHERE `word` = '" + word + "';";
    }
}
