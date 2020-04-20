package AppKickstarter.myHanlderThreads;

import AppKickstarter.misc.*;

import AppKickstarter.AppKickstarter;
import AppKickstarter.timer.Timer;

//======================================================================
// ThreadA
public class ThreadA extends AppThread {
	private final int sleepTime = 0;

	// ------------------------------------------------------------
	// ThreadA
	public ThreadA(String id, AppKickstarter appKickstarter) {
		super(id, appKickstarter);

	} // ThreadA

	// ------------------------------------------------------------
	// run
	public void run() {
		log.info(id + ": starting...");
		Timer.setTimer(id, mbox, sleepTime);

		for (boolean quit = false; !quit;) {
			Msg msg = mbox.receive();

			log.info(id + ": message received: [" + msg + "].");

			switch (msg.getType()) {
			case TimesUp:
				log.info(id + ": say hello to Thread B...");
				appKickstarter.getThread("ThreadB").getMBox()
						.send(new Msg(id, mbox, Msg.Type.Hello, "Hello, this is Thread A!"));
				Timer.setTimer(id, mbox, sleepTime);
				break;

			case HiHi:
				log.info(id + ": " + msg.getSender() + " is saying HiHi to me!!!");
				break;

			case Terminate:
				quit = true;
				break;

			default:
				log.severe(id + ": unknown message type!!");
				break;
			}
		}

		// declaring our departure
		appKickstarter.unregThread(this);
		log.info(id + ": terminating...");
	} // run
} // ThreadA
