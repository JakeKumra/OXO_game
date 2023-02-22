package edu.uob;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import edu.uob.OXOMoveException.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;

class ExampleControllerTests {
  private OXOModel model;
  private OXOController controller;

  // Make a new "standard" (3x3) board before running each test case (i.e. this method runs before every `@Test` method)
  // In order to test boards of different sizes, winning thresholds or number of players, create a separate test file (without this method in it !)
  @BeforeEach
  void setup() {
    model = new OXOModel(3, 3, 3);
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
  void testBasicMoveTaking() throws OXOMoveException {
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    sendCommandToController("a1");
    String failedTestComment = "Cell a1 wasn't claimed by the first player";
    assertEquals(firstMovingPlayer, controller.gameModel.getCellOwner(0, 0), failedTestComment);
  }

  @Test
  void testBasicWin() throws OXOMoveException {
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    sendCommandToController("a1"); // First player
    sendCommandToController("b1"); // Second player
    sendCommandToController("a2"); // First player
    sendCommandToController("b2"); // Second player
    sendCommandToController("a3"); // First player

    String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
  }

  @Test
  void testWinInBottomRow() throws OXOMoveException {

    OXOPlayer secondMovingPlayer = model.getPlayerByNumber(1);

    sendCommandToController("a1"); // First player
    sendCommandToController("b1"); // Second player
    sendCommandToController("a2"); // First player
    sendCommandToController("b2"); // Second player
    sendCommandToController("c1"); // First player
    sendCommandToController("b3"); // Second player
    sendCommandToController("c3"); // First player

    // b1, b2, and b3 should be a win for the second player
    String failedTestComment = "Winner was expected to be " + secondMovingPlayer.getPlayingLetter() + " but it wasn't";
    assertEquals(secondMovingPlayer, model.getWinner(), failedTestComment);
  }

  @Test
  void testWinInLeftColumn() throws OXOMoveException {
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());

    sendCommandToController("a1"); // First player
    sendCommandToController("b2"); // Second player
    sendCommandToController("a2"); // First player
    sendCommandToController("c2"); // Second player
    sendCommandToController("a3"); // First player

    // a1, a2, and a3 should be a win for the first player
    String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
  }

  @Test
  void testWinInTopLeftToBottomRightDiagonal() throws OXOMoveException {
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());

    sendCommandToController("a1"); // First player
    sendCommandToController("b1"); // Second player
    sendCommandToController("b2"); // First player
    sendCommandToController("c1"); // Second player
    sendCommandToController("c3"); // First player

