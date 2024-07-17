package kr.seula.nagoserver.global;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseResponse<T> {

    private Boolean success;

    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

}
