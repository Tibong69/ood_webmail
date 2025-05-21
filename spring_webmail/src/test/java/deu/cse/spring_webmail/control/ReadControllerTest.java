package deu.cse.spring_webmail.control;

import deu.cse.spring_webmail.creator.Pop3Creator;
import deu.cse.spring_webmail.model.Pop3Agent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;

public class ReadControllerTest {

    @Mock
    private HttpSession session;

    @Mock
    private HttpServletRequest request;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private Pop3Agent pop3Agent;

    @Mock
    private Pop3Creator pop3Creator;

    @InjectMocks
    private ReadController readController;

    public static void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        readController = new ReadController();
        setField(readController, "session", session);
        setField(readController, "request", request);
        setField(readController, "popCreator", pop3Creator);
        setField(readController, "DOWNLOAD_FOLDER", "/some/download/path");
    }

    @Test
    public void testShowMessage() {
        int msgId = 1;
        String expectedMsg = "This is a test message";

        System.out.println("===== 테스트 시작 =====");
        when(pop3Creator.createPopAgent(session)).thenReturn(pop3Agent);
        System.out.println("pop3Agent mock 준비됨");

        when(pop3Agent.getMessage(msgId)).thenReturn(expectedMsg);
        when(pop3Agent.getSender()).thenReturn("sender@test.com");
        when(pop3Agent.getSubject()).thenReturn("Test Subject");
        when(pop3Agent.getBody()).thenReturn("Test Body");
        System.out.println("pop3Agent 메서드 리턴값 세팅 완료");

        String view = readController.showMessage(msgId, model);
        System.out.println("컨트롤러 메소드 호출 완료, 뷰 이름: " + view);

        verify(pop3Agent).setRequest(request);
        verify(session).setAttribute("sender", "sender@test.com");
        verify(session).setAttribute("subject", "Test Subject");
        verify(session).setAttribute("body", "Test Body");
        verify(model).addAttribute("msg", expectedMsg);
        System.out.println("verify() 메서드 검증 완료");
        System.out.println("읽은 메시지 내용: " + expectedMsg);
        assertEquals("/read_mail/show_message", view);
        System.out.println("===== 테스트 종료 (성공) =====");
    }
    public void testShowMessage_printActualMessageFromModel() {
    int msgId = 1;
    String expectedMsg = "This is a test message";

    when(pop3Creator.createPopAgent(session)).thenReturn(pop3Agent);
    when(pop3Agent.getMessage(msgId)).thenReturn(expectedMsg);
    when(pop3Agent.getSender()).thenReturn("sender@test.com");
    when(pop3Agent.getSubject()).thenReturn("Test Subject");
    when(pop3Agent.getBody()).thenReturn("Test Body");

    String view = readController.showMessage(msgId, model);

    // ArgumentCaptor 사용
    ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Object> valueCaptor = ArgumentCaptor.forClass(Object.class);

    verify(model).addAttribute(keyCaptor.capture(), valueCaptor.capture());

    System.out.println("model에 추가된 속성 key = " + keyCaptor.getValue());
    System.out.println("model에 추가된 메시지 내용 = " + valueCaptor.getValue());

    assertEquals("/read_mail/show_message", view);
}
    

    @Test
    public void testDeleteMailDo_success() {
        int msgId = 1;

        when(pop3Creator.createPopAgent(session)).thenReturn(pop3Agent);
        when(pop3Agent.deleteMessage(msgId, true)).thenReturn(true);

        String view = readController.deleteMailDo(msgId, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("msg", "메시지 삭제를 성공하였습니다.");
        assertEquals("redirect:main_menu", view);
    }

    @Test
    public void testDeleteMailDo_failure() {
        int msgId = 1;

        when(pop3Creator.createPopAgent(session)).thenReturn(pop3Agent);
        when(pop3Agent.deleteMessage(msgId, true)).thenReturn(false);

        String view = readController.deleteMailDo(msgId, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("msg", "메시지 삭제를 실패하였습니다.");
        assertEquals("redirect:main_menu", view);
    }
}
