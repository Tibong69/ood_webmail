package deu.cse.spring_webmail.model;

import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class Pop3AgentTest {

    private Pop3Agent pop3Agent;
    private Store mockStore;
    private Folder mockFolder;
    private Message mockMessage;

    @BeforeEach
    void setUp() throws Exception {
        pop3Agent = new Pop3Agent("test.host", "testuser", "testpass");

        // 목 객체 설정
        mockStore = mock(Store.class);
        mockFolder = mock(Folder.class);
        mockMessage = mock(Message.class);

        // Store 연결되도록 설정
        doNothing().when(mockStore).connect(anyString(), anyString(), anyString());
        when(mockStore.getFolder(anyString())).thenReturn(mockFolder);

        // Folder 동작 설정
        when(mockFolder.getMessage(anyInt())).thenReturn(mockMessage);
        when(mockFolder.getMessages()).thenReturn(new Message[]{mockMessage});

        // 메시지 포맷터는 진짜 객체 사용
        pop3Agent.setStore(mockStore);
    }

    @Test
    void testValidateSuccess() throws Exception {
        Pop3Agent agent = spy(new Pop3Agent("test.host", "testuser", "testpass"));

        // connectToStore()가 true를 반환하도록 설정
        doReturn(true).when(agent).connectToStore();

        // store.close() 호출 시 NPE 방지용 더미 Store 설정
        Store dummyStore = mock(Store.class);
        agent.setStore(dummyStore);

        boolean result = agent.validate();

        assertTrue(result);  // 기대값 true
    }

    @Test
    void testGetMessageListReturnsFormattedList() throws Exception {
        Pop3Agent agent = spy(pop3Agent);
        doReturn(true).when(agent).connectToStore();
        doReturn(mockStore).when(agent).getStore();

        when(mockFolder.isOpen()).thenReturn(true);
        doNothing().when(mockFolder).fetch(any(), any());

        when(mockFolder.getMessages()).thenReturn(new Message[]{mockMessage});
        when(mockStore.getFolder("INBOX")).thenReturn(mockFolder);

        doNothing().when(mockFolder).open(Folder.READ_ONLY);
        doNothing().when(mockFolder).close(true);
        doNothing().when(mockStore).close();

        String result = agent.getMessageList();

        assertNotNull(result);
    }

    @Test
    void testGetMessageReturnsContent() throws Exception {
        Pop3Agent agent = spy(pop3Agent);
        doReturn(true).when(agent).connectToStore();
        doReturn(mockStore).when(agent).getStore();

        when(mockFolder.getMessage(1)).thenReturn(mockMessage);
        when(mockStore.getFolder("INBOX")).thenReturn(mockFolder);

        doNothing().when(mockFolder).open(Folder.READ_ONLY);
        doNothing().when(mockFolder).close(true);
        doNothing().when(mockStore).close();

        agent.setRequest(mock(HttpServletRequest.class));
        String result = agent.getMessage(1);

        assertNotNull(result);
    }

    @Test
    void testDeleteMessageSuccess() throws Exception {
        Pop3Agent agent = spy(pop3Agent);
        doReturn(true).when(agent).connectToStore();
        doReturn(mockStore).when(agent).getStore();

        when(mockStore.getFolder("MAILBOX_INBOX")).thenReturn(mockFolder);
        when(mockFolder.getMessage(1)).thenReturn(mockMessage);

        doNothing().when(mockFolder).open(Folder.READ_WRITE);
        doNothing().when(mockMessage).setFlag(any(), anyBoolean());
        doNothing().when(mockFolder).close(true);
        doNothing().when(mockStore).close();

        boolean result = agent.deleteMessage(1, true);

        assertTrue(result);
    }

    @Test
    void testValidateFailsWhenConnectionFails() {
        Pop3Agent agent = spy(new Pop3Agent("invalid.host", "id", "pw"));
        doReturn(false).when(agent).connectToStore();
        assertFalse(agent.validate());
    }

    @Test
    void testDeleteMessageFalseFlag() throws Exception {
        Pop3Agent agent = spy(pop3Agent);
        doReturn(true).when(agent).connectToStore();
        doReturn(mockStore).when(agent).getStore();

        when(mockStore.getFolder("MAILBOX_INBOX")).thenReturn(mockFolder);
        when(mockFolder.getMessage(1)).thenReturn(mockMessage);

        doNothing().when(mockFolder).open(Folder.READ_WRITE);
        doNothing().when(mockMessage).setFlag(any(), eq(false));
        doNothing().when(mockFolder).close(true);
        doNothing().when(mockStore).close();

        boolean result = agent.deleteMessage(1, false);

        assertTrue(result);
    }

}
