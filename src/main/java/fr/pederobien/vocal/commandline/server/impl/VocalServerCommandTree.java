package fr.pederobien.vocal.commandline.server.impl;

import java.util.Locale;
import java.util.function.Consumer;

import fr.pederobien.commandline.CommandLineDictionaryContext;
import fr.pederobien.commandline.ICode;
import fr.pederobien.commandtree.impl.CommandRootNode;
import fr.pederobien.commandtree.interfaces.ICommandRootNode;
import fr.pederobien.commandtree.interfaces.INode;
import fr.pederobien.dictionary.impl.MessageEvent;
import fr.pederobien.utils.AsyncConsole;
import fr.pederobien.vocal.server.interfaces.IVocalServer;

public class VocalServerCommandTree {
	private IVocalServer server;
	private ICommandRootNode<ICode> root;

	public VocalServerCommandTree() {
		Consumer<INode<ICode>> displayer = node -> {
			String label = node.getLabel();
			String explanation = CommandLineDictionaryContext.instance().getMessage(new MessageEvent(Locale.getDefault(), node.getExplanation().toString()));
			AsyncConsole.println(String.format("%s - %s", label, explanation));
		};

		root = new CommandRootNode<ICode>("vocal", EVocalServerCode.VOCAL_SERVER_CL__ROOT__EXPLANATION, () -> true, displayer);
	}

	/**
	 * @return The underlying vocal server managed by this command tree.
	 */
	public IVocalServer getServer() {
		return server;
	}

	/**
	 * Set the underlying vocal server managed by this command tree.
	 * 
	 * @param server The new vocal server managed by this command tree.
	 */
	public void setServer(IVocalServer server) {
		this.server = server;
	}

	/**
	 * @return The root of this command tree.
	 */
	public ICommandRootNode<ICode> getRoot() {
		return root;
	}
}
