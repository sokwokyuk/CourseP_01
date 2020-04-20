package AppKickstarter.Server;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class implements QueueTooLong message
 * 
 * @author user
 * @version 1.0
 */
public class TicketQueue extends Subject {
	private String status = "";
	private BlockingQueue<Ticket> ticketQueue;
	private int ForTableSize;
	private int ServerForgetItQueueSz;
	private Ticket LastRemovedTicket;

	/**
	 * This constructs a queue ( named as ticketQueue ) which store the ticket with
	 * ForTableSize and ServerForgetItQueueSz.
	 * 
	 * @param ForTableSize
	 *            : The capacity of the table
	 * @param ServerForgetItQueueSz
	 *            : The preassigned number of the table
	 */
	public TicketQueue(int ForTableSize, int ServerForgetItQueueSz) {
		this.ticketQueue = new LinkedBlockingQueue<Ticket>();
		this.ForTableSize = ForTableSize;
		this.ServerForgetItQueueSz = ServerForgetItQueueSz;
	}

	/**
	 * This adds ticket to the queue.
	 * 
	 * @param t
	 *            : The ticket which will add to queue ( named as ticketQueue )
	 * @return The current queue ( named as ticketQueue )
	 * @throws InterruptedException
	 */
	public Queue<Ticket> addTicketToQueue(Ticket t) throws InterruptedException {
		this.setStatus("Add");
		t.setInQueueTime();
		this.ticketQueue.put(t);
		notifyObs();
		return this.ticketQueue;
	}

	/**
	 * This remove ticket from the queue.
	 * 
	 * @param t
	 *            : The ticket which will remove from the queue ( named as
	 *            ticketQueue )
	 * @return The boolean determines whether this function runs or not
	 */
	public boolean removeTicketFromQueue(Ticket t) {
		if (this.ticketQueue.size() > 0) {
			this.ticketQueue.remove(t);
			return true;
		}
		return false;
	}

	/**
	 * This returns the information of last removed ticket.
	 * 
	 * @return Last removed ticket's information
	 */
	public Ticket getLastRemovedTicket() {
		return this.LastRemovedTicket;
	}

	/**
	 * This returns the ticket's information of the head of queue and remove from
	 * the queue.
	 * 
	 * @return Ticket's information on the head of this queue ( named as ticketQueue
	 *         )
	 */
	public Ticket pollTicketQueue() {
		return this.ticketQueue.poll();
	}

	/**
	 * This returns the ticket's information of the head of queue
	 * 
	 * @return Ticket's information on the head of this queue ( named as ticketQueue
	 *         )
	 */
	public Ticket peekTicketQueue() {
		return this.ticketQueue.peek();
	}

	/**
	 * This returns the entire queue of the ticket.
	 * 
	 * @return Whole queue ( named as ticketQueue )
	 */
	public Queue<Ticket> getTicketQueue() {
		return this.ticketQueue;
	}

	/**
	 * This returns the capacity of table
	 * 
	 * @return Capacity of the table
	 */
	public int getForTableSize() {
		return this.ForTableSize;
	}

	/**
	 * This sets the status of current ticket
	 */
	@Override
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * This returns the status of current ticket
	 */
	@Override
	public String getStatus() {
		return this.status;
	}

}
