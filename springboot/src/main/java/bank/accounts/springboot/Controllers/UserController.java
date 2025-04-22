package bank.accounts.springboot.Controllers;

import bank.accounts.springboot.Entities.BankAccount;
import bank.accounts.springboot.Entities.BankAccountDTO;
import bank.accounts.springboot.Entities.User;
import bank.accounts.springboot.Services.AccountService;
import bank.accounts.springboot.Services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AccountService accountService;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user){
        return userService.createNewUser(user);
    }

    //Update user's username and password, you can't update one of them, create endpoints to change either username or password
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User updatedCustomer){
        return userService.updateUser(id,updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        return userService.deleteUser(id);
    }

    @PostMapping("/account/{user_id}")
    public ResponseEntity<String> createAccount(@PathVariable Long user_id){
        return accountService.createNewAccount(user_id);
    }

    @DeleteMapping("/account/{account_id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long account_id){
        return accountService.deleteAccount(account_id);
    }

    @GetMapping("/account/{user_id}")
    public ResponseEntity<List<BankAccountDTO>> getBankAccouts(@PathVariable Long user_id) throws Exception {
        return accountService.getBankAccounts(user_id);
    }

    @PutMapping("/account/addmoney/{account_id}")
    public ResponseEntity<String> addMoney(@PathVariable Long account_id, @RequestBody BigDecimal amount){
        return accountService.addMoney(account_id, amount);
    }

    @PutMapping("/account/withdraw/{account_id}")
    public ResponseEntity<String> withdrawMoney(@PathVariable Long account_id, @RequestBody BigDecimal amount){
        return accountService.withdrawMoney(account_id, amount);
    }

    @GetMapping("/account/balance/{account_id}")
    public ResponseEntity<Map<String,BigDecimal>> getAccountBalance(@PathVariable Long account_id){
        return accountService.getBalance(account_id);
    }


}
