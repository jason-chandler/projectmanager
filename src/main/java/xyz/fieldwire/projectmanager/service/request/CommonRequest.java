package xyz.fieldwire.projectmanager.service.request;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class CommonRequest {
    public static final int DEFAULT_PAGE_SIZE = 500;
    public static final int DEFAULT_PAGE_NUMBER = 0;

    @Builder.Default
    private Integer pageNumber = DEFAULT_PAGE_NUMBER;
    @Builder.Default
    private Integer pageSize = DEFAULT_PAGE_SIZE;

    protected CommonRequest(Integer pageNumber, Integer pageSize) {
        if (Objects.nonNull(pageNumber)) {
            this.pageNumber = pageNumber;
        }
        if (Objects.nonNull(pageSize)) {
            this.pageSize = pageSize;
        }
    }

    protected CommonRequest() {
    }
}
