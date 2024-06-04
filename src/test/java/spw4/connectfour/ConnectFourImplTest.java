package spw4.connectfour;

import jdk.jfr.Description;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConnectFourImplTest {

    @Mock
    private ConnectFour connectFour;

    @Description("Test if the constructor of ConnectFourImpl sets the player on turn correctly.")
    @ParameterizedTest(name = "{index} => player={0}")
    @EnumSource(Player.class)
    void testConstructorSetsPlayerOnTurn(Player player) {
        ConnectFourImpl sut = new ConnectFourImpl(player);
        assertNotNull(sut);
        assertEquals(player, sut.getPlayerOnTurn());
    }

    @Description("Test if the constructor of ConnectFourImpl initializes the board correctly.")
    @Test
    void testConstructorInitializesBoard() {
        ConnectFourImpl sut = new ConnectFourImpl(Player.red);
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                assertEquals(Player.none, sut.getPlayerAt(row, col));
            }
        }
    }

    @Description("Test dropping a coin into the first column.")
    @Test
    void testDropCoin() {
        ConnectFourImpl sut = new ConnectFourImpl(Player.red);
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                assertEquals(Player.none, sut.getPlayerAt(row, col));
            }
        }
        sut.drop(0);
    }

    @Description("Test dropping a coin into a full column.")
    @Test
    void testDropCoinFullColumn() {
        ConnectFourImpl sut = new ConnectFourImpl(Player.red);
        // Fill the column (index 0) with 6 coins
        for (int i = 0; i < 6; i++) {
            sut.drop(0);
        }

        ConnectFourImpl sut2 = new ConnectFourImpl(Player.red);
        for (int i = 0; i < 6; i++) {
            sut2.drop(0);
        }

        sut.drop(0);

        assertEquals(sut2, sut);
    }

    @Description("Test the toString method for an empty board.")
    @Test
    void testToStringEmptyBoard() {
        ConnectFourImpl sut = new ConnectFourImpl(Player.red);
        String expected = "Player: RED\n" +
                "| .  .  .  .  .  .  . |\n" +
                "| .  .  .  .  .  .  . |\n" +
                "| .  .  .  .  .  .  . |\n" +
                "| .  .  .  .  .  .  . |\n" +
                "| .  .  .  .  .  .  . |\n" +
                "| .  .  .  .  .  .  . |\n";
        assertEquals(expected, sut.toString());
    }

    @Description("Test the toString method during gameplay.")
    @Test
    void testToStringDuringGameplay() {
        ConnectFourImpl sut = new ConnectFourImpl(Player.red);
        sut.drop(0);
        sut.drop(1);
        sut.drop(2);
        sut.drop(3);
        sut.drop(4);

        String expected = "Player: YELLOW\n" +
                "| .  .  .  .  .  .  . |\n" +
                "| .  .  .  .  .  .  . |\n" +
                "| .  .  .  .  .  .  . |\n" +
                "| .  .  .  .  .  .  . |\n" +
                "| .  .  .  .  .  .  . |\n" +
                "| R  Y  R  Y  R  .  . |\n";
        assertEquals(expected, sut.toString());
    }

    @Description("Test the reset method to initialize a new game.")
    @Test
    void testResetGame() {
        ConnectFourImpl sut = new ConnectFourImpl(Player.red);
        sut.drop(0);
        sut.drop(6);
        sut.drop(0);
        sut.reset(Player.red);

        ConnectFourImpl expected = new ConnectFourImpl(Player.red);
        assertEquals(expected, sut);
    }

    @Description("Test isGameOver method returns false when the game is not over.")
    @Test
    void testIsGameOverFalseNoWinner() {
        ConnectFourImpl sut = new ConnectFourImpl(Player.red);
        assertFalse(sut.isGameOver());
    }

    @Description("Test isGameOver method returns true when the game ends in a draw.")
    @Test
    void testIsGameOverTrueDraw() {
        ConnectFourImpl sut = new ConnectFourImpl(Player.red);
        int[] moves = {1, 2, 1, 2, 1, 2, 3, 4, 3, 4, 3, 4, 5, 6, 5, 6, 5, 6, 7, 1, 7, 1, 7, 1, 2, 3, 2, 3, 2, 3, 4, 5, 4, 5, 4, 5, 6, 7, 6, 7, 6, 7};
        for (int move : moves) {
            sut.drop(move - 1);
        }
        assertTrue(sut.isGameOver());
        assertEquals(Player.none, sut.getWinner());
    }

    @Description("Test isGameOver method returns true when a player wins.")
    @Test
    void testIsGameOverTrueWin() {
        ConnectFourImpl sut = new ConnectFourImpl(Player.red);
        sut.drop(0);
        sut.drop(1);
        sut.drop(0);
        sut.drop(1);
        sut.drop(0);
        sut.drop(1);
        sut.drop(0);
        assertTrue(sut.isGameOver());
        assertEquals(Player.red, sut.getWinner());
    }

    @Description("Test getWinner method for a diagonal win from bottom left to top right.")
    @Test
    void testGetWinnerDiagonalRight() {
        ConnectFourImpl sut = new ConnectFourImpl(Player.red);
        int[] moves = {4, 1, 4, 2, 4, 4, 3, 2, 5, 6, 5, 6, 5, 5, 7, 2, 2, 1, 1, 4, 7, 7, 7, 6};
        for (int move : moves) {
            sut.drop(move - 1);
        }
        sut.drop(5);
        assertTrue(sut.isGameOver());
        assertEquals(Player.red, sut.getWinner());
    }

    @Description("Test getWinner method for a horizontal win.")
    @Test
    void testGetWinnerRow() {
        ConnectFourImpl sut = new ConnectFourImpl(Player.red);
        int[] moves = {1, 7, 2, 6, 3, 5, 4};
        for (int move : moves) {
            sut.drop(move - 1);
        }
        assertTrue(sut.isGameOver());
        assertEquals(Player.red, sut.getWinner());
    }

    @Description("Test getWinner method for a diagonal win from bottom right to top left.")
    @Test
    void testGetWinnerDiagonalLeft() {
        ConnectFourImpl sut = new ConnectFourImpl(Player.red);
        int[] moves = {7, 6, 6, 5, 4, 5, 5, 3, 2, 4, 3, 4, 4};
        for (int move : moves) {
            sut.drop(move - 1);
        }
        assertTrue(sut.isGameOver());
        assertEquals(Player.red, sut.getWinner());
    }

    @Description("Test dropping a coin into an invalid column.")
    @Test
    void testDropInvalidColumn() {
        ConnectFourImpl sut = new ConnectFourImpl(Player.red);
        sut.drop(8);
        assertEquals(Player.red, sut.getPlayerOnTurn());

        sut.drop(-1);
        assertEquals(Player.red, sut.getPlayerOnTurn());
    }

    @Description("Test equality of the same object.")
    @Test
    void testEqualsSameObject() {
        ConnectFourImpl sut = new ConnectFourImpl(Player.red);
        assertEquals(sut, sut);
    }

    @Description("Test equality of different class objects.")
    @Test
    void testEqualsDifferentClass() {
        ConnectFourImpl sut = new ConnectFourImpl(Player.red);
        assertNotEquals(sut, new Object());
    }

    @Description("Test equality with different players on turn.")
    @Test
    void testEqualsDifferentPlayerOnTurn() {
        ConnectFourImpl sut1 = new ConnectFourImpl(Player.red);
        ConnectFourImpl sut2 = new ConnectFourImpl(Player.yellow);
        assertNotEquals(sut1, sut2);
    }

    @Description("Test equality with different board states.")
    @Test
    void testEqualsDifferentBoard() {
        ConnectFourImpl sut1 = new ConnectFourImpl(Player.red);
        ConnectFourImpl sut2 = new ConnectFourImpl(Player.red);
        sut2.drop(0);
        assertNotEquals(sut1, sut2);
    }

    @Description("Test equality with identical board states and players.")
    @Test
    void testEqualsIdenticalObjects() {
        ConnectFourImpl sut1 = new ConnectFourImpl(Player.red);
        ConnectFourImpl sut2 = new ConnectFourImpl(Player.red);
        assertEquals(sut1, sut2);
    }
}
