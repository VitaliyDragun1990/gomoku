package com.revenat.game.gomoku.infra;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

/**
 * Utility class that manages collections of listeners
 * 
 * @author Vitaly Dragun
 *
 */
public class Announcer<T extends EventListener> {
	private final T proxy;
	private final List<T> listeners = new ArrayList<>();
	
	public Announcer(Class<? extends T> listenerType) {
		proxy = listenerType.cast(Proxy.newProxyInstance(
				listenerType.getClassLoader(),
				new Class<?>[] {listenerType},
				(aProxy, method, args) -> {
					announce(method, args);
					return null;
				}));
	}
	
	/**
	 * Add listener to this Announcer
	 * @param listener
	 */
	public void addListener(T listener) {
		listeners.add(listener);
	}
	
	/**
	 * Remove specified listener from this Announcer
	 * @param listener
	 */
	public void removeListener(T listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Returns an entry point in form of the given listener type
	 * via which you can announce appropriate kind of events.
	 * @return
	 */
	public T announce() {
		return proxy;
	}
	
	private void announce(Method m, Object[] args) {
		try {
			for (T listener : listeners) {
				m.invoke(listener, args);
			}
		} 
		catch (IllegalAccessException e) {
			throw new IllegalArgumentException("could not invoke listener", e);
		}
		catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			
			if (cause instanceof RuntimeException) {
				throw (RuntimeException) cause;
			}
			else if (cause instanceof Error) {
				throw (Error) cause;
			}
			else {
				throw new UnsupportedOperationException("listener threw exception", cause);
			}
		}
	}
	
	/**
	 * Creates Announcer for specified listener type
	 * @param listenerType specific listener type to create Announcer for
	 * @return
	 */
	public static <T extends EventListener> Announcer<T> to(Class<? extends T> listenerType) {
		return new Announcer<>(listenerType);
	}
}
