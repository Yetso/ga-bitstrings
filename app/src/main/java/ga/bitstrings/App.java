package ga.bitstrings;

public class App {
	public static void main(String[] args) {
		System.out.println("2 - Simple Genetic Algorithm");
		SimpleGeneticAlgorithm ga = new SimpleGeneticAlgorithm();
		ga.runAlgorithm(50, "010011010010101010111010010", 3);
	}
}
