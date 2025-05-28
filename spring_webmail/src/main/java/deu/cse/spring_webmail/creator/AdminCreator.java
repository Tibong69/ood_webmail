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
            String rootId, String rootPass, String adminId){
        
        UserAdminAgent adminAgent = new UserAdminAgent(server, port, cwd, rootId, rootPass, adminId);
        return adminAgent;
    }
    
}
