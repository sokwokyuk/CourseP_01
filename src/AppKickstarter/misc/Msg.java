package AppKickstarter.misc;

import AppKickstarter.Msg.Command;

//======================================================================
// Msg
public class Msg {
	private String sender;
	private MBox senderMBox;
	private Type type;
	private String details;
	private Command command;

	// ------------------------------------------------------------
	// Msg
	public Msg(String sender, MBox senderMBox, Type type, String details) {
		this.sender = sender;
		this.senderMBox = senderMBox;
		this.type = type;
		this.details = details;
	} // Msg

	// Msg
	public Msg(String sender, MBox senderMBox, Type type, Command command) {
		this.sender = sender;
		this.senderMBox = senderMBox;
		this.type = type;
		this.command = command;
	} // Msg

	// ------------------------------------------------------------
	// getters
	public String getSender() {
		return sender;
	}

	public MBox getSenderMBox() {
		return senderMBox;
	}

	public Type getType() {
		return type;
	}

	public Command getCommand() {
		return command;
	}

	public String getDetails() {
		if (details == null)
			return command.toString();
		return details;
	}

	// ------------------------------------------------------------
	// toString
	public String toString() {
		if (details == null)
			return sender + " (" + type + ") -- " + command;

		return sender + " (" + type + ") -- " + details;
	} // toString

	// ------------------------------------------------------------
	// Msg Types
	public enum Type {
		Terminate, // Terminate the running thread
		SetTimer, // Set a timer
		CancelTimer, // Set a timer
		Tick, // Timer clock ticks
		TimesUp, // Time's up for the timer
		Hello, // Hello -- a testing msg type
		HiHi, // HiHi -- a testing msg type
		TicketReq, TicketRep, TicketCall, TicketAck, CheckOut, QueueTooLong, TableAssign

	} // Type
} // Msg
