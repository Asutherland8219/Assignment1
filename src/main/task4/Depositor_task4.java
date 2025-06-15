
/**
 * Class Depositor
 * Implements Depositor thread class.
 *
 * @author Malek Barhoush, mbarhoush@hotmail.com;
 * 
 *
 * $Revision: 1.0 $
 * $Last Revision Date: 2018/12/31
 */

public class Depositor_task4 extends Thread {
	private Account_task4 accountTask4;
	public Depositor_task4(Account_task4 accountTask4){
		this.accountTask4 = accountTask4;
	}
	
	public void run(){
		for (int i=0;i<10000000;i++)
		{
			synchronized (accountTask4) {
				accountTask4.deposit4(10);
			}
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
