package net.pevnostgames.property;

public class Example implements PropertyListener {

	/**
	 * This Property represents the key "NUMBER", the class Integer, and the default value of 3943
	 */
	public static final Property<Integer> NUMBER = new Property<Integer>("NUMBER", Integer.class, 3943);

	/**
	 * This Property represents the key "STRING", the class String, and the default value of "This requires no cast"
	 */
	public static final Property<String> STRING = new Property<String>("STRING", String.class, "This requires no cast");

	/**
	 * This is the object that stores these properties for you.
	 * You may extend it, or have an instance of it, depending on how you want to expose access.
	 */
	private final PropertyHolder holder = new PropertyHolder();

	public Example() {
		// This gets the default value of Example.NUMBER from the holder, because the property Example.NUMBER has not been set.
		System.out.println(holder.getProperty(Example.NUMBER));					// Console output: 3943

		// This gets the default value 9342327 from the holder, because the property Example.NUMBER has not been set.
		System.out.println(holder.getProperty(Example.NUMBER, 9342327));		// Console output: 9342327

		// This gets the default value of Example.STRING from the holder, because the property Example.STRING has not been set.
		System.out.println(holder.getProperty(Example.STRING));					// Console output: This requires no cast

		// This gets the default value null from the holder, because the property Example.STRING has not been set.
		System.out.println(holder.getProperty(Example.STRING, null));			// Console output: null

		Example.NUMBER.registerGlobalListener(this);

		// This sets the value of Example.NUMBER to 84588 in the holder, where the previous value was null
		System.out.println(holder.setProperty(Example.NUMBER, 84588));			// Console output: null

		// This sets the value of Example.NUMBER to 24 in the holder, where the previous value was 84558
		System.out.println(holder.setProperty(Example.NUMBER, 24));				// Console output: 84558

		// This gets the value of Example.NUMBER, which should be 24
		System.out.println(holder.getProperty(Example.NUMBER));					// Console output: 24

		// This sets the value of Example.STRING to "test1" in the holder, where the previous value was null
		System.out.println(holder.setProperty(Example.STRING, "test1"));		// Console output: null

		Example.STRING.registerListener(holder, this);

		// This sets the value of Example.STRING to "test2" in the holder, where the previous value was "test1"
		System.out.println(holder.setProperty(Example.STRING, "test2"));		// Console output: test1

		// This gets the value of Example.STRING, which should be "test2"
		System.out.println(holder.getProperty(Example.STRING));					// Console output: test2

	}

	@PropertyChanged(key = "NUMBER", type = Integer.class)
	public void numberChanged(PropertyEvent<Integer> event) {
		System.out.println("Global listener test: " + event);
	}

	@PropertyChanged(key = "STRING", type = String.class)
	public void stringChanged(PropertyEvent<String> event) {
		System.out.println("Holder listener test: " + event);
	}

	public static void main(String[] args) {
		new Example();
	}
}
