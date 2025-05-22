package deu.cse.spring_webmail.control;

import deu.cse.spring_webmail.model.SmtpAgent;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WriteControllerTest {

    @InjectMocks
    private WriteController controller;

    @Mock
    private HttpSession session;

    @Mock
    private ServletContext servletContext;

    private RedirectAttributes redirectAttributes;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        redirectAttributes = new RedirectAttributesModelMap();

        // UPLOAD_FOLDER, MAX_SIZE 값을 직접 주입
        setPrivateField(controller, "UPLOAD_FOLDER", "/test/upload");
        setPrivateField(controller, "MAX_SIZE", "52428800");

        // private 필드인 session, ctx 필드에 mock 주입
        setPrivateField(controller, "session", session);
        setPrivateField(controller, "ctx", servletContext);
    }

    // 리플렉션으로 private 필드 값 설정하는 헬퍼 메서드
    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    public void testWriteMail() {
        doNothing().when(session).removeAttribute("sender");

        String result = controller.writeMail();
        assertEquals("write_mail/write_mail", result);
    }

    @Test
    public void testWriteMailDo_WithAttachment() throws Exception {
        String to = "recipient@example.com";
        String cc = "cc@example.com";
        String subj = "Test Subject";
        String body = "Test Body";

        MockMultipartFile upFile = new MockMultipartFile(
                "file1", "test.txt", "text/plain", "Hello, World!".getBytes(StandardCharsets.UTF_8));

        when(session.getAttribute("host")).thenReturn("localhost");
        when(session.getAttribute("userid")).thenReturn("user@example.com");

        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        when(servletContext.getRealPath("/test/upload")).thenReturn(tempDir.getAbsolutePath());

        String result = controller.writeMailDo(to, cc, subj, body, upFile, redirectAttributes);

        assertEquals("redirect:/main_menu", result);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("msg"));
    }

    @Test
    public void testWriteMailDo_WithoutAttachment() {
        String to = "recipient@example.com";
        String cc = "cc@example.com";
        String subj = "No Attachment";
        String body = "No file attached";
        MockMultipartFile upFile = new MockMultipartFile("file1", "", "text/plain", new byte[0]);

        when(session.getAttribute("host")).thenReturn("localhost");
        when(session.getAttribute("userid")).thenReturn("user@example.com");

        String result = controller.writeMailDo(to, cc, subj, body, upFile, redirectAttributes);

        assertEquals("redirect:/main_menu", result);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("msg"));
    }
}
