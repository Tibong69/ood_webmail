package deu.cse.spring_webmail.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.Date;

@Getter
@RequiredArgsConstructor
public class MailSummary {
    private final int messageNumber;
    private final String from;
    private final String subject;
    private final Date sentDate;
}
