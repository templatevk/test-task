package test.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponseDto {

    @JsonProperty
    private Long accountFromBalance;
    @JsonProperty
    private Long accountToBalance;
}
