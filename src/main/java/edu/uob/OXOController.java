package edu.uob;
import edu.uob.OXOMoveException.*;

public class OXOController {
    OXOModel gameModel;

    public OXOController(OXOModel model) {
        gameModel = model;
    }

    public void handleIncomingCommand(String command) throws OXOMoveException {

        if (command.length() != 2) {
            throw new InvalidIdentifierLengthException(command.length());
        }

        if (!(command.charAt(0) >= 'A' && command.charAt(0) <= 'Z') && !(command.charAt(0) >= 'a' && command.charAt(0) <= 'z')) {
            System.out.println("This is not a valid first character");
            throw new InvalidIdentifierCharacterException(RowOrColumn.ROW, command.charAt(0));
        }

        if (!(command.charAt(1) >= '0' && command.charAt(1) <= '9')) {
            System.out.println("This is not a valid second character");
            throw new InvalidIdentifierCharacterException(RowOrColumn.COLUMN, command.charAt(1));
        }

        int rowIndex = command.charAt(0) >= 'a' ? command.charAt(0) - 'a' : command.charAt(0) - 'A';
        int colIndex = command.charAt(1) - '1';

        if (rowIndex < 0 || rowIndex > gameModel.getNumberOfRows() - 1) {
            throw new OutsideCellRangeException(RowOrColumn.ROW, rowIndex);
        }
        if (colIndex < 0 || colIndex > gameModel.getNumberOfColumns() - 1) {
            throw new OutsideCellRangeException(RowOrColumn.COLUMN, colIndex+1);
        }

        if (gameModel.getCellOwner(rowIndex, colIndex) != null) {
            throw new CellAlreadyTakenException(rowIndex, colIndex);
        }

        int current_player = gameModel.getCurrentPlayerNumber();

        if (gameModel.getWinner() == null) {
            gameModel.setCellOwner(rowIndex, colIndex, gameModel.getPlayerByNumber(current_player));
            gameModel.setCurrentPlayerNumber((gameModel.getCurrentPlayerNumber() + 1) % gameModel.getNumberOfPlayers());
                // check for a win if not check for a draw if not continue
            if (hasWon(current_player)) {
                System.out.println("We found a winner!");

            } else {
                if (isDraw()) {
                    // update the game to be a draw
                    gameModel.setGameDraw(true);
                    System.out.println("There's been a draw.");
                }
            }
        }
    }

    private boolean isDraw() {
        boolean isDraw = true;
        int numRows = gameModel.getNumberOfRows();
        int numCols = gameModel.getNumberOfColumns();
        for (int r=0; r<numRows; r++) {
            for (int c=0; c<numCols; c++) {
                if (gameModel.getCellOwner(r, c) == null) {
                  return false;
                }
            }
        }
        return isDraw;
    }

    private boolean hasWon(int curr_player_num) {
        OXOPlayer curr_player = gameModel.getPlayerByNumber(curr_player_num);
        int winThreshold = gameModel.getWinThreshold();
        int numRows = gameModel.getNumberOfRows();
        int numCols = gameModel.getNumberOfColumns();
        boolean gameWon = false;

        if (checkHorizontalWin(numRows, numCols, winThreshold, curr_player)) {
            System.out.println("checkHorizontalWin found Winner!");
            gameWon = true;
        } else if (checkVerticalWin(numRows, numCols, winThreshold, curr_player)) {
            System.out.println("checkVerticalWin found Winner!");
            gameWon = true;
        } else if (checkTopLeftToBottomRight(numRows, numCols, winThreshold, curr_player)) {
            System.out.println("checkTopLeftToBottomRight found Winner!");
            gameWon = true;
        } else if (checkTopRightToBottomLeft(numRows, numCols, winThreshold, curr_player)) {
            System.out.println("checkTopRightToBottomLeft found Winner!");
            gameWon = true;
        }

        if (gameWon) {
            gameModel.setWinner(gameModel.getPlayerByNumber(curr_player_num));
            return true;
        } else {
            return false;
        }
    }

    private boolean checkHorizontalWin(int numRows, int numCols, int winThreshold, OXOPlayer curr_player) {
        for (int i=0; i<numRows; i++) {
            int continuousFieldCount = 0;
            for (int j=0; j<numCols; j++) {
                if (gameModel.getCellOwner(i, j) == curr_player) {
                    continuousFieldCount++;
                    if (continuousFieldCount == winThreshold) {
                        return true;
                    }
                } else {
                    continuousFieldCount = 0;
                }
            }
        }
        return false;
    }

    private boolean checkVerticalWin(int numRows, int numCols, int winThreshold, OXOPlayer curr_player) {
        for (int j=0; j<numCols; j++) {
            int continuousFieldCount = 0;
            for (int i=0; i<numRows; i++) {
                if (gameModel.getCellOwner(i, j) == curr_player) {
                    continuousFieldCount++;
                    if (continuousFieldCount == winThreshold) {
                        return true;
                    }
                } else {
                    continuousFieldCount = 0;
                }
            }
        }
        return false;
    }

    private boolean checkTopLeftToBottomRight(int numRows, int numCols, int winThreshold, OXOPlayer curr_player) {
        int minimumDimension = Math.min(numRows, numCols);
        for (int r=0; r<numRows; r++) {
            for (int c=0; c<numCols; c++) {
                int count = 0;
                if (gameModel.getCellOwner(r, c) == curr_player) {
                    count++;
                    for (int i=1; i<minimumDimension; i++) {
                        if (r+i < numRows && c+i < numCols) {
                            if (gameModel.getCellOwner(r+i, c+i) == curr_player) {
                                count++;
                                if (count == winThreshold) {return true;}
                            } else {
                                count = 0;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean checkTopRightToBottomLeft(int numRows, int numCols, int winThreshold, OXOPlayer curr_player) {
        int minimumDimension = Math.min(numRows, numCols);
        for (int r=0; r<numRows; r++) {
            for (int c=0; c<numCols; c++) {
                int count = 0;
                if (gameModel.getCellOwner(r, c) == curr_player) {
                    count++;
                    for (int i=1; i<minimumDimension; i++) {
                        if (r+i < numRows && c-i >= 0) {
                            if (gameModel.getCellOwner(r+i, c-i) == curr_player) {
                                count++;
                                if (count == winThreshold) {return true;}
                            } else {
                                count = 0;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public void addRow() {
        if (gameModel.getNumberOfRows() < 9) {
            gameModel.addRow();
        }
    }
    public void removeRow() {
        if (gameModel.getNumberOfRows() > 1) {
            gameModel.removeRow();
        }
    }
    public void addColumn() {
        if (gameModel.getNumberOfColumns() < 9) {
            gameModel.addColumn();
        }
    }
    public void removeColumn() {
        if (gameModel.getNumberOfColumns() > 1) {
              gameModel.removeColumn();
        }
    }
    public void increaseWinThreshold() {
        int currentWinThreshold = gameModel.getWinThreshold();
        gameModel.setWinThreshold(++currentWinThreshold);
    }
    public void decreaseWinThreshold() {
        int currentWinThreshold = gameModel.getWinThreshold();
        if (currentWinThreshold > 3) {
            gameModel.setWinThreshold(--currentWinThreshold);
        }
    }

    public void reset() {
        for (int r=0; r<gameModel.getNumberOfRows(); r++) {
            for (int c=0; c<gameModel.getNumberOfColumns(); c++) {
                gameModel.setCellOwner(r, c, null);
            }
        }
        gameModel.setWinner(null);
        gameModel.setCurrentPlayerNumber(0);
        gameModel.setGameDraw(false);
    }
}
