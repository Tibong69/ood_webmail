package deu.cse.spring_webmail;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringWebmailApplicationTests {
    
        //TODO
        //@Test를 사용해서 유닛 단위 테스트(junit) 메서드 작성
	@Test
	void contextLoads() {
            // Spring Boot 애플리케이션 컨텍스트가 정상적으로 로딩되는지만 확인하는 smoke test
	}

        //jacoco는 이러한 단위 테스트들을 총괄해서 평가
}
