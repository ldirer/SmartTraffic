package Servlets;

import java.io.IOException;

/**
 * Created by Mathieu on 02/04/2016.
 */

public class ServletTraffic extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        String action= request.getParameter("action");

        switch (action){
            case "horizontal":
                break;
            case "vertical":
                break;
            default:


        }

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }
}
