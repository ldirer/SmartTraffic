package Servlets;

import Controller.AlgoTraffic;
import Model.Voiture;

import java.io.IOException;

/**
 * Created by Mathieu on 02/04/2016.
 */

public class ServletTraffic extends javax.servlet.http.HttpServlet {

    private AlgoTraffic algoTraffic = new AlgoTraffic();



    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        String action= request.getParameter("action");

        switch (action){
            case "horizontal":
                algoTraffic.ajouterVoiture(action);
                break;
            case "vertical":
                algoTraffic.ajouterVoiture(action);
                break;
            case "ajoutAuto":
                algoTraffic.ajouterVoiturePoisson(100);
            default:


        }

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }
}
