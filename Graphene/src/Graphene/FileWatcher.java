package Graphene;

import crypto.EncryptedData;

import java.io.IOException;
import java.nio.file.*;

/**
 * Created with IntelliJ IDEA.
 * User: jsu
 * Date: 10/27/13
 * Time: 12:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class FileWatcher extends Thread {

    private Path path = null;

    public boolean isRunning = true;

    public FileWatcher(Path path) {
        this.path = path;
    }

    @Override
    public void run() {
        try {
            WatchService service = FileSystems.getDefault().newWatchService();
            path.register(service,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE);

            while(isRunning) {
                WatchKey key = service.take();

                for (WatchEvent event : key.pollEvents()) {
                    if(event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                        String fileName = event.context().toString();
                        if(DataStore.hasFile(fileName)) continue;

                        System.out.println("FS: File " + event.context() + " was created");
                        EncryptedData data = DataStore.encrypt(fileName);

                        // Let other nodes know of the change
                        CreateRequest request = new CreateRequest(fileName, DataStore.getFileContents(fileName), data.secretShare);
                        request.run();
                    }
                    else if(event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {

                    }
                    else if(event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {

                    }
                }

                boolean valid = key.reset();
                if (!valid) {
                    break;	// Exit if directory is deleted
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}
