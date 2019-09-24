package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
        // On lit le texte ligne par ligne dans le fichier :
        final BufferedReader reader = new BufferedReader(new FileReader(path));
        
        String line = reader.readLine();
        String data = "";
        
        while (line != null)
        {
            data += line;
            line = reader.readLine();
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
        final BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        writer.write(data);

        writer.close();
    }
}
