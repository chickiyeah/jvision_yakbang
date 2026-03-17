package com.deco.yakbang.util;

import java.time.LocalDate;

public class ResidentNumberUtil {

    public static String getGender(String residentNumber) {
        // 형식: 000304-3 -> '-' 뒤의 첫 번째 숫자 추출
        char genderCode = residentNumber.split("-")[1].charAt(0);
        
        if (genderCode == '1' || genderCode == '3') {
            return "M"; // 남성
        } else if (genderCode == '2' || genderCode == '4') {
            return "F"; // 여성
        }
        return "U"; // Unknown
    }

    public static int getAge(String residentNumber) {
        String birthYearPart = residentNumber.substring(0, 2);
        char genderCode = residentNumber.split("-")[1].charAt(0);
        
        int birthYear;
        // 1, 2번은 1900년대 / 3, 4번은 2000년대생
        if (genderCode == '1' || genderCode == '2') {
            birthYear = 1900 + Integer.parseInt(birthYearPart);
        } else {
            birthYear = 2000 + Integer.parseInt(birthYearPart);
        }

        // 현재 연도(2026년 기준) 한국 나이 계산
        int currentYear = LocalDate.now().getYear();
        return currentYear - birthYear + 1;
    }
}