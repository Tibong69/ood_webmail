/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package deu.cse.spring_webmail.creator;

import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author 박상현
 */
public class AdminCreatorTest {
    
    private String server = "";
    private int port = 9997;
    private String cwd = "";
    private String root_id = "";
    private String root_pass = "";
    private String admin_id = ""; 
    
    @Mock
    private ServletContext ctx;
    
    @InjectMocks
    private AdminCreator creator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        creator = new AdminCreator();
    }
    
    /**
     * Test of creatAdminAgent method, of class AdminCreator.
     */
    @Test
    public void testCreatAdminAgent() {
        System.out.println("creatAdminAgent");
        creator.creatAdminAgent(server, port, cwd, root_id, root_pass, admin_id);
    }
    
}
