package bank.accounts.springboot.Services;

import bank.accounts.springboot.Entities.User;
import bank.accounts.springboot.Entities.UserPrincipal;
import bank.accounts.springboot.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { //To return an object UserDetails we create our own class
                                                                                              // and implement the interface UserDetails
        Optional<User> user = userRepository.findByUsername(username);

        if(user.isPresent()){
            return new UserPrincipal(user.get());
        }else{
           System.out.println("User Not Found");
           throw new UsernameNotFoundException("User Not Found");
        }


    }
}
