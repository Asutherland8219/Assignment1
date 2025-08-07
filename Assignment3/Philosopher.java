import common.BaseThread;

/**
 * Class Philosopher.
 * Outlines main subrutines of our virtual philosopher.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Philosopher extends BaseThread
{
	/**
	 * Max time an action can take (in milliseconds)
	 */
	public static final long TIME_TO_WASTE = 1000;
	private final int id;

	public Philosopher(int id) {
//		super();
		// TASK-3 Add ID field instaed of relying on thread id
		this.id = id + 1;
		setName("Philosopher-" + id);
	}


	/**
	 * The act of eating.
	 * - Print the fact that a given phil (their TID) has started eating.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done eating.
	 */
	public void eat()
	{
		try
		{
			System.out.println("Philosopher " + id + " is eating");
			Thread.yield();
			sleep((long)(Math.random() * TIME_TO_WASTE));
			Thread.yield();
			System.out.println("Philosopher " + id + " is DONE eating");
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.eat():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * The act of thinking.
	 * - Print the fact that a given phil (their TID) has started thinking.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done thinking.
	 */
	public void think()
	{
		try {
			System.out.println("Philosopher " + id + " is thinking...");
			Thread.yield();
			sleep((long)(Math.random() * TIME_TO_WASTE));
			Thread.yield();
			System.out.println("Philosopher " + id + " is DONE thinking...");
		} catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	/**
	 * The act of talking.
	 * - Print the fact that a given phil (their TID) has started talking.
	 * - yield
	 * - Say something brilliant at random
	 * - yield
	 * - The print that they are done talking.
	 */
	public void talk()
	{
		try {
			System.out.println("Philosopher " + id + " is talking...");
			Thread.yield();
			saySomething();
			Thread.yield();
			System.out.println("Philosopher " + id + " is DONE talking...");
		}
		catch(Exception e) {
			System.err.println("Philosopher.talk():");
		}
	}

	/**
	 * No, this is not the act of running, just the overridden Thread.run()
	 */
	public void run()
	{
		for(int i = 0; i < DiningPhilosophers.DINING_STEPS; i++)
		{
			DiningPhilosophers.soMonitor.pickUp(id);

			eat();

			DiningPhilosophers.soMonitor.putDown(id);

			think();

			// 50% chance the philosopher decides to talk
			if (Math.random() < 0.5)
			{
				DiningPhilosophers.soMonitor.requestTalk();
				talk();
				DiningPhilosophers.soMonitor.endTalk();
			}

			Thread.yield();
		}
	}

	/**
	 * Prints out a phrase from the array of phrases at random.
	 * Feel free to add your own phrases.
	 */
	public void saySomething()
	{
		String[] astrPhrases =
		{
			"Eh, it's not easy to be a philosopher: eat, think, talk, eat...",
			"You know, true is false and false is true if you think of it",
			"2 + 2 = 5 for extremely large values of 2...",
			"If thee cannot speak, thee must be silent",
			"My number is " + id + ""
		};

		System.out.println
		(
			"Philosopher " + id + " says: " +
			astrPhrases[(int)(Math.random() * astrPhrases.length)]
		);
	}
}

// EOF
