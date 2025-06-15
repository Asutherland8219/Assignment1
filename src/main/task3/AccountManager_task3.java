/**
 * Class AccountManager
 * Implements account manager that twists depositor and withdrawal threads.
 *
 * @author Malek Barhoush, mbarhoush@hotmail.com;
 * 
 *
 * $Revision: 1.0 $
 * $Last Revision Date: 2018/12/31
 */

public class AccountManager_task3 {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Task 3: Method Level Synchronization");
		Account_task3[] accountTask3 = new Account_task3[10];
		Depositor_task3[] deposit3 = new Depositor_task3[10];
		Withdrawer_task3[] withdraw3= new Withdrawer_task3[10];
		
		// The birth of  10 accounts
		accountTask3[0] = new Account_task3(1234,"Mike",1000);
		accountTask3[1] = new Account_task3(2345,"Adam",2000);
		accountTask3[2] = new Account_task3(3456,"Linda",3000);
		accountTask3[3] = new Account_task3(4567,"John",4000);
		accountTask3[4] = new Account_task3(5678,"Rami",5000);
		accountTask3[5] = new Account_task3(6789,"Lee",6000);
		accountTask3[6] = new Account_task3(7890,"Tom",7000);
		accountTask3[7] = new Account_task3(8901,"Lisa",8000);
		accountTask3[8] = new Account_task3(9012,"Sam",9000);
		accountTask3[9] = new Account_task3(4321,"Ted",10000);
		
		// The birth of 10 depositors 
		deposit3[0] = new Depositor_task3(accountTask3[0]);
		deposit3[1] = new Depositor_task3(accountTask3[1]);
		deposit3[2] = new Depositor_task3(accountTask3[2]);
		deposit3[3] = new Depositor_task3(accountTask3[3]);
		deposit3[4] = new Depositor_task3(accountTask3[4]);
		deposit3[5] = new Depositor_task3(accountTask3[5]);
		deposit3[6] = new Depositor_task3(accountTask3[6]);
		deposit3[7] = new Depositor_task3(accountTask3[7]);
		deposit3[8] = new Depositor_task3(accountTask3[8]);
		deposit3[9] = new Depositor_task3(accountTask3[9]);

		// The birth of  10 withdraws 
		withdraw3[0] = new Withdrawer_task3(accountTask3[0]);
		withdraw3[1] = new Withdrawer_task3(accountTask3[1]);
		withdraw3[2] = new Withdrawer_task3(accountTask3[2]);
		withdraw3[3] = new Withdrawer_task3(accountTask3[3]);
		withdraw3[4] = new Withdrawer_task3(accountTask3[4]);
		withdraw3[5] = new Withdrawer_task3(accountTask3[5]);
		withdraw3[6] = new Withdrawer_task3(accountTask3[6]);
		withdraw3[7] = new Withdrawer_task3(accountTask3[7]);
		withdraw3[8] = new Withdrawer_task3(accountTask3[8]);
		withdraw3[9] = new Withdrawer_task3(accountTask3[9]);

		System.out.println("Print initial account balances");
		// Print initial account balances
		for(int i=0;i<10;i++)
			System.out.println(accountTask3[i]);

		// Get start time in milliseconds 
		long start = System.currentTimeMillis(); 

		System.out.println("Depositor and Withdrawal threads have been created");
		/*
		 * Interleave all threads
		 */
		for(int i=0; i<10; i++){
			deposit3[i].start();
			withdraw3[i].start();
		}

		
		for(int i=0; i<10; i++){
			try {
				deposit3[i].join();
				withdraw3[i].join();
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
			System.out.println(accountTask3[i]);
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
