package Controller;

import Model.TrafficActuel;
import Model.Voiture;

/**
 * Created by Mathieu on 02/04/2016.
 */
public class AlgoTraffic {


    private TrafficActuel trafficActuel = new TrafficActuel();

    public void ajouterVoiture( String route){
        if(route.toLowerCase().compareTo("verticale") == 0){
            trafficActuel.ajouterVerticale();
        }else if(route.toLowerCase().compareTo("horizontale") == 0){
            trafficActuel.ajouterHorizontale();
        }
    }


    public void ajouterVoiturePoisson(double moyenne) {
        int nbMaxVoiture=10;

        for(int i=0; i<nbMaxVoiture; i++){
            double timeAddCar=getPoisson(moyenne);
            double timeNow = System.currentTimeMillis();

            boolean ajoutOk=false;
            while(!ajoutOk){
                if(System.currentTimeMillis()-timeNow >= timeAddCar*1000 ){
                    int route= (int)(Math.random()*2-0.1);
                    if(route == 0 ) {
                        ajouterVoiture("horizontale");
                        ajoutOk=true;
                    }
                    if(route == 1 ) {
                        ajouterVoiture("verticale");
                        ajoutOk=true;

                    }
                }
            }


        }

    }

    public double getPoisson(double moyenne) {
        double val= (Math.random()*(moyenne/2));
        val-=moyenne/4;
        return moyenne+val;
    }

}
