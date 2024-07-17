package kr.seula.nagoserver.domain.report.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.cloud.storage.Bucket;
import kr.seula.nagoserver.domain.report.entity.ReportEntity;
import kr.seula.nagoserver.domain.report.exception.ReportNotFoundException;
import kr.seula.nagoserver.domain.report.repository.ReportRepository;
import kr.seula.nagoserver.domain.report.request.ReportEditRequest;
import kr.seula.nagoserver.global.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final Bucket bucket;
    private final ReportRepository repository;
    private final ReportAiService service;

    public BaseResponse<ReportEntity> addReport(MultipartFile image) throws IOException {
        String name = UUID.randomUUID() + ".png";

        bucket.create(name, image.getBytes(), image.getContentType());

        String url = String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                bucket.getName(),
                name);

        JsonNode result = service.getResult(image);

        ReportEntity entity = ReportEntity.builder()
                .image(url)
                .title(result.get("title").asText())
                .content(result.get("content").asText())
                .large(result.get("large").asText())
                .small(result.get("small").asText())
                .build();

        repository.save(entity);

        return new BaseResponse<> (
                true,
                "신고가 접수되었습니다.",
                entity
        );
    }

    public BaseResponse<ReportEntity> editReport(long id, ReportEditRequest dto) {
        ReportEntity entity = repository.findById(id)
                .orElseThrow(ReportNotFoundException::new);

        entity.editReport(dto);

        repository.save(entity);

        return new BaseResponse<> (
                true,
                "신고가 수정되었습니다.",
                entity
        );
    }

    public BaseResponse<List<ReportEntity>> getAllReport() {
        return new BaseResponse<> (
                true,
                "전체 신고가 조회되었습니다.",
                repository.findAll()
        );
    }

    public BaseResponse<ReportEntity> getReport(long id) {
        ReportEntity entity = repository.findById(id)
                .orElseThrow(ReportNotFoundException::new);

        return new BaseResponse<> (
                true,
                "신고가 조회되었습니다.",
                entity
        );
    }

    public BaseResponse<?> delReport(long id) {
        ReportEntity entity = repository.findById(id)
                .orElseThrow(ReportNotFoundException::new);

        repository.delete(entity);

        return new BaseResponse<> (
                true,
                "신고가 삭제되었습니다.",
                null
        );
    }

    public BaseResponse<?> delAllReport() {
        repository.deleteAll();

        return new BaseResponse<> (
                true,
                "전체 신고가 삭제되었습니다.",
                null
        );
    }

}
