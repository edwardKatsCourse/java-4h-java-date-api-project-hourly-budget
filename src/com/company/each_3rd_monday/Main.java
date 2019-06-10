package com.company.each_3rd_monday;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

public class Main {

    public static void main(String[] args) {
        //for by:
        //- days
        //- weeks

        //if (currentDay == Monday)


        int year = 2019;
        byDayCounter(year);
        correctSolution(year);
    }

    //11
    private static void byDayCounter(int year) {
        int mondaysCount = 0; //since the start of the year

        LocalDate yearStart = LocalDate.of(year, Month.FEBRUARY, 1);

        while (mondaysCount < 3) { //1

            if (yearStart.getDayOfWeek() == DayOfWeek.SATURDAY) { //2
                mondaysCount++;
            }

            if (mondaysCount == 3) { //2
                break;
            }

            yearStart = yearStart.plusDays(1);
        }

        System.out.println(yearStart);
    }

    private static void correctSolution(int year) {
        LocalDate yearStart = LocalDate.of(year, Month.FEBRUARY, 1);

        for (int i = 0; i < 3; i++) {
            yearStart = yearStart.with(TemporalAdjusters.next(DayOfWeek.SATURDAY));
        }

        System.out.println(yearStart);


    }
}
