package org.eugene.common.modelCSV;

import java.io.Serializable;
import java.time.LocalDateTime;



 //Класс коллекции, которую мы храним в программе


public class SpaceMarine implements Serializable, Comparable<SpaceMarine> { //разбор на байты и сбор обратно
    private final LocalDateTime creationDate = LocalDateTime.now(); //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private Boolean loyal;

    private int health; //Поле может быть null, Значение поля должно быть больше 0
    private int heartCount; //Поле не может быть null, Значение поля должно быть больше 0, Максимальное значение поля: 3
    private MeleeWeapon meleeWeapon; //Поле не может быть null
    private Chapter chapter; //Поле может быть null


    public SpaceMarine(Long id, String name, Coordinates coordinates, Boolean loyal, int health, int heartCount, MeleeWeapon meleeWeapon, Chapter chapter) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.loyal = loyal;
        this.health = health;
        this.heartCount = heartCount;
        this.meleeWeapon = meleeWeapon;
        this.chapter = chapter;
    }

    public SpaceMarine(
            String name,
            Coordinates coordinates,
            Boolean loyal,
            int health,
            int heartCount,
            MeleeWeapon meleeWeapon,
            Chapter chapter
    ) {
        this.name = name;
        this.coordinates = coordinates;
        this.loyal = loyal;
        this.health = health;
        this.heartCount = heartCount;
        this.meleeWeapon = meleeWeapon;
        this.chapter = chapter;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long i) {
        this.id = Identifiers.generate();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return this.coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public boolean getLoyal() {
        return this.loyal;
    }

    public void setLoyal(Boolean loyal) {
        this.loyal = loyal;
    }

    public LocalDateTime getCreationDate() {
        return this.creationDate;
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHeartCount() {
        return this.heartCount;
    }

    public void setHeartCount(int heartCount) {
        this.heartCount = heartCount;
    }

    public MeleeWeapon getMeleeWeapon() {
        return this.meleeWeapon;
    }

    public void setMeleeWeapon(MeleeWeapon meleeWeapon) {
        this.meleeWeapon = meleeWeapon;
    }

    public Chapter getChapter() {
        return this.chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    @Override
    public String toString() {
        return "SpaceMarine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", loyal=" + loyal +
                ", health=" + health +
                ", heartCount=" + heartCount +
                ", meleeWeapon=" + meleeWeapon +
                ", chapter=" + chapter +
                '}';
    }

    @Override
    public int compareTo(SpaceMarine o) {
        return this.id.compareTo(Long.valueOf(o.getId()));
    }
}

