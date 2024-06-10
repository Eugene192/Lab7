package org.eugene.common.util;


import org.eugene.common.exceptions.ConversionException;
import org.eugene.common.modelCSV.MeleeWeapon;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

public class Asker {
    BufferedReader reader;

    public Asker(BufferedReader reader) {
        this.reader = reader;
    }

    public String askName() {
        String bfr;
        while (true) {
            System.out.println("input (String, notNull) SpaceMarine name: ");
            try {
                bfr = reader.readLine();

                return bfr;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public double askX() {
        String bfr;
        double x = 0;

        System.out.println("input (double) x: ");
        try {
            bfr = reader.readLine();

            if (bfr.isEmpty()) {
                return 0;
            } else {
                x = Validator.convertXFromString(bfr);
                return x;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public float askY() {
        String bfr;
        while (true) {
            System.out.println("input (float, notNull) y: ");
            try {
                bfr = reader.readLine();
                if (bfr.isEmpty()) {
                    System.out.println("Please enter a number");
                    continue;
                }
                return Validator.convertYFromString(bfr);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public Integer askOscarsCount() {
        String bfr;

        while (true) {
            System.out.println("input (Integer, >0) oscarsCount: ");
            try {
                bfr = reader.readLine();

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ConversionException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public int askTotalBoxOffice() {
        String bfr;

        while (true) {
            System.out.println("input (int, >0) totalBoxOffice: ");
            try {
                bfr = reader.readLine();

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ConversionException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public Long askUsaBoxOffice() {
        String bfr;

        while (true) {
            System.out.println("input (Long, >0) usaBoxOffice: ");
            try {
                bfr = reader.readLine();

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ConversionException e) {
                System.err.println(e.getMessage());
            }
        }
    }


    public String askOperatorName() {
        String bfr;

        while (true) {
            System.out.println("input (String, notNull) operator name: ");
            try {
                bfr = reader.readLine();

                return bfr;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public float askWeight() {
        String bfr;

        while (true) {
            System.out.println("input (float, >0) weight: ");
            try {
                bfr = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ConversionException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public LocalDateTime askBirthdate() {
        String bfr;

        while (true) {
            System.out.println("input (Date, notNull) birthdate: ");
            try {
                bfr = reader.readLine();

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ConversionException e) {
                System.err.println(e.getMessage());
            }
        }

    }


    public Boolean askLoyal() {
        while (true) {
            System.out.println("input (Boolean, notNull) loyal: ");
            try {
                var bfr = reader.readLine();
                if (bfr.equals("true")) {
                    return true;
                } else if (bfr.equals("false")) {
                    return false;
                } else {
                    System.err.println("Please enter true or false");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ConversionException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public int askHealth() {
        while (true) {
            System.out.println("input (int, notNull, >0) health: ");
            try {
                var bfr = reader.readLine();
                var h = Integer.parseInt(bfr);
                if (h <= 0) {
                    System.err.println("Please enter a number greater than 0");
                    continue;
                }
                return h;
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ConversionException e) {
                System.err.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.err.println("Please enter a number");
            }
        }
    }

    public int askHeartCount() {
        while (true) {
            System.out.println("input (int, notNull, 0<heartCount<=3) heartCount: ");
            try {
                var bfr = reader.readLine();
                var h = Integer.parseInt(bfr);
                if (h <= 0 || h > 3) {
                    System.out.println("Please enter a number between 0 and 3");
                    continue;
                }
                return h;
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ConversionException e) {
                System.err.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.err.println("Please enter a number");
            }
        }
    }

    public MeleeWeapon askMeleeWeapon() {
        String bfr;

        System.out.println("Available genres: " + Arrays.toString(MeleeWeapon.values()));
        while (true) {
            System.out.println("input (MovieGenre) genre: ");
            try {
                bfr = reader.readLine();
                return Validator.convertMeleeWeaponFromString(bfr);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ConversionException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public String askChapterName() {
        String bfr;
        while (true) {
            System.out.println("input (String, notNull) chapter name: ");
            try {
                bfr = reader.readLine();
                return bfr;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String askParentLegion() {
        System.out.println("input (String, notNull) parentLegion: ");
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Integer askMarinesCount() {
        while (true) {
            System.out.println("input (int, notNull, 0<marinesCount<=1000) marinesCount: ");
            try {
                var bfr = reader.readLine();
                var mc = Integer.parseInt(bfr);
                if (mc <= 0 || mc > 3) {
                    System.out.println("Please enter a number between 0 and 1000");
                    continue;
                }
                return mc;
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ConversionException e) {
                System.err.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.err.println("Please enter a number");
            }
        }
    }
}
