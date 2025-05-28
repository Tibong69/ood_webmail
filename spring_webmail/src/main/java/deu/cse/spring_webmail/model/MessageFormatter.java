/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.model;

import jakarta.mail.Message;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author skylo
 */
@Slf4j
@RequiredArgsConstructor
public class MessageFormatter {

    @NonNull
    private String userid;  // 파일 임시 저장 디렉토리 생성에 필요
    private HttpServletRequest request = null;

    // 220612 LJM - added to implement REPLY
    @Getter
    private String sender;
    @Getter
    private String subject;
    @Getter
    private String body;

    //HTML Tag
    String htmlBR = "<br>";
    String htmlEndTD = "</td>";

    public String getMessageTable(List<MailSummary> mailList) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("<table border='1'>")
              .append("<tr><th>No.</th><th>보낸 사람</th><th>제목</th><th>날짜</th><th>삭제</th></tr>");

        for (MailSummary mail : mailList) {
            buffer.append("<tr>")
                  .append("<td>").append(mail.getMessageNumber()).append(htmlEndTD)
                  .append("<td id='sender'>").append(mail.getFrom()).append(htmlEndTD)
                  .append("<td id='subject'><a href='show_message?msgid=")
                  .append(mail.getMessageNumber()).append("'>")
                  .append(mail.getSubject()).append("</a></td>")
                  .append("<td id='date'>").append(mail.getSentDate()).append(htmlEndTD)
                  .append("<td id='delete'><a href='delete_mail.do?msgid=")
                  .append(mail.getMessageNumber())
                  .append("' onclick=\"return confirm('정말 삭제하시겠습니까?')\">삭제</a></td>")
                  .append("</tr>");

        }

        buffer.append("</table>");
        return buffer.toString();
    }


    public String getMessage(Message message) {
        StringBuilder buffer = new StringBuilder();

        MessageParser parser = new MessageParser(message, userid, request);
        parser.parse(true);

        sender = parser.getFromAddress();
        subject = parser.getSubject();
        body = parser.getBody();

        buffer.append("보낸 사람: " + parser.getFromAddress() + htmlBR);
        buffer.append("받은 사람: " + parser.getToAddress() + htmlBR);
        buffer.append("Cc &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; : " + parser.getCcAddress() + htmlBR);
        buffer.append("보낸 날짜: " + parser.getSentDate() + htmlBR);
        buffer.append("제 &nbsp;&nbsp;&nbsp;  목: " + parser.getSubject() + htmlBR + " <hr>");

        buffer.append(parser.getBody());

        List<String> attachedFiles = parser.getFileNames();
        if (!attachedFiles.isEmpty()) {
            buffer.append(htmlBR + " <hr> 첨부파일: ");
            for (String fileName : attachedFiles) {
                buffer.append("<a href=download"
                        + "?userid=" + this.userid
                        + "&filename=" + fileName.replace(" ", "%20")
                        + " target=_top> " + fileName + "</a> ");
            }
            buffer.append(htmlBR);

        }
        return buffer.toString();
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
}
