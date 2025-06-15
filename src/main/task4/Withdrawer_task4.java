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

public class Withdrawer_task4 extends Thread {
	private Account_task4 accountTask4;
	public Withdrawer_task4(Account_task4 accountTask4){
		this.accountTask4 = accountTask4;
	}
	
	public void run(){
	
		// Withdraw 10 CAD into instance variable account
		for (int i=0;i<10000000;i++)
		{
			accountTask4.withdraw4(10);
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
