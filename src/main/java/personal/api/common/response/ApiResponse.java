package personal.api.common.response;

import java.util.List;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private T data;
    private List<FieldErrorResponse> errors;
    private PagingResponse paging;

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<List<T>> ok(Page<T> page) {
        return ApiResponse.<List<T>>builder()
                .success(true)
                .data(page.getContent())
                .paging(PagingResponse.builder()
                        .page(page.getNumber())
                        .totalPages(page.getTotalPages())
                        .totalElements(page.getTotalElements())
                        .size(page.getSize())
                        .build())
                .build();
    }

    public static <T> ApiResponse<T> fail(List<FieldErrorResponse> errors) {
        return ApiResponse.<T>builder()
                .success(false)
                .errors(errors)
                .build();
    }
}
