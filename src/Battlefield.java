import java.util.Arrays;
import java.util.Scanner;

class Battlefield {
    private static final String FOG = "~";
    private static final String OWN_SHIP = "O";
    private static final String HITTED = "X";
    private static final String MISSED = "M";
    private final String[][] field;
    private final int fieldSize;
    private final String[] shipsNames;
    private final int[] shipsSizes;


    public Battlefield(int fieldSize, String[] shipsNames, int[] shipsSizes) {
        this.fieldSize = fieldSize;
        this.field = new String[fieldSize][fieldSize];
        this.shipsNames = shipsNames;
        this.shipsSizes = shipsSizes;
    }

    public void StartGame() {
        initializeBattlefield();
        printBattlefield();
        createShips();
        startShooting();
    }

    private void startShooting() {
        boolean hit = false;

        System.out.println("The game starts!");

        Battlefield emptyBattlefield = new Battlefield(fieldSize, shipsNames, shipsSizes);
        emptyBattlefield.initializeBattlefield();
        emptyBattlefield.printBattlefield();

        System.out.println("Take a shot!");

        Scanner scan = new Scanner(System.in);
        while (!hit) {
            String word = scan.next();
            int row = Character.getNumericValue(word.charAt(0)) - 10;
            int column = Integer.parseInt(word.replaceAll("[\\D]", "")) - 1;

            if (row >= 0 && row < fieldSize && column >= 0 && column < fieldSize) {
                hit = isShipHit(row, column, emptyBattlefield);
            } else {
                System.out.println("Error! You entered the wrong coordinates! Try again:");
            }
        }
        printBattlefield();
    }

    private boolean isShipHit(int row, int column, Battlefield emptyBattlefield) {
        boolean hit = field[row][column].equals(OWN_SHIP);

        if (hit) {
            emptyBattlefield.field[row][column] = HITTED;
            field[row][column] = HITTED;
            emptyBattlefield.printBattlefield();
            System.out.println("You hit a ship!");
            return true;
        } else {
            emptyBattlefield.field[row][column] = MISSED;
            field[row][column] = MISSED;
            emptyBattlefield.printBattlefield();
            System.out.println("You missed!");
            return true;
        }
    }

