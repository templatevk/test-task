package test.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@Introspected
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequestDto {

    @NotNull
    @Positive
    @JsonProperty
    private Long accountIdFrom;

    @NotNull
    @Positive
    @JsonProperty
    private Long accountIdTo;

    @NotNull
    @Positive
    @JsonProperty
    private Long amount;
}
