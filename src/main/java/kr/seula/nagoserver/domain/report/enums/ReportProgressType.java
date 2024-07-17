package kr.seula.nagoserver.domain.report.enums;

import lombok.Getter;

@Getter
public enum ReportProgressType {

    PENDING("진행 전"), PROCESSING("진행 중"), DONE("해결 완료");

    private final String value;

    ReportProgressType(String value) {
        this.value = value;
    }

}
