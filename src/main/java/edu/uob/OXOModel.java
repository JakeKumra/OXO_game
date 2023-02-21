package edu.uob;
import java.util.ArrayList;

public class OXOModel {

    private ArrayList< ArrayList<OXOPlayer> > cells;
    // ArrayList<OXOPlayer> cells = new ArrayList<>(Arrays.asList(null));

//    private OXOPlayer[] players;
    private ArrayList<OXOPlayer> players;

    private int currentPlayerNumber;

    private OXOPlayer winner;

    private boolean gameDrawn;

    private int winThreshold;

    public OXOModel(int numberOfRows, int numberOfColumns, int winThresh) {
        winThreshold = winThresh;
        cells = new ArrayList<>();
        for (int r=0; r<numberOfRows; r++) {
            ArrayList<OXOPlayer> row = new ArrayList<>();
            for (int c=0; c<numberOfColumns; c++) {
                row.add(null);
            }
            cells.add(row);
        }
        // instantiates two new players inside the players ArrayList
        players = new ArrayList<OXOPlayer>(2);
    }

    public OXOPlayer getPlayerByIndex (int indexNum) {
        if (players.size() >= indexNum - 1) {
            return players.get(indexNum);
        }
        return null;
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    // update this so that we can add additional players inside the test scripts
    public void addPlayer(OXOPlayer player) {
        players.add(player);
    }

    public OXOPlayer getPlayerByNumber(int number) {
        return players.get(number);
//        return players[number];
    }

    public OXOPlayer getWinner() {
        return winner;
    }

    public void setWinner(OXOPlayer player) {
        winner = player;
    }

    public int getCurrentPlayerNumber() {
        return currentPlayerNumber;
    }

    public void setCurrentPlayerNumber(int playerNumber) {
        currentPlayerNumber = playerNumber;
    }

    public int getNumberOfRows() {
        return cells.size();
    }

    public int getNumberOfColumns() {
        if (cells.isEmpty()) {
            return 0;
        } else {
            return cells.get(0).size();
        }
    }

    public OXOPlayer getCellOwner(int rowNumber, int colNumber) {
        return cells.get(rowNumber).get(colNumber);
    }

    public void setCellOwner(int rowNumber, int colNumber, OXOPlayer player) {
        cells.get(rowNumber).set(colNumber, player);
    }

    public void addColumn() {
        int numRows = cells.size();
        for (int i=0; i<numRows; i++) {
            cells.get(i).add(null);
        }
    }

    public void addRow() {
        ArrayList<OXOPlayer> newRow = new ArrayList<>();
        int numRows = cells.isEmpty() ? 0 : cells.get(0).size();
        for (int i=0; i<numRows; i++) {
            newRow.add(null);
        }
        cells.add(newRow);
    }

    public void removeRow() {
        // should this allow players to remove columns / rows if it's occupied? NO IT SHOULDN'T
        if (!cells.isEmpty()) {
            int numRows = cells.size();
            cells.remove(numRows-1);
        }
    }


    public void removeColumn() {
        // should this allow players to remove columns / rows if it's occupied? NO IT SHOULD
        if (!cells.get(0).isEmpty()) {
            int numRows = cells.size();
            for (int i=0; i<numRows; i++) {
                cells.get(i).remove(cells.get(i).size() -1);
            }
        }
    }

    public void setWinThreshold(int winThresh) {
        winThreshold = winThresh;
    }

    public int getWinThreshold() {
        return winThreshold;
    }

    public boolean isGameDrawn() {
        return gameDrawn;
    }

    public void setGameDraw(boolean drawStatus) {
        gameDrawn = drawStatus;
    }

}
