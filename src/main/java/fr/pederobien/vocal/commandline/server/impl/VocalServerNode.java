package fr.pederobien.vocal.commandline.server.impl;

import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

import fr.pederobien.commandline.CommandLineDictionaryContext;
import fr.pederobien.commandline.ICode;
import fr.pederobien.commandtree.impl.CommandNode;
import fr.pederobien.dictionary.impl.MessageEvent;
import fr.pederobien.vocal.commandline.server.interfaces.IVocalServerNode;
import fr.pederobien.vocal.server.interfaces.IVocalServer;

public class VocalServerNode extends CommandNode<ICode> implements IVocalServerNode {
	private Supplier<IVocalServer> server;

	/**
	 * Creates a node specified by the given parameters.
	 * 
	 * @param server      The server associated to this node.
	 * @param label       The primary node name.
	 * @param explanation The explanation associated to this node.
	 * @param isAvailable True if this node is available, false otherwise.
	 */
	protected VocalServerNode(Supplier<IVocalServer> server, String label, ICode explanation, Function<IVocalServer, Boolean> isAvailable) {
		super(label, explanation, () -> isAvailable.apply(server == null ? null : server.get()));
		this.server = server;
	}

	/**
	 * @return The server associated to this node.
	 */
	public IVocalServer getServer() {
		return server == null ? null : server.get();
	}

	/**
	 * Send a language sensitive message in the console.
	 * 
	 * @param code Used as key to get the right message in the right dictionary.
	 * @param args Some arguments (optional) used for dynamic messages.
	 */
	protected void send(ICode code, Object... args) {
		CommandLineDictionaryContext.instance().send(new MessageEvent(Locale.getDefault(), code.getCode(), args));
	}

	/**
	 * Get a message associated to the given code translated in the OS language.
	 * 
	 * @param code Used as key to get the right message in the right dictionary.
	 * @param args Some arguments (optional) used for dynamic messages.
	 */
	protected String getMessage(ICode code, Object... args) {
		return CommandLineDictionaryContext.instance().getMessage(new MessageEvent(Locale.getDefault(), code.getCode(), args));
	}
}
