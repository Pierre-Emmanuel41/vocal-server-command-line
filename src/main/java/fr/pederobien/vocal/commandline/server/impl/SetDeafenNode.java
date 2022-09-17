package fr.pederobien.vocal.commandline.server.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import fr.pederobien.commandtree.exceptions.BooleanParseException;
import fr.pederobien.vocal.server.interfaces.IVocalPlayer;
import fr.pederobien.vocal.server.interfaces.IVocalServer;

public class SetDeafenNode extends VocalServerNode {

	/**
	 * Creates a node in order to update the deafen status of a player.
	 * 
	 * @param server The server associated to this node.
	 */
	protected SetDeafenNode(Supplier<IVocalServer> server) {
		super(server, "deafen", EVocalServerCode.VOCAL_SERVER_CL__SET__DEAFEN__EXPLANATION, s -> s != null);
	}

	@Override
	public List<String> onTabComplete(String[] args) {
		switch (args.length) {
		case 1:
			return filter(getServer().getPlayers().stream().map(player -> player.getName()), args);
		case 2:
			Predicate<String> isNameValid = name -> getServer().getPlayers().get(name).isPresent();
			return check(args[0], isNameValid, asList("true", "false"));
		default:
			return emptyList();
		}
	}

	@Override
	public boolean onCommand(String[] args) {
		IVocalPlayer player;
		try {
			Optional<IVocalPlayer> optPlayer = getServer().getPlayers().get(args[0]);
			if (!optPlayer.isPresent()) {
				send(EVocalServerCode.VOCAL_SERVER_CL__SET__DEAFEN__PLAYER_DOES_NOT_EXIST, args[0]);
				return false;
			}

			player = optPlayer.get();
		} catch (IndexOutOfBoundsException e) {
			send(EVocalServerCode.VOCAL_SERVER_CL__SET__DEAFEN__NAME_IS_MISSING);
			return false;
		}

		boolean isDeafen;
		try {
			isDeafen = getBoolean(args[1]);
		} catch (IndexOutOfBoundsException e) {
			send(EVocalServerCode.VOCAL_SERVER_CL__SET__DEAFEN__DEAFEN_STATUS_IS_MISSING, player.getName());
			return false;
		} catch (BooleanParseException e) {
			send(EVocalServerCode.VOCAL_SERVER_CL__SET__DEAFEN__DEAFEN_STATUS_BAD_FORMAT, player.getName());
			return false;
		}

		player.setDeafen(isDeafen);
		if (isDeafen)
			send(EVocalServerCode.VOCAL_SERVER_CL__SET__DEAFEN__PLAYER_DEAFEN, player.getName());
		else
			send(EVocalServerCode.VOCAL_SERVER_CL__SET__DEAFEN__PLAYER_NOT_DEAFEN, player.getName());
		return true;
	}
}
