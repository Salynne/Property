package net.pevnostgames.property;

import java.io.Serializable;

public class PropertyEvent<T extends Serializable> {

	private final PropertyHolder holder;
	private final Property<T> property;
	private final T value;

	protected PropertyEvent(PropertyHolder holder, Property<T> property, T value) {
		this.holder = holder;
		this.property = property;
		this.value = value;
	}

	public PropertyHolder getHolder() {
		return holder;
	}

	public Property<T> getProperty() {
		return property;
	}

	public T getValue() {
		return value;
	}
}
