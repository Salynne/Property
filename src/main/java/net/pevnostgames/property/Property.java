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
import java.util.HashMap;
import java.util.Map;

public class Property<T extends Serializable> {
	private static final Map<String, Property<?>> registry = new HashMap<String, Property<?>>();
	private final String key;
	private final Class<T> vClass;
	private final T defValue;

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

	@Override
	public int hashCode() {
		return key.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Property) {
			return ((Property<?>) obj).getKey().equals(key);
		}
		return false;
	}

	@Override
	public String toString() {
		return "Property { key: " + key + ", class: " + vClass + ", defaultValue: " + defValue + "}";
	}
}
