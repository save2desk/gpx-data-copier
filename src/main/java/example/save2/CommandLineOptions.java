package example.save2;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CommandLineOptions {

  public enum CommandLineOption {

    FILE_FROM("fileFrom"),
    FILE_TO("fileTo");

    private final String name;

    public String getName() {
      return name;
    }

    CommandLineOption(String name) {
      this.name = name;
    }

  }

  private static Options options;

  public static Options getOptions() {

    if (options == null) {

      options = new Options();

      Option fileFromOption = Option.builder()
              .option(CommandLineOption.FILE_FROM.name)
              .hasArg()
              .argName(CommandLineOption.FILE_FROM.name)
              .desc("Source GPX file path")
              .required(true)
              .build();
      options.addOption(fileFromOption);

      Option fileToOption = Option.builder()
              .option(CommandLineOption.FILE_TO.name)
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
