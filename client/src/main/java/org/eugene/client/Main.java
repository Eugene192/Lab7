package org.eugene.client;

import org.eugene.common.Environment;

public class Main {
    public static void main(String[] args) {
        new Client(Environment.HOST, Environment.PORT).run();
    }
}
