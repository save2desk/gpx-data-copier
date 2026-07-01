package example.save2.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CommandLineOptions {

    public enum CommandLineOption {

        OPERATION("operation", "op"),
        FIRST_INPUT_FILE("firstFile", "f1"),
        SECOND_INPUT_FILE("secondFile", "f2"),
        INPUT_FILE_FOLDER("inputFolder", "ff"),
        OUTPUT_FILE("outputFile", "fo"),
        SIMPLIFY_ITERATIONS("iterations", null),
        PARALLEL_THREADS("threads", null),
        CLOSE_POINTS_DISTANCE("closePointsDistance", null),
        LINE_POINTS_DISTANCE("linePointsDistance", null);

        private final String name;
        private final String shortName;

        CommandLineOption(String name, String shortName) {
            this.name = name;
            this.shortName = shortName;
        }

        public String getName() {
            return name;
        }

        public String getShortName() {
            return shortName;
        }

    }

    private static Options options;

    public static Options getOptions() {

        if (options == null) {

            options = new Options();

            Option operationOption = Option.builder()
                    .option(CommandLineOption.OPERATION.shortName)
                    .longOpt(CommandLineOption.OPERATION.name)
                    .hasArg()
                    .argName(CommandLineOption.OPERATION.name)
                    .desc("Source GPX file path")
                    .required(true)
                    .build();
            options.addOption(operationOption);

            Option firstInputFileOption = Option.builder()
                    .option(CommandLineOption.FIRST_INPUT_FILE.shortName)
                    .longOpt(CommandLineOption.FIRST_INPUT_FILE.name)
                    .hasArg()
                    .argName(CommandLineOption.FIRST_INPUT_FILE.name)
                    .desc("First input file path")
                    .required(true)
                    .build();
            options.addOption(firstInputFileOption);

            Option secondInputFileOption = Option.builder()
                    .option(CommandLineOption.SECOND_INPUT_FILE.shortName)
                    .longOpt(CommandLineOption.SECOND_INPUT_FILE.name)
                    .hasArg()
                    .argName(CommandLineOption.SECOND_INPUT_FILE.name)
                    .desc("Second input file path")
                    .required(false)
                    .build();
            options.addOption(secondInputFileOption);

            Option outputFileOption = Option.builder()
                    .option(CommandLineOption.OUTPUT_FILE.shortName)
                    .longOpt(CommandLineOption.OUTPUT_FILE.name)
                    .hasArg()
                    .argName(CommandLineOption.OUTPUT_FILE.name)
                    .desc("Output file path")
                    .required(false)
                    .build();
            options.addOption(outputFileOption);

        }

        return options;

    }

    private CommandLineOptions() {
    }

}
