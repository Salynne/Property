package net.pevnostgames.property;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyChanged {
	String key();
	Class type();
}
