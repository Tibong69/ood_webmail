/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.creator;

import deu.cse.spring_webmail.model.UserAdminAgent;

/**
 *
 * @author 박상현
 */
public class AdminCreator {
    
    public UserAdminAgent creatAdminAgent(String server, int port, String cwd,
            String root_id, String root_pass, String admin_id){
        
        UserAdminAgent adminAgent = new UserAdminAgent(server, port, cwd, root_id, root_pass, admin_id);
        return adminAgent;
    }
    
}
