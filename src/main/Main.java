package main;

//import controller.WKF_Decrypt;
import controller.WKF_Cpte;
import model.CAD;

/**
 * @author Matthieu Carteron
 */
public class Main
{
    /**
     * Le point d'entrée du programme.
     * @args Arguments passés en entrée.
     */
    public static void main(String[] args)
    {
        // On instantie le contrôleur d'authentification :
        new WKF_Cpte(new CAD());
        //new WKF_Decrypt();
    }
}
