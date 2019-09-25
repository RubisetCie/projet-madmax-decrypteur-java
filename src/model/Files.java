package model;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Matthieu Carteron
 */
public class Files
{
    /**
     * Lit des données dans un fichier.
     * @param path Le chemin d'accès du fichier.
     * @return Les données lues dans le fichier.
     * @throws java.io.IOException
     */
    public String getData(final String path) throws IOException
    {
        // On lit le texte caractère par caractère dans le fichier :
        final FileReader reader = new FileReader(path);

        String data = "";
        int c = reader.read();
        
        while (c != -1)
        {
            data += (char)c;
            c = reader.read();
        }

        reader.close();
        
        return data;
    }
    
    /**
     * Ecrit des données dans un fichier.
     * @param path Le chemin d'accès du fichier.
     * @param data Les données à écrire.
     * @throws java.io.IOException
     */
    public void setData(final String path, final String data) throws IOException
    {
        // On écrit le texte dans le fichier :
        final FileWriter writer = new FileWriter(path);
        
        writer.write(data);
        writer.close();
    }
}
