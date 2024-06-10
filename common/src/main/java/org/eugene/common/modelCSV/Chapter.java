package org.eugene.common.modelCSV;

import java.io.Serializable;


public class Chapter implements Serializable { // Класс для chapter

    private String name; //Поле не может быть null, Строка не может быть пустой
    private String parentLegion;
    private Integer marinesCount; //Поле может быть null, Значение поля должно быть больше 0, Максимальное значение поля: 1000

    public Chapter(String name, String parentLegion, Integer marinesCount) {
        this.name = name;
        this.parentLegion = parentLegion;
        this.marinesCount = marinesCount;
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "name='" + name + '\'' +
                ", parentLegion='" + parentLegion + '\'' +
                ", marinesCount=" + marinesCount +
                '}';
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getParentLegion() {
        return parentLegion;
    }

    private void setParentLegion(String pl) {
        parentLegion = pl;
    }

    public int getMarinesCount() {
        return marinesCount;
    }

    private void setMarinesCount(int mc) {

        marinesCount = mc;
    }

    public void setMarinesCount(Integer marinesCount) {
        this.marinesCount = marinesCount;
    }
}