package kr.seula.nagoserver.domain.report.request;

import lombok.Getter;

@Getter
public class ReportAddRequest {

    private String name;

    private String email;

    private String phone;

    private String lat;

    private String lng;

    private String address;

}
