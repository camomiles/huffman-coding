import java.util.PriorityQueue;
import java.util.NoSuchElementException;

/**
 * Concrete implementation of the AbstractPriorityQueue interface.
 *
 * This class essentially maps between the method signatures required by the interface
 * and the method of the PriorityQueue library class.
 *
 * @author PAS
 * @version 8/10/11
 */

public class ConcretePriorityQueue<E extends Comparable<?>> extends PriorityQueue<E> implements AbstractPriorityQueue<E> {
	/**
	 * @{inheritDoc}
	 */
	public void enqueue(E e) { add(e); }

	/**
	 * @{inheritDoc}
	 */
	public void dequeue() throws NoSuchElementException {
		// Note: takes advantage of side-effect of successful poll() operation removing head of queue
		if (poll() == null)
			throw new NoSuchElementException();
	}

	/**
	 * @{inheritDoc}
	 */
	public E front() throws NoSuchElementException {
		if (peek() == null)
			throw new NoSuchElementException();
		else
			return peek();
	}

	/**
	 * @{inheritDoc}
	 */
	public boolean isEmpty() { return size() == 0; }
}
