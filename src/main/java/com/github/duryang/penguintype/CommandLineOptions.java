package com.github.duryang.penguintype;

import lombok.Getter;
import org.apache.commons.cli.*;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class CommandLineOptions {

    @Getter
    private static String filePath = null;

    @Getter
    private static int wordCount = 30;

    @Getter
    private static Pattern pattern = null;

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

        Option patternOption = Option.builder("p")
                .longOpt("pattern")
                .hasArg()
                .argName("\"regex\"")
                .desc("Get words matching the regex pattern")
                .build();

        var options = new Options();

        options.addOption(helpOption);
        options.addOption(fileOption);
        options.addOption(wordCountOption);
        options.addOption(patternOption);

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
                if (wordCount < 1) {
                    throw new IllegalArgumentException("Word count must be positive");
                }
            }
            if (cmd.hasOption(patternOption)) {
                String regex = cmd.getOptionValue(patternOption);
                try {
                    pattern = Pattern.compile(regex);
                } catch (PatternSyntaxException e) {
                    throw new IllegalArgumentException("Invalid regex: " + regex);
                }
            }

        } catch (ParseException|IllegalArgumentException e) {
            System.out.println("Invalid options");
            helpFormatter.printHelp("penguintype", options);

            throw new ParseException(e);
        }

        return true;
    }
}
