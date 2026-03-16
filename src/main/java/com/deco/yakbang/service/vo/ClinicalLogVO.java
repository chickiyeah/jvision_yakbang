package com.deco.yakbang.service.vo;

import java.util.Date;
/**
 * 임상 로그 VO
 */
public class ClinicalLogVO {

    /** 로그 ID (PK) */
    private int logId;

    /** 사용자 ID */
    private int userId;

    /** 디바이스 ID */
    private String deviceId;

    /** 혈당 수치 */
    private double glucoseLevel;

    /** 마지막 주입량, 시간, 상태 */
    private double lastDoseAmount;
    private Date lastDoseTime;
    private String injectionStatus;
	public int getLogId() {
		return logId;
	}
	public void setLogId(int logId) {
		this.logId = logId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public double getGlucoseLevel() {
		return glucoseLevel;
	}
	public void setGlucoseLevel(double glucoseLevel) {
		this.glucoseLevel = glucoseLevel;
	}
	public double getLastDoseAmount() {
		return lastDoseAmount;
	}
	public void setLastDoseAmount(double lastDoseAmount) {
		this.lastDoseAmount = lastDoseAmount;
	}
	public Date getLastDoseTime() {
		return lastDoseTime;
	}
	public void setLastDoseTime(Date lastDoseTime) {
		this.lastDoseTime = lastDoseTime;
	}
	public String getInjectionStatus() {
		return injectionStatus;
	}
	public void setInjectionStatus(String injectionStatus) {
		this.injectionStatus = injectionStatus;
	}
}