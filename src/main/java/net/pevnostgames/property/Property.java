/*
 * Copyright {{ year }} {{ organization }}
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package net.pevnostgames.property;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Property<T extends Serializable> {
	private static final Map<String, Property<?>> registry = new HashMap<String, Property<?>>();
	private final String key;
	private final Class<T> vClass;
	private final T defValue;
	private final Map<PropertyListener, Method> globalListeners = new HashMap<PropertyListener, Method>();
	private final Map<PropertyHolder, Map<PropertyListener, Method>> holderListeners = new HashMap<PropertyHolder, Map<PropertyListener, Method>>();

	/**
	 * Creates a new Property Key with the specified key, class, and a default value of null.<br>
	 * If the property key is not unique, this will throw an {@link IllegalArgumentException}.<br>
	 * All property keys must be unique from each other, or you can get collisions in your data or failed casts.<br>
	 * 
	 * @param key {@link String} unique to this property
	 * @param valueClass the class of the value, must be the same as the type you declare to the generic
	 */
	public Property(String key, Class<T> valueClass) {
		this(key, valueClass, null);
	}

	/**
	 * Creates a new Property Key with the specified key, class, and default value.<br>
	 * If the property key is not unique, this will throw an {@link IllegalArgumentException}.<br>
	 * All property keys must be unique from each other, or you can get collisions in your data or failed casts.<br>
	 * 
	 * @param key {@link String} unique to this property
	 * @param valueClass the class of the value, must be the same as the type you declare to the generic
	 * @param defValue the default value to return if the property is missing
	 */
	public Property(String key, Class<T> valueClass, T defValue) {
		if (registry.containsKey(key)) {
			throw new IllegalArgumentException("The property key of " + key + " is already registered to " + registry.get(key));
		}
		this.key = key;
		this.vClass = valueClass;
		this.defValue = defValue;
		registry.put(key, this);
	}

	/**
	 * Registers a listener for a specific {@link PropertyHolder} with this specific property.
	 * Any changes to this property on the the specified holder will fire an event to the listener.
	 *
	 * @param holder to watch for changes to this property
	 * @param listener to report changes for this property to
	 */
	public void registerListener(PropertyHolder holder, PropertyListener listener) {
		Map<PropertyListener, Method> listeners = holderListeners.get(holder);
		if (listeners == null) {
			listeners = new HashMap<PropertyListener, Method>();
		}

		listeners.put(listener, getListenerMethod(listener));
		holderListeners.put(holder, listeners);
	}

	/**
	 * Registers a new listener for this property.
	 * Any changes to this property in any holder will fire an event to the listener.
	 * 
	 * @param listener to report changes for this property to
	 */
	public void registerGlobalListener(PropertyListener listener) {
		globalListeners.put(listener, getListenerMethod(listener));
	}

	/**
	 * Gets the default value associated with this property.<br>
	 * The value may be null, or any value of T.<br>
	 * This value is the value returned by {@link PropertyHolder#getProperty(Property)} if the holder does not contain this Property.<br>
	 * 
	 * @return T default value
	 */
	public T getDefaultValue() {
		return defValue;
	}

	/**
	 * Gets the {@link String} key for the {@link HashMap} in {@link PropertyHolder}.<br>
	 * Represents a unique key associated with this property only.<br>
	 * 
	 * @return key {@link String} associated with this property.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Gets the class associated with the type of this property.<br>
	 * This stores the class reference of the property type so that casting can be done at runtime.<br>
	 * 
	 * @return class {@link Class} associated with this property.
	 */
	public Class<T> getValueClass() {
		return vClass;
	}

	private Method getListenerMethod(PropertyListener listener) {
		Method[] methods = listener.getClass().getMethods();

		for (Method method : methods) {
			if (method.isAnnotationPresent(PropertyChanged.class)) {
				PropertyChanged changed = method.getAnnotation(PropertyChanged.class);
				if (changed.key().equals(getKey())) {
					return method;
				}
			}
		}
		throw new RuntimeException(listener + " does not contain a method valid to listen for " + this + " changes.");
	}

	protected void invokeEvent(PropertyEvent<T> event) {
		Set<Map.Entry<PropertyListener, Method>> entries = globalListeners.entrySet();
		for (Map.Entry<PropertyListener, Method> entry : entries) {
			Method method = entry.getValue();
			PropertyListener listener = entry.getKey();
			try {
				if (method != null) {
					method.invoke(listener, event);
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		Map<PropertyListener, Method> listeners = holderListeners.get(event.getHolder());
		if (listeners != null) {
			entries = listeners.entrySet();
			for (Map.Entry<PropertyListener, Method> entry : entries) {
				Method method = entry.getValue();
				PropertyListener listener = entry.getKey();
				try {
					if (method != null) {
						method.invoke(listener, event);
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public int hashCode() {
		return key.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Property && ((Property<?>) obj).getKey().equals(key);
	}

	@Override
	public String toString() {
		return "Property { key: " + key + ", class: " + vClass + ", defaultValue: " + defValue + "}";
	}
}
