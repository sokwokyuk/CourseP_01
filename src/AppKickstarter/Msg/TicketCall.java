package AppKickstarter.Msg;

import AppKickstarter.Server.Table;
import AppKickstarter.Server.Ticket;

/**
 * This class implements TicketCall message
 * 
 * @author user
 * @version 1.0
 *
 */
public class TicketCall extends Command {
	private Ticket ticket;
	private Table table;

	/**
	 * This constructs a message type ( named as TicketCall ) with ticket and table.
	 * 
	 * @param ticket
	 *            : The ticket which will receive the TicketCall message.
	 * @param table
	 *            : The table which will hold for the ticket.
	 */
	public TicketCall(Ticket ticket, Table table) {
		this.ticket = ticket;
		this.table = table;
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
	 * This returns the format of TicketCall message.
	 */
	@Override
	public String toString() {
		return String.format("TicketCall: %s %s", ticket.getTicketID(), table.getTableNo());
	}
}
