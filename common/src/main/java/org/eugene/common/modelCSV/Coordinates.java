package org.eugene.common.modelCSV;

import java.io.Serializable;


public class Coordinates implements Serializable { // Класс для координат
    private double x;
    private float y; //Поле не может быть null

    public Coordinates(double x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
