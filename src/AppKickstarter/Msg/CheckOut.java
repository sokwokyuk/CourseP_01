package AppKickstarter.Msg;

/**
 * This class implements CheckOut message
 * 
 * @author user
 * @version 1.0
 */
public class CheckOut extends Command {

	private int TableNo;
	private int TotalSpending;

	/**
	 * This constructs a message type ( named as CheckOut ) with TableNo and
	 * TotalSpending.
	 * 
	 * @param TableNo
	 *            : The unique number of table is going to CheckOut.
	 * @param TotalSpending
	 *            : The total spending of the checked out table.
	 */
	public CheckOut(int TableNo, int TotalSpending) {
		this.TableNo = TableNo;
		this.TotalSpending = TotalSpending;
	}

	/**
	 * This returns the current table number.
	 * 
	 * @return This table's number.
	 */
	public int getTableNo() {
		return this.TableNo;
	}

	/**
	 * This returns the total spending of current table.
	 * 
	 * @return This table's total spending
	 */
	public int getTotalSpending() {
		return this.TotalSpending;
	}

	/**
	 * This returns the format of CheckOut message.
	 */
	@Override
	public String toString() {
		return String.format("CheckOut: %s %s", this.TableNo, this.TotalSpending);
	}

}
