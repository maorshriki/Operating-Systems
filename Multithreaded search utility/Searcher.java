//Ex3
//name : Maor Shriki
//id : 208274118

import java.io.*;

public class Searcher implements Runnable {
    private int id;
    private String extension;
    private SynchronizedQueue<File> directoryQueue;
    private SynchronizedQueue<File> resultsQueue;
    private SynchronizedQueue<String> milestonesQueue;
    boolean isMilestones;


    public Searcher(int id, String extension,
                    SynchronizedQueue<File> directoryQueue,
                    SynchronizedQueue<File> resultsQueue,
                    SynchronizedQueue<String> milestonesQueue, boolean isMilestones) {
        this.id = id;
        this.directoryQueue = directoryQueue;
        this.resultsQueue = resultsQueue;
        this.extension = extension;
        this.milestonesQueue = milestonesQueue;
        this.isMilestones = isMilestones;


    }

    @Override
    public void run() {
        this.resultsQueue.registerProducer();
        File directory;
        while ((directory = this.directoryQueue.dequeue()) != null)
            try {
                File[] listFiles = directory.listFiles();
                if (listFiles != null) {
                    for (File file : listFiles) {
                        if (file.isFile() && file.getName().endsWith(this.extension)) {
                            resultsQueue.enqueue(file);
                            if (isMilestones) {
                                this.milestonesQueue.registerProducer();
                                milestonesQueue.enqueue("Searcher on thread id " + this.id + ": directory named " + file.getName() + " was found");
                                this.milestonesQueue.unregisterProducer();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        this.resultsQueue.unregisterProducer();
    }
}
