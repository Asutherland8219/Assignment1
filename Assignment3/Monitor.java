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


	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)

	{
		this.numPhilosophers = piNumberOfPhilosophers;
		this.aStates = new State[numPhilosophers];
		this.self = new Object[numPhilosophers];

		aStates = new State[piNumberOfPhilosophers];
		self = new Object[piNumberOfPhilosophers];

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
			aStates[pID] = State.HUNGRY;
			test(pID); // try to eat now

			if (aStates[pID] != State.EATING)
			{
				try {
					synchronized (self[pID]) {
						self[pID].wait(); // wait to be notified
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
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
			aStates[piTID] = State.THINKING;
			test((piTID + 1) % aStates.length);           // Right neighbor
			test((piTID + aStates.length - 1) % aStates.length); // Left neighbor
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
				aStates[right(i)] != State.EATING) {
			aStates[i] = State.EATING;
			synchronized (self[i]) {
				self[i].notify(); // signal this philosopher
			}
		}
	}
}

// EOF
