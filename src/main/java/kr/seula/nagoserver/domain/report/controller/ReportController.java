package kr.seula.nagoserver.domain.report.controller;

import kr.seula.nagoserver.domain.report.entity.ReportEntity;
import kr.seula.nagoserver.domain.report.exception.ReportNotFoundException;
import kr.seula.nagoserver.domain.report.request.ReportDelRequest;
import kr.seula.nagoserver.domain.report.request.ReportEditRequest;
import kr.seula.nagoserver.domain.report.request.ReportFinishRequest;
import kr.seula.nagoserver.domain.report.request.ReportGetRequest;
import kr.seula.nagoserver.domain.report.service.ReportService;
import kr.seula.nagoserver.global.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("report")
public class ReportController {

    private final ReportService service;

    @PostMapping
    public BaseResponse<ReportEntity> uploadImage(
            @RequestParam MultipartFile image
    ) throws IOException {
        return service.uploadImage(image);
    }

    @PatchMapping
    public BaseResponse<ReportEntity> finishReport(
            @RequestBody ReportFinishRequest dto
    ) {
        return service.finishReport(dto);
    }

    @PatchMapping("/{id}")
    public BaseResponse<ReportEntity> editReport(
            @PathVariable long id,
            @RequestBody ReportEditRequest dto
    ) {
        return service.editReport(id, dto);
    }

    @GetMapping
    public BaseResponse<List<ReportEntity>> getAllReport(
            @RequestBody ReportGetRequest dto
    ) {
        return service.getAllReport(dto);
    }

    @GetMapping("/{id}")
    public BaseResponse<ReportEntity> getReport(
            @PathVariable long id
    ) {
        return service.getReport(id);
    }

    @DeleteMapping("/{id}")
    public BaseResponse<?> delReport(
            @PathVariable long id
    ) {
        return service.delReport(id);
    }

    @DeleteMapping
    public BaseResponse<?> delAllReport(
            @RequestBody ReportDelRequest dto
    ) {
        return service.delAllReport(dto);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ReportNotFoundException.class)
    public BaseResponse<?> handleReportNotFound() {
        return new BaseResponse<> (
                false,
                "신고가 존재하지 않습니다.",
                null
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ReportNotFoundException.class)
    public BaseResponse<?> handleImageNotFound() {
        return new BaseResponse<> (
                false,
                "신고가 존재하지 않습니다.",
                null
        );
    }

}
