package kr.seula.nagoserver.domain.report.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ReportAiService {
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public ReportAiService(ChatClient.Builder chatClientBuilder, ObjectMapper objectMapper) {
        this.chatClient = chatClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    public JsonNode getResult(MultipartFile image) throws JsonProcessingException {
        String result = chatClient.prompt()
                .user((us) -> {
                    us.text(
                            "너는 이 이미지의 특징을 참고해서 이게 어떤 신고내용인지 추론하고 판단해야돼. "
                            + "신고양식을 작성해야되는데 반환타입은 json이야. 절대 ```으로 블록을 나태내지마. "
                            + "첫 번째로 신고 제목을 title로 작성해주고, "
                            + "두 번째로 신고에 대한 자세한 설명을 content로 만들어줘야 돼. 이미지에서 어떤사건이 발생한건지에 대한 설명이 필요해. "
                            + "마지막으로 신고에 대한 카테고리로 큰 카테고리는 large, 작은 카테고리는 small로 만들어줘야돼. 크게 안전신고, 생활불편 신고, 자동차 · 교통위반으로 나뉘고 "
                            + "작게는 안전신고에는 여름철 집중신고, 도로, 시설물 파손 및 고장, 건설, (해체)공사장 위험, 대기오염, 수질오염, 소방안전, 기타 안전 · 환경 위험요소로 나뉘고 "
                            + "생활 불편 신고는 불법광고물, 자전거 · 이륜차 방치 및 불편, 쓰레기 · 폐기물, 해양 쓰레기, 불법 숙박, 기타 생활불편으로 나뉘고 "
                            + "자동차 · 교통위반은 교통위반(고속도로 포함), 이륜차 위반, 버스전용차로 위반(일반도로), 번호판 규정 위반, 불법등화 · 반사판(지) 가림 · 손상, 불법 튜닝 · 해체 · 조작, 기타 자동차 안전기준 위반으로 놔뉘어. "
                            );
                    try {
                        us.media(convertContentTypeToMimeType(image.getContentType()), convertMultipartFileToResource(image));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .call()
                .content();

        System.out.println(result);

        return objectMapper.readTree(result);
    }

    public Resource convertMultipartFileToResource(MultipartFile file) throws IOException {
        byte[] fileContent = file.getBytes();
        return new ByteArrayResource(fileContent);
    }

    public MediaType convertContentTypeToMimeType(String contentType) {
        return MediaType.parseMediaType(contentType);
    }

}
