package com.revenat.game.gomoku.infra;

import java.util.Arrays;

public abstract class DataSet<E> {
	protected int size;

	public DataSet() {
		size = 0;
	}

	public final int size() {
		return size;
	}

	public void clear() {
		size = 0;
	}

	public abstract void add(E element);

	public abstract E get(int index);

	public abstract E remove(int index);

	public abstract Object[] toArray();
	
	public abstract <T> T[] toArray(T[] arr);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + size;
		result = prime * result + Arrays.hashCode(toArray());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		@SuppressWarnings("unchecked")
		DataSet<E> other = (DataSet<E>) obj;
		if (size != other.size) {
			return false;
		}
		if (!Arrays.equals(toArray(), other.toArray())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return Arrays.toString(toArray());
	}
}
