import java.text.DecimalFormat;
import java.util.ArrayList;

class Model {
    private final ArrayList<Element> elements;
    private double tCurrent;
    private double totalIncome;
    private int mainChannelUses;
    private int backupChannelUses;
    private double kValue = 0;
     double tNext = 0.0;
     int event = 0;
    public Model(ArrayList<Element> elements) {
        this.elements = elements;
        tCurrent = 0;
        totalIncome = 0;
        mainChannelUses = 0;
        backupChannelUses = 0;
    }

    boolean firstIteration = true;
    public void simulate(double time) {
        while (tCurrent < time) {
            tNext = Double.MAX_VALUE;
            for (Element e : elements) {
                if(firstIteration){
                    tNext = elements.getFirst().tNext;
                    event = 0;
                    firstIteration = false;
                    break;
                }

                if (e.getTNext() < tNext) {
                    tNext = e.getTNext();
                    event = e.getId();
                }
            }



            System.out.println("It's time for event in " + elements.get(event).getName() + ", time = " + tNext);
            for (Element e : elements) {
                e.doStatistics(tNext - tCurrent);
            }

            tCurrent = tNext;
            for (Element e : elements) {
                e.setTCurrent(tCurrent);
            }

            elements.get(event).outAct();
            for (Element e : elements) {
                if (e.getTNext() == tCurrent) {
                    e.outAct();
                }
            }
        }

        printResults(time);
    }

    public void printResults(double simulationTime) {
        DecimalFormat df = new DecimalFormat("0.00000");
        System.out.println("\n-----------------------РЕЗУЛЬТАТ-----------------------");
        System.out.println("Загальний час моделювання: " + simulationTime + " секунд");

        for (Element e : elements) {
            System.out.println(e.getName() + ": {");
            System.out.println("\tКількість оброблених: " + e.getQuantity() + ";");

            if (e instanceof Create) {
                Create c = (Create) e;
                System.out.println("\tСтворено повідомлень: " + (e.getQuantity()) + ";");
            } else if(e instanceof  MainChannel) {
                var mainC = (MainChannel)e;
                System.out.println("\t Частота переривання повідомлень: "
                + df.format(mainC.getInterruptionFrequency()));
                System.out.println("\tСереднє завантаження: " + df.format(mainC.getMeanLoad() / simulationTime) + ";");
            }
            else if (e instanceof BackupChannel) {
                Channel ch = (Channel) e;
                System.out.println("\tСереднє завантаження: " + df.format(ch.getMeanLoad() / simulationTime) + ";");
                //System.out.println("\tСередній час обробки повідомлення: " + df.format(ch.getMeanTimeIn()) + ";"); // Середній час обробки
            } else if (e instanceof Buffer) {
                Buffer b = (Buffer) e;
                System.out.println("\tСередня довжина черги: " + df.format(b.getMeanQueue() / simulationTime) + ";");
            } else if (e instanceof ErrorGenerator) {
                ErrorGenerator eg = (ErrorGenerator) e;
                System.out.println("\tКількість згенерованих помилок: " + eg.getErrorsGenerated() + ";");
                System.out.println("\tЧастота переривання (помилок/секунду): " + df.format(eg.getErrorsGenerated() / simulationTime) + ";");//Частота переривання
            } else if (e instanceof BackupStarter) {
                BackupStarter bs = (BackupStarter) e;
               //System.out.println("\tКількість запусків резервного копіювання: " + bs.getBackupStarts() + ";");
            } else if (e instanceof MainRestorer) {
                MainRestorer mr = (MainRestorer) e;
                System.out.println("\tКількість відновлень основного каналу: " + mr.getRestores() + ";");
            } else if (e instanceof Dispose) {
                Dispose d = (Dispose) e;
                System.out.println("\tКількість оброблених повідомлень: " + d.getQuantity() + ";");
            }

            System.out.println("}\n");
        }
        System.out.println("Загальний прибуток: " + getTotalIncome());
        System.out.println("------------------------------------------------------");
    }

    public void calculateIncome(MainChannel mainChannel, BackupChannel backupChannel) {
        mainChannelUses = mainChannel.getQuantity();
        backupChannelUses = backupChannel.getQuantity();
        double incomeFromMain = mainChannelUses * (50 - 0.03 * kValue);
        double incomeFromBackup = backupChannelUses * 25;
        totalIncome = incomeFromMain + incomeFromBackup;
    }

    public void setKValue(double k) {
        this.kValue = k;
    }

    public double getTotalIncome() {
        return totalIncome;
    }
}