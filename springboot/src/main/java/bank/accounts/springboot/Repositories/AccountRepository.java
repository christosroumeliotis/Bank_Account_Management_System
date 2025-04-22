package bank.accounts.springboot.Repositories;

import bank.accounts.springboot.Entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<BankAccount, Long> {

    @Query(value = "SELECT * FROM bank_accounts WHERE user_id = :userId", nativeQuery = true)
    List<BankAccount> findAccountsByUser_Id(@Param("userId") Long userId);
}
