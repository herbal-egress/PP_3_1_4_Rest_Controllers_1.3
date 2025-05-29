package mvc.spring.security.controllers.mvc;

import mvc.spring.security.entities.*;
import mvc.spring.security.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public ModelAndView pageForAdmin(Principal principal) {
        ModelAndView mav = new ModelAndView("usersview/admin_page");
        mav.addObject("new_user", new User());
        mav.addObject("all_roles", roleService.findAllRole());
        mav.addObject("all_users", userService.findAllUser());
        mav.addObject("auth_user_key", userService.findUserByName(principal.getName()));
        return mav;
    }
}