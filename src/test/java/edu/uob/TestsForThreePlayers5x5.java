package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

class TestsForThreePlayers5x5 {
    private OXOModel model;
    private OXOController controller;

    // Make a new "standard" (3x3) board before running each test case (i.e. this method runs before every `@Test` method)
    // In order to test boards of different sizes, winning thresholds or number of players, create a separate test file (without this method in it !)
    @BeforeEach
    void setup() {
        model = new OXOModel(5, 5, 4);
        model.addPlayer(new OXOPlayer('X'));
        model.addPlayer(new OXOPlayer('O'));
        model.addPlayer(new OXOPlayer('Z'));
        controller = new OXOController(model);
    }

    // This next method is a utility function that can be used by any of the test methods to _safely_ send a command to the controller
    void sendCommandToController(String command) {
        // Try to send a command to the server - call will timeout if it takes too long (in case the server enters an infinite loop)
        // Note: this is ugly code and includes syntax that you haven't encountered yet
        String timeoutComment = "Controller took too long to respond (probably stuck in an infinite loop)";
        assertTimeoutPreemptively(Duration.ofMillis(1000), () -> controller.handleIncomingCommand(command), timeoutComment);
    }

    @Test
    void TestNewPlayersAdded() {
        String failedTestComment = "Was expecting the number of players to be 3";
        assertEquals(3, controller.gameModel.getNumberOfPlayers(), failedTestComment);
    }

    @Test
    void testBasicMoveTaking() throws OXOMoveException {

        sendCommandToController("a1"); // first player move 'X'
        sendCommandToController("a2"); // second player move 'O'
        sendCommandToController("a3"); // third player move 'A'
        sendCommandToController("b2"); // first player move 'B'
        sendCommandToController("b3"); // second player move 'X'
        sendCommandToController("b1"); // third player move 'O'
        sendCommandToController("c2"); // first player move 'A'
        sendCommandToController("c1"); // second player move 'B'
        sendCommandToController("c3"); // third player move 'A'

        OXOPlayer firstPlayer = controller.gameModel.getPlayerByIndex(0);
        OXOPlayer secondPlayer = controller.gameModel.getPlayerByIndex(1);
        OXOPlayer thirdPlayer = controller.gameModel.getPlayerByIndex(2);

        String failedTestComment = "Cell a1 wasn't claimed by the first player";
        assertEquals(firstPlayer, controller.gameModel.getCellOwner(0, 0), failedTestComment);

        String failedTestComment2 = "Cell a2 wasn't claimed by the second player";
        assertEquals(secondPlayer, controller.gameModel.getCellOwner(0, 1), failedTestComment2);

        String failedTestComment3 = "Cell a3 wasn't claimed by the third player";
        assertEquals(thirdPlayer, controller.gameModel.getCellOwner(0, 2), failedTestComment3);

        String failedTestComment4 = "Cell b2 wasn't claimed by the first player";
        assertEquals(firstPlayer, controller.gameModel.getCellOwner(1, 1), failedTestComment4);

        String failedTestComment5 = "Cell b3 wasn't claimed by the second player";
        assertEquals(secondPlayer, controller.gameModel.getCellOwner(1, 2), failedTestComment5);

        String failedTestComment6 = "Cell b1 wasn't claimed by the third player";
        assertEquals(thirdPlayer, controller.gameModel.getCellOwner(1, 0), failedTestComment6);

        String failedTestComment7 = "Cell c2 wasn't claimed by the first player";
        assertEquals(firstPlayer, controller.gameModel.getCellOwner(2, 1), failedTestComment7);

        String failedTestComment8 = "Cell c1 wasn't claimed by the second player";
        assertEquals(secondPlayer, controller.gameModel.getCellOwner(2, 0), failedTestComment8);

        String failedTestComment9 = "Cell c3 wasn't claimed by the third player";
        assertEquals(thirdPlayer, controller.gameModel.getCellOwner(2, 2), failedTestComment9);
    }

    @Test
    void testBasicWinVertical() throws OXOMoveException {
        // Find out which player is going to make the first move (they should be the eventual winner)
        // Make a bunch of moves for the two players
        sendCommandToController("a1"); // First player
        sendCommandToController("a2"); // Second player
        OXOPlayer thirdMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
        sendCommandToController("a3"); // Third player

        sendCommandToController("b2"); // First player
        sendCommandToController("b1"); // Second player
        sendCommandToController("b3"); // Third player

        sendCommandToController("c1"); // First player
        sendCommandToController("c2"); // Second player
        sendCommandToController("c3"); // Third player

        sendCommandToController("d2"); // First player
        sendCommandToController("d1"); // Second player
        sendCommandToController("d3"); // Third player

        // a3, b3, c3, c4 should be a win for the third player (since players alternate between moves)
        String failedTestComment = "Winner was expected to be " + thirdMovingPlayer.getPlayingLetter() + " but wasn't";
        assertEquals(thirdMovingPlayer, model.getWinner(), failedTestComment);
    }

    @Test
    void testBasicWinHorizontal() throws OXOMoveException {
        OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());

        sendCommandToController("c2"); // First player
        sendCommandToController("a1"); // Second player
        sendCommandToController("a2"); // Third player
        sendCommandToController("c3"); // First player
        sendCommandToController("a3"); // Second player
        sendCommandToController("a4"); // Third player
        sendCommandToController("c4"); // First player
        sendCommandToController("a5"); // Second player
        sendCommandToController("b1"); // Third player
        sendCommandToController("c5"); // First player

        // c2, c3, c4, c5 should be a win for the first player (since players alternate between moves)
        String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
        assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
    }

    @Test
    void testBasicWinDiagonalLeftToRight() throws OXOMoveException {

        OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());

        sendCommandToController("a1"); // First player
        sendCommandToController("a2"); // Second player
        sendCommandToController("a3"); // Third player
        sendCommandToController("b2"); // First player
        sendCommandToController("e1"); // Second player
        sendCommandToController("e2"); // Third player
        sendCommandToController("c3"); // First player
        sendCommandToController("e3"); // Second player
        sendCommandToController("e4"); // Third player
        sendCommandToController("d4"); // First player

        // a1, b2, c3, d4 should be a win for the first player
        String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
        assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
    }

    @Test
    void testBasicWinDiagonalRightToLeft() throws OXOMoveException {

        OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());

        sendCommandToController("b5"); // First player
        sendCommandToController("a1"); // Second player
        sendCommandToController("a2"); // Third player
        sendCommandToController("c4"); // First player
        sendCommandToController("b2"); // Second player
        sendCommandToController("b1"); // Third player
        sendCommandToController("d3"); // First player
        sendCommandToController("c1"); // Second player
        sendCommandToController("c2"); // Third player
        sendCommandToController("e2"); // First player

        String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
        assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
    }
}







