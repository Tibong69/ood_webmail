package deu.cse.spring_webmail.model;

import lombok.extern.slf4j.Slf4j;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Slf4j
public class UserAdminAgent {

    private String server;
    private int port;
    private String cwd;
    private String ROOT_ID;
    private String ROOT_PASSWORD;
    private String ADMIN_ID;

    private MBeanServerConnection mbsc;
    private JMXConnector connector;
    private ObjectName userRepositoryMBean;

    public UserAdminAgent() {
    }

    public UserAdminAgent(String server, int port, String cwd,
            String root_id, String root_pass, String admin_id) {
        this.server = server;
        this.port = 9999; //jmx.properties에 적혀있는 포트
        this.cwd = cwd;
        this.ROOT_ID = root_id;
        this.ROOT_PASSWORD = root_pass;
        this.ADMIN_ID = admin_id;

        log.debug("UserAdminAgent JMX init: server = {}, port = {}, adminId = {}", server, port, admin_id);

        try {
            connect();
        } catch (Exception e) {
            log.error("JMX 연결 실패: {}", e.getMessage());
        }
    }

    private void connect() throws Exception {
        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://127.0.0.1:9999/jmxrmi");
        java.util.Map<String, String[]> environment = new java.util.HashMap<>();
        environment.put(JMXConnector.CREDENTIALS, new String[]{"james-admin", "changeme"});  // jmxremote.password 에 적혀있는 아이디, 비번 사용

        connector = JMXConnectorFactory.connect(url, environment);
        mbsc = connector.getMBeanServerConnection();

        userRepositoryMBean = new ObjectName("org.apache.james:type=component,name=usersrepository");
        log.info("JMX 연결 성공");
    }

    public boolean addUser(String userId, String password) {
       try {
        // addUser 메소드 호출
        mbsc.invoke(
                userRepositoryMBean,
                "addUser", // jconsole에서 확인한 operation 이름
                new Object[]{userId, password},
                new String[]{"java.lang.String", "java.lang.String"}
        );

        log.debug("사용자 {} 추가 성공", userId);
        return true;  // 예외 없이 실행되면 성공
    } catch (Exception e) {
        log.debug("addUser 실패: {}", e.getMessage());
        return false;  // 예외가 발생하면 실패
    }
    }

    public List<String> getUserList() {
        List<String> users = new LinkedList<>();
        try {
            // listAllUsers 메소드 호출
            Object result = mbsc.invoke(userRepositoryMBean, "listAllUsers", null, null);

            // 반환된 결과 출력 (디버깅)
            log.debug("listAllUsers 반환 값: {}", result);

            // 반환된 결과가 Set<String>이라면 그 데이터를 List로 변환
            
            if (result instanceof String[]) {
                String[] userArray = (String[]) result;
                log.debug("ADMIN_ID = {}", ADMIN_ID);
                for (String user : userArray) {
                    log.debug("비교 중 user = {}", user);
                    if (user != null && !user.trim().equalsIgnoreCase(ADMIN_ID.trim())) {
                        users.add(user);
                    }
                }
            }
        } catch (Exception e) {
            log.error("getUserList 실패: {}", e.getMessage());
        }
        return users;
    }

    public boolean deleteUsers(String[] userList) {
        boolean status = false;
        try {
            for (String userId : userList) {
                mbsc.invoke(
                        userRepositoryMBean,
                        "deleteUser",
                        new Object[]{userId},
                        new String[]{"java.lang.String"}
                );
                log.info("삭제됨: {}", userId);
            }
            status = true;
        } catch (Exception e) {
            log.error("deleteUsers 실패: {}", e.getMessage());
        }
        return status;
    }

    public boolean verify(String userId) {
        try {
            Boolean exists = (Boolean) mbsc.invoke(
                    userRepositoryMBean,
                    "contains",
                    new Object[]{userId},
                    new String[]{"java.lang.String"}
            );
            return exists;
        } catch (Exception e) {
            log.error("verify 실패: {}", e.getMessage());
            return false;
        }
    }

    public boolean quit() {
        try {
            if (connector != null) {
                connector.close();
                log.info("JMX 연결 종료");
            }
            return true;
        } catch (Exception e) {
            log.error("quit 실패: {}", e.getMessage());
            return false;
        }
    }
}
