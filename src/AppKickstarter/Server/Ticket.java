package AppKickstarter.Server;

import java.time.LocalDateTime;
/**
 * This class implements a ticket
 * @author 
 * @version 1.0
 */

public class Ticket {
	private static int AccTicketID = 0;
	private int TicketID;
	private long InQueueTime;
	private LocalDateTime checkIn, checkOut;
	private Client ClientWithTicket;

	/**
	 * This constructs a ticket with TicketID and ClientWithTicket.
	 * @param ClientWithTicket
	 */

	public Ticket(Client ClientWithTicket) {
		if (AccTicketID >= 9999)
			AccTicketID = 0;
		this.TicketID = AccTicketID++;
		this.ClientWithTicket = ClientWithTicket;
	}
	
	/**
	 * This set the check in time of the ticket.
	 * @param localDateTime
	 */
	public Ticket(int TicketID, Client ClientWithTicket) {
		this.TicketID = TicketID;
		this.ClientWithTicket = ClientWithTicket;
	}

	public void setInQueueTime() {
		this.InQueueTime = System.currentTimeMillis();
	}

	public boolean getWaitedTooLong() {
		return System.currentTimeMillis() - this.InQueueTime > 1000 ? true : false;
	}

	public void setCheckIn(LocalDateTime localDateTime) {
		this.checkIn = localDateTime;
	}

	/**
	 * 
	 * @param checkOut
	 */
	public void setCheckOut(LocalDateTime checkOut) {
		this.checkOut = checkOut;
	}

	/**
	 * 
	 * @return
	 */
	public LocalDateTime getCheckIn() {
		return this.checkIn;
	}
	
	/**
	 * 
	 * @return
	 */
	public LocalDateTime getcheckOut() {
		return this.checkOut;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getTicketID() {
		return this.TicketID;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getTotalSpending() {
		return checkOut.compareTo(checkIn);
	}
	
	/**
	 * 
	 * @return
	 */
	public Client getClientWithTicket() {
		return this.ClientWithTicket;

	}

}
