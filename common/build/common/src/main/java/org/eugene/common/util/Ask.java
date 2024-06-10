package org.eugene.common.util;


import org.eugene.common.modelCSV.Chapter;
import org.eugene.common.modelCSV.Coordinates;
import org.eugene.common.modelCSV.SpaceMarine;

public interface Ask {
    default SpaceMarine askMovie(Asker asker) {
        return new SpaceMarine(
                asker.askName(),
                new Coordinates(asker.askX(), asker.askY()),
                asker.askLoyal(),
                asker.askHealth(),
                asker.askHeartCount(),
                asker.askMeleeWeapon(),
                new Chapter(
                        asker.askChapterName(),
                        asker.askParentLegion(),
                        asker.askMarinesCount()
                )
        );
    }
}
