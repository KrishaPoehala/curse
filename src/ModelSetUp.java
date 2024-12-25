import java.util.ArrayList;

public class ModelSetUp {

    public static MainChannel mainChan;
    public static BackupChannel backupChan;
    public static Model getModel(double k,boolean printResult){
        Element.nextId = 0;
        var creator = new Create("CREATE");
        var buffer = new Buffer("BUFFER");
        var errorGenerator = new ErrorGenerator("ERROR-GENERATOR", k);
        var mainChannel = new MainChannel("MAIN-CHANNEL","main");
        var backupChannel = new BackupChannel("BACKUP-CHANNEL","backup");
        var backupStarter = new BackupStarter("BACKUP-STARTER");
        var mainRestorer = new MainRestorer("MAIN-RESTORER");
        var dispose = new Dispose("DISPOSE");
        mainChan = mainChannel;
        backupChan = backupChannel;
        creator.setNextElement(buffer);
        buffer.setChannels(mainChannel,backupChannel);
        errorGenerator.setMainChannel(mainChannel);
        errorGenerator.setNextElement(backupStarter);

        backupStarter.setChannels(backupChannel,mainChannel);
        backupStarter.setNextElement(mainRestorer);

        mainRestorer.setMainChannel(mainChannel);
        mainRestorer.setErrorGenerator(errorGenerator);
        mainRestorer.setBackupChannel(backupChannel);

        mainChannel.setNextElement(dispose);
        mainChannel.setBuffer(buffer);

        backupChannel.setNextElement(dispose);
        backupChannel.setBuffer(buffer);
        ArrayList<Element> openElements = new ArrayList<>() {{
            add(creator);
            add(buffer);
            add(errorGenerator);
            add(mainChannel);
            add(backupChannel);
            add(backupStarter);
            add(mainRestorer);
            add(dispose);
        }};

        var model= new Model(openElements);
        model.printProgress = printResult;
        return model;
    }
}
