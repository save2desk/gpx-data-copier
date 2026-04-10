package example.save2.cli;

import example.save2.cli.dto.CommandLineParameters;
import example.save2.cli.enums.Operation;
import example.save2.cli.exception.CommandLineParameterException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

import java.util.Objects;

import static example.save2.cli.CommandLineOptions.CommandLineOption.*;

public class CommandLineReader {

    private CommandLineReader() {
        /* This utility class should not be instantiated */
    }

    public static CommandLineParameters readParameters(String[] args) throws ParseException {

        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(CommandLineOptions.getOptions(), args);

        CommandLineParameters result = new CommandLineParameters();

        if (!cmd.hasOption("operation")) {
            throw new CommandLineParameterException("Необходимо указать параметр -op (--operation)");
        }

        final String operation = cmd.getOptionValue(OPERATION.getName());

        if (Objects.equals("fit2gpx", operation)) {
            result.operation = Operation.FIT2GPX;
        } else if (Objects.equals("heartrate", operation)) {
            result.operation = Operation.HEARTRATE;
        } else {
            throw new CommandLineParameterException("Неподдерживаемый параметр operation: " + operation);
        }

        if (!cmd.hasOption("inputFile")) {
            throw new CommandLineParameterException("Необходимо указать параметр -i (--inputFile)");
        }

        final String inputPathString = cmd.getOptionValue(FILE_FROM.getName());

        if (inputPathString.isEmpty()) {
            throw new CommandLineParameterException("Необходимо указать значение параметра -i (--inputFile)");
        }

        result.inputPathString = inputPathString;

        if (!cmd.hasOption("outputFile")) {
            throw new CommandLineParameterException("Необходимо указать параметр -o (--outputFile)");
        }

        String outputPathString = cmd.getOptionValue(FILE_TO.getName());

        if (outputPathString.isEmpty()) {
            if (result.operation == Operation.HEARTRATE) {
                throw new CommandLineParameterException("Необходимо указать значение параметра -o (--outputFile)");
            } else {
                outputPathString = inputPathString.replace(".fit", ".gpx");
            }
        }

        result.outputPathString = outputPathString;

        return result;

    }

}
