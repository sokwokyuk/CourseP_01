package AppKickstarter.Msg;

import AppKickstarter.Server.Client;

/**
 * This class implements QueueTooLong message
 * 
 * @author
 * @version 1.0
 * 
 */
public class QueueTooLong extends Command {
	private Client client;

	/**
	 * This constructs a message type ( named as QueueTooLong ) with client.
	 * 
	 * @param client : The client who receive the QueueTooLong message 
	 */
	public QueueTooLong(Client client) {
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
		return String.format("QueueTooLong: %s %s", client.getClientID(), client.getnPerson());
	}

}
