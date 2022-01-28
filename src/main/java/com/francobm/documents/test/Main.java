package com.francobm.documents.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        String a = "24/02/1992";
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(a);
            System.out.println(date.toString());
        } catch (ParseException e) {
            System.out.println("invalid date!");
        }
    }
}
