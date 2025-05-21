/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package deu.cse.spring_webmail.control;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;


/**
 *
 * @author 박상현
 */
public class FileDownloadControllerTest {
    @Mock
    private ServletContext ctx;
    
    @Mock
    private HttpSession session;
        
    //테스트 기본값 설정
    private static String testDownloadFolder = "testDownload"; //"C:\Users\사용자이름\AppData\Local\Temp\testDownload"
    private static String userID = "tester";
    private static String fileName = "dltestfile.txt";
    
    //임시 디렉토리 및 파일 경로
    private static Path testTmpDirPath;
    private static Path testerDirPath;
    private static Path testFilePath;
    
    @InjectMocks
    private FileDownloadController fileDownloadController;
    
    //테스팅전 설정 (다운로드 디렉토리, 다운로드 파일 설정)
    @BeforeAll
    public static void setUpClass() throws IOException {
        //테스팅용 임시 다운로드 디렉토리 경로 설정 및 생성
        testTmpDirPath = Files.createTempDirectory(testDownloadFolder);
        
        //testDownload\tester 디렉토리 생성 (다운로드 할 파일 보관용)
        testerDirPath = testTmpDirPath.resolve(userID);
        Files.createDirectory(testerDirPath);
        
        
         //다운로드 테스팅용 txt파일 생성 및 작성
        testFilePath = testerDirPath.resolve(fileName);
        Files.write(testFilePath, "This file was created for testing".getBytes());
    }
    
    //FileDownloadController의 필드 값 주입용 메서드
    public static void setField(Object target, String filedName, Object value) throws Exception{
        Field field = target.getClass().getDeclaredField(filedName);
        field.setAccessible(true);
        field.set(target, value);
    }
    
    //FileDownloadController의 필드 값 주입
    @BeforeEach
    public void setUp() throws Exception{
        MockitoAnnotations.openMocks(this);
        fileDownloadController = new FileDownloadController();
        setField(fileDownloadController, "downloadFolder", testDownloadFolder);
        setField(fileDownloadController, "ctx", ctx);
    }
    
    @Test
    public void testDownload() throws Exception{
        System.out.println("===== 테스팅 시작 =====");
        
        //ctx.getRealpath시 전달할 값(디렉토리 경로) 설정
        given(ctx.getRealPath(any())).willReturn(testDownloadFolder);
        
        //메서드 테스팅
        ResponseEntity<?> response = fileDownloadController.download(userID, fileName);
        
        
        
        System.out.println("===== 테스팅 종료 =====");
    }
    
}