package test.task.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private Long id;
    private Long balance;

    public static Account copyOf(Account account) {
        return new Account(account.getId(), account.getBalance());
    }
}
