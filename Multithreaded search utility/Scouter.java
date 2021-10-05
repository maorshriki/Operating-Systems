//Ex3
//name : Maor Shriki
//id : 208274118

import java.io.*;

public class Scouter implements Runnable {
    private int id;
    private SynchronizedQueue<File> directoryQueue;
    private java.io.File root;
    private SynchronizedQueue<String> milestonesQueue;
    boolean isMilestones;

    public Scouter(int id, SynchronizedQueue<File> directoryQueue,
                   File root, SynchronizedQueue<String> milestonesQueue,
                   boolean isMilestones) {
        this.id = id;
        this.directoryQueue = directoryQueue;
        this.root = root;
        this.milestonesQueue = milestonesQueue;
        this.isMilestones = isMilestones;
        try {
            if (this.root.isDirectory()) {
                this.directoryQueue.enqueue(root);
            }
            if (isMilestones) {
                this.milestonesQueue.registerProducer();
                milestonesQueue.enqueue("Scouter on thread id " + this.id + ": directory named " + root.getName() + " was scouted");
                this.milestonesQueue.unregisterProducer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.directoryQueue.registerProducer();
        File[] listFiles = root.listFiles();
        try {
            if (listFiles != null) {
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        directoryQueue.enqueue(file);
                        if (isMilestones) {
                            this.milestonesQueue.registerProducer();
                            milestonesQueue.enqueue("Scouter on thread id " + this.id + ": directory named " + file.getName() + " was scouted");
                            this.milestonesQueue.unregisterProducer();
                        }
                        this.root = file;
                        this.run();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        directoryQueue.unregisterProducer();
    }
}
