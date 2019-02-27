/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adzkar.app.simple.sql.parser;

/**
 *
 * @author bjgr
 */
public class Main {
    public static void main(String[] args) {
        String[] words;
        Boolean status = false;
        String err = " ";
        String s = "select * from table right join on id=id on id=id;";
        String q = s;
        s = s.toLowerCase();
//        check select
        if (!s.matches("^select.*") && !status) {
            err += "Error in your sql syntax. Missing \'Select\'";
            status = true;
        }
        words = s.split(" ", 2);
        s = words[1];
//        check column
        if (!(s.matches("\\*.*")) && !status) {
            err += "Column not found \n";
            status = true;
        }
        words = s.split(" ", 2);
        s = words[1];
//        check from
        if (!s.matches("^from.*") && !status) {
            err += "Error in your sql. Missing \'from\'";
            status = true;
        }
        words = s.split(" ",2);
        s = words[1];
//      check last words
        words = s.split(" ",2);
        if (words.length == 1) {
            if (!s.substring(s.length() - 1).equals(";") && !status) {
                err += "Missing ; in your SQL";
                status = true;
            }
//          output here without relation
            System.out.println("");
        } else {
            s = words[1];
            if (!s.matches("inner|full|left|right.*") && !status) {
                err += "Error Join Type";
                status = true;
            }
            words = s.split(" ",2);
            s = words[1];
            if(!s.matches("^join.*") && !status) {
                err += "Error in your sql syntax. Missing \'join\'";
                status = true;
            }
            words = s.split(" ",2);
            s = words[1];
//            on
            String[] tmps;
            String tmp;
            words = s.split("(?=on)");
            for(String w : words) {
                if(!w.matches("^on.*") && !status) {
                    err += "Error in your sql syntax. Missing \'on\'";
                    status = true;
                }
                tmps = w.split(" ",4);
                tmp = tmps[1];
//                check equation
                if (!tmp.matches(".*=.*") && !status) {
                    err += "Error in your sql syntax. Missing \'=\'";
                    status = true;
                }
          
            }
//            check semicolon
            tmp = words[words.length - 1];
            if (!tmp.matches(".*;$") && !status) {
                err += "Error in your sql syntax. Missing \';\'";
                status = true;
            }
        }
    
        System.out.println(err);
    }
}
