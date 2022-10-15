import java.util.Arrays;
import java.util.Scanner;

class Battlefield {
    /*
    * "~" sign appears in a lot of places. You should
    * use it as a variable. What if you wanted to change
    * this sign to something else? Then you'd need to
    * replace all occurrences.
    */
    private static final String FOG = "~";
    private final String[][] field;
    private final int fieldSize;
    private final String[] shipsNames;
    private final int[] shipsSizes;

    /*
    * I believe that the only need to use the "this." qualifier
    * is when another variable within the current scope shares
    * the same name, and you want to refer to the instance member
    * like in constructors or setters.
    */
    public Battlefield(int fieldSize, String[] shipsNames, int[] shipsSizes) {
        this.fieldSize = fieldSize;
        /*
        * It doesn't matter if you use "this.fieldSize" or
        * "fieldSize" while initializing "this.field" because
        * their values are exactly the same. In that situation
        * you should go for shorter notation.
        */
        this.field = new String[fieldSize][fieldSize];
        this.shipsNames = shipsNames;
        this.shipsSizes = shipsSizes;
    }

    public void StartGame() {
        initializeBattlefield();
        printBattlefield();
        createShips();
    }

    private void printBattlefield() {
        int[] first_row = new int[fieldSize];
        Arrays.setAll(first_row, i -> i + 1);
        char first_column = 'A';

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
                field[row1][column] = "O";
            }
        } else {
            int startRow = Math.min(row1, row2);
            int stopRow = Math.max(row1, row2);

            for (int row = startRow; row <= stopRow; row++) {
                field[row][column1] = "O";
            }
        }
    }

    private boolean areShipCoordinatesCorrect(int row1, int column1, int row2, int column2) {
        if (row1 == row2) {
            int startColumn = Math.min(column1, column2);
            int stopColumn = Math.max(column1, column2);

            for (int column = startColumn; column <= stopColumn; column++) {
                if (!areNeighborsEmpty(row1, column)) {
                    System.err.println("Error! You placed it too close to another one. Try again:");
                    return false;
                }
            }
            return true;
        }
        /*
         * No need for "else" statement, one of the
         * other statements will return value if satisfied
         */
        int startRow = Math.min(row1, row2);
        int stopRow = Math.max(row1, row2);

        for (int row = startRow; row <= stopRow; row++) {
            if (!areNeighborsEmpty(row, column1)) {
                return false;
            }
        }
        return true;
    }

    /*
    * This method's return value is always inverted
    * when you use it in other methods. Maybe it
    * should be "areNeighboursPresent" and return values
    * inside it shall be inverted.
    */
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
        /*
        * No need for "else" statement, one of the
        * other statements will return value if satisfied
        */

        /*
        * What does this part do? Maybe you should extract it
        * to another method which name would describe the effect
        */
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
            boolean nextShip = false;
            while (!nextShip) {
                System.out.printf("Enter the coordinates of the %s (%d cells)\n",
                        shipsNames[i], shipsSizes[i]);
                Scanner scan = new Scanner(System.in);
                String word = scan.next();
                int row1 = Character.getNumericValue(word.charAt(0)) - 10;
                int column1 = Integer.parseInt(word.replaceAll("[\\D]", "")) - 1;

                word = scan.next();
                int row2 = Character.getNumericValue(word.charAt(0)) - 10;
                int column2 = Integer.parseInt(word.replaceAll("[\\D]", "")) - 1;

                nextShip = isShipPlaceableAtCoordinates(row1, column1, row2, column2, shipsSizes[i]);

                if (nextShip) {
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
            System.err.println("Error! Wrong coordinates! Try again:");
        }
        return isOutOfBoundaries;
    }

    private boolean isShipDirectionWrong(int row1, int column1, int row2, int column2) {
        boolean isShipDirectionWrong = row1 != row2 && column1 != column2;
        if(isShipDirectionWrong) {
            System.err.println("Error! Wrong ship location! Try again:");
        }
        return isShipDirectionWrong;
    }

    private boolean isShipSizeWrong(int row1, int column1, int row2, int column2, int shipSize) {
        boolean isShipSizeWrong = Math.abs(column2 - column1) + 1 != shipSize && Math.abs(row1 - row2) + 1 != shipSize;
        if(isShipSizeWrong) {
            System.err.println("Error! Wrong length of the Submarine! Try again:");
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
