/**
 * Class Withdrawer
 * Implements Withdrawer thread class.
 *
 * @author Malek Barhoush, mbarhoush@hotmail.com;
 * 
 *
 * $Revision: 1.0 $
 * $Last Revision Date: 2018/12/31
 */

public class Withdrawer_task3 extends Thread {
	private Account_task3 accountTask3;
	public Withdrawer_task3(Account_task3 accountTask3){
		this.accountTask3 = accountTask3;
	}
	
	public synchronized void run(){
	
		// Withdraw 10 CAD into instance variable account
		for (int i=0;i<10000000;i++)
		{
			accountTask3.withdraw(10);
			/*
			try {
				sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
		
	}

}
