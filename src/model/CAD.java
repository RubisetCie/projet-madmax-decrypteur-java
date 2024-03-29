package model;

import java.sql.*;

/**
 * @author Matthieu Carteron
 */
public class CAD
{
    // Les constantes de la base de données :
    private final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private final String DB_HOST = "jdbc:mysql://localhost:3306/seminaire_scientifique";
    private final String DB_USER = "java";
    private final String DB_PASSWORD = "Npadw41RyWy2DZFL";
    
    // La prise de connexion à la base de donnée :
    private Connection connection;
    private Statement statement;
    
    /**
     * Initialise et connecte le composant d'accès à la base de donnée.
     */
    public CAD()
    {
        try
        {
            // On définit la classe du driver :
            Class.forName(DB_DRIVER);
            
            // On connecte à la base :
            this.connection = DriverManager.getConnection(DB_HOST, DB_USER, DB_PASSWORD);
            
            // On créé le statement :
            this.statement = this.connection.createStatement();
        }
        catch (final ClassNotFoundException | SQLException e)
        {
            this.connection = null;
        }
    }
    
    /**
     * Exécute une requête SQL envers la base de donnée et renvoie le résultat.
     * @param rq_sql La requête SQL à envoyer.
     * @return L'ensemble résultant.
     * @throws java.sql.SQLException
     */
    public ResultSet GetRows(final String rq_sql/*, final String resultSetName*/) throws SQLException
    {
        if (this.connection == null)
            throw new SQLException();
        
        return this.statement.executeQuery(rq_sql);
    }
    
    /**
     * Exécute une requête SQL modifiant la base de donnée.
     * @param rq_sql La requête SQL à envoyer.
     * @throws java.sql.SQLException
     */
    public void ActionRows(final String rq_sql) throws SQLException
    {
        if (this.connection == null)
            throw new SQLException();
        
        this.statement.executeUpdate(rq_sql);
    }
}
