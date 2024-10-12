package com.github.duryang.penguintype;

import com.github.duryang.penguintype.exception.NoMatchingWordsException;
import com.github.duryang.penguintype.formatter.SessionFormatter;
import com.github.duryang.penguintype.formatter.colored.ColoredFormatter;
import com.github.duryang.penguintype.state.Session;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.IOException;

public class PenguinTypeApp {
    public static void main(String[] args) {

        try {
            boolean continueExecution = CommandLineOptions.register(args);
            if (!continueExecution) {
                return;
            }
        } catch (Exception e) {
            return;
        }

        // Bring back the cursor when the app terminates
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\u001B[?25h");
        }));

        Session session;
        try {
            if (CommandLineOptions.getFilePath() == null) {
                session = SessionFactory.fromInternalResource("words.txt");
            } else {
                session = SessionFactory.fromFile(CommandLineOptions.getFilePath());
            }
        } catch (IOException e) {
            System.out.println("Could not load from the file...");
            return;
        } catch (NoMatchingWordsException e) {
            System.out.println("No matching words found...");
            return;
        }

        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {

            // because read() is blocking, and shutdown hook is not executed on SIGINT
            terminal.handle(Terminal.Signal.INT, signal -> System.exit(0));

            terminal.enterRawMode();

            SessionFormatter formatter = new ColoredFormatter(session, terminal.getWidth());

            boolean available = true;
            while (available) {
                // refresh by basically doing "Ctrl + L"
                terminal.puts(InfoCmp.Capability.clear_screen);

                terminal.writer().write(formatter.format());
                terminal.flush();

                int input = terminal.reader().read();
                Action action = InputMapper.toAction(input);

                switch (action) {
                    case TYPE:
                        session.type((char) input);
                        break;
                    case NEXT:
                        available = session.next();
                        break;
                    case UNDO:
                        session.undo();
                        break;
                    default:
                        break;
                }
            }

            System.out.println();
            System.out.println(session.getScoring().stats());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
