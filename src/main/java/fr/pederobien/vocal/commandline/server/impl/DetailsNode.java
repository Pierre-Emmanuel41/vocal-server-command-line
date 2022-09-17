package fr.pederobien.vocal.commandline.server.impl;

import java.util.StringJoiner;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import fr.pederobien.vocal.server.interfaces.IVocalPlayer;
import fr.pederobien.vocal.server.interfaces.IVocalServer;

public class DetailsNode extends VocalServerNode {

	/**
	 * Creates a node in order to display the characteristics of the server.
	 * 
	 * @param server The server associated to this node.
	 */
	protected DetailsNode(Supplier<IVocalServer> server) {
		super(server, "details", EVocalServerCode.VOCAL_SERVER_CL__DETAILS__EXPLANATION, s -> s != null);
	}

	@Override
	public boolean onCommand(String[] args) {
		String tabulations = "\t";
		StringJoiner serverJoiner = new StringJoiner("\n");

		// Server's name
		String serverName = getMessage(EVocalServerCode.VOCAL_SERVER_CL__DETAILS__NAME, getServer().getName());
		serverJoiner.add(serverName);

		// Server's port number
		String port = getMessage(EVocalServerCode.VOCAL_SERVER_CL__DETAILS__PORT_NUMBER, getServer().getPort());
		serverJoiner.add(port);

		// Server's speak behavior
		String speakBehavior = getMessage(EVocalServerCode.VOCAL_SERVER_CL__DETAILS__SPEAK_BEHAVIOR, getServer().getSpeakBehavior().getFriendlyName());
		serverJoiner.add(speakBehavior);

		serverJoiner.add("");

		// Server players
		String players = getMessage(EVocalServerCode.VOCAL_SERVER_CL__DETAILS__PLAYERS);
		serverJoiner.add(players);

		for (IVocalPlayer player : getServer().getPlayers()) {

			// Player's name
			String playerName = getMessage(EVocalServerCode.VOCAL_SERVER_CL__DETAILS__NAME, player.getName());
			serverJoiner.add(tabulations.concat(playerName));

			// Player's mute status
			EVocalServerCode muteCode;
			if (player.isMute())
				muteCode = EVocalServerCode.VOCAL_SERVER_CL__DETAILS__PLAYER_MUTE;
			else
				muteCode = EVocalServerCode.VOCAL_SERVER_CL__DETAILS__PLAYER_NOT_MUTE;

			String isMute = getMessage(EVocalServerCode.VOCAL_SERVER_CL__DETAILS__PLAYER_MUTE_STATUS, getMessage(muteCode));
			serverJoiner.add(tabulations.concat(isMute));

			// Player's mute by players
			String muteByPlayerNames = concat(player.getMuteByPlayers().map(p -> p.getName()).collect(Collectors.toList()));
			String muteByPlayers = getMessage(EVocalServerCode.VOCAL_SERVER_CL__DETAILS__PLAYER_MUTE_BY, muteByPlayerNames);
			serverJoiner.add(tabulations.concat(muteByPlayers));

			// Player's deafen status
			EVocalServerCode deafenCode;
			if (player.isDeafen())
				deafenCode = EVocalServerCode.VOCAL_SERVER_CL__DETAILS__PLAYER_DEAFEN;
			else
				deafenCode = EVocalServerCode.VOCAL_SERVER_CL__DETAILS__PLAYER_NOT_DEAFEN;

			String isDeafen = getMessage(EVocalServerCode.VOCAL_SERVER_CL__DETAILS__PLAYER_DEAFEN_STATUS, getMessage(deafenCode));
			serverJoiner.add(tabulations.concat(isDeafen));

			// Player's TCP address
			String tcpAddress = getMessage(EVocalServerCode.VOCAL_SERVER_CL__DETAILS__PLAYER_TCP_ADDRESS, player.getTcpAddress());
			serverJoiner.add(tabulations.concat(tcpAddress));

			// Player's UDP address
			String udpAddress = getMessage(EVocalServerCode.VOCAL_SERVER_CL__DETAILS__PLAYER_UDP_ADDRESS, player.getTcpAddress());
			serverJoiner.add(tabulations.concat(udpAddress));

			serverJoiner.add("");
		}

		send(EVocalServerCode.VOCAL_SERVER_CL__DETAILS__SERVER_CONFIGURATION, serverJoiner);
		return true;
	}
}
