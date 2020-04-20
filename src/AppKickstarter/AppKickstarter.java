package AppKickstarter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.Hashtable;

import AppKickstarter.timer.Timer;
import AppKickstarter.Msg.QueueTooLong;
import AppKickstarter.Msg.TicketRep;
import AppKickstarter.Server.Ticket;
import AppKickstarter.misc.*;
import AppKickstarter.myHanlderThreads.*;

//New commit

//======================================================================
// AppKickstarter
public class AppKickstarter {
	private String cfgFName = null;
	private Properties cfgProps = null;
	private Hashtable<String, AppThread> appThreads = null;
	private String id = null;
	private Logger log = null;
	private ConsoleHandler logConHd = null;
	private FileHandler logFileHd = null;
	private Timer timer = null;
	private ThreadA threadA;
	private SocketInHandler socketInHandler;
	private SocketOutHandler socketOutHandler;
	private TicketHandler ticketHandler;
	private TableHandler tableHandler;
	private MsgHandler msgHandler;
	private String ServerIP;
	private int ServerPort;
	private Socket socket;
	DataOutputStream out;
	DataInputStream in;
	private final int TimerIDForMatchTicketQueue = 10000;

	// ------------------------------------------------------------
	// main
	public static void main(String[] args) {
		AppKickstarter appKickstarter = new AppKickstarter("AppKickstarter", "etc/MyApp.cfg");
		appKickstarter.startApp();
		try {
			Thread.sleep(5000 * 10000);
		} catch (Exception e) {
		}
		// appKickstarter.stopApp();
	} // main

	// ------------------------------------------------------------
	// AppKickstarter
	private AppKickstarter(String id) {
		this(id, "etc/MyApp.cfg");
	} // AppKickstarter

	// ------------------------------------------------------------
	// AppKickstarter
	private AppKickstarter(String id, String cfgFName) {
		this(id, cfgFName, false);
	} // AppKickstarter

	// ------------------------------------------------------------
	// AppKickstarter
	private AppKickstarter(String id, String cfgFName, boolean append) {
		this.id = id;
		this.cfgFName = cfgFName;
		logConHd = null;
		logFileHd = null;
		id = getClass().getName();

		// set my thread name
		Thread.currentThread().setName(this.id);

		// read system config from property file
		try {
			cfgProps = new Properties();
			FileInputStream in = new FileInputStream(cfgFName);
			cfgProps.load(in);
			in.close();
			logConHd = new ConsoleHandler();
			logConHd.setFormatter(new LogFormatter());
			logFileHd = new FileHandler("etc/" + id + ".log", append);
			logFileHd.setFormatter(new LogFormatter());
		} catch (FileNotFoundException e) {
			System.out.println("Failed to open config file (" + cfgFName + ").");
			System.exit(-1);
		} catch (IOException e) {
			System.out.println("Error reading config file (" + cfgFName + ").");
			System.exit(-1);
		}

		// get and configure logger
		log = Logger.getLogger(id);
		log.addHandler(logConHd);
		log.addHandler(logFileHd);
		log.setUseParentHandlers(false);
		log.setLevel(Level.ALL);
		logConHd.setLevel(Level.INFO);
		logFileHd.setLevel(Level.FINER);
		appThreads = new Hashtable<String, AppThread>();
	} // AppKickstarter

	private int Mode;

	public int getMode() {
		return this.Mode;
	}

	public int getTimerIDForMatchTicketQueue() {
		return TimerIDForMatchTicketQueue;
	}

	// ------------------------------------------------------------
	// startApp
	private void startApp() {
		// start our application
		log.info("");
		log.info("");
		log.info("============================================================");
		log.info(id + ": Application Starting...");

		Scanner sc = new Scanner(System.in);
		while (true) {
			log.info("Press 1 to Run CLIENTSTREAM MODE, 2 to Run GUI MODE: ");
			int i = sc.nextInt();
			if (i == 1) {
				Mode = 1;
				log.info("Run CLIENTSTREAM MODE!");
				break;
			} else if (i == 2) {
				Mode = 2;
				log.info("Run GUI MODE!");
				break;
			}
		}

		System.out.println("PRESS ANY KEY TO CONTINUE: ");
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// create threads
		timer = new Timer("timer", this);
		ticketHandler = new TicketHandler("TicketHandler", this);
		msgHandler = new MsgHandler("MsgHandler", this);

		// start threads
		new Thread(timer).start();
		new Thread(ticketHandler).start();
		new Thread(msgHandler).start();

		this.ServerIP = getProperty("ServerIP");
		this.ServerPort = Integer.valueOf(getProperty("ServerPort"));
		log.info(id + ": Listening at ServerIP>" + ServerIP + " ServerPort>" + ServerPort + "...");
		try {
			this.socket = new ServerSocket(ServerPort).accept();

		} catch (IOException e) {
			e.printStackTrace();
		}
		socketInHandler = new SocketInHandler("SocketInHandler", this);
		socketOutHandler = new SocketOutHandler("SocketOutHandler", this);
		new Thread(socketInHandler).start();
		new Thread(socketOutHandler).start();
		log.info(id + ": accepted...");
	} // startApp

	// ------------------------------------------------------------
	// stopApp
	private void stopApp() {
		log.info("");
		log.info("");
		log.info("============================================================");
		log.info(id + ": Application Stopping...");
		threadA.getMBox().send(new Msg(id, null, Msg.Type.Terminate, "Terminate now!"));
		socketInHandler.getMBox().send(new Msg(id, null, Msg.Type.Terminate, "Terminate now!"));
		timer.getMBox().send(new Msg(id, null, Msg.Type.Terminate, "Terminate now!"));
	} // stopApp

	// ------------------------------------------------------------
	// getThread
	public synchronized Socket getSocket() {
		return socket;
	} // getThread

	public synchronized DataOutputStream getDataOutputStream() {
		try {
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}

	public synchronized PrintWriter getPrintWriter() {
		try {
			return new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public synchronized DataInputStream getDataInputStream() {
		try {
			in = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return in;
	}

	// ------------------------------------------------------------
	// regThread
	public void regThread(AppThread appThread) {
		log.fine(id + ": registering " + appThread.getID());
		synchronized (appThreads) {
			appThreads.put(appThread.getID(), appThread);
		}
	} // regThread

	// ------------------------------------------------------------
	// unregThread
	public void unregThread(AppThread appThread) {
		log.fine(id + ": unregistering " + appThread.getID());
		synchronized (appThreads) {
			appThreads.remove(appThread.getID());
		}
	} // unregThread

	// ------------------------------------------------------------
	// getThread
	public AppThread getThread(String id) {
		synchronized (appThreads) {
			return appThreads.get(id);
		}
	} // getThread

	// ------------------------------------------------------------
	// getLogger
	public Logger getLogger() {
		return log;
	} // getLogger

	// ------------------------------------------------------------
	// getLogConHd
	public ConsoleHandler getLogConHd() {
		return logConHd;
	}
	// getLogConHd

	// ------------------------------------------------------------
	// getLogFileHd
	public FileHandler getLogFileHd() {
		return logFileHd;
	} // getLogFileHd

	// ------------------------------------------------------------
	// getProperty
	public String getProperty(String property) {
		String s = cfgProps.getProperty(property);

		if (s == null) {
			log.severe(id + ": getProperty(" + property + ") failed.  Check the config file (" + cfgFName + ")!");
		}
		return s;
	} // getProperty

	// ------------------------------------------------------------
	// getSimulationTime (in seconds)
	public long getSimulationTime() {
		return timer.getSimulationTime();
	} // getSimulationTime
} // AppKickstarter
