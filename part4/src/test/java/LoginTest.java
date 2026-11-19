import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class LoginTest {

    private Login login;

    public LoginTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        login = new Login();
        login.firstName = "Thabo";
        login.lastName = "Mokoena";
        login.username = "k_1";
        login.password = "Pass@123";
        login.cellPhoneNumber = "+27831234567";
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCheckUserName() {
        assertTrue(login.checkUserName());

        login.username = "invalidusername";
        assertFalse(login.checkUserName());
    }

    @Test
    public void testCheckPasswordComplexity() {
        assertTrue(login.checkPasswordComplexity());

        login.password = "weak";
        assertFalse(login.checkPasswordComplexity());
    }

    @Test
    public void testCheckCellPhoneNumber() {
        assertTrue(login.checkCellPhoneNumber());

        login.cellPhoneNumber = "0831234567";
        assertFalse(login.checkCellPhoneNumber());
    }

    @Test
    public void testRegisterUser() {
        String expected = "Username successfully captured.\n"
                + "Password successfully captured.\n"
                + "Cell phone number successfully added.";
        assertEquals(expected, login.registerUser());
    }

    @Test
    public void testLoginUser() {
        assertTrue(login.loginUser("k_1", "Pass@123"));
        assertFalse(login.loginUser("k_1", "WrongPass1!"));
    }

    @Test
    public void testReturnLoginStatus() {
        assertEquals(
                "Welcome Thabo Mokoena it is great to see you again.",
                login.returnLoginStatus(true)
        );
        assertEquals(
                "Username or password incorrect, please try again.",
                login.returnLoginStatus(false)
        );
    }
}