package AppKickstarter.myHanlderThreads;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import AppKickstarter.AppKickstarter;
import AppKickstarter.Msg.TicketCall;
import AppKickstarter.Msg.TicketRep;
import AppKickstarter.Msg.TicketReq;
import AppKickstarter.Server.Client;
import AppKickstarter.Server.Observer;
import AppKickstarter.Server.Subject;
import AppKickstarter.Server.Table;
import AppKickstarter.Server.Ticket;
import AppKickstarter.Server.TicketQueue;
import AppKickstarter.misc.AppThread;
import AppKickstarter.misc.Msg;
import AppKickstarter.timer.Timer;

public class TicketHandler extends AppThread {
	public static List<TicketQueue> TqueueList = new ArrayList<TicketQueue>();
	private static int ServerForgetItQueueSz;
	private final int sleepTime = 10;
	private int TimerIDForMatchTicketQueue;
	private static Queue<Ticket> WaitForAckTicketQueue = new LinkedList<Ticket>();
	private int TicketAckWaitingTime;
	private int mode = appKickstarter.getMode();

	public TicketHandler(String id, AppKickstarter appKickstarter) {
		super(id, appKickstarter);
		createTicketQueue();

		this.TicketAckWaitingTime = Integer.valueOf(appKickstarter.getProperty("TicketAckWaitingTime"));
		this.TimerIDForMatchTicketQueue = appKickstarter.getTimerIDForMatchTicketQueue();
	}

	public void createTicketQueue() {
		String tName = "NTables_";
		this.ServerForgetItQueueSz = Integer.valueOf(appKickstarter.getProperty("ServerForgetItQueueSz"));
		TqueueList.add(new TicketQueue(2, ServerForgetItQueueSz));
		TqueueList.add(new TicketQueue(4, ServerForgetItQueueSz));
		TqueueList.add(new TicketQueue(6, ServerForgetItQueueSz));
		TqueueList.add(new TicketQueue(8, ServerForgetItQueueSz));
		TqueueList.add(new TicketQueue(10, ServerForgetItQueueSz));
		TqueueList.forEach(q -> {
			q.addObs(new TicketQueueObserver(q));
		});

		log.fine("Create TqueueList> ");
	}

	@Override
	public void run() {
		log.info(id + ": starting...");

		for (boolean quit = false; !quit;) {
			Msg msg = mbox.receive();

			// log.info(id + ": message received: [" + msg + "].");
			switch (msg.getType()) {
			// case TicketRep:
			// TicketRep ticketRep = (TicketRep) msg.getCommand();
			// Ticket ticketReped = ticketRep.getTicket();
			//
			// // MatchAllTicketQueue();
			//
			// break;
			case TimesUp:
				int timerID;
				try {
					timerID = Integer.parseInt(msg.getDetails().substring(1, 6));
					log.fine("NTimerID: " + timerID);
				} catch (NumberFormatException e) {
					timerID = Integer.valueOf(msg.getDetails().substring(1, 5));
				}

				if (timerID != TimerIDForMatchTicketQueue) {
					if (mode == 1) {
						// Waited too long for TicketAck... Remove Ticket from
						// TicketHandler.WaitForAckTicketQueue
						// UnHold Table
						log.fine(id + ": TimesUP! from>" + msg.getSender() + "> " + msg.getDetails());
						int ticketID = timerID;
						boolean TicketWaitingRemoved = removeFromWaitForAckTicketQueue(ticketID);
						log.fine(id + ": Tid=" + ticketID + " Removed> " + TicketWaitingRemoved);

						TableHandler.UnHoldTable(ticketID);
					}
				}
				MatchAllTicketQueue();
				log.fine(id + ": Set Next Timer.");
				Timer.setSimulationTimer(id, mbox, sleepTime, TimerIDForMatchTicketQueue);

				break;

			default:
				log.severe(id + ": unknown message type!!");
				break;
			}
		}
		// declaring our departure
		appKickstarter.unregThread(this);
		log.info(id + ": terminating...");

	}

