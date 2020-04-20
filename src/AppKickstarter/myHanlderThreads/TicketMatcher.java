//package AppKickstarter.myHanlderThreads;
//
//import AppKickstarter.AppKickstarter;
//import AppKickstarter.Msg.TicketRep;
//import AppKickstarter.Server.Ticket;
//import AppKickstarter.misc.AppThread;
//import AppKickstarter.misc.Msg;
//import AppKickstarter.timer.Timer;
//
//public class TicketMatcher extends AppThread {
//
//	
//	private final int sleepTime = 2000;
//	public TicketMatcher(String id, AppKickstarter appKickstarter) {
//		super(id, appKickstarter);
//	}
//
//	@Override
//	public void run() {
//		log.info(id + ": starting...");
//		Timer.setSimulationTimer(id, mbox, sleepTime, 9999);
//
//		for (boolean quit = false; !quit;) {
//			Msg msg = mbox.receive();
//			switch (msg.getType()) {
//			case TimesUp:
//				// MatchAllTicketQueue();
//				// Timer.setTimer(id, mbox, sleepTime);
//				if (Integer.valueOf(msg.getDetails().substring(2, 6)) == 9999) {
//					MatchAllTicketQueue();
//					Timer.setSimulationTimer(id, mbox, sleepTime, 9999);
//				} else {
//					log.info(id + ": TimesUP! from>" + msg.getSender() + "> " + msg.getDetails());
//					int ticketID = Integer.valueOf(msg.getDetails().substring(2, 6));
//					boolean TicketWaitingRemoved = removeFromWaitForAckTicketQueue(ticketID);
//					log.info(id + ": Tid=" + ticketID + " Removed> " + TicketWaitingRemoved);
//					// Waited too long for TicketAck... Remove Ticket from
//					// TicketHandler.WaitForAckTicketQueue
//					// UnHold Table
//					MatchAllTicketQueue();
//					TableHandler.UnHoldTable(ticketID);
//				}
//				break;
//
//			default:
//				log.severe(id + ": unknown message type!!");
//				break;
//			}
//		}
//		// declaring our departure
//		appKickstarter.unregThread(this);
//		log.info(id + ": terminating...");
//
//	}
//
//	
//	
//	
//	
//}
