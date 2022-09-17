package fr.pederobien.vocal.commandline.server.impl;

import java.util.function.Supplier;

import fr.pederobien.vocal.server.interfaces.IVocalServer;

public class SetNode extends VocalServerNode {
	private SetMuteNode muteNode;
	private SetDeafenNode deafenNode;

	/**
	 * Creates a node in order to update the characteristics of a player.
	 * 
	 * @param server The server associated to this node.
	 */
	protected SetNode(Supplier<IVocalServer> server) {
		super(server, "set", EVocalServerCode.VOCAL_SERVER_CL__SET__EXPLANATION, s -> s != null);

		add(muteNode = new SetMuteNode(server));
		add(deafenNode = new SetDeafenNode(server));
	}

	/**
	 * @return The node that update the mute status of a player.
	 */
	public SetMuteNode getMuteNode() {
		return muteNode;
	}

	/**
	 * @return The node that update the deafen status of a player.
	 */
	public SetDeafenNode getDeafenNode() {
		return deafenNode;
	}
}
