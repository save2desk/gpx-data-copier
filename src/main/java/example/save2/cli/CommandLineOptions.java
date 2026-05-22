package example.save2.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CommandLineOptions {

    public enum CommandLineOption {

        OPERATION("operation", "op"),
        FIRST_FILE("firstFile", "f1"),
        SECOND_FILE("secondFile", "f2"),
        PARALLEL("parallel", null);

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

            Option fileFromOption = Option.builder()
                    .option(CommandLineOption.FIRST_FILE.shortName)
                    .longOpt(CommandLineOption.FIRST_FILE.name)
                    .hasArg()
                    .argName(CommandLineOption.FIRST_FILE.name)
                    .desc("Main GPX or FIT file path")
                    .required(true)
                    .build();
            options.addOption(fileFromOption);

            Option fileToOption = Option.builder()
                    .option(CommandLineOption.SECOND_FILE.shortName)
                    .longOpt(CommandLineOption.SECOND_FILE.name)
                    .hasArg()
                    .argName(CommandLineOption.SECOND_FILE.name)
                    .desc("Secondary GPX file path")
                    .required(false)
                    .build();
            options.addOption(fileToOption);

            Option parallelOption = Option.builder()
                    .longOpt(CommandLineOption.PARALLEL.name)
                    .desc("Parallel mode")
                    .required(false)
                    .build();
            options.addOption(parallelOption);

        }

        return options;

    }

    private CommandLineOptions() {
    }

}
