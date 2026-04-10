package example.save2.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CommandLineOptions {

    public enum CommandLineOption {

        OPERATION("operation", "op"),
        FILE_FROM("inputFile", "i"),
        FILE_TO("outputFile", "o");

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
                    .argName(CommandLineOption.FILE_FROM.name)
                    .desc("Source GPX file path")
                    .required(true)
                    .build();
            options.addOption(operationOption);

            Option fileFromOption = Option.builder()
                    .option(CommandLineOption.FILE_FROM.shortName)
                    .longOpt(CommandLineOption.FILE_FROM.name)
                    .hasArg()
                    .argName(CommandLineOption.FILE_FROM.name)
                    .desc("Source GPX file path")
                    .required(true)
                    .build();
            options.addOption(fileFromOption);

            Option fileToOption = Option.builder()
                    .option(CommandLineOption.FILE_TO.shortName)
                    .longOpt(CommandLineOption.FILE_TO.name)
                    .hasArg()
                    .argName(CommandLineOption.FILE_TO.name)
                    .desc("Destination GPX file source")
                    .required(true)
                    .build();
            options.addOption(fileToOption);

        }

        return options;

    }

    private CommandLineOptions() {
    }

}
