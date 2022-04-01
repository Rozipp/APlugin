package ua.rozipp.example.remotesession;

import ua.rozipp.example.main.ExampleStatic;

import java.util.Iterator;
import java.util.List;

public class TickHandler implements Runnable {

	public static List<RemoteSession> sessions;

	public void run() {
		Iterator<RemoteSession> sI = sessions.iterator();
		while(sI.hasNext()) {
			RemoteSession s = sI.next();
			if (s.pendingRemoval) {
				s.close();
				sI.remove();
			} else {
				s.tick();
			}
		}
	}

	public static void stopSessions(){
		for (RemoteSession session: sessions) {
			try {
				session.close();
			} catch (Exception e) {
				ExampleStatic.getLogger().warning("Failed to close RemoteSession");
				e.printStackTrace();
			}
		}
	}
}
