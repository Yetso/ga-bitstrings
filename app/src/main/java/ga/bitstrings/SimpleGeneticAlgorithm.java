package ga.bitstrings;

import lombok.Data;

@Data
public class SimpleGeneticAlgorithm {

    private static final double uniformRate = 0.5;
    private static final double mutationRate = 0.025;
    private static final int tournamentSize = 5;
    private static final boolean elitism = true;
    private static byte[] solution = null;
    private static int nbrIterMax;

    public boolean runAlgorithm(int populationSize, String solution, int nbrIterMax) {
        if (nbrIterMax < 1) nbrIterMax = Integer.MAX_VALUE;
        setSolution(solution);
        Population myPop = new Population(populationSize, solution.length(), true);

        int generationCount = 0;
        while (myPop.getFittest().getFitness() < getMaxFitness() && generationCount < nbrIterMax) {
            System.out.println("Generation: " + generationCount + " Correct genes found: " + myPop.getFittest().getFitness());
            myPop = evolvePopulation(myPop);
            generationCount++;
        }
        if (generationCount >= nbrIterMax) {
            System.out.println("Solution not found!");
            System.out.println("Current fittest gene : " + myPop.getFittest());
            return false;
        }
        System.out.println("Solution found!");
        System.out.println("Generation: " + generationCount);
        System.out.println("Genes: ");
        System.out.println(myPop.getFittest());
        return true;
    }

    public Population evolvePopulation(Population pop) {
        int elitismOffset;
        Population newPopulation = new Population(pop.getIndividuals().size(), solution.length, false);

        if (elitism) {
            newPopulation.getIndividuals().add(0, pop.getFittest());
            elitismOffset = 1;
        } else {
            elitismOffset = 0;
        }

        for (int i = elitismOffset; i < pop.getIndividuals().size(); i++) {
            Individual indiv1 = tournamentSelection(pop);
            Individual indiv2 = tournamentSelection(pop);
            Individual newIndiv = crossover(indiv1, indiv2);
            newPopulation.getIndividuals().add(i, newIndiv);
        }

        for (int i = elitismOffset; i < newPopulation.getIndividuals().size(); i++) {
            mutate(newPopulation.getIndividual(i));
        }

        return newPopulation;
    }

    private Individual crossover(Individual indiv1, Individual indiv2) {
        Individual newSol = new Individual(solution.length);
        for (int i = 0; i < newSol.getDefaultGeneLength(); i++) {
            if (Math.random() <= uniformRate) {
                newSol.setSingleGene(i, indiv1.getSingleGene(i));
            } else {
                newSol.setSingleGene(i, indiv2.getSingleGene(i));
            }
        }
        return newSol;
    }

    private void mutate(Individual indiv) {
        for (int i = 0; i < indiv.getDefaultGeneLength(); i++) {
            if (Math.random() <= mutationRate) {
                byte gene = (byte) Math.round(Math.random());
                indiv.setSingleGene(i, gene);
            }
        }
    }

    private Individual tournamentSelection(Population pop) {
        Population tournament = new Population(tournamentSize, solution.length, false);
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.getIndividuals().size());
            tournament.getIndividuals().add(i, pop.getIndividual(randomId));
        }
        return tournament.getFittest();
    }

    protected static int getFitness(Individual individual) {
        int fitness = 0;
        for (int i = 0; i < individual.getDefaultGeneLength() && i < solution.length; i++) {
            if (individual.getSingleGene(i) == solution[i]) {
                fitness++;
            }
        }
        return fitness;
    }

    protected int getMaxFitness() {
        return solution.length;
    }

    protected void setSolution(String newSolution) {
        solution = new byte[newSolution.length()];
        for (int i = 0; i < newSolution.length(); i++) {
            String character = newSolution.substring(i, i + 1);
            if (character.contains("0") || character.contains("1")) {
                solution[i] = Byte.parseByte(character);
            } else {
                solution[i] = 0;
            }
        }
    }

}
