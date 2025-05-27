package deu.cse.spring_webmail.model;

import org.junit.jupiter.api.*;
import org.mockito.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddrBookManagerTest {

    private AddrBookManager addrBookManager;
    @Mock
    private UserAdminAgent mockUserAdminAgent;

    private final String jdbcUrl = "jdbc:mysql://localhost:3306/webmail?useSSL=false";
    private final String dbUser = "jdbctester";
    private final String dbPassword = "12345*";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        addrBookManager = new AddrBookManager(
                "localhost", "3306", dbUser, dbPassword, mockUserAdminAgent
        );
    }

    @Test
    void testAddRow_whenUserExists_thenInsertSuccess() {
        String userId = "testuser@test.com";
        String email = "target@test.com";
        String name = "홍길동";

        when(mockUserAdminAgent.verify(email)).thenReturn(true);

        assertDoesNotThrow(() -> addrBookManager.addRow(userId, email, name));
    }

    @Test
    void testAddRow_whenUserDoesNotExist_thenThrowException() {
        String userId = "testuser@test.com";
        String email = "notexist@test.com";
        String name = "홍길동";

        when(mockUserAdminAgent.verify(email)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> addrBookManager.addRow(userId, email, name)
        );
        assertEquals("존재하지 않는 사용자입니다.", ex.getMessage());
    }

    @Test
    void testGetRowsByUser_returnsList() {
        String userId = "testuser@test.com";

        List<AddrBookRow> rows = addrBookManager.getRowsByUser(userId);

        assertNotNull(rows);
    }
}
