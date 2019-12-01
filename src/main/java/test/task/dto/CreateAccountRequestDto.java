package test.task.dto;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Introspected
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequestDto {

    @NotNull
    private Long balance;
}
