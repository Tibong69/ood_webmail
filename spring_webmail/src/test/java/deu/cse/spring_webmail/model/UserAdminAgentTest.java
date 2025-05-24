package deu.cse.spring_webmail.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;

import java.lang.reflect.Field;
import javax.management.MBeanException;
import javax.management.ReflectionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserAdminAgentTest {

    @InjectMocks
    private UserAdminAgent agent;

    @Mock
    private MBeanServerConnection mbsc;

    @Mock
    private JMXConnector connector;

    private ObjectName mockObjectName;

    @BeforeEach
    public void setUp() throws Exception {
        agent = new UserAdminAgent("127.0.0.1", 9999, "", "root", "root", "admin@test.com");

        mockObjectName = new ObjectName("org.apache.james:type=component,name=usersrepository");

        setPrivateField(agent, "mbsc", mbsc);
        setPrivateField(agent, "connector", connector);
        setPrivateField(agent, "userRepositoryMBean", mockObjectName);
    }

  @Test
    public void testAddUserSuccess() throws Exception {
        when(mbsc.invoke(any(), eq("addUser"), any(), any())).thenReturn(null);
        boolean result = agent.addUser("testuser", "testpass");
        assertTrue(result);
    }

@Test
public void testAddUserFailure() throws Exception {
    doThrow(new ReflectionException(new Exception("addUser error")))
        .when(mbsc).invoke(any(), eq("addUser"), any(), any());

    boolean result = agent.addUser("failuser", "failpass");
    assertFalse(result);
}
    @Test
    public void testGetUserList() throws Exception {
        String[] mockUsers = {"user1", "admin@test.com", "user2"};
        when(mbsc.invoke(mockObjectName, "listAllUsers", null, null)).thenReturn(mockUsers);

        var users = agent.getUserList();
        assertEquals(2, users.size());
        assertTrue(users.contains("user1"));
        assertTrue(users.contains("user2"));
        assertFalse(users.contains("admin@test.com"));
    }

    @Test
    public void testGetUserListException() throws Exception {
        when(mbsc.invoke(mockObjectName, "listAllUsers", null, null)).thenThrow(new RuntimeException("fail"));
        var users = agent.getUserList();
        assertEquals(0, users.size());
    }

    @Test
    public void testDeleteUsersSuccess() throws Exception {
        when(mbsc.invoke(any(), eq("deleteUser"), any(), any())).thenReturn(null);
        assertTrue(agent.deleteUsers(new String[]{"user1", "user2"}));
    }

   @Test
    public void testDeleteUsersFailure() throws Exception {
        when(mbsc.invoke(any(), eq("deleteUser"), any(), any()))
            .thenThrow(new MBeanException(new Exception("deleteUser error")));
        boolean result = agent.deleteUsers(new String[]{"user1"});
        assertFalse(result);
    }

    @Test
    public void testVerifyTrue() throws Exception {
        when(mbsc.invoke(any(), eq("contains"), any(), any())).thenReturn(Boolean.TRUE);
        assertTrue(agent.verify("user1"));
    }

    @Test
    public void testVerifyFalse() throws Exception {
        when(mbsc.invoke(any(), eq("contains"), any(), any())).thenReturn(Boolean.FALSE);
        assertFalse(agent.verify("user2"));
    }
@Test
public void testVerifyException() throws Exception {
    when(mbsc.invoke(any(), eq("contains"), any(), any()))
        .thenThrow(new MBeanException(new Exception("fail")));
    assertFalse(agent.verify("user3"));
}

    @Test
    public void testSetPasswordSuccess() throws Exception {
        when(mbsc.invoke(any(), eq("setPassword"), any(), any())).thenReturn(null);
        assertTrue(agent.setPassword("user1", "newpass"));
    }

    @Test
    public void testSetPasswordFailure() throws Exception {
        when(mbsc.invoke(any(), eq("setPassword"), any(), any()))
    .thenThrow(new MBeanException(new Exception("setPassword error")));
        assertFalse(agent.setPassword("user2", "newpass"));
    }

    @Test
    public void testQuitSuccess() throws Exception {
        doNothing().when(connector).close();
        assertTrue(agent.quit());
    }

    @Test
    public void testQuitFailure() throws Exception {
        doThrow(new Exception("close error")).when(connector).close();
        assertFalse(agent.quit());
    }

    // 리플렉션 유틸 메서드
    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
