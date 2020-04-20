package AppKickstarter.Msg;

import AppKickstarter.Server.Client;
import AppKickstarter.Server.Ticket;

/**
 * This class implements TicketRep message
 * 
 * @author user
 *
 */
public class TicketRep extends Command {

	private Client client;
	private Ticket ticket;

	/**
	 * This constructs a message type ( named as QueueTooLong ) with client and
	 * ticket
	 * 
	 * @param client
	 *            : The client who receive the TicketRep message
	 * @param ticket
	 *            : The ticket information which received by the client
	 */
	public TicketRep(Client client, Ticket ticket) {
		this.client = client;
		this.ticket = ticket;
	}

	/**
	 * This returns the information of current client.
	 * 
	 * @return This client's information
	 */
	public Client getClient() {
		return this.client;
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
	 * This returns the format of TicketRep message.
	 */
	@Override
	public String toString() {
		return String.format("TicketRep: %s %s %s", this.client.getClientID(), this.client.getnPerson(),
				this.ticket.getTicketID());
	}

}
