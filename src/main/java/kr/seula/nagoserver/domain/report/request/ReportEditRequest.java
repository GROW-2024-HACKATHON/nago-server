package kr.seula.nagoserver.domain.report.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ReportEditRequest {

    private String title;

    private String content;

    private String large;

    private String small;

}
