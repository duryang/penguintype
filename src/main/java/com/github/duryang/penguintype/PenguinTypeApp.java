package com.github.duryang.penguintype;

import com.github.duryang.penguintype.formatter.colored.ColoredFormatter;
import com.github.duryang.penguintype.formatter.SessionFormatter;
import com.github.duryang.penguintype.state.Session;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.IOException;

public class PenguinTypeApp {
    public static void main(String[] args) {
        // TODO: parse command line args. words file name/special name, word count

        // TODO: display loading screen
        // TODO: load the words from the file

        Session session;
        try {
            session = SessionFactory.fromSource("/home/gor/words.txt");
        } catch (IOException e) {
            System.out.println("Could not load from the file...");
            return;
        }


        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {

            terminal.enterRawMode();

            SessionFormatter formatter = new ColoredFormatter(session, terminal.getWidth());

            while (true) {
                // refresh by basically doing "Ctrl + L"
                terminal.puts(InfoCmp.Capability.clear_screen);

                terminal.writer().write(formatter.format());
                terminal.flush();

                int input = terminal.reader().read();
                // TODO: validate and decide action

                if (input == 32) {
                    boolean available = session.next();
                    if (!available) {
                        System.out.println("FINITO");
                        break;
                    }
                } else {
                    session.type((char) input);
                }


            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
