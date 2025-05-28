package deu.cse.spring_webmail.model;

import jakarta.mail.Message;
import jakarta.mail.Transport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.junit.jupiter.api.Assertions.*;

class SmtpAgentTest {

    private SmtpAgent smtpAgent;

    @BeforeEach
    void setUp() {
        smtpAgent = new SmtpAgent("mail.example.com", "testuser@example.com");
        smtpAgent.setCc("");
    }

    // Constructor 테스트
    @Test
    void constructor_ShouldSetHostAndUserid() {
        assertEquals("mail.example.com", smtpAgent.getHost());
        assertEquals("testuser@example.com", smtpAgent.getUserid());
    }

    // Getter/Setter 테스트
    @Test
    void gettersAndSetters_ShouldWorkCorrectly() {
        smtpAgent.setTo("recipient@example.com");
        smtpAgent.setCc("cc@example.com");
        smtpAgent.setSubj("Test Subject");
        smtpAgent.setBody("Test Body");
        smtpAgent.setFile1("testfile.txt");
        smtpAgent.getAttachments().add("attachment1.txt");

        assertAll(
            () -> assertEquals("recipient@example.com", smtpAgent.getTo()),
            () -> assertEquals("cc@example.com", smtpAgent.getCc()),
            () -> assertEquals("Test Subject", smtpAgent.getSubj()),
            () -> assertEquals("Test Body", smtpAgent.getBody()),
            () -> assertEquals("testfile.txt", smtpAgent.getFile1()),
            () -> assertTrue(smtpAgent.getAttachments().contains("attachment1.txt"))
        );
    }

    // sendMessage() 테스트 (성공 케이스)
    @Test
    void sendMessage_ShouldReturnTrueWhenSuccessful() {
        try (MockedStatic<Transport> mockedTransport = mockStatic(Transport.class)) {
            smtpAgent.setTo("recipient@example.com");
            smtpAgent.setSubj("Test Subject");
            smtpAgent.setBody("Test Body");
            smtpAgent.setCc("");

            mockedTransport.when(() -> Transport.send(any(Message.class)))
                          .thenAnswer(invocation -> null);

            assertTrue(smtpAgent.sendMessage());
        }
    }

    // sendMessage() 첨부파일 테스트
    @Test
    void sendMessage_WithAttachments_ShouldIncludeAttachments() {
        try (MockedStatic<Transport> mockedTransport = mockStatic(Transport.class)) {
            smtpAgent.setTo("recipient@example.com");
            smtpAgent.setSubj("Test Subject");
            smtpAgent.setBody("Test Body");
            smtpAgent.setCc("");
            smtpAgent.getAttachments().add("testfile.txt");

            mockedTransport.when(() -> Transport.send(any(Message.class)))
                          .thenAnswer(invocation -> null);

            assertTrue(smtpAgent.sendMessage());
        }
    }

    // sendMessage() 실패 테스트 (예외 발생)
    @Test
    void sendMessage_ShouldReturnFalseOnException() {
        try (MockedStatic<Transport> mockedTransport = mockStatic(Transport.class)) {
            smtpAgent.setTo("invalid-address");

            mockedTransport.when(() -> Transport.send(any(Message.class)))
                          .thenThrow(new RuntimeException("SMTP Error"));

            assertFalse(smtpAgent.sendMessage());
        }
    }
}
