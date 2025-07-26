/**
 * Class BlockStack
 * Implements character block stack and operations upon it.
 *
 * $Revision: 1.4 $
 * $Last Revision Date: 2019/02/02 $
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca;
 * Inspired by an earlier code by Prof. D. Probst

 */
class BlockStack {


	public class StackOverflowException extends RuntimeException {
		public StackOverflowException(String message) {
			super(message);
		}
	}

	public class StackUnderflowException extends RuntimeException {
		public StackUnderflowException(String message) {
			super(message);
		}
	}
	/**
	 * # of letters in the English alphabet + 2
	 */
	public static final int MAX_SIZE = 28;

	private int stack_access_counter;

	/**
	 * Default stack size
	 */
	public static final int DEFAULT_SIZE = 6;

	/**
	 * Current size of the stack
	 */
	public int iSize = DEFAULT_SIZE;

	/**
	 * Current top of the stack
	 */
	public int iTop = 3;

	/**
	 * stack[0:5] with four defined values
	 */
	public char acStack[] = new char[]{'a', 'b', 'c', 'd', '$', '$'};

	/**
	 * Default constructor
	 */
	public BlockStack() {
	}

	/**
	 * Supplied size
	 */
	public BlockStack(final int piSize) {


		if (piSize != DEFAULT_SIZE) {
			this.acStack = new char[piSize];

			// Fill in with letters of the alphabet and keep
			// 2 free blocks
			for (int i = 0; i < piSize - 2; i++)
				this.acStack[i] = (char) ('a' + i);

			this.acStack[piSize - 2] = this.acStack[piSize - 1] = '$';

			this.iTop = piSize - 3;
			this.iSize = piSize;
		}
	}

	/**
	 * Picks a value from the top without modifying the stack
	 *
	 * @return top element of the stack, char
	 */
	public char pick()
	{
		BlockManager.mutex.P();
		try
		{
			stack_access_counter++;

			if (isEmpty()) {
				throw new StackUnderflowException("Stack is empty. Cannot pick.");
			}

			char item = acStack[iTop];
			System.out.println("Picked: " + item);
			return item;
		}
		finally
		{
			BlockManager.mutex.V();
		}
	}

	/**
	 * Returns arbitrary value from the stack array
	 *
	 * @return the element, char
	 */
	public char getAt(int position)
	{
		BlockManager.mutex.P();
		try
		{
			stack_access_counter++;

			if (position < 0 || position > iTop) {
				throw new IndexOutOfBoundsException("Invalid stack position: " + position);
			}

			char item = acStack[position];
			System.out.println("Got at " + position + ": " + item);
			return item;
		}
		finally
		{
			BlockManager.mutex.V();
		}
	}

	/**
	 * Standard push operation
	 */
	public void push(final char pcBlock)
	{
		BlockManager.mutex.P();
		try
		{
			stack_access_counter++;

			if (iTop + 1 >= iSize) {
				throw new StackOverflowException("Stack is full. Cannot push '" + pcBlock + "'.");
			}

			if (isEmpty()) {
				this.acStack[++this.iTop] = 'a';
			} else {
				this.acStack[++this.iTop] = pcBlock;
			}

			System.out.println("Pushed: " + this.acStack[iTop]); // Optional: log action
		}
		finally
		{
			BlockManager.mutex.V();
		}
	}



	/**
	 * Standard pop operation
	 *
	 * @return ex-top element of the stack, char
	 */
	public char pop()
	{
		BlockManager.mutex.P();
		try
		{
			stack_access_counter++;

			if (isEmpty()) {
				throw new StackUnderflowException("Stack is empty. Cannot pop.");
			}

			char item = acStack[iTop];
			acStack[iTop--] = '*'; // Mark popped position
			System.out.println("Popped: " + item);
			return item;
		}
		finally
		{
			BlockManager.mutex.V();
		}
	}


	// new methods
	public int getITop() {
		stack_access_counter++;
		return this.iTop;
	}

	public int getAccessCounter() {
		return stack_access_counter;
	}

	public boolean isEmpty() {
		stack_access_counter++;
		return this.iTop == -1;
	}

	public int getISize() {
		stack_access_counter++;
		return this.iSize;
	}
}

// EOF
