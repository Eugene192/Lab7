package org.eugene.common.util;

import org.eugene.common.exceptions.ConversionException;
import org.eugene.common.exceptions.ValidationException;
import org.eugene.common.modelCSV.MeleeWeapon;


public class Validator {
    public static <T extends Number & Comparable<T>> void isAboveZero(T x) throws ValidationException {
        if (x.doubleValue() <= 0) {
            throw new ValidationException("This number less than 0");
        }
    }

    public static MeleeWeapon convertMeleeWeaponFromString(String bfr) {
        if (bfr.isEmpty()) {
            return null;
        } else {
            try {
                return MeleeWeapon.valueOf(bfr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ConversionException("No such genre");
            }
        }
    }

    public static double convertXFromString(String bfr) {
        try {
            return Double.parseDouble(bfr);
        } catch (NumberFormatException e) {
            throw new ConversionException("This is not a number");
        }
    }

    public static float convertYFromString(String bfr) {
        try {
            return Float.parseFloat(bfr);
        } catch (NumberFormatException e) {
            throw new ConversionException("This is not a number");
        }
    }

}
