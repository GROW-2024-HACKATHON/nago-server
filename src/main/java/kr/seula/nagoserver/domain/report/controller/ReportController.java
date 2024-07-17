package kr.seula.nagoserver.domain.report.controller;

import kr.seula.nagoserver.domain.report.entity.ReportEntity;
import kr.seula.nagoserver.domain.report.exception.IllegalParkingExcpetion;
import kr.seula.nagoserver.domain.report.exception.ImageNotFoundException;
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
@RequestMapping("/report")
public class ReportController {

    private final ReportService service;

    @PostMapping("/upload")
    public BaseResponse<ReportEntity> uploadImage(
            @RequestParam MultipartFile image
    ) throws IOException {
        return service.uploadImageAndPredict(image);
    }

    @PatchMapping("/finish")
    public BaseResponse<ReportEntity> finishReport(
            @RequestBody ReportFinishRequest dto
    ) {
        return service.finishReport(dto);
    }

    @PatchMapping("/add-image")
    public BaseResponse<ReportEntity> addMoreImage(
            @RequestParam MultipartFile image,
            @RequestParam long id
    ) throws IOException {
        return service.addMoreImage(id, image);
    }

    @PatchMapping("/edit/{id}")
    public BaseResponse<ReportEntity> editReport(
            @PathVariable long id,
            @RequestBody ReportEditRequest dto
    ) {
        return service.editReport(id, dto);
    }

    @GetMapping("/get-all")
    public BaseResponse<List<ReportEntity>> getAllReport(
            @RequestBody ReportGetRequest dto
    ) {
        return service.getAllReport(dto);
    }

    @GetMapping("/get/{id}")
    public BaseResponse<ReportEntity> getReport(
            @PathVariable long id
    ) {
        return service.getReport(id);
    }

    @DeleteMapping("/del/{id}")
    public BaseResponse<?> delReport(
            @PathVariable long id
    ) {
        return service.delReport(id);
    }

    @DeleteMapping("/del-all")
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
    @ExceptionHandler(ImageNotFoundException.class)
    public BaseResponse<?> handleImageNotFound() {
        return new BaseResponse<> (
                false,
                "이미지가 존재하지 않습니다.",
                null
        );
    }

    @ResponseStatus(HttpStatus.UPGRADE_REQUIRED)
    @ExceptionHandler(IllegalParkingExcpetion.class)
    public BaseResponse<?> handleIllegalParking() {
        return new BaseResponse<> (
                false,
                "불법주정차이므로 사진이 더 필요합니다.",
                null
        );
    }

}
