package hu.awm.srtm.cli;

import static picocli.CommandLine.Command;

import picocli.CommandLine;

@Command(name = "srtm", subcommands = { ReliefCli.class, ContourCli.class, CanSeeCli.class },
		mixinStandardHelpOptions = true)
public class SRTMApplication {

	public static void main(String[] args) {
		new CommandLine(new SRTMApplication()).execute(args);
	}

}
