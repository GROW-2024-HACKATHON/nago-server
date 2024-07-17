package kr.seula.nagoserver.domain.report.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportGetRequest {

    private String name;

    private String email;

    private String phone;

}
