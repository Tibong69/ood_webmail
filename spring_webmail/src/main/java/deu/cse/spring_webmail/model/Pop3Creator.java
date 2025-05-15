/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.model;

import jakarta.servlet.http.HttpSession;


/**
 *
 * @author 박상현
 */
public class Pop3Creator {
    
    public Pop3Agent createPopAgent(String host, String userId, String pw){
        return new Pop3Agent(host, userId, pw);
    } 
    
    public Pop3Agent createPopAgent(HttpSession session){
        return new Pop3Agent(
                (String) session.getAttribute("host"), 
                (String) session.getAttribute("userid"), 
                (String) session.getAttribute("passwd"));
    } 
}
