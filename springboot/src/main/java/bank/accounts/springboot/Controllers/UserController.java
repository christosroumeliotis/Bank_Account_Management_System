package bank.accounts.springboot.Controllers;

import bank.accounts.springboot.Entities.User;
import bank.accounts.springboot.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody User user){
        return userService.createNewUser(user);
    }

    //Update user's username and password, you can't update one of them, create endpoints to change either username or password
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @Valid @RequestBody User updatedCustomer){
        return userService.updateUser(id,updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        return userService.deleteUser(id);
    }

    /*@GetMapping
    getAccouts*/

}
