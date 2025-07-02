package ga.bitstrings;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Population {

    private List<Individual> individuals;
    private int geneLength;

    public Population(int size, int geneLength, boolean createNew) {
        this.individuals = new ArrayList<>();
        this.geneLength = geneLength;
        if (createNew) {
            createNewPopulation(size);
        }
    }

    protected Individual getIndividual(int index) {
        return individuals.get(index);
    }

    protected Individual getFittest() {
        Individual fittest = individuals.get(0);
        for (int i = 0; i < individuals.size(); i++) {
            if (fittest.getFitness() <= getIndividual(i).getFitness()) {
                fittest = getIndividual(i);
            }
        }
        return fittest;
    }

    private void createNewPopulation(int size) {
        for (int i = 0; i < size; i++) {
            Individual newIndividual = new Individual(geneLength);
            individuals.add(i, newIndividual);
        }
    }
}

