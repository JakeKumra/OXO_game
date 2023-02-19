package edu.uob;
import java.util.ArrayList;

public class OXOModel {

    private ArrayList< ArrayList<OXOPlayer> > cells;
    // ArrayList<OXOPlayer> cells = new ArrayList<>(Arrays.asList(null));

    // OXOPlayer contains the players in the game stored in an array of type OXOPlayer (2)
    private OXOPlayer[] players;

    // currentPLayerNumber states whose turn it is next. 0 for first player and 1 for second player
    private int currentPlayerNumber;

    // OXOPlayer is of type OXOPlayer and will be assigned a player if the game is won
    private OXOPlayer winner;

    // gameDrawn is a boolean that defaults to false, but will be assigned true if no one wins
    private boolean gameDrawn;

    // winThreshold dictates the win threshold of the game (i.e. 3 as default)
    private int winThreshold;


    // modify this constructor properly
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
        players = new OXOPlayer[2];
    }

    public int getNumberOfPlayers() {
        return players.length;
    }

    public void addPlayer(OXOPlayer player) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                players[i] = player;
                return;
            }
        }
    }

    public OXOPlayer getPlayerByNumber(int number) {
        return players[number];
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
        if (!cells.isEmpty()) {
            int numRows = cells.size();
            cells.remove(numRows-1);
        }
    }

    public void removeColumn() {
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

    public void setGameDrawn() {
        gameDrawn = true;
    }

    public boolean isGameDrawn() {
        return gameDrawn;
    }

}
