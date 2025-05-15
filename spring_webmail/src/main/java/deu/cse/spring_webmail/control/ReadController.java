/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.control;

import deu.cse.spring_webmail.model.Pop3Agent;
import jakarta.mail.internet.MimeUtility;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Prof.Jong Min Lee
 */
@Controller
@PropertySource("classpath:/system.properties")
@Slf4j
public class ReadController {

    @Autowired
    private ServletContext ctx;
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;
    @Value("${file.download_folder}")
    private String DOWNLOAD_FOLDER;

    @GetMapping("/show_message")
    public String showMessage(@RequestParam Integer msgid, Model model) {
        log.debug("download_folder = {}", DOWNLOAD_FOLDER);
        
        Pop3Agent pop3 = new Pop3Agent();
        pop3.setHost((String) session.getAttribute("host"));
        pop3.setUserid((String) session.getAttribute("userid"));
        pop3.setPassword((String) session.getAttribute("passwd"));
        pop3.setRequest(request);
        
        String msg = pop3.getMessage(msgid);
        session.setAttribute("sender", pop3.getSender());  // 220612 LJM - added
        session.setAttribute("subject", pop3.getSubject());
        session.setAttribute("body", pop3.getBody());
        model.addAttribute("msg", msg);
        return "/read_mail/show_message";
    }

    @GetMapping("/delete_mail.do")
    public String deleteMailDo(@RequestParam("msgid") Integer msgId, RedirectAttributes attrs) {
        log.debug("delete_mail.do: msgid = {}", msgId);
        
        String host = (String) session.getAttribute("host");
        String userid = (String) session.getAttribute("userid");
        String password = (String) session.getAttribute("passwd");

        Pop3Agent pop3 = new Pop3Agent(host, userid, password);
        boolean deleteSuccessful = pop3.deleteMessage(msgId, true);
        if (deleteSuccessful) {
            attrs.addFlashAttribute("msg", "메시지 삭제를 성공하였습니다.");
        } else {
            attrs.addFlashAttribute("msg", "메시지 삭제를 실패하였습니다.");
        }
        
        return "redirect:main_menu";
    }
}
