package AppKickstarter.Gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import AppKickstarter.AppKickstarter;
import AppKickstarter.Msg.*;
import AppKickstarter.Server.Client;
import AppKickstarter.Server.Ticket;
import AppKickstarter.misc.AppThread;

public class GuiTicketPanel {

	private final String ServerIP = "127.0.0.1";
	private final int ServerPort = 54321;
	private Socket socket;

	private PrintWriter out;
	private DataInputStream in;
	ArrayList<TicketRep> TicketRepList = new ArrayList<TicketRep>();

	public void run() throws IOException {
		this.socket = new ServerSocket(ServerPort).accept();
		in = new DataInputStream(socket.getInputStream());
		out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
	}

	static int clientID = 0;

	public void SendTicketReq(int nPerson) {
		String ClientID = "Client-" + clientID++;
		TicketReq ticketReq = new TicketReq(new Client(ClientID, nPerson));
		out.write(ticketReq.toString());
	}


	class SocketInHandler extends AppThread {
		private DataInputStream in;

		public SocketInHandler(String id, AppKickstarter appKickstarter) {
			super(id, appKickstarter);
		} // ServerThread

		public void run() {
			log.info(id + ": starting...");
			try {
				while (true) {
					byte[] buffer = new byte[1024];
					in.read(buffer);
					String IncomingMsg = new String(buffer);
					log.info(id + ": IncomingMsg> " + IncomingMsg);
					String[] SplitedMsg = IncomingMsg.split(":");
					String Type = SplitedMsg[0];
					String MsgDetail = SplitedMsg[1];
					String[] DetailParts = MsgDetail.trim().split("\\s+");

					String ClientID = DetailParts[0];
					int nPerson = Integer.valueOf(DetailParts[1]);
					int TicketID = Integer.valueOf(DetailParts[2]);
					Client client = new Client(ClientID, nPerson);
					Ticket ticket = new Ticket(TicketID, client);
					TicketRepList.add(new TicketRep(client, ticket));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} // run
	}

}
