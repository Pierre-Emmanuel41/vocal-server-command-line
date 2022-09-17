package fr.pederobien.vocal.commandline.server.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import fr.pederobien.commandtree.exceptions.BooleanParseException;
import fr.pederobien.vocal.server.interfaces.IVocalPlayer;
import fr.pederobien.vocal.server.interfaces.IVocalServer;

public class SetMuteByNode extends VocalServerNode {

	/**
	 * Creates a node in order to update the mute by status of a player.
	 * 
	 * @param server The server associated to this node.
	 */
	protected SetMuteByNode(Supplier<IVocalServer> server) {
		super(server, "muteby", EVocalServerCode.VOCAL_SERVER_CL__SET__MUTE_BY__EXPLANATION, s -> s != null);
	}

	@Override
	public List<String> onTabComplete(String[] args) {
		switch (args.length) {
		case 1:
			return filter(getServer().getPlayers().stream().map(player -> player.getName()), args);
		case 2:
			Predicate<String> isNameValid = name -> getServer().getPlayers().get(name).isPresent();
			return check(args[0], isNameValid, asList("true", "false"));
		case 3:
			Predicate<String> isStatusValid = isMute -> {
				try {
					return getBoolean(isMute);
				} catch (BooleanParseException e) {
					return false;
				}
			};
			IVocalPlayer source = getServer().getPlayers().get(args[0]).get();
			Stream<String> players = getServer().getPlayers().stream().filter(player -> !player.equals(source)).map(player -> player.getName());
			return check(args[1], isStatusValid, filter(players, args));
		default:
			return emptyList();
		}
	}

	@Override
	public boolean onCommand(String[] args) {
		IVocalPlayer target;
		try {
			Optional<IVocalPlayer> optPlayer = getServer().getPlayers().get(args[0]);
			if (!optPlayer.isPresent()) {
				send(EVocalServerCode.VOCAL_SERVER_CL__SET__MUTE_BY__PLAYER_TARGET_DOES_NOT_EXIST, args[0]);
				return false;
			}

			target = optPlayer.get();
		} catch (IndexOutOfBoundsException e) {
			send(EVocalServerCode.VOCAL_SERVER_CL__SET__MUTE_BY__NAME_IS_MISSING);
			return false;
		}

		boolean isMute;
		try {
			isMute = getBoolean(args[1]);
		} catch (IndexOutOfBoundsException e) {
			send(EVocalServerCode.VOCAL_SERVER_CL__SET__MUTE_BY__MUTE_STATUS_IS_MISSING, target.getName());
			return false;
		} catch (BooleanParseException e) {
			send(EVocalServerCode.VOCAL_SERVER_CL__SET__MUTE_BY__MUTE_STATUS_BAD_FORMAT, target.getName());
			return false;
		}

		List<IVocalPlayer> players = emptyList();
		String[] names = extract(args, 2);
		for (String name : names) {
			Optional<IVocalPlayer> optPlayer = getServer().getPlayers().get(name);
			if (!optPlayer.isPresent()) {
				send(EVocalServerCode.VOCAL_SERVER_CL__SET__MUTE_BY__PLAYER_SOURCE_DOES_NOT_EXIST, name);
				return false;
			}

			players.add(optPlayer.get());
		}

		for (IVocalPlayer player : players)
			target.setMuteBy(player, isMute);

		if (isMute)
			send(EVocalServerCode.VOCAL_SERVER_CL__SET__MUTE_BY__PLAYER_MUTE, target.getName(), concat(names, ", "));
		else
			send(EVocalServerCode.VOCAL_SERVER_CL__SET__MUTE_BY__PLAYER_NOT_MUTE, target.getName(), concat(names, ", "));
		return true;
	}
}
