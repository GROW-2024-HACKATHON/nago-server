package kr.seula.nagoserver.domain.report.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.cloud.storage.Bucket;
import kr.seula.nagoserver.domain.report.entity.ReportEntity;
import kr.seula.nagoserver.domain.report.exception.ImageNotFoundException;
import kr.seula.nagoserver.domain.report.exception.ReportNotFoundException;
import kr.seula.nagoserver.domain.report.repository.ReportRepository;
import kr.seula.nagoserver.domain.report.request.ReportDelRequest;
import kr.seula.nagoserver.domain.report.request.ReportFinishRequest;
import kr.seula.nagoserver.domain.report.request.ReportEditRequest;
import kr.seula.nagoserver.domain.report.request.ReportGetRequest;
import kr.seula.nagoserver.global.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final Bucket bucket;
    private final ReportRepository repository;
    private final ReportAiService service;

    public String uploadImage(MultipartFile image) throws IOException {
        String name = UUID.randomUUID() + ".png";

        bucket.create(name, image.getBytes(), image.getContentType());

        return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                bucket.getName(),
                name
        );
    }

    public BaseResponse<ReportEntity> uploadImageAndPredict(MultipartFile image) throws IOException {
        String name = UUID.randomUUID() + ".png";

        bucket.create(name, image.getBytes(), image.getContentType());

        String url = String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                bucket.getName(),
                name
        );

        JsonNode result = service.getResult(image);

        ReportEntity entity = ReportEntity.builder()
                .firstImage(url)
                .title(result.get("title").asText())
                .content(result.get("content").asText())
                .large(result.get("large").asText())
                .small(result.get("small").asText())
                .build();

        repository.save(entity);

        if (Objects.equals(result.get("large").asText(), "불법주정차")) {
            return new BaseResponse<> (
                    false,
                    "불법주정차이므로 사진이 더 필요합니다.",
                    entity
            );
        }

        return new BaseResponse<>(
                true,
                "이미지를 업로드하였습니다.",
                entity
        );
    }

    public BaseResponse<ReportEntity> addMoreImage(long id, MultipartFile image) throws IOException {
        ReportEntity entity = repository.findById(id)
                .orElseThrow(ReportNotFoundException::new);

        String url = uploadImage(image);

        entity.addMoreImage(url);

        repository.save(entity);

        return new BaseResponse<> (
                true,
                "이미지가 추가되었습니다.",
                entity
        );
    }

    public BaseResponse<ReportEntity> finishReport(ReportFinishRequest dto) {
        ReportEntity entity = repository.findById(dto.getId())
                .orElseThrow(ImageNotFoundException::new);

        entity.finish(dto);

        repository.save(entity);

        return new BaseResponse<>(
                true,
                "신고가 접수되었습니다.",
                entity
        );
    }

    public BaseResponse<ReportEntity> editReport(long id, ReportEditRequest dto) {
        ReportEntity entity = repository.findById(id)
                .orElseThrow(ReportNotFoundException::new);

        entity.edit(dto);

        repository.save(entity);

        return new BaseResponse<>(
                true,
                "신고가 수정되었습니다.",
                entity
        );
    }

    public BaseResponse<List<ReportEntity>> getAllReport(ReportGetRequest dto) {
        return new BaseResponse<>(
                true,
                "전체 신고가 조회되었습니다.",
                repository.findAllByNameAndEmailAndPhone(
                        dto.getName(),
                        dto.getEmail(),
                        dto.getPhone()
                )
        );
    }

    public BaseResponse<ReportEntity> getReport(long id) {
        ReportEntity entity = repository.findById(id)
                .orElseThrow(ReportNotFoundException::new);

        return new BaseResponse<>(
                true,
                "신고가 조회되었습니다.",
                entity
        );
    }

    public BaseResponse<?> delReport(long id) {
        ReportEntity entity = repository.findById(id)
                .orElseThrow(ReportNotFoundException::new);

        repository.delete(entity);

        return new BaseResponse<>(
                true,
                "신고가 삭제되었습니다.",
                null
        );
    }

    public BaseResponse<?> delAllReport(ReportDelRequest dto) {
        repository.deleteAllByNameAndEmailAndPhone(
                dto.getName(),
                dto.getEmail(),
                dto.getPhone()
        );

        return new BaseResponse<>(
                true,
                "전체 신고가 삭제되었습니다.",
                null
        );
    }

}
