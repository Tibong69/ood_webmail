/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.model;

import jakarta.mail.Address;
import jakarta.mail.FetchProfile;
import jakarta.mail.Flags;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;
import java.util.Properties;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author skylo
 */
@Slf4j
@NoArgsConstructor        // 기본 생성자 생성
public class Pop3Agent {

    private static final String FALSE_STRING = "false";
    private static final String STORE_FOLDER = "INBOX";
    
    @Getter @Setter private String host;
    @Getter @Setter private String userid;
    @Getter @Setter private String password;
    @Getter @Setter private Store store;
    @Getter @Setter private String excveptionType;
    @Getter @Setter private HttpServletRequest request;
    
    // 220612 LJM - added to implement REPLY
    @Getter
    private String sender;
    @Getter
    private String subject;
    @Getter
    private String body;

    public Pop3Agent(String host, String userid, String password) {
        this.host = host;
        this.userid = userid;
        this.password = password;
    }

    public boolean validate() {
        boolean status = false;

        try {
            status = connectToStore();
            store.close();
        } catch (Exception ex) {
            log.error("Pop3Agent.validate() error : " + ex);
            status = false;  // for clarity
        }
        return status;
    }

    public boolean deleteMessage(int msgid, boolean really_delete) {
        boolean status = false;

        if (!connectToStore()) {
            return status;
        }

        try {
            // Folder 설정
            Folder folder = store.getFolder(STORE_FOLDER);
            folder.open(Folder.READ_WRITE);

            // Message에 DELETED flag 설정
            Message msg = folder.getMessage(msgid);
            msg.setFlag(Flags.Flag.DELETED, really_delete);

            // 폴더에서 메시지 삭제
            folder.close(true);  // expunge == true
            store.close();
            status = true;
        } catch (Exception ex) {
            log.error("deleteMessage() error: {}", ex.getMessage());
        }
        return status;
    }

    /*
     * 페이지 단위로 메일 목록을 보여주어야 함.
     */
    public String getMessageList() {
        String result = "";
        List<MailSummary> mailSummaries = getAllMailSummaries();

        if (mailSummaries.isEmpty()) {
            log.error("메일이 없습니다.");
            return "메일이 없습니다.";
        }

        MessageFormatter formatter = new MessageFormatter(userid);
        result = formatter.getMessageTable(mailSummaries);

        return result;
    }

    public String getMessage(int n) {
        String result = "POP3  서버 연결이 되지 않아 메시지를 볼 수 없습니다.";

        if (!connectToStore()) {
            log.error("POP3 connection failed!");
            return result;
        }

        try {
            Folder folder = store.getFolder(STORE_FOLDER);
            folder.open(Folder.READ_ONLY);

            Message message = folder.getMessage(n);

            MessageFormatter formatter = new MessageFormatter(userid);
            formatter.setRequest(request);  // 210308 LJM - added
            result = formatter.getMessage(message);
            sender = formatter.getSender();  // 220612 LJM - added
            subject = formatter.getSubject();
            body = formatter.getBody();

            folder.close(true);
            store.close();
        } catch (Exception ex) {
            log.error("Pop3Agent.getMessageList() : exception = {}", ex);
            result = "Pop3Agent.getMessage() : exception = " + ex;
        }
        return result;
    }

    protected boolean connectToStore() {
        boolean status = false;
        Properties props = System.getProperties();
        // https://jakarta.ee/specifications/mail/2.1/apidocs/jakarta.mail/jakarta/mail/package-summary.html
        props.setProperty("mail.pop3.host", host);
        props.setProperty("mail.pop3.user", userid);
        props.setProperty("mail.pop3.apop.enable", FALSE_STRING);
        props.setProperty("mail.pop3.disablecapa", "true");  // 200102 LJM - added cf. https://javaee.github.io/javamail/docs/api/com/sun/mail/pop3/package-summary.html
        props.setProperty("mail.debug", FALSE_STRING);
        props.setProperty("mail.pop3.debug", FALSE_STRING);

        Session session = Session.getInstance(props);
        session.setDebug(false);

        try {
            store = session.getStore("pop3");
            store.connect(host, userid, password);
            status = true;
        } catch (Exception ex) {
            log.error("connectToStore 예외: {}", ex.getMessage());
        }
        return status;
    }

    public List<MailSummary> getAllMailSummaries() {
        List<MailSummary> summaries = new ArrayList<>();
        if (!connectToStore()) {
            log.error("POP3 연결 실패");
            return summaries;
        }

        try {
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
            Message[] messages = folder.getMessages();

            FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);
            folder.fetch(messages, fp);

            for (Message msg : messages) {
                String from = extractSender(msg);
                String subject = msg.getSubject() != null ? msg.getSubject() : "(제목 없음)";
                Date sentDate = msg.getSentDate() != null ? msg.getSentDate() : new Date(0);

                summaries.add(new MailSummary(
                        msg.getMessageNumber(),
                        from,
                        subject,
                        sentDate
                ));
            }

            folder.close(false);
            store.close();
            return summaries;
        } catch (Exception ex) {
            log.error("메일 가져오기 실패: {}", ex.getMessage());
            return summaries;
        }
    }

    private String extractSender(Message msg) {
        try {
            Address[] froms = msg.getFrom();
            if (froms != null && froms.length > 0) {
                return froms[0].toString();
            }
        } catch (Exception e) {
            log.error("발신자 추출 실패: {}", e.getMessage());
        }
        return "(알 수 없음)";
    }

}
