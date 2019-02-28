/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adzkar.app.simple.sql.parser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author bjgr
 */
public class Main {
    public static void main(String[] args) {
//        init
        List<String> tables = new ArrayList<>();
        List<List<String>> attr = new ArrayList<>();
        String[] tmps;
        String tmp = " ";
        String output = " ";
        String columns = " ";
//        read json
        JSONParser parser = new JSONParser();
        
        String path = "src/main/java/com/adzkar/app/simple/sql/parser/";
        
        try {

            Object obj = parser.parse(new FileReader(path+"data.json"));

            JSONObject jsonObject = (JSONObject) obj;
            
            
            JSONArray table = (JSONArray) jsonObject.get("tables");
            for (int i = 0; i < table.size(); i++) {
                JSONObject o = (JSONObject) table.get(i);
                String a = (String) o.get("name");
                tables.add(a.toLowerCase());
                
                JSONObject j = (JSONObject) table.get(i);
                JSONArray b = (JSONArray) j.get("attr");
                List<String> c = new ArrayList<>();
                for (int k = 0; k < b.size(); k++) {
                    c.add((String) b.get(k));
                }
                attr.add(c);
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        check query
        String[] words;
        Boolean status = false;
        String err = " ";
        String s = "select id_post,title from post join tag_post on tag_post.id_post=post.id_post;";
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
        if (!(s.matches("\\*.*"))) {
            tmps = s.split(" ",2);
            columns = tmps[0];
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
//        check table
        words = s.split(" ",2);
        tmp = words[0];
        if (tmp.substring(tmp.length()-1).equals(";")) {
            tmp = tmp.replace(";","");
        }
        if (!tables.contains(tmp) && !status) {
            err += "Table Not Found";
            status = true;
        }
        String abc = words[0];
        if (abc.substring(abc.length()-1).equals(";") && tables.contains(tmp)) {
            System.out.println("Table: "+tmp);
            int i = tables.indexOf(tmp);
            System.out.println(attr.get(i));
        }
//        check column
        boolean found = false;
        int n = 0;
        tmps = columns.split(",");
        for (int i = 0; i < tmps.length; i++) {
            int j = 0;
            while(j < attr.size() && n < tmps.length) {
                if (attr.get(j).contains(tmps[i])) {
                   n++;
                   tmps[i] = " ";
                }
                j++;
            }
            if (n == tmps.length) {
               found = true; 
            }
        }
        if (!(s.matches("\\*.*")) && !status && !found) {
            err += "Column not found \n";
            status = true;
        }
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
            words = s.split("(?=join)");
            for (String word : words) {
                if(!s.matches("^join.*") && !status) {
                    err += "Error in your sql syntax. Missing \'join\'";
                    status = true;
                }
//              check table
                words = s.split(" ",2);
                s = words[1];
                tmps = s.split(" ");
                tmp = tmps[0];
                if (!tables.contains(tmp) && !status) {
                    err += "Table Not Found";
                    status = true;
                }
                if (!s.matches(".*on.*")) {
                    err += "Missing 'on' on your sql";
                    status = true;
                }
                if(!s.matches(".*=.*")) {
                    err += "Missing '=' on your sql";
                    status = true;
                }
                s = tmps[2];
                words = s.split("=");
                tmps = words[0].split("\\.");
                String x = tmps[1];
                int nId = StringUtils.countMatches(s, x);
                if (nId != 2) {
                    err += "Missing ID:Not Found\n";
                    status = true;
                }
                for (String w : words) {
                    tmps = w.split("\\.");
                    if (!tables.contains(tmps[0]) && !status) {
                        err += "Table Not Found";
                        status = true;
                    }
                    else {
                        int i = 0;
                        while (i < tmps.length && !tables.get(i).equals(tmps[0])) {
                            i++;
                        }
                        if (!tables.get(i).contains(x) && status) {
                            err = "Missing ID:Not Found\n";
                            status = true;
                        }
        
                    }
                }
            }
            
//          check semicolon
            tmp = words[words.length - 1];
            if (!tmp.matches(".*;$") && !status) {
                err = "Error in your sql syntax. Missing \';\'";
                status = true;
            }
            if (!status) {
                String[] c;
                tmps = s.split("=");
                for(String x : tmps) {
                    c = x.split("\\.");
                    System.out.println("Table: "+c[0]);
                    System.out.println("Attr: "+attr.get(tables.indexOf(c[0])));
                }
            }
        }
    
        System.out.println(err);
    }
}
