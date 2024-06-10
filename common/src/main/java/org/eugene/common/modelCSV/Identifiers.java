package org.eugene.common.modelCSV;

import java.util.ArrayList;
import java.util.List;

public class Identifiers { // Класс для индифицирования ID
    private static final List<Long> listID = new ArrayList<>();

    public static boolean have(long id) {
        return listID.contains(id);
    }

    public static Long generate() {
        if (listID.isEmpty()) {
            listID.add(1L);
            return 1L;
        }
        listID.add(listID.get(listID.size() - 1) + 1);
        return listID.get(listID.size() - 1);
    }

    public static boolean delete(Long id) {
        return listID.remove(id);
    }
}
