package AppKickstarter.Msg;

public abstract class Command {

	/**
	 * {@inheritDoc} This implementation also does in different message type such as
	 * TicketReq, TicketRep, TicketCall, TicketAck, etc.
	 * 
	 */
	public abstract String toString();

}
