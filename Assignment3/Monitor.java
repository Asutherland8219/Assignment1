
import java.util.ArrayList;

/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Monitor
{
	// TASK-5 Remove final as this value is now dynamic
	private int numPhilosophers;

	/*
	 * ------------
	 * Data members
	 * ------------
	 */

	// TASK-3 NEW states for chopsticks
	// TASK-5 Change to arrayList to be dynamic, add LEFT state indicating a philosopher has left the table
	private enum State { THINKING, HUNGRY, EATING, LEFT }
	private ArrayList<State> aStates;
	private ArrayList<Object> self;
	boolean isTalking = false;

	// TASK-3 NEW variables from Priority Monitor
	private ArrayList<Integer> priority_list; // priority list

	// TASK 6 - Pepper shakers management
	private int availablePepperShakers = 2; // Two pepper shakers available
	private ArrayList<Boolean> philosopherHasPepper; // Track which philosophers have pepper shakers



	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers, int[] n)

	{
		this.numPhilosophers = piNumberOfPhilosophers;
        this.aStates = new ArrayList<>();
		this.self = new ArrayList<>();

		// Task 3 priority list
		this.priority_list = new ArrayList<>();

		// TASK 6 - Initialize pepper shaker tracking
		this.philosopherHasPepper = new ArrayList<>();

		for (int i = 0; i < piNumberOfPhilosophers; i++)
		{
			aStates.add(State.THINKING);
			self.add(new Object());
			priority_list.add(n[i]);
			philosopherHasPepper.add(false); // Initially no philosopher has pepper
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
			aStates.set(index, State.HUNGRY);

			while (aStates.get(index) != State.EATING)
			{
				test(index);
				if (aStates.get(index) != State.EATING) {
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
			aStates.set(index, State.THINKING);
			
			// Test all philosophers to see if any can now eat
			for (int i = 0; i < numPhilosophers; i++) {
				if (aStates.get(i) == State.HUNGRY) {
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
		if (aStates.get(i) == State.HUNGRY &&
				aStates.get(left(i)) != State.EATING &&
				aStates.get(right(i)) != State.EATING &&
				canEatWithPriority(i)) {
			aStates.set(i, State.EATING);
			// Notify all waiting philosophers
			notifyAll();
		}
	}

	/** TASK-3 Simplified priority checking to prevent deadlock
	 * A philosopher can eat if they have the highest priority among all hungry philosophers
	 */
	private boolean canEatWithPriority(int i) {
		int myPriority = priority_list.get(i);
		
		// Only check priority if there are other hungry philosophers
		boolean hasOtherHungry = false;
		for (int j = 0; j < numPhilosophers; j++) {
			if (j != i && aStates.get(j) == State.HUNGRY) {
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
			if (j != i && aStates.get(j) == State.HUNGRY && priority_list.get(j) < myPriority) {
				return false;
			}
		}
		return true;
	}
	/**
	 * TASK 5 - Add a new philosopher to the table
	 * and add priority as param
	 */
	public synchronized int addPhilosopher(int priority) {
		int newId = numPhilosophers + 1;
		numPhilosophers++;
		
		// Add new philosopher's data
		aStates.add(State.THINKING);
		self.add(new Object());
		priority_list.add(priority);
		philosopherHasPepper.add(false); // TASK-6 - Initialize pepper tracking
		
		System.out.println("Philosopher " + newId + " joined the table with priority " + priority);
		return newId;
	}

	/**
	 * TASK 5 - Remove a philosopher from the table
	 * param is The ID of the philosopher to remove
	 * return true if successfully removed, false if philosopher was eating
	 */
	public synchronized boolean removePhilosopher(int philosopherId) {
		int index = philosopherId - 1;
		
		// Check if philosopher is eating
		if (aStates.get(index) == State.EATING) {
			System.out.println("Cannot remove Philosopher " + philosopherId + " - currently eating");
			return false;
		}
		
		// TASK 6 - Return pepper shaker if philosopher has one
		if (philosopherHasPepper.get(index)) {
			returnPepperShaker(philosopherId);
		}
		
		// Mark philosopher as left
		aStates.set(index, State.LEFT);
		
		System.out.println("Philosopher " + philosopherId + " left the table");
		return true;
	}

	/**
	 * TASK-5 - Get current number of active philosophers
	 * return Number of philosophers at the table
	 */

	public synchronized int getPhilosopherCount() {
		return numPhilosophers;
	}

	/**
	 * TASK 5 - Check if a philosopher is still at the table
	 * passing philosopherId as param
	 * return true if philosopher is still at the table
	 */
	public synchronized boolean isPhilosopherActive(int philosopherId) {
		int index = philosopherId - 1;
		return index >= 0 && index < aStates.size() && aStates.get(index) != State.LEFT;
	}

	/**
	 * TASK 6 - Request a pepper shaker for eating
	 * passing philosopherId as param
	 * return true if pepper shaker was obtained, false otherwise
	 */

	public synchronized boolean requestPepperShaker(int philosopherId) {
		int index = philosopherId - 1;
		
		// Only eating philosophers can use pepper shakers
		if (aStates.get(index) != State.EATING) {
			System.out.println("Philosopher " + philosopherId + " cannot use pepper - not eating");
			return false;
		}
		
		// Check if philosopher already has a pepper shaker
		if (philosopherHasPepper.get(index)) {
			return true; // Already has pepper
		}
		
		// Check if pepper shakers are available
		if (availablePepperShakers > 0) {
			availablePepperShakers--;
			philosopherHasPepper.set(index, true);
			System.out.println("Philosopher " + philosopherId + " grabbed a pepper shaker (" +
				availablePepperShakers + " remaining)");
			return true;
		} else {
			System.out.println("Philosopher " + philosopherId + " waiting for pepper shaker");
			return false;
		}
	}

	/**
	 * TASK 6 - Return a pepper shaker after eating
	 * pass the param as yhe ID of the philosopher returning pepper
	 */
	public synchronized void returnPepperShaker(int philosopherId) {
		int index = philosopherId - 1;
		
		if (philosopherHasPepper.get(index)) {
			availablePepperShakers++;
			philosopherHasPepper.set(index, false);
			System.out.println("Philosopher " + philosopherId + " returned pepper shaker (" + 
				availablePepperShakers + " available)");
			
			// Notify waiting philosophers that pepper is available
			notifyAll();
		}
	}

	/**
	 * TASK 6 - Wait for pepper shaker if not immediately available
	 * param is the ID of the philosopher waiting for pepper
	 */
	public void waitForPepperShaker(int philosopherId) {
		synchronized(this) {
			while (!requestPepperShaker(philosopherId)) {
				try {
					wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
		}
	}
}

// EOF
