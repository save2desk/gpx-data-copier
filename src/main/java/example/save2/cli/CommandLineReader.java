package example.save2.cli;

import example.save2.cli.dto.CommandLineParameters;
import example.save2.cli.enums.Operation;
import example.save2.cli.exception.CommandLineParameterException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

import static example.save2.cli.CommandLineOptions.CommandLineOption.*;

public class CommandLineReader {

    private CommandLineReader() {
        /* This utility class should not be instantiated */
    }

    public static CommandLineParameters readParameters(String[] args) throws ParseException {

        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(CommandLineOptions.getOptions(), args);

        CommandLineParameters result = new CommandLineParameters();
        result.setOperation(readOperation(cmd));
        result.setFirstFilePathString(readFirstFile(cmd));
        if (result.getOperation() == Operation.HEARTRATE) {
            result.setSecondFilePathString(readSecondFile(cmd));
        }
        result.setParallel(readParallel(cmd));

        return result;

    }

    private static Operation readOperation(CommandLine cmd) {

        if (!cmd.hasOption("operation")) {
            throw new CommandLineParameterException("Необходимо указать параметр -op (--operation)");
        }

        final String operation = cmd.getOptionValue(OPERATION.getName());
        return switch (operation) {
            case "fit2gpx" -> Operation.FIT2GPX;
            case "heartrate" -> Operation.HEARTRATE;
            case "simplify" -> Operation.SIMPLIFY;
            case null, default ->
                    throw new CommandLineParameterException("Неподдерживаемый параметр operation: " + operation);
        };

    }

    private static String readFirstFile(CommandLine cmd) {

        if (!cmd.hasOption("firstFile")) {
            throw new CommandLineParameterException("Необходимо указать параметр -f1 (--firstFile)");
        }
        final String firstFilePathString = cmd.getOptionValue(FIRST_FILE.getName());
        if (firstFilePathString.isEmpty()) {
            throw new CommandLineParameterException("Необходимо указать значение параметра -f1 (--firstFile)");
        }
        return firstFilePathString;

    }

    private static String readSecondFile(CommandLine cmd) {

        if (!cmd.hasOption("outputFile")) {
            throw new CommandLineParameterException("Необходимо указать параметр -f2 (--secondFile)");
        }
        String secondFilePathString = cmd.getOptionValue(SECOND_FILE.getName());
        if (secondFilePathString.isEmpty()) {
            throw new CommandLineParameterException("Необходимо указать значение параметра -f2 (--secondFile)");
        }
        return secondFilePathString;

    }

    private static boolean readParallel(CommandLine cmd) {
        return cmd.hasOption("parallel");
    }

}
