import java.util.Arrays;

class Battlefield {
    String[][] field;
    final int fieldSize;

    Battlefield(int fieldSize) {
        this.fieldSize = fieldSize;
        this.field = new String[this.fieldSize][this.fieldSize];
    }

    void placeShipOnBattlefield(int row1, int column1, int row2, int column2) {
        if (row1 == row2) {                                 // horizontal ship
            int startColumn = Math.min(column1, column2);
            int stopColumn = Math.max(column1, column2);

            for (int column = startColumn; column <= stopColumn; column++) {
                this.field[row1][column] = "O";
            }
        } else {
            int startRow = Math.min(row1, row2);
            int stopRow = Math.max(row1, row2);

            for (int row = startRow; row <= stopRow; row++) {
                this.field[row][column1] = "O";
            }
        }
    }

    boolean areShipCoordinatesCorrect(int row1, int column1, int row2, int column2) {
        if (row1 == row2) {                                 // horizontal ship
            int startColumn = Math.min(column1, column2);
            int stopColumn = Math.max(column1, column2);

            for (int column = startColumn; column <= stopColumn; column++) {
                if (!areNeighborsEmpty(row1, column)) {
                    return false;
                }
            }
            return true;
        } else {                                            // vertical ship
            int startRow = Math.min(row1, row2);
            int stopRow = Math.max(row1, row2);

            for (int row = startRow; row <= stopRow; row++) {
                if (!areNeighborsEmpty(row, column1)) {
                    return false;
                }
            }
            return true;
        }
    }

    boolean areNeighborsEmpty(int row, int column) {            // check if input coordinates and neighbors are empty
        if(row == 0 && column == 0){
            return isLeftTopCornerEmpty(row, column);
        } else if (row == 0 && column == this.fieldSize-1) {
            return isRightTopCornerEmpty(row, column);
        } else if (row == this.fieldSize-1 && column == 0) {
            return isLeftBottomCornerEmpty(row, column);
        } else if (row == this.fieldSize-1 && column == this.fieldSize-1) {
            return isRightBottomCornerEmpty(row, column);
        } else if (row == 0) {
            return isTopRowEmpty(row, column);
        } else if (row == this.fieldSize-1) {
            return isBottomRowEmpty(row, column);
        } else if (column == 0) {
            return isLeftColumnEmpty(row, column);
        } else if (column == this.fieldSize-1) {
            return isRightColumnEmpty(row, column);
        } else {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (!this.field[row-i][column-j].equals("~")) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    boolean isLeftTopCornerEmpty(int row, int column) {
        return this.field[row][column].equals("~") && this.field[row][column+1].equals("~") &&
                this.field[row+1][column].equals("~") && this.field[row+1][column+1].equals("~");
    }

    boolean isRightTopCornerEmpty(int row, int column) {
        return this.field[row][column].equals("~") && this.field[row][column-1].equals("~") &&
                this.field[row+1][column].equals("~") && this.field[row+1][column-1].equals("~");
    }

    boolean isLeftBottomCornerEmpty(int row, int column) {
        return this.field[row][column].equals("~") && this.field[row][column+1].equals("~") &&
                this.field[row-1][column].equals("~") && this.field[row-1][column+1].equals("~");
    }

    boolean isRightBottomCornerEmpty(int row, int column) {
        return this.field[row][column].equals("~") && this.field[row][column-1].equals("~") &&
                this.field[row-1][column].equals("~") && this.field[row-1][column-1].equals("~");
    }

    boolean isTopRowEmpty(int row, int column) {
        return this.field[row][column].equals("~") && this.field[row][column-1].equals("~") &&
                this.field[row][column+1].equals("~") && this.field[row+1][column].equals("~") &&
                this.field[row+1][column-1].equals("~") && this.field[row+1][column+1].equals("~");
    }

    boolean isBottomRowEmpty(int row, int column) {
        return this.field[row][column].equals("~") && this.field[row][column-1].equals("~") &&
                this.field[row][column+1].equals("~") && this.field[row-1][column].equals("~") &&
                this.field[row-1][column-1].equals("~") && this.field[row-1][column+1].equals("~");
    }

    boolean isLeftColumnEmpty(int row, int column) {
        return this.field[row][column].equals("~") && this.field[row][column+1].equals("~") &&
                this.field[row-1][column].equals("~") && this.field[row-1][column+1].equals("~") &&
                this.field[row+1][column].equals("~") && this.field[row+1][column+1].equals("~");
    }

    boolean isRightColumnEmpty(int row, int column) {
        return this.field[row][column].equals("~") && this.field[row][column-1].equals("~") &&
                this.field[row-1][column].equals("~") && this.field[row-1][column-1].equals("~") &&
                this.field[row+1][column].equals("~") && this.field[row+1][column-1].equals("~");
    }

    void initializeBattlefield() {
        for (String[] row: this.field) {
            Arrays.fill(row, "~");
        }
    }

    void printBattlefield() {
        int[] first_row = new int[this.fieldSize];
        Arrays.setAll(first_row, i -> i + 1);
        char first_column = 'A';

        for (int i = 0; i < this.fieldSize + 1; i++) {
            for (int j = 0; j < this.fieldSize + 1; j++) {
                if (i == 0 && j == 0) {                             // top left corner - white space
                    System.out.print("  ");
                } else if (i == 0) {                                // first row
                    System.out.print(first_row[j - 1] + " ");
                } else if (j == 0) {                                // first column
                    System.out.print(Character.toString(first_column + i - 1) + " ");
                } else {                                            // battlefield
                    System.out.print(this.field[i - 1][j - 1] + " ");
                }
            }
            System.out.println();
        }
    }
}
