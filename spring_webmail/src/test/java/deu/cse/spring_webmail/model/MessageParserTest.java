/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.model;

/**
 *
 * @author gpy11
 */
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;

class MessageParserTest {

    private MimeMessage message;
    private final String userid = "testuser";

    @BeforeEach
    void setup() throws Exception {
        // 메시지 초기화
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props);
        message = new MimeMessage(session);
        message.setFrom(new InternetAddress("sender@example.com"));
        message.setRecipients(Message.RecipientType.TO, "receiver@example.com");
        message.setSubject("Test Subject");
        message.setText("This is the body");
        message.setSentDate(new java.util.Date());
    }

    @Test
    void testParse_withBody() {
        MessageParser parser = new MessageParser(message, userid);

        boolean result = parser.parse(true);

        assertTrue(result);
        assertEquals("sender@example.com", parser.getFromAddress());
        assertEquals("receiver@example.com", parser.getToAddress());
        assertEquals("Test Subject", parser.getSubject());
        assertTrue(parser.getBody().contains("This is the body"));
    }

    @Test
    void testParse_withoutBody() {
        MessageParser parser = new MessageParser(message, userid);
        boolean result = parser.parse(false);

        assertTrue(result);
        assertNotNull(parser.getSubject());
        assertNull(parser.getBody());
    }

    @Test
    void testDownloadDirCreated_withServletRequest() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getServletContext()).thenReturn(Mockito.mock(jakarta.servlet.ServletContext.class));
        Mockito.when(request.getServletContext().getRealPath(Mockito.anyString())).thenReturn("C:/temp/download");

        MessageParser parser = new MessageParser(message, userid, request);

        assertTrue(parser.getDownloadTempDir().contains("C:/temp/download"));
    }
}