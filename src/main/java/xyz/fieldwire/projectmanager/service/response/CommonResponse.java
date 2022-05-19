package xyz.fieldwire.projectmanager.service.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class CommonResponse {
    private Boolean success = Boolean.TRUE;
    private String message;

    protected CommonResponse(Boolean success, String message) {
        if(Objects.nonNull(success)) {
            this.success = success;
        }
        this.message = message;
    }
}
