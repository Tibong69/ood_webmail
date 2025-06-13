package deu.cse.spring_webmail;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * @author 박상현
 */

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //테스트용 포트 사용
public class LoginTest {
    @LocalServerPort 
    private int testPort;
    
    private WebDriver driver; // WebDriver 객체
    private String loginURL;
    
    //로그인 성공 유무 관련 URL
    private String adminSuccessURL = "/admin_menu";
    private String userSuccessURL = "/main_menu";
    private String loginFailURL = "login_fail";
    
    @BeforeEach //테스트 전 준비
    void setUp() {
        //로그인 URL 설정
        loginURL = "http://localhost:" + testPort + "/webmail/";
        
        //WebDriver 경로 설정
        System.setProperty("webdriver.chrome.driver", "src/test/resource/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();

        //ChromeDriver 인스턴스 생성
        driver = new ChromeDriver(options);

        //브라우저 창 최대화
        driver.manage().window().maximize();
    }
    
    
    @Test
    @DisplayName("/login 테스트: id와 암호가 정상인 경우(Admin)")
    void loginTestAdmin(){
        log.info("Admin 로그인 테스트");
        driver.get(loginURL);
        
        //HTML의 name이 userid과, password인 곳을 찾아 값 넣기
        driver.findElement(By.name("userid")).sendKeys("test1@test.com");
        driver.findElement(By.name("passwd")).sendKeys("12345");
        
        //제출 버튼(name="B1") 찾아서 누르기
        driver.findElement(By.name("B1")).click();
        
        //검증
        assertTrue(driver.getCurrentUrl().contains(adminSuccessURL));
        log.info("Admin 로그인 테스트 완료");
    }
    
    @Test
    @DisplayName("/login 테스트: id와 암호가 정상인 경우(User)")
    void loginTestUser(){
        log.info("User(일반 사용자) 로그인 테스트");
        driver.get(loginURL);
        
        driver.findElement(By.name("userid")).sendKeys("user1@test.com");
        driver.findElement(By.name("passwd")).sendKeys("12345");
        
        driver.findElement(By.name("B1")).click();
        
        assertTrue(driver.getCurrentUrl().contains(userSuccessURL));
        log.info("User(일반 사용자) 로그인 테스트 완료");
    }
    
    
    @Test
    @DisplayName("/login 테스트: id와 암호가 일치하지 않은 경우")
    void loginTestFail(){
        log.info("로그인 실패 테스트");
        driver.get(loginURL);
        
        driver.findElement(By.name("userid")).sendKeys("user1@test.com");
        driver.findElement(By.name("passwd")).sendKeys("wrongpassword");
        
        driver.findElement(By.name("B1")).click();
        
        assertTrue(driver.getCurrentUrl().contains(loginFailURL));
        log.info("로그인 실패 테스트 완료");
    }
    
    @AfterEach
    void tearDown() {
        // 테스트 완료 후 브라우저 닫기
        if (driver != null) {
            driver.quit();
        }
    }
}
