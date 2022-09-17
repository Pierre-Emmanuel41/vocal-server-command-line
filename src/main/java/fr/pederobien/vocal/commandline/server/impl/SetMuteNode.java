package fr.pederobien.vocal.commandline.server.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import fr.pederobien.commandtree.exceptions.BooleanParseException;
import fr.pederobien.vocal.server.interfaces.IVocalPlayer;
import fr.pederobien.vocal.server.interfaces.IVocalServer;

public class SetMuteNode extends VocalServerNode {

	/**
	 * Creates a node in order to update the mute status of a player.
	 * 
	 * @param server The server associated to this node.
	 */
	protected SetMuteNode(Supplier<IVocalServer> server) {
		super(server, "mute", EVocalServerCode.VOCAL_SERVER_CL__SET__MUTE__EXPLANATION, s -> s != null);
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
				send(EVocalServerCode.VOCAL_SERVER_CL__SET__MUTE__PLAYER_DOES_NOT_EXIST, args[0]);
				return false;
			}

			player = optPlayer.get();
		} catch (IndexOutOfBoundsException e) {
			send(EVocalServerCode.VOCAL_SERVER_CL__SET__MUTE__NAME_IS_MISSING);
			return false;
		}

		boolean isMute;
		try {
			isMute = getBoolean(args[1]);
		} catch (IndexOutOfBoundsException e) {
			send(EVocalServerCode.VOCAL_SERVER_CL__SET__MUTE__MUTE_STATUS_IS_MISSING, player.getName());
			return false;
		} catch (BooleanParseException e) {
			send(EVocalServerCode.VOCAL_SERVER_CL__SET__MUTE__MUTE_STATUS_BAD_FORMAT, player.getName());
			return false;
		}

		player.setMute(isMute);
		if (isMute)
			send(EVocalServerCode.VOCAL_SERVER_CL__SET__MUTE__PLAYER_MUTE, player.getName());
		else
			send(EVocalServerCode.VOCAL_SERVER_CL__SET__MUTE__PLAYER_NOT_MUTE, player.getName());
		return true;
	}
}
