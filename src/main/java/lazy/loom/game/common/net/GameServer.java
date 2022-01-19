package lazy.loom.game.common.net;

import com.github.simplenet.Server;
import com.github.simplenet.packet.Packet;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static lazy.loom.engine.util.Assert.tryCatch;

public class GameServer {

    public static Server server;
    private static JFrame frame;
    private static JTextArea area;
    private static PrintStream stream;

    private static DateFormat format = new SimpleDateFormat("HH:mm:ss");

    // FIXME: 07/01/2022 Use threads

    public static void main(String[] args) throws IOException {
        server = new Server();
        simpleWindow();
        server.onConnect(client -> {
            log(client + " has connected.");
            client.postDisconnect(()-> log(client + " has disconnected."));
            server.queueAndFlushToAllExcept(Packet.builder().putBoolean(true));
        });

        log("Server is running");

        server.bind("localhost", 6969);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                tryCatch(server::close);
            }
        });
    }

    private static void simpleWindow() {
        frame = new JFrame("Server");
        area = new JTextArea();
        area.setEditable(false);
        stream = new PrintStream(new CustomOutputStream(area));

        JTextField field = new JTextField();
        field.setSize(100, 10);

        frame.add(area);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        frame.setVisible(true);
    }

    public static class CustomOutputStream extends OutputStream {
        private JTextArea textArea;

        public CustomOutputStream(JTextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int b) throws IOException {
            // redirects data to the text area
            textArea.append(String.valueOf((char) b));
            // scrolls the text area to the end of data
            textArea.setCaretPosition(textArea.getDocument().getLength());
            // keeps the textArea up to date
            textArea.update(textArea.getGraphics());
        }
    }

    public static void log(String msg) {
        var forMsg = String.format("[%s][%s]: %s", format.format(new Date(System.currentTimeMillis())), Thread.currentThread().getName(), msg);
        stream.println(forMsg);
    }
}
