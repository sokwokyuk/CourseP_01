package AppKickstarter.Msg;

import AppKickstarter.Server.Table;
import AppKickstarter.Server.Ticket;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;

/**
 * This class implements TableAssign message.
 * 
 * @author user
 * @version 1.0
 */
public class TableAssign extends Command {

	private Ticket ticket;
	private Table table;
	private int TableNo;

	/**
	 * This constructs a message type ( named as TableAssign ) with ticket and
	 * table.
	 * 
	 * @param ticket
	 *            : The ticket who receive the TableAssign message
	 * @param table
	 *            : The table which will assign to current ticket
	 */
	public TableAssign(Ticket ticket, Table table) {
		this.ticket = ticket;
		this.table = table;
	}

	/**
	 * This constructs a message type ( named as TableAssign ) with calledTicket and
	 * tableNo.
	 * 
	 * @param calledTicket
	 *            : The ticket who received the TicketCall message
	 * @param tableNo
	 *            : The unique number of the table
	 */
	public TableAssign(Ticket calledTicket, int tableNo) {
		this.ticket = calledTicket;
		this.TableNo = tableNo;
	}

	/**
	 * This returns the information of current ticket.
	 * 
	 * @return This ticket's information
	 */
	public Ticket getTicket() {
		return this.ticket;
	}

	/**
	 * This returns the information of current table.
	 * 
	 * @return This table's information
	 */
	public Table getTable() {
		return this.table;
	}

	/**
	 * This returns the number of current table.
	 * 
	 * @return The number of this table
	 */
	public int getTableNo() {
		return this.TableNo;
	}

	/**
	 * This returns the format of TableAssign message.
	 */
	@Override
	public String toString() {
		return String.format("TableAssign: %s %s", ticket.getTicketID(), TableNo);
	}

}
