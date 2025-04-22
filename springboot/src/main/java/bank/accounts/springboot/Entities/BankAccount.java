package bank.accounts.springboot.Entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "Bank_Accounts")
public class BankAccount{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal balance;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    public BankAccount(){}

    public BankAccount(User owner) {
        this.balance = BigDecimal.valueOf(0);
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}


