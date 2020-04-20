package AppKickstarter.Msg;

import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;

/**
 * This class implements QueueTooLong message
 * 
 * @author user
 * @version 1.0
 */
public class TicketAck extends Command {
	private int TicketID;
	private int TableNo;
	private int nPerson;

	/**
	 * This constructs a message type ( named as TicketAck ) with TicketID,TableNo
	 * and client.
	 * 
	 * @param TicketID
	 *            : The unique id of the ticket
	 * @param TableNo
	 *            : The unique number of the table
	 * @param nPerson
	 *            : Number of person included in this TicketID
	 */
	public TicketAck(int TicketID, int TableNo, int nPerson) {
		this.TicketID = TicketID;
		this.TableNo = TableNo;
		this.nPerson = nPerson;
	}

	/**
	 * This returns the id of current ticket.
	 * 
	 * @return This ticket's id
	 */
	public int getTicketID() {
		return this.TicketID;
	}

	/**
	 * This returns the id of current table.
	 * 
	 * @return This table's id
	 */
	public int getTableNo() {
		return this.TableNo;
	}

	/**
	 * This returns the number of person.
	 * 
	 * @return The number of person in this ticket
	 */
	public int getNPerson() {
		return this.nPerson;
	}

	/**
	 * This returns the format of TicketAck message.
	 */
	@Override
	public String toString() {
		return String.format("TicketAck: %s %s %s", this.TicketID, this.TableNo, this.nPerson);
	}
}
