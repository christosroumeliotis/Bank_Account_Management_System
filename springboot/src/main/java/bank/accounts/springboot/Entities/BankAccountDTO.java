package bank.accounts.springboot.Entities;

import java.math.BigDecimal;

public class BankAccountDTO {

    private Long id;
    private BigDecimal balance;

    public BankAccountDTO(Long id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
