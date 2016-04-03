package org.movsim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by laura on 03/04/2016.
 */
public class Solution {

    private List<ResultCore> listeIndividus;
    private int nIndividus;
    private static int dureeMinVert = 5;
    private static int dureeMaxVert = 72;
    private static int dureeMinOrange = 1;
    private static int dureeMaxOrange = 4;
    private static int dureeMinRouge = 12;
    private static int dureeMaxRouge = 60;
    private static double crossoverRate = 0.4;
    private static double mutationRate = 0.2;
    private static double selectionRate = 0.6;

    public Solution(List<ResultCore> resultats, int nIndividus) {
        this.listeIndividus = resultats;
        this.nIndividus = nIndividus;
        // Initialize pop
        for (int i = 0; i < nIndividus; i++) {
            // TODO HACK: see path issues.
            ResultCore core = new ResultCore("sim/generated/" + 0 + ".xprj");
            resultats.add(core);

            List<Integer> genes = new ArrayList<>();
            for (int j = 0; j < 12; j++) {
                genes.add((int) (Math.random() * 90));
            }
            core.setGenes(genes);
        }
    }

    /**
     * Note this is not super-useful if we passed a list to the constructor
     * In that case we already have the list and it's modified inplace all along.
     *
     * @return
     */
    public List<ResultCore> getListeIndividus() {
        return listeIndividus;
    }


    public ResultCore croisement(ResultCore individu1, ResultCore individu2) {
        ResultCore enfant = new ResultCore();

        List<Integer> genesParent1 = individu1.getGenes();
        List<Integer> genesParent2 = individu2.getGenes();

        List<Integer> genesEnfant = new ArrayList<>();
        enfant.setPath(individu1.getPath());
        enfant.setGenes(genesEnfant);

        int indiceGene = 0;
        while (indiceGene < genesParent1.size()) {
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
        List<Double> poids = new ArrayList<>();
        double poidsCumules = 0;
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
            aleaIndividu = (int) (Math.random() * listeIndividus.size());
            individuEnModification = listeIndividus.get(aleaIndividu);

            aleaGene = (int) (Math.random() * individuEnModification.getGenes().size());
            if (aleaGene % 3 == 0) {
                dureeMin = dureeMinVert;
                dureeMax = dureeMaxVert;
            } else if (aleaGene % 3 == 1) {
                dureeMin = dureeMinOrange;
                dureeMax = dureeMaxOrange;
            } else if (aleaGene % 3 == 2) {
                dureeMin = dureeMinRouge;
                dureeMax = dureeMaxRouge;
            }
            aleaDuree = (int) (Math.random() * (dureeMax + dureeMin)) + dureeMin;
            individuEnModification.getGenes().set(aleaGene, aleaDuree);
        }
    }

    public void selection(List<ResultCore> populationEnfant) {
        int nbIndividuPop = listeIndividus.size();
        List<ResultCore> populationTemporaire = new ArrayList<>();
        int aleaIndividu;

        // Concatenate the two populations into populationTemporaire.
        for (int indice = 0; indice < listeIndividus.size(); indice++)
            populationTemporaire.add(listeIndividus.get(indice));
        for (int indice = 0; indice < populationEnfant.size(); indice++)
            populationTemporaire.add(populationEnfant.get(indice));

        Collections.sort(populationTemporaire);
        //Collections.sort(populationTemporaire, (a, b) -> (int) Math.signum(a.getResult() - b.getResult()));
        listeIndividus.clear();
        int nbIndividus = 0;
        for (int i = 0; i < selectionRate * listeIndividus.size(); i++) {
            listeIndividus.add(populationTemporaire.get(i));
            nbIndividus++;
        }
        for (int i = nbIndividus; i < nbIndividuPop; i++) {
            aleaIndividu = (int) (Math.random() * (nbIndividuPop + nbIndividus)) - nbIndividus;
            listeIndividus.add(populationTemporaire.get(aleaIndividu));
        }

        populationTemporaire.clear();

    }

    public ArrayList<ResultCore> selection(int nSelected) {
        Collections.sort(listeIndividus);
        // Need to reverse to get the highest objective function results first.
        Collections.reverse(listeIndividus);
        // listeIndividus = listeIndividus.subList(0, nSelected);
        return new ArrayList<ResultCore>(listeIndividus.subList(0, nSelected));
    }

    /**
     * Apply mutation, selection, crossover... to get the next generation of individuals.
     * Returns a **new list of individuals**.
     *
     * Process:
     *
     *     1. We select the top X%. We do this here because we don't want to compute scores
     *     for newly created individuals (which we'd need to do if we were applying selection at the end).
     *     2. We generate children from crossovers. We pick parents using roulette selection.
     *     3. We apply mutations.
     *
     */
    public List<ResultCore> evolveToNextGeneration() {
        int nCrossover = (int) (crossoverRate * nIndividus);
        // int nSelected = (int) (selectionRate * nIndividus);
        int nSelected = nIndividus - nCrossover;

        // TODO HACK: Here we create a copy so that the crossovers are done with all the individuals (not only selected ones)
        ArrayList<ResultCore> nextGeneration = selection(nSelected);
        for (int i = 0; i < nCrossover; i++) {
            nextGeneration.add(croisement(rouletteRusse(), rouletteRusse()));
        }

        // I'm doing this because mutation acts on listeIndividus.... (lame)
        listeIndividus = nextGeneration;
        // TODO HACK: arbitrary number of modified genes.
        mutation((int) (mutationRate * nIndividus), 2);

        return listeIndividus;
    }
}
