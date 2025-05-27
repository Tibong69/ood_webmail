package deu.cse.spring_webmail.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 *
 * @author psm
 */
@AllArgsConstructor
@Builder
@Getter
public class AddrBookRow {
    private String user_id;
    private String email;
    private String name;
}
