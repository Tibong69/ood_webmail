package deu.cse.spring_webmail.control;

import deu.cse.spring_webmail.model.AddrBookManager;
import deu.cse.spring_webmail.model.AddrBookRow;
import deu.cse.spring_webmail.model.UserAdminAgent;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AddressBookController {

    private final AddrBookManager addrBookManager;
    private final UserAdminAgent userAdminAgent;
    
    @GetMapping("/addrbook")
    public String addressBook(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userid");
        List<AddrBookRow> contacts = addrBookManager.getRowsByUser(userId);
        model.addAttribute("dataRows", contacts);
        return "addrbook/addrbook";
    }

    @GetMapping("/addrbook/insert")
    public String showInsertForm() {
        return "addrbook/addrbook_insert";  // /WEB-INF/views/addrbook/addrbook_insert.jsp 매핑
    }

    @PostMapping("/addrbook/insert")
    public String insertAddressBook(
        HttpSession session,
        @RequestParam String email,
        @RequestParam String name,
        RedirectAttributes attrs
    ) {
        String userId = (String) session.getAttribute("userid");
        
        boolean exists = userAdminAgent.verify(email);

        if (!exists) {
            attrs.addFlashAttribute("msg", "존재하지 않는 사용자(ID)입니다.");
            return "redirect:/addrbook/insert";
        }

        addrBookManager.addRow(userId, email, name);
        return "redirect:/addrbook";
    }

}
