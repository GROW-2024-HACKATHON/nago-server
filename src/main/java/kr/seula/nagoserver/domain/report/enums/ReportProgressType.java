package kr.seula.nagoserver.domain.report.enums;

import lombok.Getter;

@Getter
public enum ReportProgressType {

    PENDING("진행전"), PROCESSING("처리중"), DONE("처리완료");

    private final String value;

    ReportProgressType(String value) {
        this.value = value;
    }

}
