
/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Monitor
{
	private final int numPhilosophers;

	/*
	 * ------------
	 * Data members
	 * ------------
	 */

	// NEW states for chopsticks
	private enum State { THINKING, HUNGRY, EATING }
	private State[] aStates;
	private Object[] self;
	boolean isTalking = false;

	// NEW variables from Priority Monitor (Task 3)
	private final int[] priority_list; // priority list



	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers, int[] n)

	{
		this.numPhilosophers = piNumberOfPhilosophers;
        this.aStates = new State[piNumberOfPhilosophers];
		this.self = new Object[piNumberOfPhilosophers];

		// Task 3 priority list
		this.priority_list = n.clone();

		for (int i = 0; i < piNumberOfPhilosophers; i++)
		{
			aStates[i] = State.THINKING;
			self[i] = new Object();
		}
	}
	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */

	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public void pickUp(int pID)
	{
		synchronized(this)
		{
			int index = pID - 1; // Convert philosopher ID to array index
			aStates[index] = State.HUNGRY;

			while (aStates[index] != State.EATING)
			{
				test(index);
				if (aStates[index] != State.EATING) {
					try {
						// Release the monitor lock before waiting
						wait();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}
		}
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public void putDown(int piTID)
	{
		synchronized(this)
		{
			int index = piTID - 1; // Convert philosopher ID to array index
			aStates[index] = State.THINKING;
			
			// Test all philosophers to see if any can now eat
			for (int i = 0; i < numPhilosophers; i++) {
				if (aStates[i] == State.HUNGRY) {
					test(i);
				}
			}
		}
	}

	/**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public void requestTalk() {
		synchronized (this) {
			while (isTalking) {
				try {
					wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			isTalking = true;
		}
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public void endTalk() {
		synchronized (this) {
			isTalking = false;
			notifyAll();
		}
	}

	// Slides 7.14 Chapter 7 Lesson 6

	private int left(int i) {
		return (i + numPhilosophers - 1) % numPhilosophers;
	}

	private int right(int i) {
		return (i + 1) % numPhilosophers;
	}

	private void test(int i) {
		if (aStates[i] == State.HUNGRY &&
				aStates[left(i)] != State.EATING &&
				aStates[right(i)] != State.EATING &&
				canEatWithPriority(i)) {
			aStates[i] = State.EATING;
			// Notify all waiting philosophers
			notifyAll();
		}
	}

	/** TASK-3 Simplified priority checking to prevent deadlock
	 * A philosopher can eat if they have the highest priority among all hungry philosophers
	 */
	private boolean canEatWithPriority(int i) {
		int myPriority = priority_list[i];
		
		// Only check priority if there are other hungry philosophers
		boolean hasOtherHungry = false;
		for (int j = 0; j < numPhilosophers; j++) {
			if (j != i && aStates[j] == State.HUNGRY) {
				hasOtherHungry = true;
				break;
			}
		}
		
		// If no other hungry philosophers, allow eating
		if (!hasOtherHungry) {
			return true;
		}
		
		// Check if any higher priority philosopher is hungry
		for (int j = 0; j < numPhilosophers; j++) {
			if (j != i && aStates[j] == State.HUNGRY && priority_list[j] < myPriority) {
				return false;
			}
		}
		return true;
	}
}

// EOF
