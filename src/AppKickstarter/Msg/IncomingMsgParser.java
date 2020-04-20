package AppKickstarter.Msg;

import AppKickstarter.Server.Client;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;

/**
 * This class parsers message from the client stream.
 * 
 * @author user
 * @version 1.0
 */
public class IncomingMsgParser {

	/**
	 * This returns message with sender, mbox, message type and create an instance
	 * of this message type.
	 * 
	 * @param sender
	 *            : The thread which used for the message box
	 * @param mbox
	 *            : The message box for sending and receiving message
	 * @param incomingMsg
	 *            : The message received from the client stream
	 * @return
	 */
	public static Msg IncomingMsgParser(String sender, MBox mbox, String incomingMsg) {
		String[] SplitedMsg = incomingMsg.split(":");
		String Type = SplitedMsg[0];
		String MsgDetail = SplitedMsg[1];
		String[] DetailParts = MsgDetail.trim().split("\\s+");
		switch (Type) {
		case "TicketReq":
			return new Msg(sender, mbox, Msg.Type.TicketReq,
					new TicketReq(new Client(DetailParts[0], Integer.valueOf(DetailParts[1]))));
		case "TicketAck":
			return new Msg(sender, mbox, Msg.Type.TicketAck, new TicketAck(Integer.valueOf(DetailParts[0]),
					Integer.valueOf(DetailParts[1]), Integer.valueOf(DetailParts[2])));
		case "CheckOut":
			return new Msg(sender, mbox, Msg.Type.CheckOut,
					new CheckOut(Integer.valueOf(DetailParts[0]), Integer.valueOf(DetailParts[1])));
		default:
			break;
		}
		return null;
	}

}
