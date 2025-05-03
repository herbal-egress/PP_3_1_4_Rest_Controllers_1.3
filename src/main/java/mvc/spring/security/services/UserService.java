package mvc.spring.security.services;

import mvc.spring.security.entities.Role;
import mvc.spring.security.entities.User;

import mvc.spring.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findUserById(Long id) {
        return userRepository.findUserById(id);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll(); // использую стандартный метод из репозитория
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("Пользователь '%s' не найден", username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    //  регистрируется новый пользователь БД с ролями
    @Transactional
    public void register(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRoles(user.getRoles());
        userRepository.save(user);
    }

    @Transactional
    public void update(Long id, User userWithNewInfo) {
        User updatedUser = userRepository.findUserById(id);
        updatedUser.setUsername(userWithNewInfo.getUsername());
        updatedUser.setEmail(userWithNewInfo.getEmail());
        updatedUser.setRoles(userWithNewInfo.getRoles());
        // если метод save видит в аргументах id юзера, то он работает как merge (обновление данных)!!!
        userRepository.save(updatedUser);
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteUserById(id);
    }

    // принимает имя пользователя и возвращает весь объект этого пользователя из нашей БД
    @Transactional
    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        User user = findUserById(id);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("Пользователь '%s' не найден", id));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    // тут получаем коллекцию прав доступа из коллекций ролей.
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }
}