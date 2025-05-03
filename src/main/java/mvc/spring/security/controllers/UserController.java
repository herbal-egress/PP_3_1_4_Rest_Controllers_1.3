package mvc.spring.security.controllers;

import mvc.spring.security.dao.UserDbDAO;
import mvc.spring.security.entities.User;
import mvc.spring.security.repositories.UserRepository;
import mvc.spring.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.security.Principal;


@Controller

public class UserController {
    private final UserDbDAO userDbDAO;
    private UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserDbDAO userDbDAO, UserRepository userRepository) {
        this.userDbDAO = userDbDAO;
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    // http://localhost:8080/start
    @GetMapping("/user") // защищённый адрес
    public String pageForAuthenticatedUsers(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("auth_user", user);
        model.addAttribute("all_users", userRepository.findAll());
        model.addAttribute("all_usersDb", userDbDAO.allUsers());
        return "usersview/user_page";
    }

    // сохраняет нового юзера, приходящего с формы admin_page
    @PostMapping("/admin/save")
    public String performRegistration(@ModelAttribute("user") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/admin";
        }
        userService.register(user);
        return "redirect:/admin";
    }

    // отправляет запрос в БД для записи отредактированного юзера /admin/id?id=
    @PatchMapping("/admin/id")
    public ResponseEntity<?> updateUser(@RequestParam Long id, @RequestBody User user) {
        userService.update(id, user);
        return ResponseEntity.ok().build();
    }

    // принимает DELETE запрос для удаления user по id на странице /admin/id?id=
    @DeleteMapping("/admin/id")
    public ResponseEntity<?> deleteUser(@RequestParam Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}