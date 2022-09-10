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
			return true;
		});

		builder.onStop(root -> commandLine.send(EVocalServerCode.VOCAL_SERVER_CL__STOPPING));

		commandLine = builder.build(tree.getRoot(), args);
		commandLine.start();
	}
}
