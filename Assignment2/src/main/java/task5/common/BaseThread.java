package common;

/**
 * Class BaseThread
 * Simply one customized base class for many of our own threads.
 *
 * An attempt to maintain an automatic unique TID (thread ID)
 * among all the derivatives and allow setting your own if needed.
 * Plus some methods for the sync exercises.
 *
 * $Revision: 1.4 $
 * $Last Revision Date: 2019/02/02 $
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class BaseThread extends Thread
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */

	/**
	 * Preserves value across all instances.
	 */
	public static int siNextTID = 1;

	/**
	 * Our Thread ID.
	 */
	protected int iTID;

	/**
	 * TID of a thread to proceed to the phase II.
	 */
	public static int siTurn = 1;

	/*
	 * ------------
	 * Constructors
	 * ------------
	 */

	public static void incrementTurn() {
		siTurn++;
	}

	/**
	 * Default
	 */
	public BaseThread()
	{
		setTID();
	}

	/**
	 * Assigns name to the thread and places it to the specified group.
	 *
	 * @param poGroup ThreadGroup to add this thread to
	 * @param pstrName A string indicating human-readable thread's name
	 */
	public BaseThread(ThreadGroup poGroup, String pstrName)
	{
		super(poGroup, pstrName);
		setTID();
	}

	/**
	 * Sets user-specified TID.
	 */
	public BaseThread(final int piTID)
	{
		this.iTID = piTID;
	}

	/**
	 * Retrieves our TID.
	 * @return TID, integer
	 */
	public final int getTID()
	{
		return this.iTID;
	}

	/**
	 * Sets internal TID and updates next TID on contruction time, so it's private.
	 */
	private final void setTID()
	{
		this.iTID = siNextTID++;
	}

	/**
	 * Allows setting initial turn value to something else
	 * other than the default "1" (one).
	 * @param piInitTurn new initial value of the turn.
	 */
	public static synchronized final void setInitialTurn(int piInitTurn)
	{
		siTurn = piInitTurn;
	}

	/**
	 * Just a make up for the PHASE I to make it somewhat tangeable.
	 * Must be atomic as it touches siTurn and siNextTID.
	 */
	protected synchronized void phase1()
	{
		System.out.println(this.getClass().getName() + " thread [TID=" + this.iTID + "] starts PHASE I.");

		System.out.println
		(
			"Some stats info in the PHASE I:\n" +
			"    iTID = " + this.iTID +
			", siNextTID = " + siNextTID +
			", siTurn = " + siTurn +
			".\n    Their \"checksum\": " + (siNextTID * 100 + this.iTID * 10 + siTurn)
		);

		System.out.println(this.getClass().getName() + " thread [TID=" + this.iTID + "] finishes PHASE I.");
	}

	/**
	 * Just a make up for the PHASE II to make it somewhat tangeable.
         * Must be atomic as it touches siTurn and siNextTID.
	 */

	protected synchronized void phase2()
	{
		System.out.println(this.getClass().getName() + " thread [TID=" + this.iTID + "] starts PHASE II.");

		System.out.println
				(
						"Some stats info in the PHASE II:\n" +
								"    iTID = " + this.iTID +
								", siNextTID = " + siNextTID +
								", siTurn = " + siTurn +
								".\n    Their \"checksum\": " + (siNextTID * 100 + this.iTID * 10 + siTurn)
				);

		System.out.println(this.getClass().getName() + " thread [TID=" + this.iTID + "] finishes PHASE II.");
	}

	/**
	 * Test-and-Set for the iTurn variable.
	 *
	 * Use to proceed to the phase II in the correct order.
	 * Must be atomic.
	 *
	 * @param pcIncreasingOrder true if TIDs are in increasing order; false otherwise
	 *
	 * @return Returns true if if the TID of currently running thread  matches the turn, 'false' otherwise
	 */
	public synchronized boolean turnTestAndSet(boolean pcIncreasingOrder)
	{
		// test
		if(siTurn == this.iTID)
		{
			// set siTurn = siTurn +/- 1;
			if(pcIncreasingOrder == true)
				siTurn++;
			else
				siTurn--;

			return true;
		}

		return false;
	}

	/**
	 * Always assumes the increasing order.
	 */
	public synchronized boolean turnTestAndSet()
	{
		return turnTestAndSet(true);
	}
}


// EOF
