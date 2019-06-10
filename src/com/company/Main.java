package com.company;

import com.sun.org.apache.regexp.internal.RE;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final Integer REGULAR_HOURS_PER_DAY = 9;
    private static final Integer OVERTIME_RATE_PERCENT = 125;
    private static final Integer MAX_OVERTIME_HOURS = 2;

    private static final Integer REGULAR_WORKING_HOURS_PER_WEEK = 55;
    private static final DayOfWeek START_WEEK = DayOfWeek.SUNDAY;
    private static final DayOfWeek END_WEEK = DayOfWeek.SATURDAY;

    private static final Integer MIN_START_WORKING_DAY = 7;
    private static final Integer MAX_END_WORKING_DAY = 21;

    public static void main(String[] args) {
        //having a budget of x money
        //how many workers to employ
        //how many hours will they will work

        //36 - break between working weeks
        //45 - hours working week

        Double employeeBudget = 50_000.0;

        //10 * 9 = 90 + 25 (115)



        Double employee1Salary = workingWeekHours(10.0, true);
//        Double employee2Salary = workingWeekHours(10.0, true);

        Double totalExpense = employee1Salary;

        int countWeeks = 0;
        while (true) {

            if (totalExpense > employeeBudget) {
                break;
            }

            employeeBudget -= totalExpense;
            countWeeks++;
        }

        System.out.println("Weeks per employee: " + countWeeks);
        System.out.println("Finance department budget in the end: " + employeeBudget);




    }

    private static Double workingWeekHours(Double tariff, boolean withOvertime) {

        LocalDateTime startWeek = startOfWeek();
        LocalDateTime endWeek = endOfWeek(startWeek);
        System.out.println(startWeek);
        System.out.println(endWeek);

        LocalDateTime current = startWeek;

        Integer dailyHours = 0;
        Integer weeklyHours = 0;

        boolean dayStarted = false;

        Double currentSalary = 0.;

        while (current.isBefore(endWeek)) {
            current = current.plusHours(1);

            boolean isInsideWorkingDay = current.toLocalTime().isAfter(LocalTime.of(MIN_START_WORKING_DAY, 0))
                    &&
                    current.toLocalTime().isBefore(LocalTime.of(MAX_END_WORKING_DAY, 0));


            if (weeklyHours >= REGULAR_WORKING_HOURS_PER_WEEK) {
                break;
            }

            if (current.toLocalTime().equals(LocalTime.of(MIN_START_WORKING_DAY, 0))) {
                dayStarted = true;
            }

            if (current.toLocalTime().equals(LocalTime.of(MAX_END_WORKING_DAY, 0))) {
                weeklyHours += dailyHours;
                dailyHours = 0;
                dayStarted = false;
            }

            if (isEndOfDay(dailyHours, withOvertime)) { //11
                System.out.println("end of the day: " + dailyHours);
                dayStarted = false;
                continue;
            }


            if (isInsideWorkingDay) {
                if (dayStarted) {
                    dailyHours++;
                    currentSalary += getHourlySalary(tariff, dailyHours, withOvertime);
                }
            }
        }

        System.out.println("Daily: " + dailyHours);
        System.out.println("Weekly: " + weeklyHours);


        System.out.println("Salary per week: " + currentSalary);

        return currentSalary;
    }

    private static Double getHourlySalary(Double hourlyTariff, Integer dailyHours, boolean withOvertime) {
        if (dailyHours <= REGULAR_HOURS_PER_DAY) { //9 - 10
            return hourlyTariff;
        }

        Integer maxDailyHours = withOvertime ? (REGULAR_HOURS_PER_DAY + MAX_OVERTIME_HOURS) : REGULAR_HOURS_PER_DAY;

        if (dailyHours <= maxDailyHours) {
            Double overtimePay = (hourlyTariff / 100) * OVERTIME_RATE_PERCENT;
            return overtimePay;
        }

        return 0.;
    }

    private static boolean isEndOfDay(Integer dailyHours, boolean withOvertime) {

        if (dailyHours < REGULAR_HOURS_PER_DAY) {
            return false;
        }
        Integer maxDailyHours = withOvertime ? REGULAR_HOURS_PER_DAY + MAX_OVERTIME_HOURS : REGULAR_HOURS_PER_DAY;

        if (dailyHours.equals(maxDailyHours)) {
            System.out.println("is end of the day: " + dailyHours);
            System.out.println("is end of the day (max): " + maxDailyHours);
            return true;
        }

        return false;
    }

    private static LocalDateTime startOfWeek() {
        LocalDateTime start =  LocalDateTime
                .now()
                .toLocalDate()
                .atTime(LocalTime.MIDNIGHT);

        while (start.getDayOfWeek() != START_WEEK) {
            start = start.plusDays(1);
        }

        return start;
    }

    private static LocalDateTime endOfWeek(LocalDateTime startWeek) {

        while (startWeek.getDayOfWeek() != END_WEEK) {
            startWeek = startWeek.plusDays(1);
        }

        return startWeek.plusDays(1).minusSeconds(1);
    }
}
