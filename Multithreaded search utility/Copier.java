//Ex3
//name : Maor Shriki
//id : 208274118

import java.io.*;

public class Copier implements Runnable {
    private int id;
    private File destination;
    private SynchronizedQueue<File> resultsQueue;
    private SynchronizedQueue<String> milestonesQueue;
    boolean isMilestones;
    public static final int BUFFER_SIZE = 4096;


    public Copier(int id, File destination, SynchronizedQueue<File> resultsQueue,
                  SynchronizedQueue<String> milestonesQueue, boolean isMilestones) {
        this.id = id;
        this.resultsQueue = resultsQueue;
        this.destination = destination;
        this.milestonesQueue = milestonesQueue;
        this.isMilestones = isMilestones;
    }

    @Override
    public void run() {
        int length;
        byte[] buffer = new byte[BUFFER_SIZE];
        File readF;
        FileInputStream in;
        FileOutputStream out;

        while ((readF = this.resultsQueue.dequeue()) != null) {
            try {
                String name = readF.getName();
                File newF = new File(this.destination, name);
                in = new FileInputStream(readF);
                out = new FileOutputStream(newF);
                while ((length = in.read(buffer)) > 0) out.write(buffer, 0, length);
                // in and out already != null
                in.close();
                out.close();
                if (isMilestones) {
                    this.milestonesQueue.registerProducer();
                    milestonesQueue.enqueue("Copier from thread " + this.id + ": file named " + name + " was copied");
                    this.milestonesQueue.unregisterProducer();
                }
            } catch (NullPointerException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}


