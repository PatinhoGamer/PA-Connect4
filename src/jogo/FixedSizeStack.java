package jogo;

import java.util.Stack;

public class FixedSizeStack<T> extends Stack<T> {
	private int maxSize;
	
	public FixedSizeStack(int size) {
		super();
		this.maxSize = size;
	}
	
	@Override
	public T push(T object) {
		while (this.size() >= maxSize)
			this.remove(0);
		return super.push(object);
	}
}
