package myUtil;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class ListQueue<T> implements myUtil.Queue<T> 
{
	private class Node<T>
	{
		private T data;
		private Node<T> next;
		private Node(T data, Node<T> next)
		{
			this.data = data;
			this.next = next;
		}
	}
	
	private Node<T> head;
	private Node<T> rear;
	private int size;
	private int capacity;
	
	public ListQueue()
	{
		head = rear = null;
		size = 0;
		capacity = -1;
	}
	public ListQueue(int n)
	{
		head = rear = null;
		size = 0; 
		capacity = n;
	}
	@Override
	public boolean offer(T item)
	{
		if(size == capacity)
		{
			return false;
		}
		if(rear == null)
		{
			head = rear = new Node<T>(item, null);
		}
		else
		{
			rear = rear.next = new Node<T>(item, null);
		}
		
		size++;
		return true;
	}
	@Override
	public T remove()
	{
		if(head == null)
		{
			throw new NoSuchElementException();
		}
		
		T item = head.data;
		if(head == rear)
		{
			head = rear = null;
		}
		else
		{
			head = head.next;
		}
		
		size--;
		return item;
	}
	@Override
	public T poll()
	{
		if(head == null)
		{
			return null;
		}
		
		T item = head.data;
		if(head == rear)
		{
			head = rear = null;
		}
		else
		{
			head = head.next;
		}
		
		size--;
		return item;
	}
	@Override
	public T peek()
	{
		if(size == 0)
		{
			return null;
		}
		
		return head.data;
	}
	@Override
	public T element()
	{
		if(head == null)
		{
			throw new NoSuchElementException();
		}
		
		return head.data;
	}
	@Override
	public boolean empty()
	{
		if(head == null)
		{
			return true;
		}
		
		return false;
	}
	@Override
	public int size()
	{
		return size;
	}
	@Override
	public int capacity()
	{
		return capacity;
	}
}