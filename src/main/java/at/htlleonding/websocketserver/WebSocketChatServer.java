package at.htlleonding.websocketserver;

import org.glassfish.tyrus.server.Server;

/**
 * @author Sebastian S.
 * */

public class WebSocketChatServer {
    private static final String URL = "localhost";
    private static final String ROOT_PATH = "/websockets";
    private static final int PORT = 8025;
    public static void main(String[] args) {
        Server server = new Server(
                WebSocketChatServer.URL,
                WebSocketChatServer.PORT,
                WebSocketChatServer.ROOT_PATH,
                ChatEndpoint.class);
        try {
            server.start();
            System.out.println("Server is listening at ws://" + URL + ":" + PORT);
            System.out.println("Press enter to stop the server...");
            System.in.read();
            server.stop();
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            server.stop();
        }
    }
}
