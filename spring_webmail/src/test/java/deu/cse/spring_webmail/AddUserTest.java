package deu.cse.spring_webmail;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 *
 * @author 박상현
 */

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //테스트용 포트 사용
public class AddUserTest {
    @LocalServerPort 
    private int testPort;
    
    private WebDriver driver; // WebDriver 객체
    private String loginURL;
    
    //사용자 추가 성공 유무 메시지
    private String successMSG = "추가를 성공하였습니다.";
    private String failMSG = "추가를 실패하였습니다.";    
    @BeforeEach //테스트 전 준비
    void setUp() {
        loginURL = "http://localhost:" + testPort + "/webmail/";
        
        System.setProperty("webdriver.chrome.driver", "src/test/resource/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
        
        //add_user 페이지까지 이동
        driver.get(loginURL);
        
        //로그인 페이지 -> 관리자 페이지
        driver.findElement(By.name("userid")).sendKeys("test1@test.com");
        driver.findElement(By.name("passwd")).sendKeys("12345");
        driver.findElement(By.name("B1")).click();
        
        //관리자 페이지 -> 사용자 추가 페이지
        driver.findElement(By.linkText("사용자 추가")).click();
    }
    
    @Test
    @DisplayName("/add_user 테스트: id와 암호가 정상인 경우(Admin)")   
    void loginTestSuccess(){
        log.info("사용자 추가 성공 테스트");
        
        //추가할 사용자 정보 입력
        driver.findElement(By.name("id")).sendKeys("user01@test.com");
        driver.findElement(By.name("password")).sendKeys("12345");
        
        //등록
        driver.findElement(By.name("register")).click();
        
        //메시지 출력 기다리기
         new WebDriverWait(driver, Duration.ofSeconds(1))
            .until(ExpectedConditions.alertIsPresent());

        Alert alert = driver.switchTo().alert();
        String alertMessage = alert.getText();
        
        assertTrue(alertMessage.contains(successMSG));

        log.info("사용자 추가 성공 테스트 완료");
    }
    
    @Test
    @DisplayName("/login 테스트: 잘못된 id(도메인 주소)인 경우")
    void loginTestFail(){
        log.info("사용자 추가 실패 테스트");
        
        driver.findElement(By.name("id")).sendKeys("user02@wrongdomain.com");
        driver.findElement(By.name("password")).sendKeys("12345");
        
        driver.findElement(By.name("register")).click();
        
        new WebDriverWait(driver, Duration.ofSeconds(1))
            .until(ExpectedConditions.alertIsPresent());

        Alert alert = driver.switchTo().alert();
        String alertMessage = alert.getText();
        assertTrue(alertMessage.contains(failMSG));

        log.info("사용자 추가 실패 테스트 완료");
    }
    
    @AfterEach
    void tearDown() {
        // 테스트 완료 후 브라우저 닫기
        if (driver != null) {
            driver.quit();
        }
    }
}
