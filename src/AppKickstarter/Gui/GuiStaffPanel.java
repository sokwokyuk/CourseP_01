package AppKickstarter.Gui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import AppKickstarter.AppKickstarter;
import AppKickstarter.Msg.*;
import AppKickstarter.Server.Table;
import AppKickstarter.Server.Ticket;
import AppKickstarter.misc.AppThread;
import AppKickstarter.misc.Msg;
import AppKickstarter.myHanlderThreads.TableHandler;
import AppKickstarter.timer.Timer;

public class GuiStaffPanel extends AppThread {
	private final int sleepTime = 2000;

	public GuiStaffPanel(String id, AppKickstarter appKickstarter) {
		super(id, appKickstarter);
	}

	private ArrayList<Table> TableList;
	private Queue<TicketCall> Tcqueue = new LinkedList<TicketCall>();

	@Override
	public void run() {
		log.info(id + ": starting...");
		Timer.setSimulationTimer(id, mbox, sleepTime);

		for (boolean quit = false; !quit;) {
			Msg msg = mbox.receive();
			switch (msg.getType()) {
			case TicketCall:
				TicketCall tc = (TicketCall) msg.getCommand();
				Tcqueue.add(tc);
				break;
			case TimesUp:
				Timer.setSimulationTimer(id, mbox, sleepTime);
				this.TableList = TableHandler.getTableList();
				// gui...display TableList
				DisplayTableList();
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

	public void DisplayTableList() {

	}

	// 1.display whole tcqueue by jlist
	// Select tc in the list then click ack button to send ack for selected tc
	// OR
	// 2.display only nexttc Click ack button to send ack for nexttc
	// if ack button clicked
	public void Sendack() {
		TicketCall nextTicketCall = Tcqueue.poll();
		Ticket nextTicket = nextTicketCall.getTicket();
		TicketAck ticketAck = new TicketAck(nextTicket.getTicketID(), nextTicketCall.getTable().getTableNo(),
				nextTicket.getClientWithTicket().getnPerson());
		appKickstarter.getThread("MsgHandler").getMBox().send(new Msg(id, mbox, Msg.Type.TicketAck, ticketAck));
	}

	// Selected table and checkout button clicked, send Checkout To server
	public void SendCheckout(int tableno) {
		int Spending = 1234;// Random
		appKickstarter.getThread("MsgHandler").getMBox()
				.send(new Msg(id, mbox, Msg.Type.CheckOut, new CheckOut(tableno, Spending)));
	}
}