	public static Ticket GetTicketAndAddToTicketQueueIfQueueNotFull(Client reqClient) throws InterruptedException {
		TicketQueue ticketQueue = TqueueList.get((reqClient.getnPerson() - 1) / 2);

		if (ticketQueue.getTicketQueue().size() < ServerForgetItQueueSz) {
			Ticket t = new Ticket(reqClient);
			ticketQueue.addTicketToQueue(t);
			return t;
		}

		return null;
	}

	public static Queue<Ticket> getWaitForAckTicketQueue() {
		return WaitForAckTicketQueue;
	}

	public boolean removeFromWaitForAckTicketQueue(int TicketID) {
		log.info("removeFromWaitForAckTicketQueue");
		WaitForAckTicketQueue.forEach(t -> log.info(TicketID + "< >tid=" + t.getTicketID()));
		boolean removed;
		try {
			removed = WaitForAckTicketQueue.removeIf(tc -> tc.getTicketID() == TicketID);
		} catch (Exception e) {
			removed = false;
		}
		return removed;
	}

	public static Ticket FindWaitingTicketAndPoll(int TicketId) {
		Ticket tc = WaitForAckTicketQueue.stream().filter(t -> Objects.equals(t.getTicketID(), TicketId)).findFirst()
				.orElse(null);
		if (tc != null)
			WaitForAckTicketQueue.remove(tc);
		return tc;
	}

	public void MatchAllTicketQueue() {
		log.info("Match All TicketQueue.");
		for (TicketQueue tq : TqueueList) {
			MatchTicketInQueueWithTable(tq);
		}
	}

	public void MatchTicketInQueueWithTable(TicketQueue ticketqueue) {
		// Find Table For Ticket
		Table avaTable = null;
		for (Ticket incomingTicket : ticketqueue.getTicketQueue()) {
			avaTable = TableHandler.MatchAvailableTable(incomingTicket);
			if (avaTable != null) {
				// If Found, Create TicketCall and Sent,
				// Poll Ticket From TicketQueue
				// Put Ticket in WaitForAckTicketQueue
				Ticket WaitForAckTicket = incomingTicket;
				ticketqueue.removeTicketFromQueue(incomingTicket);
				TicketCall tickCall = new TicketCall(WaitForAckTicket, avaTable);

				WaitForAckTicketQueue.add(tickCall.getTicket());

				log.fine("Found Table & Poll TicketQueue> Tid=" + WaitForAckTicket.getTicketID() + ", TableNo="
						+ avaTable.getTableNo());
				appKickstarter.getThread("SocketOutHandler").getMBox()
						.send(new Msg(id, mbox, Msg.Type.TicketCall, tickCall));
				TableHandler.HoldTable(WaitForAckTicket, avaTable);
				Timer.setSimulationTimer(id, mbox, TicketAckWaitingTime, WaitForAckTicket.getTicketID());
				log.fine(id + ": SetTimer>  TimerID=" + WaitForAckTicket.getTicketID());
				log.info(id + ": TicketCall Sent> Tid=" + WaitForAckTicket.getTicketID() + " Wait For TickerAck");
			} else {
				if (incomingTicket.getWaitedTooLong() && mode == 1) {

					ticketqueue.removeTicketFromQueue(incomingTicket);
					log.fine("Ticket Waited Too Long> " + incomingTicket.getTicketID() + " remove From Queue");
				} else {
					log.finer("No Table For> Tid=" + incomingTicket.getTicketID() + " Cid="
							+ incomingTicket.getClientWithTicket().getClientID());
				}
			}
		}
	}

	private class TicketQueueObserver extends Observer {
		private final String name = "TicketQueueObserver";

		// ------------------------------------------------------------
		// TicketQueueObserver
		public TicketQueueObserver(Subject subject) {
			super(subject);
		} // TicketQueueObserver

		// ------------------------------------------------------------
		// update
		public void update() {
			String status = subject.getStatus();
			// When Ticket Added To TicketQueue
			if (status.equals("Add")) {
				// MatchTicketQueue(((TicketQueue) subject));
				log.info(name + ": [" + status + "] Find Table for Ticket> ");
			}
		} // update

		// ------------------------------------------------------------
		// toString
		public String toString() {
			return name;
		} // toString
	}

}
