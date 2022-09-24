package fr.pederobien.vocal.commandline.server.impl;

import java.util.List;
import java.util.function.Predicate;

import fr.pederobien.vocal.server.impl.SpeakBehavior;
import fr.pederobien.vocal.server.impl.VocalServer;

public class OpenServerNode extends VocalServerNode {
	private VocalServerCommandTree tree;

	/**
	 * Creates a node in order to open a vocal server.
	 * 
	 * @param server The server associated to this node.
	 */
	protected OpenServerNode(VocalServerCommandTree tree) {
		super(() -> tree.getServer(), "open", EVocalServerCode.VOCAL_SERVER_CL__OPEN__EXPLANATION, s -> true);
		this.tree = tree;
	}

	@Override
	public List<String> onTabComplete(String[] args) {
		switch (args.length) {
		case 1:
			return asList(getMessage(EVocalServerCode.VOCAL_SERVER_CL__NAME__COMPLETION));
		case 2:
			Predicate<String> isNameValid = name -> !name.equals("");
			return check(args[0], isNameValid, asList(getMessage(EVocalServerCode.VOCAL_SERVER_CL__PORT__COMPLETION)));
		case 3:
			Predicate<String> isPortValid = port -> {
				try {
					int portValue = getInt(port);
					return 0 <= portValue && portValue <= 65535;
				} catch (NumberFormatException e) {
					return false;
				}
			};
			return check(args[2], isPortValid, SpeakBehavior.NAMES);
		default:
			return emptyList();
		}
	}

	@Override
	public boolean onCommand(String[] args) {
		String name;
		try {
			name = args[0];
		} catch (IndexOutOfBoundsException e) {
			send(EVocalServerCode.VOCAL_SERVER_CL__OPEN__SERVER_NAME_IS_MISSING);
			return false;
		}

		int port;
		try {
			port = getInt(args[1]);
			if (port < 0 || port > 65535) {
				send(EVocalServerCode.VOCAL_SERVER_CL__OPEN__SERVER_PORT_OUT_OF_RANGE);
				return false;
			}
		} catch (IndexOutOfBoundsException e) {
			send(EVocalServerCode.VOCAL_SERVER_CL__OPEN__SERVER_PORT_IS_MISSING);
			return false;
		} catch (NumberFormatException e) {
			send(EVocalServerCode.VOCAL_SERVER_CL__OPEN__SERVER_PORT_BAD_FORMAT);
			return false;
		}

		SpeakBehavior speakBehavior = SpeakBehavior.TO_EVERYONE;
		try {
			speakBehavior = SpeakBehavior.fromName(args[2]);
		} catch (IndexOutOfBoundsException e) {
		}

		if (tree.getServer() != null)
			tree.getServer().close();

		tree.setServer(new VocalServer(name, port, speakBehavior));
		tree.getServer().open();

		send(EVocalServerCode.VOCAL_SERVER_CL__OPEN__SERVER_OPENED, name, port);
		return true;
	}
}
