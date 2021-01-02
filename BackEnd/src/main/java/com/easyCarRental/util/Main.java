package com.easyCarRental.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static java.time.temporal.ChronoUnit.HOURS;

public class Main {
    public static void main(String[] args) {


        LocalDateTime parse = LocalDateTime.parse("2020-11-27T08:00:00");

        LocalDateTime now = LocalDateTime.now();

        System.out.println(HOURS.between(parse,
                now));

    }
}
