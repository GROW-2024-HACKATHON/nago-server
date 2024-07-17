package kr.seula.nagoserver.domain.report.entity;

import jakarta.persistence.*;
import kr.seula.nagoserver.domain.report.request.ReportEditRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String image;

    private String title;

    private String content;

    private String large;

    private String small;

    @Builder
    public ReportEntity(String image, String title, String content, String large, String small) {
        this.image = image;
        this.title = title;
        this.content = content;
        this.large = large;
        this.small = small;
    }

    public void editReport(ReportEditRequest dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.large = dto.getLarge();
        this.small = dto.getSmall();
    }
}
