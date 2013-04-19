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

public class PropertyHolder implements Serializable {

	/**
	 * Initial version. This really should never change, as this classes is a wrapper for Map only.
	 */
	private static final long serialVersionUID = 1L;

	private final Map<String, Serializable> properties = new HashMap<String, Serializable>();

	/**
	 * Gets a property from this PropertyHolder.<br>
	 * If the property does not exist, returns the default value of the property. <br>
	 * 
	 * @param key {@link Property} used to define what property you are getting
	 * @return value of the property, or the default value of the property if the property was not found.
	 */
	public <K extends Serializable> K getProperty(Property<K> key) {
		return getProperty(key, key.getDefaultValue());
	}

	/**
	 * Gets a property from this PropertyHolder.<br>
	 * If the property does not exist, returns the default value passed into the method. <br>
	 * 
	 * @param key {@link Property} used to define what property you are getting
	 * @param defValue to return if this holder does not contain the specified property
	 * @return value of the property, or the default value of the property if the property was not found.
	 */
	public <K extends Serializable> K getProperty(Property<K> key, K defValue) {
		Object obj = properties.get(key.getKey());
		if (obj == null) {
			return defValue;
		}

		if (key.getValueClass().isInstance(obj)) {
			return key.getValueClass().cast(obj);
		} else {
			return key.getDefaultValue();
		}
	}

	/**
	 * Sets a property for this PropertyHolder.<br>
	 * 
	 * @param key {@link Property} used to define what property you are setting
	 * @param value of the property you want to set
	 * @return previous value of the property, or null if the property did not have a value already
	 */
	public <T extends Serializable> T setProperty(Property<T> key, T value) {
		Object obj = properties.put(key.getKey(), value);
		if (obj == null) {
			return null;
		}

		T oldVal = null;
		if (key.getValueClass().isInstance(obj)) {
			oldVal = key.getValueClass().cast(obj);
		}
		return oldVal;
	}
}
