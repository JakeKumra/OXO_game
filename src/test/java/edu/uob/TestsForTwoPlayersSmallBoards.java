package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class TestsForTwoPlayersSmallBoards {
    private OXOModel model;
    private OXOController controller;

    // Make a new "standard" (6x6) board before running each test case (i.e. this method runs before every `@Test` method)
    // In order to test boards of different sizes, winning thresholds or number of players, create a separate test file (without this method in it !)
    @BeforeEach
    void setup() {
        model = new OXOModel(1, 1, 1);
        model.addPlayer(new OXOPlayer('X'));
        model.addPlayer(new OXOPlayer('O'));
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
        String failedTestComment = "Was expecting the number of players to be 2";
        assertEquals(2, controller.gameModel.getNumberOfPlayers(), failedTestComment);
    }

    @Test
    void testBasicWin1x1() throws OXOMoveException {
        OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
        sendCommandToController("a1"); // First player
        String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
        assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
    }

    @Test
    void testBasicWin2x2() throws OXOMoveException {

        model = new OXOModel(2, 2, 2);
        model.addPlayer(new OXOPlayer('X'));
        model.addPlayer(new OXOPlayer('O'));
        controller = new OXOController(model);

        OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());

        sendCommandToController("a2"); // First player
        sendCommandToController("a1"); // Second player
        sendCommandToController("b2"); // First player

        String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
        assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
    }

    @Test
    void testBasicDraw2x2() throws OXOMoveException {

        model = new OXOModel(2, 2, 3);
        model.addPlayer(new OXOPlayer('X'));
        model.addPlayer(new OXOPlayer('O'));
        controller = new OXOController(model);

        sendCommandToController("a1"); // First player
        sendCommandToController("a2"); // Second player
        sendCommandToController("b2"); // First player
        sendCommandToController("b1"); // Second player

        String failedTestComment = "Expected to be a draw but wasn't";
        assertNull(model.getWinner(), failedTestComment);
    }
}





