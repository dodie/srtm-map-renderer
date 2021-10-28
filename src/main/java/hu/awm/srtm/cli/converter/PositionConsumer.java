package hu.awm.srtm.cli.converter;

import java.util.Stack;

import hu.awm.srtm.data.hgt.Position;
import picocli.CommandLine;

public class PositionConsumer implements CommandLine.IParameterConsumer {

	@Override
	public void consumeParameters(Stack<String> args, CommandLine.Model.ArgSpec argSpec,
			CommandLine.Model.CommandSpec commandSpec) {
		if (args.size() < 2) {
			throw new CommandLine.ParameterException(commandSpec.commandLine(), "Missing coordinates for Position. Please "
					+ "specify 2 coordinates.");
		}
		double lat = Double.parseDouble(args.pop());
		double lon = Double.parseDouble(args.pop());
		argSpec.setValue(new Position(lat, lon));
	}
}
