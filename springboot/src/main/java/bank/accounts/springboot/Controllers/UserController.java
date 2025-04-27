package bank.accounts.springboot.Controllers;

import bank.accounts.springboot.Entities.BankAccount;
import bank.accounts.springboot.Entities.BankAccountDTO;
import bank.accounts.springboot.Entities.User;
import bank.accounts.springboot.Entities.UserPrincipal;
import bank.accounts.springboot.Services.AccountService;
import bank.accounts.springboot.Services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @PutMapping("")
    public ResponseEntity<String> updateUser(@AuthenticationPrincipal UserPrincipal principal, @RequestBody User updatedCustomer){
        return userService.updateUser(principal.getId(),updatedCustomer);
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal UserPrincipal principal){
        return userService.deleteUser(principal.getId());
    }

    @PostMapping("/account")
    public ResponseEntity<String> createAccount(@AuthenticationPrincipal UserPrincipal principal){
        return accountService.createNewAccount(principal.getId());
    }

    @DeleteMapping("/account/{account_id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long account_id){
        return accountService.deleteAccount(account_id);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<BankAccountDTO>> getBankAccouts(@AuthenticationPrincipal UserPrincipal principal) throws Exception {
        return accountService.getBankAccounts(principal.getId());
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
