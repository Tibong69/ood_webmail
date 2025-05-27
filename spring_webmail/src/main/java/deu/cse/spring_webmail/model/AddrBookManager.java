package deu.cse.spring_webmail.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AddrBookManager {
    private final String jdbcUrl;
    private final String user;
    private final String password;
    private final UserAdminAgent userAdminAgent;

    @Autowired
    public AddrBookManager(
        @Value("${db.host}") String host,
        @Value("${db.control.port}") String port,
        @Value("${spring.datasource.username}") String user,
        @Value("${spring.datasource.password}") String password,
        UserAdminAgent userAdminAgent
    ) {
        this.jdbcUrl = String.format("jdbc:mysql://%s:%s/webmail", host, port);
        this.user = user;
        this.password = password;
        this.userAdminAgent = userAdminAgent;
    }

    public List<AddrBookRow> getRowsByUser(String userId) {
        List<AddrBookRow> dataList = new ArrayList<>();
        String sql = "SELECT email, name FROM addrbook WHERE user_id = ?";
        
        try (Connection conn = DriverManager.getConnection(jdbcUrl, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                dataList.add(new AddrBookRow(
                    userId,
                    rs.getString("email"),
                    rs.getString("name")
                ));
            }
        } catch (Exception ex) {
            log.error("DB 오류: {}", ex.getMessage());
        }
        return dataList;
    }

    public void addRow(String userId, String email, String name) {
        if (!userAdminAgent.verify(email)) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }
        String sql = "INSERT INTO addrbook (user_id, email, name) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            pstmt.setString(2, email);
            pstmt.setString(3, name);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            log.error("Insert 오류: {}", ex.getMessage());
        }
    }
}
