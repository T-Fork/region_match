class MutableInt
{
	private int value;

	MutableInt() {
		// the default value is 0
		this.value = 0;
	}

	MutableInt(int value) {
		// initialize with the specified value
		this.value = value;
	}

	public void increment() {
		// increment value by 1
		value++;
	}

	public int getAndIncrement() {
		// increment value by 1 and return the previous value
		return value++;
	}

	public int incrementAndGet() {
		// increment value by 1 and return
		return ++value;
	}

	public void decrement() {
		// decrement value by 1
		value--;
	}

	public int getAndDecrement() {
		// decrement value by 1 and return the previous value
		return value--;
	}

	public int decrementAndGet() {
		// decrement value by 1 and return
		return --value;
	}

	public void add(int operand) {
		// add operand value to the value of this instance
		value += operand;
	}

	public void remove(int operand) {
		// remove operand value from the value of this instance
		value -= operand;
	}

	public int getValue() {
		// return value of this instance as int
		return value;
	}

	public void setValue(int value) {
		// set value of this instance to specified int value
		this.value = value;
	}

	@Override
	public String toString() {
		// returns the corresponding string value of this mutable
		return String.valueOf(getValue());
	}
}

// Program to implement `MutableInt` class in Java
class Main
{
	public static void main(String[] args)
	{
		MutableInt i = new MutableInt();
		i.increment();
		i.add(9);
		i.remove(4);

		System.out.println(i);
	}
}