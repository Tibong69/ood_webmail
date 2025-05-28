package deu.cse.spring_webmail.model;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import deu.cse.spring_webmail.model.MailSummary;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class MessageFormatterTest {

    private static final String TEST_USER_ID = "testuser";
    private static final String TEST_SENDER = "sender@example.com";
    private static final String TEST_SUBJECT = "Test Subject";
    private static final String TEST_BODY = "Test Body Content";

    private MessageFormatter formatter;
    private MimeMessage realMessage;
    private HttpServletRequest mockRequest;

    @BeforeEach
    void setUp() throws Exception {
        // Mock 객체 초기화
        mockRequest = Mockito.mock(HttpServletRequest.class);
        ServletContext mockServletContext = Mockito.mock(ServletContext.class);
        when(mockRequest.getServletContext()).thenReturn(mockServletContext);
        when(mockServletContext.getRealPath(anyString())).thenReturn("C:/temp/download");

        // 테스트 객체 생성
        formatter = new MessageFormatter(TEST_USER_ID);

        // MIME 메시지 구성
        Session session = Session.getDefaultInstance(new Properties());
        realMessage = new MimeMessage(session);
        initializeMimeMessageStructure();
    }

    private void initializeMimeMessageStructure() throws MessagingException {
        realMessage.setFrom(new InternetAddress(TEST_SENDER));
        realMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse("receiver@example.com"));
        realMessage.setSubject(TEST_SUBJECT);
        realMessage.setText(TEST_BODY);
        realMessage.setSentDate(new java.util.Date());
        realMessage.saveChanges();
    }

    @Test
    void testGetMessage_NormalMessage() throws Exception {
        formatter.setRequest(mockRequest);

        String html = formatter.getMessage(realMessage);

        assertAll("필드 값 검증",
                () -> assertEquals(TEST_SENDER, formatter.getSender()),
                () -> assertEquals(TEST_SUBJECT, formatter.getSubject()),
                () -> assertTrue(formatter.getBody().contains(TEST_BODY))
        );

        assertAll("HTML 출력 검증",
                () -> assertTrue(html.contains("보낸 사람: " + TEST_SENDER)),
                () -> assertTrue(html.contains(TEST_BODY))
        );
    }

    @Test
    void testGetMessage_WithAttachment() throws Exception {
        MimeMultipart multipart = new MimeMultipart();

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(TEST_BODY);
        multipart.addBodyPart(textPart);

        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.setContent("Test Attachment", "text/plain");
        attachmentPart.setFileName("testfile.txt");
        attachmentPart.setDisposition(MimeBodyPart.ATTACHMENT);
        multipart.addBodyPart(attachmentPart);

        realMessage.setContent(multipart);
        realMessage.saveChanges();

        formatter.setRequest(mockRequest);
        String html = formatter.getMessage(realMessage);

        assertTrue(html.contains("download?userid=" + TEST_USER_ID));
    }

    @Test
    void testGetMessageTable_EmptyMessages() {
        // given
        List<MailSummary> emptyList = new ArrayList<>();

        // when
        String html = formatter.getMessageTable(emptyList);

        // then
        assertTrue(html.contains("<table border='1'>"));
        assertTrue(html.contains("</table>"));
        assertFalse(html.contains("<tr><td>"));
    }

    @Test
    void testGetMessageTable_MultipleMessages() {
        // given
        List<MailSummary> mailList = Arrays.asList(
                new MailSummary(1, "sender1@test.com", "Subject 1", new Date()),
                new MailSummary(2, "sender2@test.com", "Subject 2", new Date())
        );

        // when
        String html = formatter.getMessageTable(mailList);

        // then
        assertAll("HTML 구조 검증",
                () -> assertTrue(html.contains("<table border='1'>")),
                () -> assertTrue(html.contains("</table>")),
                () -> assertTrue(html.split("<tr>").length >= 3) // 헤더 + 2개 행
        );

        assertAll("메일 데이터 검증",
                () -> assertTrue(html.contains("sender1@test.com")),
                () -> assertTrue(html.contains("Subject 1")),
                () -> assertTrue(html.contains("msgid=1")),
                () -> assertTrue(html.contains("msgid=2"))
        );
    }
}
