package AppKickstarter.myHanlderThreads;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import AppKickstarter.AppKickstarter;
import AppKickstarter.Server.Table;
import AppKickstarter.Server.Ticket;
import AppKickstarter.misc.AppThread;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import AppKickstarter.timer.Timer;

public class TableHandler extends AppThread {

	private static ArrayList<Table> TableList;
	private static int TotalSpending;
	private int mode = appKickstarter.getMode();
	private static int KickOutTime = 3600;
	private static int PrintTime = 3600;
	private static int PrintTimerID = 20000;

	public TableHandler(String id, AppKickstarter appKickstarter) {
		super(id, appKickstarter);
		createTable();
	}
	

	@Override
	public void run() {
		log.info(id + ": starting...");

		for (boolean quit = false; !quit;) {
			Msg msg = mbox.receive();
			switch (msg.getType()) {
			case TimesUp:

				int timerID;
				try {
					timerID = Integer.parseInt(msg.getDetails().substring(1, 6));
					log.fine("NTimerID: " + timerID);
				} catch (NumberFormatException e) {
					timerID = Integer.parseInt(msg.getDetails().substring(1, 5));
				}
				//TimesUP For PrintALLTable
				if (timerID == PrintTimerID) {
					PrintAllTable();
					Timer.setSimulationTimer(id, mbox, PrintTime, PrintTimerID);
				} else {
					int TableNo = timerID;
					if (mode == 1) {
						CheckOutTable(TableNo, 0);
						log.fine(id + ": CLient in Table> " + TableNo + " ate too Long, Kick out.");
					}
				}

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

	public void createTable() {
		String tName = "NTables_";
		TableList = new ArrayList<Table>();

		for (int i = 1; i <= 5; i++) {
			int AmountOfTable = Integer.valueOf(appKickstarter.getProperty(tName + i));
			for (int a = 0; a < AmountOfTable; a++) {
				int tNo = ((i - 1) * 100) + a;
				int tSize = i * 2;
				TableList.add(new Table(tNo, tSize));
				log.fine("Create Table> no:" + String.format("%05d", tNo) + " with Size:" + tSize);
			}
		}

	}

	public static Table MatchAvailableTable(Ticket ticket) {
		Table avaTable = TableList.stream()
				.filter(table -> table.getState().equals("Available")
						&& (table.getTableSize() >= ticket.getClientWithTicket().getnPerson()
								&& table.getTableSize() <= ticket.getClientWithTicket().getnPerson() + 3))
				.findFirst().orElse(null);

		if (avaTable != null) {
			return avaTable;
		}
		
		return null;
	}

	static int ts;
	static String logstring;

	public static void PrintAllTable() {
		logstring = "";
		for (ts = 1; ts <= 5; ts++) {
			logstring += ("\nTables[" + (ts - 1) + "]:\t");
			TableList.stream().filter(t -> t.getTableSize() == (ts * 2)).forEach(t -> {
				if (t.getTicketAtTable().size() > 0) {
					String tno = String.format("%05d", t.getTableNo());
					if (t.getTicketAtTable().get(0) != null)
						logstring += ("[" + tno + ", " + t.getTicketAtTable().get(0).getClientWithTicket().getClientID()
								+ "]\t");
				} else {
					logstring += ("[.................]\t");

				}
			});
		}
		log.info(logstring + "\n");
		PrintTicketQueue();
		log.info("--------------------------------------------------------------");
	}

	static String logstring2;

	public static void PrintTicketQueue() {
		logstring2 = "\n";
		TicketHandler.TqueueList.forEach(q -> {
			logstring2 += ("TicketQueue[" + q.getForTableSize()) + "] (" + q.getTicketQueue().size() + "): ";
			q.getTicketQueue().forEach(t -> {
				logstring2 += (">" + String.format("%05d", t.getTicketID())) + " ";
			});
			logstring2 += "\n";
		});
		log.info(logstring2);
	}

	public   LocalDateTime CheckInWaitingTicketToTable(Ticket TicketWaiting, int TableNo) {
		return CheckInTable(TicketWaiting, getTableByTableNo(TableNo));
	}

	public   LocalDateTime CheckInTable(Ticket ticket, Table table) {
		// if (table.getAvailable()) {
		ticket.setCheckIn(LocalDateTime.now());
		// table.setAvailable(false);
		// table.addTicketToTable(ticket);
		table.setCheckedInState();
		TableList.set(FindTableIndex(table), table);
		PrintAllTable();
		Timer.setSimulationTimer(id, mbox, KickOutTime, table.getTableNo());
		return LocalDateTime.now();
		// }
		// return null;
	}

	public static void HoldTable(Ticket ticket, Table table) {
		log.fine("Hold> Tid=" + ticket.getTicketID() + " TableNo=" + table.getTableNo());
		if (table.getState().equals("Available")) {
			table.setAvailable(false);
			table.addTicketToTable(ticket);
			table.setHoldState();
			TableList.set(FindTableIndex(table), table);
		}
	}

	public static void UnHoldTable(int ticketID) {
		log.fine("Unhold> Tid=" + ticketID);
		Table tableHeldByTicket = TableList.stream()
				.filter(t -> t.getTicketAtTable().size() > 0 && t.getTicketAtTable().get(0).getTicketID() == ticketID)
				.findFirst().orElse(null);
		if (tableHeldByTicket != null && tableHeldByTicket.getState().equals("Hold")) {
			tableHeldByTicket.setAvailable(true);
			tableHeldByTicket.setAvailableState();
			tableHeldByTicket.removeTicketToTable(ticketID);
			TableList.set(FindTableIndex(tableHeldByTicket), tableHeldByTicket);
		}
	}

	public LocalDateTime CheckOutTable(int TableNo, int totalSpending) {
		TotalSpending += totalSpending;
		Table table = getTableByTableNo(TableNo);
		Ticket ticketAtTable = table.getTicketAtTable().size() > 0 ? table.getTicketAtTable().get(0) : null;
		if (ticketAtTable != null) {
			ticketAtTable.setCheckOut(LocalDateTime.now());
			table.setAvailable(true);
			table.setAvailableState();
			table.removeTicketToTable(ticketAtTable);
			table.clearTable();
		}
		TableList.set(FindTableIndex(table), table);
		log.info("Checked Out Table> " + TableNo + " TotalSpending: $" + TotalSpending);
		Timer.cancelTimer(logstring, mbox, TableNo);
		// TicketHandler.MatchTicketForSize(table.getTableSize());
		return LocalDateTime.now();
	}

	public static Table getTableByTableNo(int TableNo) {
		return TableList.stream().filter(t -> Objects.equals(t.getTableNo(), TableNo)).findFirst().get();
	}

	private static int FindTableIndex(Table table) {
		for (int i = 0; i < TableList.size(); i++) {
			if (table.getTableNo() == TableList.get(i).getTableNo()) {
				return i;
			}
		}
		return -1;
	}

	public static ArrayList<Table> getTableList() {
		return TableList;
	}

}
