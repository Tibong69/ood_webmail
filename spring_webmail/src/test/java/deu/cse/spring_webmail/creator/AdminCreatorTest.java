/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package deu.cse.spring_webmail.creator;

import deu.cse.spring_webmail.model.UserAdminAgent;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
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

    
    public AdminCreatorTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        creator = new AdminCreator();
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of creatAdminAgent method, of class AdminCreator.
     */
    @Test
    public void testCreatAdminAgent() {
        System.out.println("creatAdminAgent");
        creator.creatAdminAgent(server, port, cwd, root_id, root_pass, admin_id);
        
        // TODO review the generated test code and remove the default call to fail.
    }
    
}
