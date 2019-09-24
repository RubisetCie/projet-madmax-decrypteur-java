package model;

import java.sql.*;

/**
 * @author Matthieu Carteron
 */
public class CAD
{
    // Les constantes de la base de données :
    private final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private final String DB_HOST = "jdbc:mysql://localhost/EMP";
    private final String DB_USER = "";
    private final String DB_PASSWORD = "";
    
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
            // On enregistre le driver :
            Class.forName(DB_DRIVER);
            
            // On connecte à la base :
            this.connection = DriverManager.getConnection(DB_HOST, DB_USER, DB_PASSWORD);
            
            // On créé le statement :
            this.statement = this.connection.createStatement();
        }
        catch (final ClassNotFoundException | SQLException e)
        {
            this.connection = null;
            this.statement = null;
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
        return this.statement.executeQuery(rq_sql);
    }
    
    /**
     * Exécute une requête SQL modifiant la base de donnée.
     * @param rq_sql La requête SQL à envoyer.
     * @throws java.sql.SQLException
     */
    public void ActionRows(final String rq_sql) throws SQLException
    {
        this.statement.executeUpdate(rq_sql);
    }
}
