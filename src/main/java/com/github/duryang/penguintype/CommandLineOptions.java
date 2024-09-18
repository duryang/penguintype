package com.github.duryang.penguintype;

import lombok.Getter;
import org.apache.commons.cli.*;

public class CommandLineOptions {

    @Getter
    private static String filePath = null;

    @Getter
    private static int wordCount = 30;

    /**
     * Parses and registers the command line options.
     *
     * @param args command line args
     * @return true if the application needs to continue, false otherwise
     * @throws ParseException in case of invalid arguments
     */
    public static boolean register(String[] args) throws ParseException {

        Option helpOption = Option.builder("h")
                .longOpt("help")
                .desc("Show this message")
                .build();

        Option fileOption = Option.builder("f")
                .longOpt("file")
                .hasArg()
                .argName("file-path")
                .desc("Load words from file")
                .build();

        Option wordCountOption = Option.builder("n")
                .hasArg()
                .argName("number")
                .desc("Number of words")
                .build();

        var options = new Options();

        options.addOption(helpOption);
        options.addOption(fileOption);
        options.addOption(wordCountOption);

        CommandLineParser parser = new DefaultParser();
        var helpFormatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption(helpOption)) {
                helpFormatter.printHelp("penguintype", options);
                return false;
            }
            if (cmd.hasOption(fileOption)) {
                filePath = cmd.getOptionValue(fileOption);
            }
            if (cmd.hasOption(wordCountOption)) {
                wordCount = Integer.parseInt(cmd.getOptionValue(wordCountOption));
            }

        } catch (ParseException|NumberFormatException e) {
            System.out.println("Invalid options");
            helpFormatter.printHelp("penguintype", options);

            throw new ParseException(e);
        }

        return true;
    }
}
