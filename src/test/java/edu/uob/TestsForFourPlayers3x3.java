package edu.uob;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;

class TestsForFourPlayers3x3 {
    private OXOModel model;
    private OXOController controller;

    // Make a new "standard" (3x3) board before running each test case (i.e. this method runs before every `@Test` method)
    // In order to test boards of different sizes, winning thresholds or number of players, create a separate test file (without this method in it !)
    @BeforeEach
    void setup() {
        model = new OXOModel(3, 3, 3);
        model.addPlayer(new OXOPlayer('X'));
        model.addPlayer(new OXOPlayer('O'));
        model.addPlayer(new OXOPlayer('A'));
        model.addPlayer(new OXOPlayer('B'));
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
        String failedTestComment = "Was expecting the number of players to be 4";
        assertEquals(4, controller.gameModel.getNumberOfPlayers(), failedTestComment);
    }

    @Test
    void testBasicMoveTaking() throws OXOMoveException {
        // Find out which player is going to make the first move

        sendCommandToController("a1"); // first player move 'X' [0][0]
        sendCommandToController("a2"); // second player move 'O' [0][1]
        sendCommandToController("a3"); // third player move 'A' [0][2]
        sendCommandToController("b1"); // fourth player move 'B' [1][0]
        sendCommandToController("b2"); // first player move 'X' [1][1]
        sendCommandToController("b3"); // second player move 'O' [1][2]
        sendCommandToController("c1"); // third player move 'A' [2][0]
        sendCommandToController("c2"); // fourth player move 'B' [2][1]
        sendCommandToController("c3"); // first player move 'A' [2][2]

        OXOPlayer firstPlayer = controller.gameModel.getPlayerByIndex(0);
        OXOPlayer secondPlayer = controller.gameModel.getPlayerByIndex(1);
        OXOPlayer thirdPlayer = controller.gameModel.getPlayerByIndex(2);
        OXOPlayer fourthPlayer = controller.gameModel.getPlayerByIndex(3);

        // Check that A1 (cell [0,0] on the board) is now "owned" by the first player
        String failedTestComment = "Cell a1 wasn't claimed by the first player";
        assertEquals(firstPlayer, controller.gameModel.getCellOwner(0, 0), failedTestComment);

        // Check that A2 (cell [0,1] on the board) is now "owned" by the second player
        String failedTestComment2 = "Cell a2 wasn't claimed by the second player";
        assertEquals(secondPlayer, controller.gameModel.getCellOwner(0, 1), failedTestComment2);

        // Check that A3 (cell [0,2] on the board) is now "owned" by the third player
        String failedTestComment3 = "Cell a3 wasn't claimed by the third player";
        assertEquals(thirdPlayer, controller.gameModel.getCellOwner(0, 2), failedTestComment3);

        // Check that B1 (cell [1,0] on the board) is now "owned" by the third player
        String failedTestComment4 = "Cell b1 wasn't claimed by the fourth player";
        assertEquals(fourthPlayer, controller.gameModel.getCellOwner(1, 0), failedTestComment4);

        // Check that B2 (cell [1,1] on the board) is now "owned" by the first player
        String failedTestComment5 = "Cell b2 wasn't claimed by the first player";
        assertEquals(firstPlayer, controller.gameModel.getCellOwner(1, 1), failedTestComment5);

        // Check that B3 (cell [1,2] on the board) is now "owned" by the second player
        String failedTestComment6 = "Cell b3 wasn't claimed by the second player";
        assertEquals(secondPlayer, controller.gameModel.getCellOwner(1, 2), failedTestComment6);

        // Check that C1 (cell [2,0] on the board) is now "owned" by the third player
        String failedTestComment7 = "Cell c1 wasn't claimed by the third player";
        assertEquals(thirdPlayer, controller.gameModel.getCellOwner(2, 0), failedTestComment7);

        // Check that C2 (cell [2,1] on the board) is now "owned" by the fourth player
        String failedTestComment8 = "Cell c2 wasn't claimed by the fourth player";
        assertEquals(fourthPlayer, controller.gameModel.getCellOwner(2, 1), failedTestComment8);

        // Check that C3 (cell [2,2] on the board) is now "owned" by the first player
        String failedTestComment9 = "Cell c3 wasn't claimed by the first player";
        assertEquals(firstPlayer, controller.gameModel.getCellOwner(2, 2), failedTestComment9);
    }
}





