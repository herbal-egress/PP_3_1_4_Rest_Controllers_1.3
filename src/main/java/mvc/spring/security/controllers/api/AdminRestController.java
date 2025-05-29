package mvc.spring.security.controllers.api;

import mvc.spring.security.entities.User;
import mvc.spring.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

    @RestController
    public class AdminRestController {
        private final UserService userService;

        @Autowired
        public AdminRestController(UserService userService) {
            this.userService = userService;
        }

    @PostMapping("api/admin/save")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            userService.register(user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка регистрации: " + e.getMessage());
        }
    }

    @PatchMapping("api/admin/id")
    public ResponseEntity<?> updateUser(@RequestParam int id, @RequestBody User user) {
        userService.update(id, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("api/admin/id")
    public ResponseEntity<?> deleteUser(@RequestParam int id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }
}