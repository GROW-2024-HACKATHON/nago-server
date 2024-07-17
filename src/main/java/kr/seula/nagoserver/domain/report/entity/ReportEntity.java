package kr.seula.nagoserver.domain.report.entity;

import jakarta.persistence.*;
import kr.seula.nagoserver.domain.report.request.ReportFinishRequest;
import kr.seula.nagoserver.domain.report.request.ReportEditRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstImage;

    private String secondImage;

    private String title;

    private String content;

    private String large;

    private String small;

    private String name;

    private String email;

    private String phone;

    private String lat;

    private String lng;

    private String address;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public ReportEntity(
            String firstImage,
            String secondImage,
            String title,
            String content,
            String large,
            String small,
            String name,
            String email,
            String phone,
            String lat,
            String lng,
            String address
    ) {
        this.firstImage = firstImage;
        this.secondImage = secondImage;
        this.title = title;
        this.content = content;
        this.large = large;
        this.small = small;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
    }

    public void edit(ReportEditRequest dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.large = dto.getLarge();
        this.small = dto.getSmall();
    }

    public void finish(ReportFinishRequest dto) {
        this.name = dto.getName();
        this.email = dto.getEmail();
        this.phone = dto.getPhone();
        this.lat = dto.getLat();
        this.lng = dto.getLng();
        this.address = dto.getAddress();
    }

    public void addMoreImage(String image) {
        this.secondImage = image;
    }
}
