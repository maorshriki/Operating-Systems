//Ex3
//name : Maor Shriki
//id : 208274118

/**
 * A synchronized bounded-size queue for multithreaded producer-consumer applications.
 *
 * @param <T> Type of data items
 */
public class SynchronizedQueue<T> {

    private final int capacity;
    private T[] buffer;
    private int producers;
    private int in;
    private int out;

    /**
     * Constructor. Allocates a buffer (an array) with the given capacity and
     * resets pointers and counters.
     *
     * @param capacity Buffer capacity
     */
    @SuppressWarnings("unchecked")
    public SynchronizedQueue(int capacity) {
        this.buffer = (T[]) (new Object[capacity]);
        this.capacity = capacity;
        this.producers = 0;
        this.in = 0;
        this.out = 0;
    }

    /**
     * Dequeues the first item from the queue and returns it.
     * If the queue is empty but producers are still registered to this queue,
     * this method blocks until some item is available.
     * If the queue is empty and no more items are planned to be added to this
     * queue (because no producers are registered), this method returns null.
     *
     * @return The first ite, or null if there are no more items
     * @see #registerProducer()
     * @see #unregisterProducer()
     */

    public T dequeue() {
        synchronized (this) {
            while ((this.in - this.out) == 0 && this.producers != 0) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (this.producers == 0) {
                if ((this.in - this.out) == 0) return null;
            }
            T item = this.buffer[this.out % this.capacity];
            this.out++;
            return item;
        }
    }

    /**
     * Enqueues an item to the end of this queue. If the queue is full, this
     * method blocks until some space becomes available.
     *
     * @param item Item to enqueue
     */

    public void enqueue(T item) {
        synchronized (this) {
            while ((this.in - this.out) >= this.getCapacity()) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.buffer[this.in % this.capacity] = item;
            this.in = this.in + 1;
            this.notifyAll();
        }
    }

    /**
     * Returns the capacity of this queue
     *
     * @return queue capacity
     */
    public int getCapacity() {
        return this.capacity;
    }

    /**
     * Returns the current size of the queue (number of elements in it)
     *
     * @return queue size
     */
    public int getSize() {
        return (this.in - this.out);
    }

    /**
     * Registers a producer to this queue. This method actually increases the
     * internal producers counter of this queue by 1. This counter is used to
     * determine whether the queue is still active and to avoid blocking of
     * consumer threads that try to dequeue elements from an empty queue, when
     * no producer is expected to add any more items.
     * Every producer of this queue must call this method before starting to
     * enqueue items, and must also call <see>{@link #unregisterProducer()}</see> when
     * finishes to enqueue all items.
     *
     * @see #dequeue()
     * @see #unregisterProducer()
     */
    public void registerProducer() {
        synchronized (this) {
            if ((this.in - this.out) >= this.capacity) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.producers = this.producers + 1;
        }
    }

    /**
     * Unregisters a producer from this queue. See <see>{@link #registerProducer()}</see>.
     *
     * @see #dequeue()
     * @see #registerProducer()
     */
    public void unregisterProducer() {
        synchronized (this) {
            this.producers = this.producers - 1;
            this.notifyAll();
        }
    }
}
