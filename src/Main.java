import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

public class Main {
    DecimalFormat df = new DecimalFormat("0.000000");
    public static void main(String[] args) {
        //geneticAlgorithm();
        //evaluateIncome(0,10000);
        var s = new CsvSaver();
        s.saveDataToCsv("simulation_data2.csv");
        //var model = ModelSetUp.getModel(0,true);
        //model.simulate(100000);
    }

    private static void geneticAlgorithm() {
        int simulationTime = 50000;
        int populationSize = 20;
        int generations = 1000;
        double mutationRate = 0.1;

        double minK = 0;
        double maxK = 1666;
        Random random = new Random();

        double[] population = random.doubles(populationSize, minK, maxK).toArray();

        double bestIncome = -1;
        double bestK = 0;

        // Еволюційний цикл
        for (int gen = 0; gen < generations; gen++) {
            double[] fitness = new double[populationSize];

            // Обчислення пристосованості
            for (int i = 0; i < populationSize; i++) {
                double k = population[i];
                fitness[i] = evaluateIncome(k, simulationTime);

                // Оновлення найкращого значення
                if (fitness[i] > bestIncome) {
                    bestIncome = fitness[i];
                    bestK = k;
                }
            }

            // Відбір: залишаємо найкращих
            int[] selected = selectTopIndices(fitness, populationSize / 2);
            double[] newPopulation = new double[populationSize];
            for (int i = 0; i < selected.length; i++) {
                newPopulation[i] = population[selected[i]];
            }

            // Схрещування
            for (int i = selected.length; i < populationSize; i++) {
                double parent1 = newPopulation[random.nextInt(selected.length)];
                double parent2 = newPopulation[random.nextInt(selected.length)];
                newPopulation[i] = (parent1 + parent2) / 2;
            }

            // Мутація
            for (int i = 0; i < populationSize; i++) {
                if (random.nextDouble() < mutationRate) {
                    newPopulation[i] += random.nextGaussian() * 10; // невелика випадкова зміна
                    newPopulation[i] = Math.min(maxK, Math.max(minK, newPopulation[i])); // обмеження діапазону
                }
            }

            population = newPopulation;

            System.out.println("Generation " + gen + ", best income: " + bestIncome + " at k: " + bestK);
        }

        System.out.println("Найкраще значення k: " + bestK + ", з прибутком: " + bestIncome);
    }

    private static double evaluateIncome(double k, int simulationTime) {
        var openModel = ModelSetUp.getModel(k,false);
        openModel.setKValue(k);
        openModel.simulate(simulationTime);
        return openModel.calculateIncome(ModelSetUp.mainChan, ModelSetUp.backupChan);
    }

    private static int[] selectTopIndices(double[] fitness, int num) {
        return IntStream.range(0, fitness.length)
                .boxed()
                .sorted((i, j) -> Double.compare(fitness[j], fitness[i]))
                .limit(num)
                .mapToInt(i -> i)
                .toArray();
    }
}