package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService,UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(@Lazy PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly=true)
    public User findByUserName(String name) {
        return userRepository.findByUsername(name);
    }
    @Override
    @Transactional(readOnly=true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @Override
    @Transactional(readOnly=true)
    public User getUserBiId(long id) {
        return userRepository.getById(id);
    }
    @Override
    @Transactional
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
    @Override
    @Transactional
    public void updateUser( User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setId(user.getId());
        userRepository.save(user);
    }
    @Override
    @Transactional
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }
   /* @Transactional
    public void updeta(User user) {
        userRepository
    }*/

    @Override
    @Transactional(readOnly=true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUserName(username);

        if (user == null) {
            throw new UsernameNotFoundException("Netu takogo name");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword()
        ,mapRolesToAuthorities(user.getRoleList()));
    }
    //из коллекции ролей получаем коллекцию прав доступа
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getRole())).collect(Collectors.toList());
    }
}
