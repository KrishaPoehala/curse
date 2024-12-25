import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CsvSaver {

    final String CSV_HEADER = "ITERATION;TIME;MAIN_MEAN_QUEUE;MAIN_MEAN_LOAD;" +
            "BACKUP_MEAN_QUEUE;BACKUP_MEAN_LOAD;ERRORS_RATIO;AVG_TIME_TO_PASS_MESSAGE\n";
    final int ITERATIONS = 5;
    final int TIME_STEP = 100;
    final int SIMULATION_TIME = 100_000;

    public void saveDataToCsv(String csvFilePath) {
        try (BufferedWriter csvWriter = new BufferedWriter(new FileWriter(csvFilePath))) {
            csvWriter.write(CSV_HEADER);
            for (int i = 0;i < ITERATIONS; ++i){
                for (double time = 0;time <= SIMULATION_TIME; time +=TIME_STEP){
                    var model = ModelSetUp.getModel(0,false);
                    model.simulate(time);
                    double[] stats = getStats(model);
                    String csvRow = String.format(
                            "%d;%.0f;%.5f;%.5f;%.5f;%.5f;%.5f;%.5f\n",
                            i + 1,
                            time,
                            stats[0], // MAIN_MEAN_QUEUE
                            stats[1], // MAIN_MEAN_LOAD
                            stats[2], // BACKUP_MEAN_QUEUE
                            stats[3], // BACKUP_MEAN_LOAD
                            stats[4], // ERRORS_RATIO
                            stats[5]  // AVG_TIME_TO_PASS_MESSAGE
                    );
                    csvWriter.write(csvRow);
                }
                System.out.println("Дані моделювання успішно збережено у: " + csvFilePath);
            }
        } catch (IOException e) {
            System.err.println("[ПОМИЛКА] Помилка запису у файл: " + e.getMessage());
        }
    }

    private double[] getStats(Model model) {
        double mainMeanQueue = 0;
        double mainMeanLoad = 0;
        double backupMeanQueue = 0;
        double backupMeanLoad = 0;
        double errorsRatio = 0;
        double avgTimeToPassMessage = 0;

        for (Element element : model.getElements()) {
            if (element instanceof MainChannel) {
                MainChannel mainChannel = (MainChannel) element;
                mainMeanQueue = mainChannel.getMeanQueue() / model.getTCurrent();
                mainMeanLoad = mainChannel.getMeanLoad() / model.getTCurrent();
            } else if (element instanceof BackupChannel) {
                BackupChannel backupChannel = (BackupChannel) element;
                backupMeanQueue = backupChannel.getMeanQueue() / model.getTCurrent();
                backupMeanLoad = backupChannel.getMeanLoad() / model.getTCurrent();
            } else if (element instanceof ErrorGenerator) {
                ErrorGenerator errorGenerator = (ErrorGenerator) element;
                errorsRatio = errorGenerator.getErrorsGenerated() / model.getTCurrent();
            } else if (element instanceof Dispose) {
                Dispose dispose = (Dispose) element;
                avgTimeToPassMessage = dispose.getMeanTimeInSystem();
            }
        }

        return new double[]{
                mainMeanQueue,
                mainMeanLoad,
                backupMeanQueue,
                backupMeanLoad,
                errorsRatio,
                avgTimeToPassMessage
        };
    }

}
