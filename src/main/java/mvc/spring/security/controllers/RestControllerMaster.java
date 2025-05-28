package mvc.spring.security.controllers;
// В этом контроллере вынес отдельно методы для заполнения таблиц и модальных окон через передачу объектов в формате JSON.
// Можно было поместить их в UserController и UserDbController, пометив каждый метод аннотацией @ResponseBody

import mvc.spring.security.entities.Role;
import mvc.spring.security.entities.User;
import mvc.spring.security.services.RoleService;
import mvc.spring.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/users")
public class RestControllerMaster {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public RestControllerMaster(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    // даём JSON со всеми юзерами с ролями (через репозиторий):
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUser());
    }

    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        return ResponseEntity.ok(userService.findUserByName(principal.getName()));
    }

    // даём JSON со всеми существующими ролями (через репозиторий):
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.findAllRole());
    }

    @PostMapping("/admin/save")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            userService.register(user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка регистрации: " + e.getMessage());
        }
    }

    @PatchMapping("/admin/id")
    public ResponseEntity<?> updateUser(@RequestParam int id, @RequestBody User user) {
        userService.update(id, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/admin/id")
    public ResponseEntity<?> deleteUser(@RequestParam int id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }
}