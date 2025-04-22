package bank.accounts.springboot.Services;

import bank.accounts.springboot.Entities.BankAccount;
import bank.accounts.springboot.Entities.BankAccountDTO;
import bank.accounts.springboot.Entities.User;
import bank.accounts.springboot.Repositories.AccountRepository;
import bank.accounts.springboot.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class AccountService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    public ResponseEntity<String> createNewAccount(Long id) {

        Optional<User> searchedUser = userRepository.findById(id);
        if(searchedUser.isPresent()){

            BankAccount bankAccount = new BankAccount(searchedUser.get());
            accountRepository.save(bankAccount);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("User with id: " + id + " created a new bank account.");
        }else{

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with id: " + id + " not found.");
        }

    }

    public ResponseEntity<String> deleteAccount(Long id) {

        Optional<BankAccount> searchedAccount = accountRepository.findById(id);
        if(searchedAccount.isPresent()){

            accountRepository.delete(searchedAccount.get());
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Bank account with id: " + id + " deleted.");
        }else{

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Bank account with id: " + id + " not found.");
        }

    }

    public ResponseEntity<List<BankAccountDTO>> getBankAccounts(Long id) throws Exception {

        Optional<User> searchedUser = userRepository.findById(id);
        if(searchedUser.isPresent()){

            List<BankAccount> bankAccounts = accountRepository.findAccountsByUser_Id(id);
            List<BankAccountDTO> bankAccountsDto = new ArrayList<>();
            for(BankAccount ba : bankAccounts){
                bankAccountsDto.add(new BankAccountDTO(ba.getId(),ba.getBalance()));
            }
            return ResponseEntity.status(HttpStatus.FOUND)
                    .body(bankAccountsDto);
        }else{
            throw new RuntimeException("User with id: " + id + " not found!");
        }
    }


    public ResponseEntity<String> addMoney(Long id, BigDecimal amount) {

        if(amount.compareTo(BigDecimal.ZERO) < 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("You cannot add negative amount of money.");
        }

        Optional<BankAccount> searchedAccount = accountRepository.findById(id);
        if(searchedAccount.isPresent()){
            searchedAccount.get().setBalance(searchedAccount.get().getBalance().add(amount));
            accountRepository.save(searchedAccount.get());
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Bank's account balance with id: " + id + " increased: " + searchedAccount.get().getBalance());
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Bank account with id: " + id + " not found.");
        }

    }

    public ResponseEntity<String> withdrawMoney(Long id, BigDecimal amount) {

        if(amount.compareTo(BigDecimal.ZERO) < 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("You cannot withdraw negative amount of money.");
        }

        Optional<BankAccount> searchedAccount = accountRepository.findById(id);
        if(searchedAccount.isPresent()){
            searchedAccount.get().setBalance(searchedAccount.get().getBalance().subtract(amount));
            accountRepository.save(searchedAccount.get());
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Bank's account balance with id: " + id + " is: " + searchedAccount.get().getBalance());
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Bank account with id: " + id + " not found.");
        }
    }

    public ResponseEntity<Map<String,BigDecimal>> getBalance(Long id) {

        Optional<BankAccount> searchedAccount = accountRepository.findById(id);
        Map<String,BigDecimal> balance = new HashMap<>();
        if(searchedAccount.isPresent()){
            balance.put("Balance", searchedAccount.get().getBalance());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(balance);
        }else{
            throw new RuntimeException("Account with id: " + id + " not found!");
        }
    }
}
