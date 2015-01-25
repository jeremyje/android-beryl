package org.beryl.database;

import java.util.LinkedList;

/** Runs the DatabaseUpdateScript objects. Each script is run sequentially as it was en-queued.
 * These scripts are run on the same thread that the runner is invoked from.
 *
 * TODO: Create a more general purpose Job Queue system that this class should inherit.
 * */
class DatabaseUpdateRunner implements Runnable {

	private final LinkedList<Runnable> updateScripts = new LinkedList<Runnable>();

	public DatabaseUpdateRunner() {
	}

	/** Add update script to the queue. */
	void add(Runnable script) {
		updateScripts.add(script);
	}

	public void run() {
		while(! updateScripts.isEmpty()) {
			Runnable script = updateScripts.remove();
			script.run();
		}
	}
}
