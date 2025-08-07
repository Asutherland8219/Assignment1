import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Demonstration of Task 5: Dynamic Modification of the Number of Philosophers
 * This program shows how philosophers can be added and removed during execution.
 */
public class DynamicPhilosophersDemo
{
	public static void main(String[] argv)
	{
		try
		{
			System.out.println("=== TASK 5: Dynamic Modification Demo ===");
			System.out.println("This demonstrates adding and removing philosophers during execution.\n");

			// Start with 3 philosophers
			int initialPhilosophers = 3;
			System.out.println("Starting with " + initialPhilosophers + " philosophers...");

			// Generate initial priorities
			List<Integer> prioritiesList = new ArrayList<>();
			for (int i = 1; i <= initialPhilosophers; i++) {
				prioritiesList.add(i);
			}
			Collections.shuffle(prioritiesList);

			// Convert to int[]
			int[] priorities = new int[initialPhilosophers];
			for (int i = 0; i < initialPhilosophers; i++) {
				priorities[i] = prioritiesList.get(i);
			}

			// Print initial priorities
			System.out.print("Initial priorities (1 = highest): ");
			for (int p : priorities) {
				System.out.print(p + " ");
			}
			System.out.println();

			// Create monitor and philosophers
			DiningPhilosophers.soMonitor = new Monitor(initialPhilosophers, priorities);
			Philosopher aoPhilosophers[] = new Philosopher[initialPhilosophers];

			// Start initial philosophers
			for(int j = 0; j < initialPhilosophers; j++)
			{
				aoPhilosophers[j] = new Philosopher(j);
				aoPhilosophers[j].start();
			}

			System.out.println(initialPhilosophers + " philosopher(s) came in for a dinner.");

			// Let them run for a bit, then add a new philosopher
			Thread.sleep(3000);
			System.out.println("\n--- Adding a new philosopher ---");
			int newPhilosopherId = DiningPhilosophers.soMonitor.addPhilosopher(1); // Highest priority
			
			// Create and start the new philosopher
			Philosopher newPhilosopher = new Philosopher(newPhilosopherId - 1);
			newPhilosopher.start();

			// Let them run for a bit more, then remove a philosopher
			Thread.sleep(3000);
			System.out.println("\n--- Attempting to remove a philosopher ---");
			boolean removed = DiningPhilosophers.soMonitor.removePhilosopher(2);
			if (removed) {
				System.out.println("Successfully removed philosopher 2");
			} else {
				System.out.println("Could not remove philosopher 2 (probably eating)");
			}

			// Let them run for a bit more, then add another philosopher
			Thread.sleep(3000);
			System.out.println("\n--- Adding another philosopher ---");
			int anotherPhilosopherId = DiningPhilosophers.soMonitor.addPhilosopher(5); // Lower priority
			
			// Create and start the new philosopher
			Philosopher anotherPhilosopher = new Philosopher(anotherPhilosopherId - 1);
			anotherPhilosopher.start();

			// Let them run for a bit more, then remove another philosopher
			Thread.sleep(3000);
			System.out.println("\n--- Attempting to remove another philosopher ---");
			removed = DiningPhilosophers.soMonitor.removePhilosopher(1);
			if (removed) {
				System.out.println("Successfully removed philosopher 1");
			} else {
				System.out.println("Could not remove philosopher 1 (probably eating)");
			}

			// Wait for all philosophers to finish
			System.out.println("\n--- Waiting for all philosophers to finish ---");
			for(int j = 0; j < initialPhilosophers; j++)
			{
				aoPhilosophers[j].join();
			}
			
			// Wait for dynamically added philosophers
			newPhilosopher.join();
			anotherPhilosopher.join();

			System.out.println("All philosophers have left. System terminates normally.");
			System.out.println("\n=== TASK 5 Demo Complete ===");
			System.out.println("This demonstrates that the dining philosophers problem");
			System.out.println("can be modified to support dynamic addition and removal");
			System.out.println("of philosophers during execution.");
		}
		catch(InterruptedException e)
		{
			System.err.println("main():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}
}
