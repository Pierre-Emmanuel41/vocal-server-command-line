package fr.pederobien.vocal.commandline.server.impl;

import java.util.Locale;
import java.util.function.Consumer;

import fr.pederobien.commandline.impl.CommandLineDictionaryContext;
import fr.pederobien.commandtree.impl.CommandRootNode;
import fr.pederobien.commandtree.interfaces.ICommandRootNode;
import fr.pederobien.commandtree.interfaces.INode;
import fr.pederobien.dictionary.impl.MessageEvent;
import fr.pederobien.dictionary.interfaces.ICode;
import fr.pederobien.utils.AsyncConsole;
import fr.pederobien.vocal.server.interfaces.IVocalServer;

public class VocalServerCommandTree {
	private IVocalServer server;
	private ICommandRootNode<ICode> root;
	private OpenServerNode openNode;
	private CloseServerNode closeNode;
	private TestNode testNode;
	private DetailsNode detailsNode;
	private SetNode setNode;

	public VocalServerCommandTree() {
		Consumer<INode<ICode>> displayer = node -> {
			String label = node.getLabel();
			String explanation = CommandLineDictionaryContext.instance().getMessage(new MessageEvent(Locale.getDefault(), node.getExplanation()));
			AsyncConsole.println(String.format("%s - %s", label, explanation));
		};

		root = new CommandRootNode<ICode>("vocal", EVocalServerCode.VOCAL_SERVER_CL__ROOT__EXPLANATION, () -> true, displayer);
		root.add(openNode = new OpenServerNode(this));
		root.add(closeNode = new CloseServerNode(this));
		root.add(testNode = new TestNode(() -> getServer()));
		root.add(detailsNode = new DetailsNode(() -> getServer()));
		root.add(setNode = new SetNode(() -> getServer()));
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

	/**
	 * @return The node that open a vocal server.
	 */
	public OpenServerNode getOpenNode() {
		return openNode;
	}

	/**
	 * @return The node that close a vocal server.
	 */
	public CloseServerNode getCloseNode() {
		return closeNode;
	}

	/**
	 * @return The node that enable or disable the test mode of a vocal server.
	 */
	public TestNode getTestNode() {
		return testNode;
	}

	/**
	 * @return The node to display the characteristics of a vocal server.
	 */
	public DetailsNode getDetailsNode() {
		return detailsNode;
	}

	/**
	 * @return The node that modify the characteristics of a player.
	 */
	public SetNode getSetNode() {
		return setNode;
	}
}
