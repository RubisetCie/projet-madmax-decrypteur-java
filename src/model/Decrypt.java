package model;

/**
 * @author Matthieu Carteron
 */
public class Decrypt
{
    /**
     * Décrypte une chaîne de caractère.
     * @param data La chaîne à décrypter.
     * @param key La clef de décryptage.
     * @return La chaîne décryptée.
     */
    public String decrypt(String data, String key)
    {
        // Buffer pour le string de sortie :
        char[] outputString = new char[data.length()];
        
        // Variables pour itérer au travers la clef :
        int key_i = 0;
        
        for (int i = 0; i < data.length(); i++)
        {
            outputString[i] = (char)(data.charAt(i) ^ key.charAt(key_i));
            
            // On décale dans la clef :
            if (key_i < key.length()-1)
                key_i++;
            else
                key_i = 0;
        }
        
        // Enfin, on retourne la chaîne de caractère chiffrée :
        return new String(outputString);
    }
}
