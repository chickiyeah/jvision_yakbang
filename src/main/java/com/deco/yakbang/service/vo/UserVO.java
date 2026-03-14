package com.deco.yakbang.service.vo;


/**
 * 사용자 정보 VO
 */
public class UserVO {

    /** 사용자 ID (PK) */
    private int userId;

    /** 로그인 ID */
    private String loginId;

    /** 비밀번호 */
    private String password;

    /** 이름 */
    private String name;

    /** 나이 */
    private int age;

    /** 성별 */
    private String gender;

    /** 키, 몸무게, 목표 혈당, 일일 최대 인슐린 등 */
    private double height;
    private double weight;
    private double targetGlucose;
    private double maxInsulinDaily;
}