package com.deco.yakbang.service.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "활동 기록 VO")
public class ActivityLogVO {

    @Schema(description = "로그 ID (자동 생성)", example = "1")
    private int logId;

    @Schema(description = "유저 고유 ID", example = "1")
    private int userId;

    @Schema(description = "로그 시간", example = "2026-03-16 09:00:00")
    private String logDate;

    @Schema(description = "걸음 수", example = "5000")
    private int stepCount;

    @Schema(description = "섭취한 칼로리", example = "350.5")
    private float consumedCalories;

    @Schema(description = "현재 섭취량", example = "2.5")
    private float currentIntakeAmt;

    @Schema(description = "최대 섭취 한도", example = "10.0")
    private float maxIntakeLimit;
}