package com.deco.yakbang.service.vo;

import java.util.Date;
/**
 * 디바이스 정보 VO
 */
public class DeviceVO {

    /** 디바이스 ID */
    private String deviceId;

    /** 사용자 ID (FK) */
    private int userId;

    /** 모델명 */
    private String modelName;

    /** 회당 최대 주입량 */
    private double hardLimitPerDose;

    /** 남은 인슐린, 배터리 잔량, 바늘 교체 횟수 */
    private double insulinRemaining;
    private int batteryLevel;
    private int needleCount;
    private Date lastUpdate;
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public double getHardLimitPerDose() {
		return hardLimitPerDose;
	}
	public void setHardLimitPerDose(double hardLimitPerDose) {
		this.hardLimitPerDose = hardLimitPerDose;
	}
	public double getInsulinRemaining() {
		return insulinRemaining;
	}
	public void setInsulinRemaining(double insulinRemaining) {
		this.insulinRemaining = insulinRemaining;
	}
	public int getBatteryLevel() {
		return batteryLevel;
	}
	public void setBatteryLevel(int batteryLevel) {
		this.batteryLevel = batteryLevel;
	}
	public int getNeedleCount() {
		return needleCount;
	}
	public void setNeedleCount(int needleCount) {
		this.needleCount = needleCount;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}