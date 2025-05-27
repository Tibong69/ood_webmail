/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package deu.cse.spring_webmail.creator;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author 박상현
 */
public class Pop3CreatorTest {
    
    private String host = "testhost";
    private String userID = "testuserid";
    private String userPW = "testpasswd";
    
    @Mock
    private HttpSession session;
    
    @InjectMocks
    private Pop3Creator creator;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        creator = new Pop3Creator();
    }

    /**
     * Test of createPopAgent method, of class Pop3Creator.
     */
    @Test
    public void testCreatePopAgent_3args() {
        creator.createPopAgent(host, userID, userPW);
    }

    /**
     * Test of createPopAgent method, of class Pop3Creator.
     */
    @Test
    public void testCreatePopAgent_HttpSession() {
        System.out.println("createPopAgent");
        
        when(session.getAttribute("host")).thenReturn(host);
        when(session.getAttribute("userid")).thenReturn(userID);
        when(session.getAttribute("passwd")).thenReturn(userPW);
        
        creator.createPopAgent(session);
    }
    
}
