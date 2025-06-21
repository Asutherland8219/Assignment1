/**
 * Class AccountManager
 * Implements account manager that twists depositor and withdrawal threads.
 *
 * @author Malek Barhoush, mbarhoush@hotmail.com;
 *
 * $Revision: 1.0 $
 * $Last Revision Date: 2018/12/31
 */

public class AccountManager_task4 {
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("Task 4: Block Level Synchronization");
		Account_task4[] accountTask4 = new Account_task4[10];
		Depositor_task4[] deposit4 = new Depositor_task4[10];
		Withdrawer_task4[] withdraw4 = new Withdrawer_task4[10];

		// The birth of  10 accounts
		accountTask4[0] = new Account_task4(1234,"Mike",1000);
		accountTask4[1] = new Account_task4(2345,"Adam",2000);
		accountTask4[2] = new Account_task4(3456,"Linda",3000);
		accountTask4[3] = new Account_task4(4567,"John",4000);
		accountTask4[4] = new Account_task4(5678,"Rami",5000);
		accountTask4[5] = new Account_task4(6789,"Lee",6000);
		accountTask4[6] = new Account_task4(7890,"Tom",7000);
		accountTask4[7] = new Account_task4(8901,"Lisa",8000);
		accountTask4[8] = new Account_task4(9012,"Sam",9000);
		accountTask4[9] = new Account_task4(4321,"Ted",10000);

		// The birth of 10 depositors
		deposit4[0] = new Depositor_task4(accountTask4[0]);
		deposit4[1] = new Depositor_task4(accountTask4[1]);
		deposit4[2] = new Depositor_task4(accountTask4[2]);
		deposit4[3] = new Depositor_task4(accountTask4[3]);
		deposit4[4] = new Depositor_task4(accountTask4[4]);
		deposit4[5] = new Depositor_task4(accountTask4[5]);
		deposit4[6] = new Depositor_task4(accountTask4[6]);
		deposit4[7] = new Depositor_task4(accountTask4[7]);
		deposit4[8] = new Depositor_task4(accountTask4[8]);
		deposit4[9] = new Depositor_task4(accountTask4[9]);

		// The birth of  10 withdraws
		withdraw4[0] = new Withdrawer_task4(accountTask4[0]);
		withdraw4[1] = new Withdrawer_task4(accountTask4[1]);
		withdraw4[2] = new Withdrawer_task4(accountTask4[2]);
		withdraw4[3] = new Withdrawer_task4(accountTask4[3]);
		withdraw4[4] = new Withdrawer_task4(accountTask4[4]);
		withdraw4[5] = new Withdrawer_task4(accountTask4[5]);
		withdraw4[6] = new Withdrawer_task4(accountTask4[6]);
		withdraw4[7] = new Withdrawer_task4(accountTask4[7]);
		withdraw4[8] = new Withdrawer_task4(accountTask4[8]);
		withdraw4[9] = new Withdrawer_task4(accountTask4[9]);

		System.out.println("Print initial account balances");
		// Print initial account balances
		for(int i=0;i<10;i++)
			System.out.println(accountTask4[i]);

		// Get start time in milliseconds
		long start = System.currentTimeMillis();

		System.out.println("Depositor and Withdrawal threads have been created");
		/*
		 * Interleave all threads
		 */
		for(int i=0; i<10; i++){
			deposit4[i].start();
			withdraw4[i].start();
		}


		for(int i=0; i<10; i++){
			try {
				deposit4[i].join();
				withdraw4[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Get elapsed time in milliseconds
		long elapsedTimeMillis = System.currentTimeMillis()-start;

		System.out.println("Print final account balances after all the child thread terminated...");
		// Print final account balances after all the child thread terminated...
		for(int i=0;i<10;i++)
			System.out.println(accountTask4[i]);
		// Get elapsed time in seconds
		float elapsedTimeSec = elapsedTimeMillis/1000F;

		System.out.println("Elapsed time in milliseconds "+elapsedTimeMillis);
		System.out.println("Elapsed time in seconds is "+elapsedTimeSec);

		//  Get elapsed time in minutes
		float elapsedTimeMin = elapsedTimeMillis/(60*1000F);
		// Get elapsed time in hours
		float elapsedTimeHour = elapsedTimeMillis/(60*60*1000F);
		// Get elapsed time in days
		float elapsedTimeDay = elapsedTimeMillis/(24*60*60*1000F);

	}
}