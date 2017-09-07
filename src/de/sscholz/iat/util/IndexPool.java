package de.sscholz.iat.util;

import java.util.LinkedList;
import java.util.Queue;

public class IndexPool {

	private final Queue<Integer> free = new LinkedList<>();
	private final Queue<Integer> used = new LinkedList<>();

	public void add(int value) {
		free.add(value);
	}

	public int acquire() {
		Integer value = free.poll();
		if (value == null)
			throw new IllegalStateException("No free index left to acquire.");
		used.add(value);
		return value;
	}

	public void release(int value) {
		if (used.contains(value)) {
			used.remove(value);
			free.add(value);
		}
	}

}
