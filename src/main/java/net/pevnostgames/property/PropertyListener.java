package net.pevnostgames.property;

import java.io.Serializable;

public interface PropertyListener<T extends Serializable> {

	/**
	 * Called when the given property is updated.
	 * 
	 * @param event representing the property update
	 */
	public void onPropertyUpdate(PropertyEvent<T> event);
}
