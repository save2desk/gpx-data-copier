package example.save2.cli;

import example.save2.cli.enums.Operation;
import example.save2.cli.enums.ParameterKey;
import example.save2.cli.exception.CommandLineParameterException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

import java.util.Optional;

import static example.save2.cli.CommandLineOptions.CommandLineOption.*;

@SuppressWarnings("OptionalIsPresent")
public class CommandLineReader {

    private CommandLineReader() {
        /* This utility class should not be instantiated */
    }

    public static void readParametersIntoStorage(String[] args) throws ParseException {

        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(CommandLineOptions.getOptions(), args);

        Operation operation = readOperation(cmd);

        ParametersStorage.addParameter(ParameterKey.OPERATION, operation);

        switch (operation) {
            case FIT2GPX -> readDefaultMultiFileParameters(cmd);
            case HEARTRATE -> {
                ParametersStorage.addParameter(ParameterKey.FIRST_INPUT_FILE, readFirstFile(cmd));
                ParametersStorage.addParameter(ParameterKey.SECOND_INPUT_FILE, readSecondFile(cmd));
                ParametersStorage.addParameter(ParameterKey.OUTPUT_FILE, readOptionalParameter(cmd, OUTPUT_FILE));
            }
            case SIMPLIFY -> {
                readDefaultMultiFileParameters(cmd);

                Optional<String> threadsOptional = readOptionalParameter(cmd, PARALLEL_THREADS);
                ParametersStorage.addParameter(ParameterKey.PARALLEL_THREADS,
                        threadsOptional.isPresent() ? Integer.parseInt(threadsOptional.get()) : 1);

                Optional<String> iterationsOptional = readOptionalParameter(cmd, SIMPLIFY_ITERATIONS);
                ParametersStorage.addParameter(ParameterKey.SIMPLIFY_ITERATIONS,
                        iterationsOptional.isPresent() ? Integer.parseInt(iterationsOptional.get()) : 5);

                Optional<String> closePointsDistanceOptional = readOptionalParameter(cmd, CLOSE_POINTS_DISTANCE);
                ParametersStorage.addParameter(ParameterKey.CLOSE_POINTS_DISTANCE,
                        closePointsDistanceOptional.isPresent() ? Double.parseDouble(closePointsDistanceOptional.get()) : 15.0);

                Optional<String> linePointsDistanceOptional = readOptionalParameter(cmd, LINE_POINTS_DISTANCE);
                ParametersStorage.addParameter(ParameterKey.LINE_POINTS_DISTANCE,
                        linePointsDistanceOptional.isPresent() ? Double.parseDouble(linePointsDistanceOptional.get()) : 4.0);
            }
        }

    }

    private static void readDefaultMultiFileParameters(CommandLine cmd) {
        Optional<String> inputFileFolder = readOptionalParameter(cmd, INPUT_FILE_FOLDER);
        if (inputFileFolder.isPresent()) {
            ParametersStorage.addParameter(ParameterKey.INPUT_FILE_FOLDER, inputFileFolder.get());
        } else {
            ParametersStorage.addParameter(ParameterKey.FIRST_INPUT_FILE, readFirstFile(cmd));
        }
        Optional<String> outputFile = readOptionalParameter(cmd, OUTPUT_FILE);
        if (outputFile.isPresent()) {
            ParametersStorage.addParameter(ParameterKey.OUTPUT_FILE, outputFile.get());
        }
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

        if (!cmd.hasOption("firstFile") && ParametersStorage.containsKey(ParameterKey.INPUT_FILE_FOLDER)) {
            throw new CommandLineParameterException("Необходимо указать параметр -f1 (--firstFile)");
        }
        final String pathString = cmd.getOptionValue(FIRST_INPUT_FILE.getName());
        if (pathString == null || pathString.isEmpty()) {
            throw new CommandLineParameterException("Необходимо указать значение параметра -f1 (--firstFile)");
        }
        return pathString;

    }

    private static String readSecondFile(CommandLine cmd) {

        if (!cmd.hasOption("secondFile")) {
            throw new CommandLineParameterException("Необходимо указать параметр -f2 (--secondFile)");
        }
        String pathString = cmd.getOptionValue(SECOND_INPUT_FILE.getName());
        if (pathString == null || pathString.isEmpty()) {
            throw new CommandLineParameterException("Необходимо указать значение параметра -f2 (--secondFile)");
        }
        return pathString;

    }

    private static Optional<String> readOptionalParameter(CommandLine cmd, CommandLineOptions.CommandLineOption option) {

        if (!cmd.hasOption(option.getName())) {
            return Optional.empty();
        }
        String pathString = cmd.getOptionValue(option.getName());
        if (pathString == null || pathString.isEmpty()) {
            String errorMessage = "Необходимо указать значение параметра --%s".formatted(option.getName());
            if (option.getShortName() != null) {
                errorMessage += " (-%s)".formatted(option.getShortName());
            }
            throw new CommandLineParameterException(errorMessage);
        }
        return Optional.of(pathString);

    }

}
