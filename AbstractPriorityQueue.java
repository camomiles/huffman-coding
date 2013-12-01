import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * PriorityQueue.java:  priority queue interface
 *
 * This interface supposes the natural ordering of the element type will
 * be used for priorities.
 *
 * @author (PAS)
 * @version (7/7/05)
 */

public interface AbstractPriorityQueue<E> {
	/**
	 * enqueue the element with the given priority
	 *
	 * @param elem	the element being enqueued
	 */
	public void enqueue(E elem);

	/**
	 * Remove the highest priority element.  If the queue is empty, throw an exception.
	 */
  	public void dequeue() throws NoSuchElementException;

	/**
	 * Return the highest priority element.  If the queue is empty, throw an exception.
	 *
	 * @return the item of the queue with highest priortity
	 */
  	public E front() throws NoSuchElementException;

	/**
	 * Determines whether or not the queue is empty.
	 *
	 * @return true if the queue is empty, false otherwise
	 */
	public boolean isEmpty();

	/**
	 * Determines the size of the queue
	 *
	 * @return the number of items currently enqueued
	 */
	public int size();

}
