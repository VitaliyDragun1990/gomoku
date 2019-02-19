package com.revenat.game.gomoku.infra;

import java.util.Arrays;

public class DynaArray<E> extends DataSet<E> {
	private E[] array;

	@SuppressWarnings("unchecked")
	public DynaArray() {
		array = (E[]) new Object[10];
	}
	
	public DynaArray(E[] initialData) {
		int initialArraySize = Math.max(initialData.length * 2, 10);
		array = Arrays.copyOf(initialData, initialArraySize);
		size = initialData.length;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void add(E element) {
		if (size == array.length) {
			E[] temp = array;
			array = (E[]) new Object[temp.length * 2];
			System.arraycopy(temp, 0, array, 0, size);
		}
		array[size++] = element;
	}

	@Override
	public E remove(int index) {
		checkIndex(index);
		return removeAtIndex(index);
	}

	@SuppressWarnings("unchecked")
	private E removeAtIndex(int index) {
		E element = array[index];
		size--;

		E[] temp = array;
		array = (E[]) new Object[size];
		System.arraycopy(temp, 0, array, 0, index);
		if (isNotLastElement(index)) {
			System.arraycopy(temp, index + 1, array, index, size - index);
		}

		return element;
	}

	private void checkIndex(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Invalid index " + index
					+ ". Index should be between 0 inclusive and current array size exclusive. "
					+ "(current array size = " + size() + ")");
		}
	}

	private boolean isNotLastElement(int index) {
		return index != size - 1;
	}

	@Override
	public E get(int index) {
		checkIndex(index);
		return array[index];
	}

	@SuppressWarnings("unchecked")
	@Override
	public void clear() {
		super.clear();
		array = (E[]) new Object[10];
	}

	@Override
	public Object[] toArray() {
		return Arrays.copyOf(array, size);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] arr) {
		if (arr.length < size()) {
			// Make a new array of arr's runtime type, but my contents:
			return (T[]) Arrays.copyOf(array, size, arr.getClass());
		}
		System.arraycopy(array, 0, arr, 0, size);
		if (arr.length > size())
			arr[size] = null;
		return arr;
	}

}
