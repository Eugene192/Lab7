package org.eugene.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eugene.common.exceptions.ValidationException;
import org.eugene.server.managers.ClientHandler;
import org.eugene.server.managers.CollectionManager;
import org.eugene.server.managers.CommandInvoker;
import org.eugene.server.managers.DBManager;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.concurrent.ForkJoinPool;

public class TCPServer {
    private static final Logger logger = LogManager.getLogger(TCPServer.class);
    private final String host;
    private final int port;
    private CommandInvoker commandInvoker;
    private boolean isStopped = false;
    private ForkJoinPool forkJoinPool = new ForkJoinPool();


    public TCPServer(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            var db = new DBManager();
            var collectionManager = new CollectionManager();
            commandInvoker = new CommandInvoker(collectionManager, db);
        } catch (ValidationException e) {
            logger.error("Collection validation failed: " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            logger.error("IOException: " + e.getMessage());
            System.exit(1);
        } catch (SQLException e) {
            logger.error("SQLException: " + e.getMessage());
            System.exit(1);
        }
    }

    public void run() {
        try (var serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress(host, port));
            serverSocketChannel.configureBlocking(false);
            logger.info("Server started: " + serverSocketChannel.getLocalAddress());

            interactive(serverSocketChannel);
        } catch (BindException e) {
            logger.error("BindException: " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            logger.error(e);
            throw new RuntimeException(e);
        } finally {
            logger.info("Server stopped");
        }
    }


    private void interactive(ServerSocketChannel serverSocketChannel) throws IOException {
        while (!isStopped) {
            try (var selector = Selector.open()) {
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

                logger.info("Awaiting client");
                while (!isStopped) {
                    try {
                        selector.selectNow();
                        var selectedKeys = selector.selectedKeys();
                        var keyIterator = selectedKeys.iterator();

                        while (keyIterator.hasNext()) {
                            SelectionKey key = keyIterator.next();
                            if (key.isAcceptable()) {
                                var clientChannel = serverSocketChannel.accept();
                                if (clientChannel != null) {
                                    clientChannel.configureBlocking(false);
                                    clientChannel.register(selector, SelectionKey.OP_READ);
                                    logger.info("Client connected: " + clientChannel.getRemoteAddress());
                                }
                            } else if (key.isReadable()) {
                                var clientChannel = (SocketChannel) key.channel();
                                if (clientChannel != null) {
                                    forkJoinPool.execute(() -> {
                                        new ClientHandler(commandInvoker, clientChannel).run();
                                    });
                                    clientChannel.register(selector, SelectionKey.OP_WRITE); // to prevent multiple threads
                                }
                            }
                            keyIterator.remove();
                        }
                    } catch (IOException e) {
                        logger.error(e);
                        throw e;
                    }
                }
            } catch (IOException | RuntimeException e) {
                logger.error(e);
            } finally {
                stop();
                logger.info("Selector closed");
            }
        }
    }

    public void stop() {
        isStopped = true;
    }
}