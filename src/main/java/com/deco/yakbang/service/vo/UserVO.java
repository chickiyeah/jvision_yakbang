package com.deco.yakbang.service.vo;


/**
 * 사용자 정보 VO
 */
public class UserVO {



	// --- 기본 계정 정보 ---
    private String userId;      // 로그인 ID
    private String password;    // 해싱된 비밀번호
    private String salt;        // 유저별 고유 솔트
    private String name;        // 사용자 이름
    private Integer age;        // 나이
    private String gender;      // 성별 (M/F)
    private String residentNumber; // 000304-3 형식으로 수신

    // --- 신체 및 당뇨 관리 정보 ---
    private Double height;      // 키 (cm)
    private Double weight;      // 몸무게 (kg)
    private Double targetGlucose;    // 목표 혈당 (기본값: 100.0)
    private Double maxInsulinDaily;  // 일일 최대 인슐린 (기본값: 50.0)

    // --- DB 관리용 (필요 시) ---
    private int id;
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
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getResidentNumber() {
		return residentNumber;
	}
	public void setResidentNumber(String residentNumber) {
		this.residentNumber = residentNumber;
	}
    
    
}