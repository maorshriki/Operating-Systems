//Ex3
//name : Maor Shriki
//id : 208274118

import java.io.*;

public class DiskSearcher {
    public static final int DIRECTORY_QUEUE_CAPACITY = 50;
    public static final int RESULTS_QUEUE_CAPACITY = 50;
    public static final int MILE_STONES_QUEUE_CAPACITY = 50000;

    public static void main(String[] args) {

        boolean milestoneQueueFlag = Boolean.parseBoolean(args[0]);
        String extension = args[1];
        File rootDirectory = new File(args[2]);
        File destinationDirectory = new File(args[3]);
        int numOfSearchers = Integer.parseInt(args[4]);
        int numOfCopiers = Integer.parseInt(args[5]);

        SynchronizedQueue<String> milestonesQueue = null;
        if (milestoneQueueFlag) {
            milestonesQueue = new SynchronizedQueue<String>(MILE_STONES_QUEUE_CAPACITY);
            milestonesQueue.enqueue("General, program has started the search");
        }

        SynchronizedQueue<File> directoryQueue = new SynchronizedQueue<File>(DIRECTORY_QUEUE_CAPACITY);
        SynchronizedQueue<File> resultsQueue = new SynchronizedQueue<File>(RESULTS_QUEUE_CAPACITY);

        // The application will use (numOfSearchers) searcher threads and (numOfCopiers) copier threads
        Thread[] searchArray = new Thread[numOfSearchers];
        Thread[] copierArray = new Thread[numOfCopiers];

        Scouter scouter = new Scouter(0, directoryQueue, rootDirectory, milestonesQueue, milestoneQueueFlag);
        Thread threadScouter = new Thread(scouter);
        threadScouter.start();

        for (int i = 0; i < numOfSearchers; i++) {
            int SearcherIndex = i + 1;
            Searcher Search = new Searcher(SearcherIndex, extension, directoryQueue, resultsQueue, milestonesQueue, milestoneQueueFlag);
            searchArray[i] = new Thread(Search);
            searchArray[i].start();
        }

        for (int i = 0; i < numOfCopiers; i++) {
            int copyIndex = i + numOfSearchers + 1;
            Copier Copy = new Copier(copyIndex, destinationDirectory, resultsQueue, milestonesQueue, milestoneQueueFlag);
            copierArray[i] = new Thread(Copy);
            copierArray[i].start();
        }

        // join Scouter
        try {
            threadScouter.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // join Searchers
        for (int i = 0; i < numOfSearchers; i++) {
            try {
                searchArray[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // join Copiers
        for (int i = 0; i < numOfCopiers; i++) {
            try {
                copierArray[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (milestoneQueueFlag) {
            int len = milestonesQueue.getSize();
            for (int i = 0; i < len; i++) {
                String toPrint = milestonesQueue.dequeue();
                System.out.println(i + ": " + toPrint);
            }
        }
    }
}
