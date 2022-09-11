package fr.pederobien.vocal.commandline.server.impl;

import java.util.List;
import java.util.function.Supplier;

import fr.pederobien.commandtree.exceptions.BooleanParseException;
import fr.pederobien.utils.event.EventHandler;
import fr.pederobien.utils.event.EventLogger;
import fr.pederobien.utils.event.EventManager;
import fr.pederobien.utils.event.EventPriority;
import fr.pederobien.utils.event.IEventListener;
import fr.pederobien.vocal.common.impl.VolumeResult;
import fr.pederobien.vocal.server.event.VocalPlayerSpeakEvent;
import fr.pederobien.vocal.server.interfaces.IVocalServer;

public class TestNode extends VocalServerNode {
	private Listener listener;

	/**
	 * Creates a node in order to run the vocal server in test mode.
	 * 
	 * @param server The server associated to this node
	 */
	protected TestNode(Supplier<IVocalServer> server) {
		super(server, "test", EVocalServerCode.VOCAL_SERVER_CL__TEST__EXPLANATION, s -> s != null);
		listener = new Listener();
	}

	@Override
	public List<String> onTabComplete(String[] args) {
		switch (args.length) {
		case 1:
			return asList("true", "false");
		default:
			return emptyList();
		}
	}

	@Override
	public boolean onCommand(String[] args) {
		boolean isRegistered;
		try {
			isRegistered = getBoolean(args[0]);
		} catch (IndexOutOfBoundsException e) {
			send(EVocalServerCode.VOCAL_SERVER_CL__TEST__TEST_STATUS_IS_MISSING, getServer().getName());
			return false;
		} catch (BooleanParseException e) {
			send(EVocalServerCode.VOCAL_SERVER_CL__TEST__TEST_STATUS_BAD_FORMAT, getServer().getName());
			return false;
		}

		listener.setRegistered(isRegistered);
		if (isRegistered)
			send(EVocalServerCode.VOCAL_SERVER_CL__TEST__TEST_MODE_ACTIVATED, getServer().getName());
		else
			send(EVocalServerCode.VOCAL_SERVER_CL__TEST__TEST_MODE_DEACTIVATED, getServer().getName());
		return true;
	}

	private class Listener implements IEventListener {
		private boolean isRegistered;

		private Listener() {
			isRegistered = false;
		}

		/**
		 * Register or unregister this listener for the EventManager.
		 * 
		 * @param isRegistered True to register, false to unregister.
		 */
		public void setRegistered(boolean isRegistered) {
			if (this.isRegistered == isRegistered)
				return;

			this.isRegistered = isRegistered;
			if (isRegistered) {
				EventManager.registerListener(this);
				EventLogger.instance().accept(VocalPlayerSpeakEvent.class);
			} else {
				EventManager.unregisterListener(this);
				EventLogger.instance().ignore(VocalPlayerSpeakEvent.class);
			}
		}

		@EventHandler(priority = EventPriority.HIGH)
		private void onPlayerSpeak(VocalPlayerSpeakEvent event) {
			if (!event.getServer().equals(getServer()))
				return;

			event.getVolumes().clear();
			event.getVolumes().put(event.getTransmitter(), VolumeResult.DEFAULT);
		}
	}
}
