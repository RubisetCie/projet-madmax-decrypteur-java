package model;

/**
 * @author Matthieu Carteron
 */
public class DecryptResult
{
    /**
     * L'opération s'est déroulée comme prévue.
     */
    public boolean succeed = false;
    
    /**
     * La chaîne de sortie est reconnue comme étant du français.
     */
    public boolean recognized = false;
    
    /**
     * La clef trouvée par décryptage bruteforce.
     */
    public String key = "";
}
