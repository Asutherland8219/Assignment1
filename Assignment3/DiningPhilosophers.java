import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Class DiningPhilosophers
 * The main starter.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class DiningPhilosophers
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */

	/**
	 * This default may be overridden from the command line
	 */
	public static final int DEFAULT_NUMBER_OF_PHILOSOPHERS = 4;

	/**
	 * Dining "iterations" per philosopher thread
	 * while they are socializing there
	 */
	public static final int DINING_STEPS = 10;

	/**
	 * Our shared monitor for the philosphers to consult
	 */
	public static Monitor soMonitor = null;

	/*
	 * -------
	 * Methods
	 * -------
	 */

	/**
	 * Main system starts up right here
	 */
	public static void main(String[] argv)
	{
		try
		{
			int iPhilosophers = DEFAULT_NUMBER_OF_PHILOSOPHERS;
			
			// TASK 4 - Accept command line argument for number of philosophers
			if (argv.length > 0) {
				try {
					iPhilosophers = Integer.parseInt(argv[0]);
					if (iPhilosophers <= 0) {
						System.err.println("\"" + argv[0] + "\" is not a positive decimal integer");
						System.err.println("Usage: java DiningPhilosophers [NUMBER_OF_PHILOSOPHERS]");
						System.exit(1);
					}
				} catch (NumberFormatException e) {
					System.err.println("\"" + argv[0] + "\" is not a positive decimal integer");
					System.err.println("Usage: java DiningPhilosophers [NUMBER_OF_PHILOSOPHERS]");
					System.exit(1);
				}
			}

			/** TASK 3 - Generate priorities for the specified number of philosophers
			 */
			Random rand = new Random();

			// Generate unique random priorities from 1 to N
			List<Integer> prioritiesList = new ArrayList<>();
			for (int i = 1; i <= iPhilosophers; i++) {
				prioritiesList.add(i);
			}
			Collections.shuffle(prioritiesList);

			// Convert to int[]
			int[] priorities = new int[iPhilosophers];
			for (int i = 0; i < iPhilosophers; i++) {
				priorities[i] = prioritiesList.get(i);
			}

			// Print priorities
			System.out.print("Assigned priorities (1 = highest): ");
			for (int p : priorities) {
				System.out.print(p + " ");
			}
			System.out.println();

			// Make the monitor aware of how many philosophers there are
			soMonitor = new Monitor(iPhilosophers, priorities);

			// Space for all the philosophers
			Philosopher aoPhilosophers[] = new Philosopher[iPhilosophers];

			// Let 'em sit down
			for(int j = 0; j < iPhilosophers; j++)
			{
				// TASK-3 Add the id passed in so we can identify the philosopher
				aoPhilosophers[j] = new Philosopher(j);
				aoPhilosophers[j].start();
			}

			System.out.println
			(
				iPhilosophers +
				" philosopher(s) came in for a dinner."
			);

			// Main waits for all its children to die...
			// I mean, philosophers to finish their dinner.
			for(int j = 0; j < iPhilosophers; j++)
				aoPhilosophers[j].join();

			System.out.println("All philosophers have left. System terminates normally.");
		}
		catch(InterruptedException e)
		{
			System.err.println("main():");
			reportException(e);
			System.exit(1);
		}
	} // main()

	/**
	 * Outputs exception information to STDERR
	 * @param poException Exception object to dump to STDERR
	 */
	public static void reportException(Exception poException)
	{
		System.err.println("Caught exception : " + poException.getClass().getName());
		System.err.println("Message          : " + poException.getMessage());
		System.err.println("Stack Trace      : ");
		poException.printStackTrace(System.err);
	}
}

// EOF
