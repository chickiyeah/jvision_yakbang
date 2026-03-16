package com.deco.yakbang.service.vo;


/**
 * 사용자 정보 VO
 */
public class UserVO {



	/** 사용자 ID (PK) */
    private int id;

    /** 로그인 ID */
    private String userId;

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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public double getTargetGlucose() {
		return targetGlucose;
	}
	public void setTargetGlucose(double targetGlucose) {
		this.targetGlucose = targetGlucose;
	}
	public double getMaxInsulinDaily() {
		return maxInsulinDaily;
	}
	public void setMaxInsulinDaily(double maxInsulinDaily) {
		this.maxInsulinDaily = maxInsulinDaily;
	}
    
    
}