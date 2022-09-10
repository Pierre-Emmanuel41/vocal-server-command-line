package fr.pederobien.vocal.commandline.server.impl;

public class CloseServerNode extends VocalServerNode {
	private VocalServerCommandTree tree;

	/**
	 * Creates a node to close a vocal server.
	 * 
	 * @param server The server associated to this node.
	 */
	protected CloseServerNode(VocalServerCommandTree tree) {
		super(() -> tree.getServer(), "close", EVocalServerCode.VOCAL_SERVER_CL__CLOSE__EXPLANATION, s -> s != null);
		this.tree = tree;
	}

	@Override
	public boolean onCommand(String[] args) {
		send(EVocalServerCode.VOCAL_SERVER_CL__CLOSE__SERVER_CLOSED, tree.getServer().getName());
		tree.getServer().close();
		tree.setServer(null);
		return true;
	}
}
