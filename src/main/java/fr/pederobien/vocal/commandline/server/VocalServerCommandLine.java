package fr.pederobien.vocal.commandline.server;

import fr.pederobien.commandline.CommandLine;
import fr.pederobien.commandline.CommandLine.CommandLineBuilder;
import fr.pederobien.commandtree.events.NodeEvent;
import fr.pederobien.communication.event.ConnectionEvent;
import fr.pederobien.dictionary.event.DictionaryEvent;
import fr.pederobien.utils.event.EventLogger;
import fr.pederobien.vocal.commandline.server.impl.EVocalServerCode;
import fr.pederobien.vocal.commandline.server.impl.VocalServerCommandTree;

public class VocalServerCommandLine {
	private static final String DEV_DICTIONARY_FOLDER = "src/main/resources/dictionaries/";
	private static final String PROD_DICTIONARY_FOLDER = "resources/dictionaries/vocal/server/";

	private static VocalServerCommandTree tree;
	private static CommandLine commandLine;

	public static void main(String[] args) {
		tree = new VocalServerCommandTree();

		CommandLineBuilder builder = new CommandLineBuilder(root -> {
			EventLogger.instance().newLine(true).timeStamp(true).register();

			EventLogger.instance().ignore(DictionaryEvent.class);
			EventLogger.instance().ignore(ConnectionEvent.class);
			EventLogger.instance().ignore(NodeEvent.class);

			String dictionaryFolder = commandLine.getEnvironment() == CommandLine.DEVELOPMENT_ENVIRONMENT ? DEV_DICTIONARY_FOLDER : PROD_DICTIONARY_FOLDER;
			commandLine.registerDictionaries(dictionaryFolder, new String[] { "English.xml", "French.xml" });
			return true;
		});

		builder.onStart((root, arguments) -> {
			commandLine.send(EVocalServerCode.VOCAL_SERVER_CL__STARTING);
			if (arguments.length == 0)
				return true;

			if (arguments.length < 2) {
				commandLine.send(EVocalServerCode.VOCAL_SERVER_CL__STARTING__IGNORING_ARGUMENTS__NOT_ENOUGH_ARGUMENT);
				return true;
			}

			String[] commands = new String[arguments.length + 1];
			commands[0] = "open";
			for (int i = 0; i < arguments.length; i++)
				commands[i + 1] = arguments[i];

			root.onCommand(commands);
			return true;
		});

		builder.onStop(root -> {
			if (tree.getServer() != null)
				tree.getServer().close();
			commandLine.send(EVocalServerCode.VOCAL_SERVER_CL__STOPPING);
		});

		commandLine = builder.build(tree.getRoot(), args);
		commandLine.start();
	}
}