    private void printBattlefield() {
        int[] first_row = new int[fieldSize];
        Arrays.setAll(first_row, i -> i + 1);
        char first_column = 'A';

        System.out.println();
        for (int i = 0; i < fieldSize + 1; i++) {
            for (int j = 0; j < fieldSize + 1; j++) {
                if (i == 0 && j == 0) {
                    System.out.print("  ");
                } else if (i == 0) {
                    System.out.print(first_row[j - 1] + " ");
                } else if (j == 0) {
                    System.out.print(Character.toString(first_column + i - 1) + " ");
                } else {
                    System.out.print(field[i - 1][j - 1] + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private void initializeBattlefield() {
        for (String[] row : field) {
            Arrays.fill(row, FOG);
        }
    }

    private void placeShipOnBattlefield(int row1, int column1, int row2, int column2) {
        if (row1 == row2) {
            int startColumn = Math.min(column1, column2);
            int stopColumn = Math.max(column1, column2);

            for (int column = startColumn; column <= stopColumn; column++) {
                field[row1][column] = OWN_SHIP;
            }
        } else {
            int startRow = Math.min(row1, row2);
            int stopRow = Math.max(row1, row2);

            for (int row = startRow; row <= stopRow; row++) {
                field[row][column1] = OWN_SHIP;
            }
        }
    }

    private boolean areShipCoordinatesCorrect(int row1, int column1, int row2, int column2) {
        if (row1 == row2) {
            int startColumn = Math.min(column1, column2);
            int stopColumn = Math.max(column1, column2);

            for (int column = startColumn; column <= stopColumn; column++) {
                if (!areNeighborsEmpty(row1, column)) {
                    System.out.println("Error! You placed it too close to another one. Try again:");
                    return false;
                }
            }
            return true;
        }

        int startRow = Math.min(row1, row2);
        int stopRow = Math.max(row1, row2);

        for (int row = startRow; row <= stopRow; row++) {
            if (!areNeighborsEmpty(row, column1)) {
                System.out.println("Error! You placed it too close to another one. Try again:");
                return false;
            }
        }
        return true;
    }

    private boolean areNeighborsEmpty(int row, int column) {
        if (row == 0 && column == 0) {
            return isLeftTopCornerEmpty(row, column);
        } else if (row == 0 && column == fieldSize - 1) {
            return isRightTopCornerEmpty(row, column);
        } else if (row == fieldSize - 1 && column == 0) {
            return isLeftBottomCornerEmpty(row, column);
        } else if (row == fieldSize - 1 && column == fieldSize - 1) {
            return isRightBottomCornerEmpty(row, column);
        } else if (row == 0) {
            return isTopRowEmpty(row, column);
        } else if (row == fieldSize - 1) {
            return isBottomRowEmpty(row, column);
        } else if (column == 0) {
            return isLeftColumnEmpty(row, column);
        } else if (column == fieldSize - 1) {
            return isRightColumnEmpty(row, column);
        }

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!this.field[row - i][column - j].equals(FOG)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void createShips() {
        /*
         * Consider renaming "nextShip" boolean variable.
         * Please take a quick look at this article:
         * https://dev.to/michi/tips-on-naming-boolean-variables-cleaner-code-35ig
         */
        for (int i = 0; i < shipsNames.length; i++) {
            boolean isNextShip = false;
            System.out.printf("Enter the coordinates of the %s (%d cells)\n",
                    shipsNames[i], shipsSizes[i]);
            System.out.println();

            while (!isNextShip) {
                Scanner scan = new Scanner(System.in);
                String word = scan.next();
                int row1 = Character.getNumericValue(word.charAt(0)) - 10;
                int column1 = Integer.parseInt(word.replaceAll("[\\D]", "")) - 1;

                word = scan.next();
                int row2 = Character.getNumericValue(word.charAt(0)) - 10;
                int column2 = Integer.parseInt(word.replaceAll("[\\D]", "")) - 1;

                isNextShip = isShipPlaceableAtCoordinates(row1, column1, row2, column2, shipsSizes[i]);

                if (isNextShip) {
                    placeShipOnBattlefield(row1, column1, row2, column2);
                }
            }
            printBattlefield();
        }
    }

    private boolean isShipPlaceableAtCoordinates(int row1, int column1, int row2, int column2, int shipSize) {
        /*
         * No need for multiple "if" and "else if" statements.
         * You can make this work by the way shown below
         * because of lazy evaluation. Please take a look at:
         * https://en.wikipedia.org/wiki/Lazy_evaluation
         */
        return !isOutOfBoundaries(row1, column1, row2, column2)
                && !isShipDirectionWrong(row1, column1, row2, column2)
                && !isShipSizeWrong(row1, column1, row2, column2, shipSize)
                && areShipCoordinatesCorrect(row1, column1, row2, column2);
    }

    private boolean isOutOfBoundaries(int row1, int column1, int row2, int column2) {
        boolean isOutOfBoundaries = row1 < 0
                || row2 < 0
                || column1 < 0
                || column2 < 0
                || row1 > fieldSize - 1
                || row2 > fieldSize - 1
                || column1 > fieldSize - 1
                || column2 > fieldSize - 1;
        if(isOutOfBoundaries) {
            System.out.println("Error! Wrong coordinates! Try again:");
        }
        return isOutOfBoundaries;
    }

    private boolean isShipDirectionWrong(int row1, int column1, int row2, int column2) {
        boolean isShipDirectionWrong = row1 != row2 && column1 != column2;
        if(isShipDirectionWrong) {
            System.out.println("Error! Wrong ship location! Try again:");
        }
        return isShipDirectionWrong;
    }

    private boolean isShipSizeWrong(int row1, int column1, int row2, int column2, int shipSize) {
        boolean isShipSizeWrong = Math.abs(column2 - column1) + 1 != shipSize && Math.abs(row1 - row2) + 1 != shipSize;
        if(isShipSizeWrong) {
            System.out.println("Error! Wrong length of the Submarine! Try again:");
        }
        return isShipSizeWrong;
    }

    private boolean isLeftTopCornerEmpty(int row, int column) {
        return field[row][column].equals(FOG)
                && field[row][column + 1].equals(FOG)
                && field[row + 1][column].equals(FOG)
                && field[row + 1][column + 1].equals(FOG);
    }

    private boolean isRightTopCornerEmpty(int row, int column) {
        return field[row][column].equals(FOG)
                && field[row][column - 1].equals(FOG)
                && field[row + 1][column].equals(FOG)
                && field[row + 1][column - 1].equals(FOG);
    }

    private boolean isLeftBottomCornerEmpty(int row, int column) {
        return field[row][column].equals(FOG)
                && field[row][column + 1].equals(FOG)
                && field[row - 1][column].equals(FOG)
                && field[row - 1][column + 1].equals(FOG);
    }

    private boolean isRightBottomCornerEmpty(int row, int column) {
        return field[row][column].equals(FOG)
                && field[row][column - 1].equals(FOG)
                && field[row - 1][column].equals(FOG)
                && field[row - 1][column - 1].equals(FOG);
    }

    private boolean isTopRowEmpty(int row, int column) {
        return field[row][column].equals(FOG)
                && field[row][column - 1].equals(FOG)
                && field[row][column + 1].equals(FOG)
                && field[row + 1][column].equals(FOG)
                && field[row + 1][column - 1].equals(FOG)
                && field[row + 1][column + 1].equals(FOG);
    }

    private boolean isBottomRowEmpty(int row, int column) {
        return field[row][column].equals(FOG)
                && field[row][column - 1].equals(FOG)
                && field[row][column + 1].equals(FOG)
                && field[row - 1][column].equals(FOG)
                && field[row - 1][column - 1].equals(FOG)
                && field[row - 1][column + 1].equals(FOG);
    }

    private boolean isLeftColumnEmpty(int row, int column) {
        return field[row][column].equals(FOG)
                && field[row][column + 1].equals(FOG)
                && field[row - 1][column].equals(FOG)
                && field[row - 1][column + 1].equals(FOG)
                && field[row + 1][column].equals(FOG)
                && field[row + 1][column + 1].equals(FOG);
    }

    private boolean isRightColumnEmpty(int row, int column) {
        return field[row][column].equals(FOG)
                && field[row][column - 1].equals(FOG)
                && field[row - 1][column].equals(FOG)
                && field[row - 1][column - 1].equals(FOG)
                && field[row + 1][column].equals(FOG)
                && field[row + 1][column - 1].equals(FOG);
    }
}