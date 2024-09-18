package com.github.duryang.penguintype;

import com.github.duryang.penguintype.formatter.colored.ColoredFormatter;
import com.github.duryang.penguintype.formatter.SessionFormatter;
import com.github.duryang.penguintype.state.Session;
import org.apache.commons.cli.ParseException;
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
        // TODO: for some reason this does not run when interrupted with ctrl + c
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.print("\u001B[?25h");
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
        }

        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {

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
