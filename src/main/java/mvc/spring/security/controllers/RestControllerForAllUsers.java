package mvc.spring.security.controllers;
// В этом контроллере вынес отдельно методы для заполнения таблиц и модальных окон через передачу объектов в формате JSON.
// Можно было поместить их в UserController и UserDbController, пометив каждый метод аннотацией @ResponseBody

import mvc.spring.security.dao.UserDbDAOImpl;
import mvc.spring.security.entities.Role;
import mvc.spring.security.entities.User;
import mvc.spring.security.model.UserDb;
import mvc.spring.security.repositories.RoleRepository;
import mvc.spring.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestControllerForAllUsers {
    private final RoleRepository roleRepository;
    private final UserService userService;
    private final UserDbDAOImpl userDbDAO;

    @Autowired
    public RestControllerForAllUsers(RoleRepository roleRepository, UserService userService, UserDbDAOImpl userDbDAO) {
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.userDbDAO = userDbDAO;
    }

    // даём JSON со всеми юзерами с ролями (через репозиторий):
    @GetMapping("/start/users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    // даём JSON со всеми юзерами из БД (через DAO):
    @GetMapping("/start/usersdb") // страница для всех. http://localhost:8080/start
    public ResponseEntity<List<UserDb>> getUsersDb() {
        List<UserDb> users = userDbDAO.allUsers();
        return ResponseEntity.ok(users);
    }

    // даём JSON со всеми существующими ролями (через репозиторий):
    @GetMapping("/start/roles")
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}