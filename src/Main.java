import java.text.DecimalFormat;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        DecimalFormat df = new DecimalFormat("0.00");
        int simulationTime = 50000;
        double bestIncome = -1;
        double bestK = 0;
        var k = 2;
        //for (double k = 0; k <= 200; k += 10) {
           var creator = new Create("CREATE");
           var buffer = new Buffer("BUFFER");
           var mainChannel = new MainChannel("MAIN-CHANNEL");
           var backupChannel = new BackupChannel("BACKUP-CHANNEL");
           var errorGenerator = new ErrorGenerator("ERROR-GENERATOR");
           var backupStarter = new BackupStarter("BACKUP-STARTER");
           var mainRestorer = new MainRestorer("MAIN-RESTORER");
           var despose = new Despose("DESPOSE");
           creator.setNextElement(buffer);
           buffer.setChannels(mainChannel,backupChannel);
           mainChannel.setBuffer(buffer);

           errorGenerator.setMainChannel(mainChannel);
           errorGenerator.setNextElement(backupStarter);

           backupStarter.setBackupChannel(backupChannel);
           backupStarter.setNextElement(mainRestorer);

           mainRestorer.setMainChannel(mainChannel);
           mainRestorer.setErrorGenerator(errorGenerator);
           mainRestorer.setBackupChannel(backupChannel);

           mainChannel.setNextElement(despose);
           backupChannel.setNextElement(despose);
           ArrayList<Element> openElements = new ArrayList<>() {{
               add(creator);
               add(buffer);
               add(mainChannel);
               add(backupChannel);
               add(errorGenerator);
               add(backupStarter);
               add(mainRestorer);
               add(despose);
            }};

            Model openModel = new Model(openElements);
            openModel.setKValue(k);
            openModel.simulate(simulationTime);
            openModel.calculateIncome(mainChannel, backupChannel);

            System.out.println("For k = " + k + ", total income: " + openModel.getTotalIncome());
            System.out.println("Завантаження primary-каналу: " + df.format(mainChannel.getMeanLoad() / simulationTime));
            System.out.println("Завантаження backup-каналу: " + df.format(backupChannel.getMeanLoad() / simulationTime));
            System.out.println("---------------------------------------------");

            if (openModel.getTotalIncome() > bestIncome) {
                bestIncome = openModel.getTotalIncome();
                bestK = k;
            }
       // }
        System.out.println("Найкраще значення k: " + bestK + ", з прибутком: " + bestIncome);
    }
}