    // a1, b2, and c3 should be a win for the first player
    String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
  }

  @Test
  void testWinInTopRightToBottomLeftDiagonal() throws OXOMoveException {
    OXOPlayer secondMovingPlayer = model.getPlayerByNumber(1);

    sendCommandToController("a2"); // First player
    sendCommandToController("a3"); // Second player
    sendCommandToController("c2"); // First player
    sendCommandToController("b2"); // Second player
    sendCommandToController("b1"); // First player
    sendCommandToController("c1"); // Second player

    // a3, b2, and c1 should be a win for the second player
    String failedTestComment = "Winner was expected to be " + secondMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(secondMovingPlayer, model.getWinner(), failedTestComment);
  }


  @Test
  void testTieGame() throws OXOMoveException {

    sendCommandToController("a1"); // First player
    sendCommandToController("b1"); // Second player
    sendCommandToController("c1"); // First player
    sendCommandToController("b2"); // Second player
    sendCommandToController("b3"); // First player
    sendCommandToController("c2"); // Second player
    sendCommandToController("a2"); // First player
    sendCommandToController("a3"); // Second player
    sendCommandToController("c3"); // First player

    String failedTestComment = "Was expecting a draw so winner should be null";
    assertEquals(model.getWinner(), null, failedTestComment);
  }

  @Test
  void testTieGame2() throws OXOMoveException {
    sendCommandToController("a1"); // First player
    sendCommandToController("a2"); // Second player
    sendCommandToController("a3"); // First player
    sendCommandToController("b2"); // Second player
    sendCommandToController("b1"); // First player
    sendCommandToController("c1"); // Second player
    sendCommandToController("c2"); // First player
    sendCommandToController("b3"); // Second player
    sendCommandToController("c3"); // First player

    String failedTestComment = "Was expecting a draw so winner should be null";
    assertEquals(model.getWinner(), null, failedTestComment);
  }

  @Test
  void testInvalidIdentifierException() throws OXOMoveException {
    String failedTestComment = "Controller failed to throw an InvalidIdentifierLengthException for command `abc123`";
    assertThrows(InvalidIdentifierLengthException.class, () -> sendCommandToController("abc123"), failedTestComment);
  }

  @Test
  void testInvalidIdentifierException2() throws OXOMoveException {
    String failedTestComment = "Controller failed to throw an InvalidIdentifierLengthException for command `a`";
    assertThrows(InvalidIdentifierLengthException.class, () -> sendCommandToController("a"), failedTestComment);
  }

  @Test
  void testInvalidIdentifierException3() throws OXOMoveException {
    String failedTestComment = "Controller failed to throw an InvalidIdentifierLengthException for command ``";
    assertThrows(InvalidIdentifierLengthException.class, () -> sendCommandToController(""), failedTestComment);
  }

  @Test
  void InvalidIdentifierCharacterException() throws OXOMoveException {
    String failedTestComment = "Controller failed to throw an InvalidIdentifierCharacterException for command `22`";
    assertThrows(InvalidIdentifierCharacterException.class, () -> sendCommandToController("22"), failedTestComment);
  }

  @Test
  void InvalidIdentifierCharacterException2() throws OXOMoveException {
    String failedTestComment = "Controller failed to throw an InvalidIdentifierCharacterException for command `!2`";
    assertThrows(InvalidIdentifierCharacterException.class, () -> sendCommandToController("!2"), failedTestComment);
  }

  @Test
  void InvalidIdentifierCharacterException3() throws OXOMoveException {
    String failedTestComment = "Controller failed to throw an InvalidIdentifierCharacterException for command ` 2`";
    assertThrows(InvalidIdentifierCharacterException.class, () -> sendCommandToController(" 2"), failedTestComment);
  }

  @Test
  void InvalidIdentifierCharacterException4() throws OXOMoveException {
    String failedTestComment = "Controller failed to throw an InvalidIdentifierCharacterException for command `aa`";
    assertThrows(InvalidIdentifierCharacterException.class, () -> sendCommandToController("aa"), failedTestComment);
  }

  @Test
  void InvalidIdentifierCharacterException5() throws OXOMoveException {
    String failedTestComment = "Controller failed to throw an InvalidIdentifierCharacterException for command `a@`";
    assertThrows(InvalidIdentifierCharacterException.class, () -> sendCommandToController("a@"), failedTestComment);
  }

  @Test
  void InvalidIdentifierCharacterException6() throws OXOMoveException {
    String failedTestComment = "Controller failed to throw an InvalidIdentifierCharacterException for command `a `";
    assertThrows(InvalidIdentifierCharacterException.class, () -> sendCommandToController("a "), failedTestComment);
  }

  @Test
  void OutsideCellRangeException() throws OXOMoveException {
    String failedTestComment = "Controller failed to throw an OutsideCellRangeException for command `d1`";
    assertThrows(OutsideCellRangeException.class, () -> sendCommandToController("d1"), failedTestComment);
  }

  @Test
  void OutsideCellRangeException1() throws OXOMoveException {
    String failedTestComment = "Controller failed to throw an OutsideCellRangeException for command `z1`";
    assertThrows(OutsideCellRangeException.class, () -> sendCommandToController("z1"), failedTestComment);
  }

  @Test
  void OutsideCellRangeException2() throws OXOMoveException {
    String failedTestComment = "Controller failed to throw an OutsideCellRangeException for command `i1`";
    assertThrows(OutsideCellRangeException.class, () -> sendCommandToController("i1"), failedTestComment);
  }

  @Test
  void OutsideCellRangeException3() throws OXOMoveException {
    String failedTestComment = "Controller failed to throw an OutsideCellRangeException for command `h1`";
    assertThrows(OutsideCellRangeException.class, () -> sendCommandToController("h1"), failedTestComment);
  }

  @Test
  void OutsideCellRangeException4() throws OXOMoveException {
    String failedTestComment = "Controller failed to throw an OutsideCellRangeException for command `a4`";
    assertThrows(OutsideCellRangeException.class, () -> sendCommandToController("a4"), failedTestComment);
  }

  @Test
  void OutsideCellRangeException5() throws OXOMoveException {
    String failedTestComment = "Controller failed to throw an OutsideCellRangeException for command `a4`";
    assertThrows(OutsideCellRangeException.class, () -> sendCommandToController("a0"), failedTestComment);
  }

  @Test
  void OutsideCellRangeException6() throws OXOMoveException {
    String failedTestComment = "Controller failed to throw an OutsideCellRangeException for command `a4`";
    assertThrows(OutsideCellRangeException.class, () -> sendCommandToController("a9"), failedTestComment);
  }

  @Test
  void CellAlreadyTakenException() throws OXOMoveException {
    sendCommandToController("a1"); // First player
    String failedTestComment = "Controller failed to throw an CellAlreadyTakenException for command `a1`";
    assertThrows(CellAlreadyTakenException.class, () -> sendCommandToController("a1"), failedTestComment);
  }

  @Test
  void CellAlreadyTakenException2() throws OXOMoveException {
    sendCommandToController("c3");
    String failedTestComment = "Controller failed to throw an CellAlreadyTakenException for command `c3`";
    assertThrows(CellAlreadyTakenException.class, () -> sendCommandToController("c3"), failedTestComment);
  }

  @Test
  void TestUnableToMoveAfterGameWon() throws OXOMoveException {

    sendCommandToController("a1"); // First player
    sendCommandToController("b1"); // Second player
    sendCommandToController("a2"); // First player
    sendCommandToController("b2"); // Second player
    sendCommandToController("a3"); // First player
    sendCommandToController("c1"); // Second player

    String failedTestComment = "c1 should not be occupied by second player after game was won`";
    assertEquals(controller.gameModel.getCellOwner(2, 0), null, failedTestComment);
  }

  @Test
  void testResetMidGame() throws OXOMoveException {

    sendCommandToController("a1"); // First player
    sendCommandToController("b1"); // Second player
    sendCommandToController("a2"); // First player
    sendCommandToController("b2"); // Second player

    // check the draw, win, states here.
    // then call reset method
    // then check again that they were reset correctly
  }
  @Test
  void testResetPostWin() throws OXOMoveException {

    sendCommandToController("a1"); // First player
    sendCommandToController("b1"); // Second player
    sendCommandToController("a2"); // First player
    sendCommandToController("b2"); // Second player

    // test the game resets correctly
    // call the RESET METHOD DIRECTLY
  }


}





