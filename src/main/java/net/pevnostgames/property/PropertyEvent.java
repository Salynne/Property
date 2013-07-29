package net.pevnostgames.property;

import java.io.Serializable;

public class PropertyEvent<T extends Serializable> {

	private final Property<T> property;
	private final PropertyHolder holder;
	private final T oldValue;
	private T newValue;

	protected PropertyEvent(Property<T> property, PropertyHolder holder, T oldValue, T newValue) {
		this.property = property;
		this.holder = holder;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public Property<T> getProperty() {
		return property;
	}

	public PropertyHolder getHolder() {
		return holder;
	}

	public T getOldValue() {
		return oldValue;
	}

	public T getNewValue() {
		return newValue;
	}

	public void setNewValue(T newValue) {
		this.newValue = newValue;
	}

	@Override
	public String toString() {
		return "PropertyEvent{" +
				"property=" + property +
				", holder=" + holder +
				", oldValue=" + oldValue +
				", newValue=" + newValue +
				'}';
	}
}
