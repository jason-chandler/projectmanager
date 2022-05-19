package xyz.fieldwire.projectmanager.service.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonResponse {
    @Builder.Default
    private Boolean success = Boolean.TRUE;
    private String message;
}
