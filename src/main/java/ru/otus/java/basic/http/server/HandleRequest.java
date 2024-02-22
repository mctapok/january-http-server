package ru.otus.java.basic.http.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

public class HandleRequest {
    public static final Logger logger = LogManager.getLogger(HandleRequest.class);

    public HandleRequest(Socket socket, Dispatcher dispatcher) {
        try {
            byte[] buffer = new byte[8192];
            int n = socket.getInputStream().read(buffer);
            logger.info("input stream value " + n);
            String rawRequest = new String(buffer, 0, n);
            HttpRequest httpRequest = new HttpRequest(rawRequest);
            dispatcher.execute(httpRequest, socket.getOutputStream());
        } catch (IOException e) {
            logger.error("catch IOException: " + e);
            e.printStackTrace();
        } finally {
            try {
                if (socket != null)
                    socket.close();
                logger.info("socket close");
            } catch (IOException e) {
                logger.error("catch IOException on closing socket " + e);
                e.printStackTrace();
            }
        }
    }
}
