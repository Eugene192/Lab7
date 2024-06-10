package org.eugene.server;

import org.eugene.common.Environment;

public class Main {
    public static void main(String[] args) {
        new TCPServer(Environment.HOST, Environment.PORT).run();
    }
}
