package myUtil;

/**
 * This generic interface is the standard Queue API 
 * @author Chung-Chih Li
 * @param <T> any user defined type
 */
public interface Queue<T> {
	
	/**
	 * Offer the item into this Queue at the rear end.
	 * @param item of the same type
	 * @return false if fail to do so, in case the capacity is restricted. 
	 */
	boolean offer(T item);  
	
	/**
	 * Remove the item from the Queue at the front end.
	 * @return the item that is removed.
	 * @exception NoSuchElementException if no item to be removed.
	 * 
	 */
	T remove();  
	
	/**
	 * Remove the item at the front end. This is same as Remove but no exception 
	 * will be thrown.
	 * @return the item that is removed, or null if no item to be removed.
	 */
	T poll();   
	
	/**
	 * Peek at the first item at the front end.
	 * @return the first item at the front end, or null if this Queue is empty.
	 */
	T peek();  
	
	/**
	 * Peek at the first item at the front end.
	 * @return the first item at the front end.
	 * @exception NoSuchElementException if this Queue is empty.
	 */
	T element(); 
	
	/**
	 * @return true if this Queue is empty, false otherwise
	 */
	boolean empty();
	
	/**
	 * @return the number of the element in the queue.
	 */
	int size();
	
	/**
	 * @return the capacity of the queue, -1 if unlimited. 
	 */
	int capacity();
}