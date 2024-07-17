package kr.seula.nagoserver.domain.report.request;

import lombok.Getter;

@Getter
public class ReportFinishRequest {

    private long id;

    private String title;

    private String content;

    private String name;

    private String email;

    private String phone;

    private String lat;

    private String lng;

    private String address;

}
