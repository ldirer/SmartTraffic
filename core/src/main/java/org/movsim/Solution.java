package org.movsim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by laura on 03/04/2016.
 */
public class Solution {

    private List<ResultCore> listeIndividus;
    private static int dureeMinVert = 5;
    private static int dureeMaxVert = 72;
    private static int dureeMinOrange = 1;
    private static int dureeMaxOrange = 4;
    private static int dureeMinRouge = 12;
    private static int dureeMaxRouge = 60;
    private static int nbFeux;
    private static int nbCroisements = 10;

    public static int getPourcentageSelection() {
        return pourcentageSelection;
    }

    public static void setPourcentageSelection(int pourcentageSelection) {
        Solution.pourcentageSelection = pourcentageSelection;
    }

    private static int pourcentageSelection = 40;

    public static int getNbCroisements() {
        return nbCroisements;
    }

    public static void setNbCroisements(int nbCroisements) {
        Solution.nbCroisements = nbCroisements;
    }


    public Solution(List<ResultCore> listeIndividus) {
        this.listeIndividus = listeIndividus;
        nbFeux = listeIndividus.get(0).getGenes().size() / 3;
    }

    public static int getNbFeux() {
        return nbFeux;
    }

    public static void setNbFeux(int nbFeux) {
        Solution.nbFeux = nbFeux;
    }


    public static int getDureeMinVert() {
        return dureeMinVert;
    }

    public static void setDureeMinVert(int dureeMinVert) {
        Solution.dureeMinVert = dureeMinVert;
    }

    public static int getDureeMaxVert() {
        return dureeMaxVert;
    }

    public static void setDureeMaxVert(int dureeMaxVert) {
        Solution.dureeMaxVert = dureeMaxVert;
    }

    public static int getDureeMinOrange() {
        return dureeMinOrange;
    }

    public static void setDureeMinOrange(int dureeMinOrange) {
        Solution.dureeMinOrange = dureeMinOrange;
    }

    public static int getDureeMaxOrange() {
        return dureeMaxOrange;
    }

    public static void setDureeMaxOrange(int dureeMaxOrange) {
        Solution.dureeMaxOrange = dureeMaxOrange;
    }

    public static int getDureeMinRouge() {
        return dureeMinRouge;
    }

    public static void setDureeMinRouge(int dureeMinRouge) {
        Solution.dureeMinRouge = dureeMinRouge;
    }

    public static int getDureeMaxRouge() {
        return dureeMaxRouge;
    }

    public static void setDureeMaxRouge(int dureeMaxRouge) {
        Solution.dureeMaxRouge = dureeMaxRouge;
    }


    public List<ResultCore> getListeIndividus() {
        return listeIndividus;
    }

    public void setListeIndividus(List<ResultCore> listeIndividus) {
        this.listeIndividus = listeIndividus;
    }

    public int getFonctionObjective(ResultCore individu) {
        // return time;
        return -1;
    }


    public ResultCore croisement(ResultCore individu1, ResultCore individu2) {
        ResultCore enfant = new ResultCore();

        List<Integer> genesParent1 = individu1.getGenes();
        List<Integer> genesParent2 = individu2.getGenes();

        List<Integer> genesEnfant = new ArrayList<>();

        int indiceGene = 0;
        while (indiceGene < getNbFeux() * 3) {
            genesEnfant.add(indiceGene, genesParent1.get(indiceGene));
            genesEnfant.add(indiceGene + 1, genesParent1.get(indiceGene + 1));
            genesEnfant.add(indiceGene + 2, genesParent1.get(indiceGene + 2));
            genesEnfant.add(indiceGene + 3, genesParent2.get(indiceGene + 3));
            genesEnfant.add(indiceGene + 4, genesParent2.get(indiceGene + 4));
            genesEnfant.add(indiceGene + 5, genesParent2.get(indiceGene + 5));
            indiceGene += 6;
        }

        return enfant;
    }

    public ResultCore rouletteRusse() {
        ResultCore individuSelectionne = new ResultCore();
        List<Integer> poids = new ArrayList<>();
        int poidsCumules = 0;
        int sommeFonctionsObjectives = 0;
        boolean trouve = false;

        for (int indice = 0; indice < listeIndividus.size(); indice++) {
            sommeFonctionsObjectives += listeIndividus.get(indice).getResult();
        }

        for (int indice = 0; indice < listeIndividus.size(); indice++) {
            poidsCumules += listeIndividus.get(indice).getResult() / sommeFonctionsObjectives;
            poids.add(poidsCumules);
        }
        //retourne l'angle choisi sur la roulette
        double angle = Math.random();

        //recupere l'individu ayant le poids correspondant à l'angle trouvé
        int indiceIndividu = 0;
        while (!trouve) {
            if (angle < poids.get(indiceIndividu)) {
                individuSelectionne = listeIndividus.get(indiceIndividu);
                trouve = true;
            } else indiceIndividu++;
        }

        return individuSelectionne;
    }


    public void mutation(int nbIndividusModifies, int nbGenesModifies) {
        int aleaIndividu;
        int aleaGene;
        int aleaDuree;
        int dureeMin = 0;
        int dureeMax = 0;

        ResultCore individuEnModification;
        for (int i = 0; i < nbIndividusModifies; i++) {
            aleaIndividu = (int) Math.random() * listeIndividus.size();
            individuEnModification = listeIndividus.get(aleaIndividu);

            aleaGene = (int) Math.random() * listeIndividus.get(aleaIndividu).getGenes().size();
            if (aleaGene % 3 == 0) {
                dureeMin = getDureeMinVert();
                dureeMax = getDureeMaxVert();
            } else if (aleaGene % 3 == 1) {
                dureeMin = getDureeMinOrange();
                dureeMax = getDureeMaxOrange();
            } else if (aleaGene % 3 == 2) {
                dureeMin = getDureeMinRouge();
                dureeMax = getDureeMaxRouge();
            }
            aleaDuree = (int) Math.random() * (dureeMax + dureeMin) + dureeMin;
            listeIndividus.get(aleaIndividu).getGenes().set(aleaGene, aleaDuree);
        }
    }

    public void Selection(List<ResultCore> populationEnfant) {
        int nbIndividuPop = listeIndividus.size();
        List<ResultCore> populationTemporaire = new ArrayList<>();
        int aleaIndividu;

        for (int indice = 0; indice < listeIndividus.size(); indice++)
            populationTemporaire.add(listeIndividus.get(indice));
        for (int indice = 0; indice < populationEnfant.size(); indice++)
            populationTemporaire.add(populationEnfant.get(indice));

        Collections.sort(populationTemporaire);
        //Collections.sort(populationTemporaire, (a, b) -> (int) Math.signum(a.getResult() - b.getResult()));
        listeIndividus.clear();
        int nbIndividus = 0;
        for (int i = 0; i < pourcentageSelection * listeIndividus.size() / 100; i++) {
            listeIndividus.add(populationTemporaire.get(i));
            nbIndividus++;
        }
        for (int i = nbIndividus; i < nbIndividuPop; i++) {
            aleaIndividu = (int) Math.random() * (nbIndividuPop + nbIndividus) - nbIndividus;
            listeIndividus.add(populationTemporaire.get(aleaIndividu));
        }

        populationTemporaire.clear();

    }
}
