package xyz.fieldwire.projectmanager.service.response;

import lombok.Getter;

@Getter
public class ErrorResponse extends CommonResponse {
    public ErrorResponse(String msg) {
        super(Boolean.FALSE, msg);
    }
}
