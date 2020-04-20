package AppKickstarter.Msg;

import AppKickstarter.Server.Client;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;

/**
 * This class implements TicketReq message
 * 
 * @author user
 *
 */
public class TicketReq extends Command {
	private Client client;

	/**
	 * This constructs a message type ( named as TicketReq ) with client.
	 * 
	 * @param client
	 *            : The client who receive the TicketReq message
	 */
	public TicketReq(Client client) {
		this.client = client;
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
	 * This returns the format of QueueTooLong message.
	 */
	@Override
	public String toString() {
		return String.format("TicketReq: %s %s", client.getClientID(), client.getnPerson());
	}

}
