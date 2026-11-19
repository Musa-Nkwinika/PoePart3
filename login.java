/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poepart3;

import java.util.Scanner;

/**
 *
 * @author Student
 */
public class login {
    private Object userame;
    private Object userName;
    public boolean loginUser(String name,String password){
        Object passWord = null;
        if(name.equals(userName) && password.equals(passWord)){
            return true;
        }else{
            return false;
        }
    }

    public String returnLoginStatus(boolean loginStatus){
        if(loginStatus){
            return "Login success";
        }else{
            return "Login failed";
        }
    }
}

 