package example.save2;

import example.save2.exceptions.CommandLineParameterException;
import example.save2.exceptions.ExceptionUtils;
import example.save2.xml.XmlManager;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;

import static example.save2.CommandLineOptions.CommandLineOption.*;

public class MainApplication {

    public static void main(String[] args) {

        CommandLineParser parser = new DefaultParser();

        try {

            CommandLine cmd = parser.parse(CommandLineOptions.getOptions(), args);

            validateParameters(cmd);

            final String inputPathString = cmd.getOptionValue(FILE_FROM.getName());
            final String outputPathString = cmd.getOptionValue(FILE_TO.getName());

            XmlManager xmlManager = new XmlManager(inputPathString, outputPathString);
            xmlManager.copyHrValues();

        } catch (Exception e) {
            System.out.println(ExceptionUtils.stackTraceToString(e));
        }

    }

    private static void validateParameters(CommandLine cmd) {

        if (!cmd.hasOption("fileFrom")) {
            throw new CommandLineParameterException("Необходимо указать параметр fileFrom");
        }

        if (!cmd.hasOption("fileTo")) {
            throw new CommandLineParameterException("Необходимо указать параметр fileTo");
        }

    }

}
