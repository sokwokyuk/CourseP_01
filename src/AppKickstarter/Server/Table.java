package AppKickstarter.Server;

import java.util.ArrayList;

/**
 * This class implements a Table
 * 
 * @author user
 *
 */
public class Table {

	private int TableNo;
	private int TableSize;
	private boolean Available;
	private int AvailableSize;
	private ArrayList<Ticket> TicketAtTable;
	private String state;

	/**
	 * This constructs a table with TableNo and TableSize.
	 * 
	 * @param TableNo
	 *            : The unique number of the table
	 * @param TableSize
	 *            : The capacity of the table
	 */
	public Table(int TableNo, int TableSize) {
		this.TableNo = TableNo;
		this.TableSize = TableSize;
		this.AvailableSize = TableSize;
		this.Available = true;
		this.state="Available";
		this.TicketAtTable = new ArrayList<Ticket>();
	}

	public String getState() {return this.state;}
	public void setHoldState() {this.state="Hold";}
	public void setCheckedInState() {this.state="Eating";}
	public void setAvailableState() {this.state="Available";}
	
	public int getTableNo() {
		return this.TableNo;
	}

	/**
	 * This returns the capacity of current table
	 * 
	 * @return This table's capacity
	 */
	public int getTableSize() {
		return this.TableSize;
	}

	/**
	 * This sets the current table's availability
	 * 
	 * @param Available
	 *            : This table's availability
	 */
	public void setAvailable(boolean Available) {
		this.Available = Available;
	}

	/**
	 * This returns the availability of current table
	 * 
	 * @return This current table's availability
	 */
	public boolean getAvailable() {
		return this.Available;
	}

	/**
	 * This returns the number of availability according to the table size
	 * 
	 * @return This table's size of availability
	 */
	public int getAvailableSize() {
		return this.AvailableSize;
	}

	/**
	 * This returns the number of availability according to the table size
	 * 
	 * @param t
	 *            : The current ticket which will add to the ArrayList ( named as
	 *            TicketAtTable ).
	 * 
	 */
	public void addTicketToTable(Ticket t) {
		this.Available = false;
		AvailableSize -= t.getClientWithTicket().getnPerson();
		this.TicketAtTable.add(t);
	}

	/**
	 * This clears all the tickets in the ArrayList ( named as TicketAtTable ).
	 */
	public void clearTable() {
		this.TicketAtTable.clear();
	}

	public void removeTicketToTable(Ticket t) {
		this.Available = true;
		AvailableSize += t.getClientWithTicket().getnPerson();
		this.TicketAtTable.remove(t);
	}

	/**
	 * This returns the whole ArrayList of the ticket
	 * 
	 * @return The whole ArrayList ( named as TicketAtTable ).
	 */
	public ArrayList<Ticket> getTicketAtTable() {
		return this.TicketAtTable;
	}

	/**
	 * This remove the ticket in the ArrayList ( named as TicketAtTable ) according
	 * to the ticket's id.
	 * 
	 * @param ticketID : The unique id of the ticket
	 */
	public void removeTicketToTable(int ticketID) {
		TicketAtTable.removeIf(t -> t.getTicketID() == ticketID);
	}

}
