package ru.otus.java.basic.http.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private int port;
    private Dispatcher dispatcher;
    private ExecutorService executor;
    public static final Logger logger = LogManager.getLogger(HttpServer.class);

    public HttpServer(int port) {
        this.port = port;
        this.dispatcher = new Dispatcher();
        this.executor = Executors.newFixedThreadPool(12);
    }

    public void start() {

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Server listening on port " + port);
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    executor.submit(() -> new HandleRequest(socket, dispatcher));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            logger.error("catch Exception: " + e);
            e.printStackTrace();
        } finally {
            logger.warn("execute finally executor shutdown");
            executor.shutdown();
        }
    }
}
