package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.services.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@Controller
public class AdminController {

    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;

    private final UserValidator userValidator;
    @Autowired
    public AdminController(UserServiceImpl userService, RoleServiceImpl roleService, UserValidator userValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;
    }

    @GetMapping("/admin")
    public String pageForCrudOperationsAdmin(Model model) {
        List<User> userList = userService.getAllUsers();
        model.addAttribute("users", userList);
        return "/admin";
    }
    @GetMapping("/updateInfo")
    public String updateUser(@RequestParam("empId")long id, Model model) {
        User user = userService.getUserBiId(id);
        List<Role> roleList = user.getRoleList();
        for (Role role : roleList) {
            if (role.equals(roleService.getRoleByName("ROLE_ADMIN"))) {
                model.addAttribute("ROLE_ADMIN", true);
            }
            if (role.equals(roleService.getRoleByName("ROLE_USER"))) {
                model.addAttribute("ROLE_USER", true);
            }
        }
        model.addAttribute("user", user);
        return "edit_user";
    }
    @RequestMapping("/save_edit")
    public String saveUpdateUser(@ModelAttribute("user")@Valid User user, @RequestParam(required=false) String roleAdmin,
                                 @RequestParam(required=false) String roleUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/edit_user";
        }
        List<Role> roles = new LinkedList<>();
        //roles.add(roleService.findByRoleName(6));
        if (roleAdmin != null && roleAdmin.equals("ROLE_ADMIN")) {
            roles.add(roleService.getRoleByName("ROLE_ADMIN"));
        }
        if (roleUser != null && roleUser.equals("ROLE_USER")) {
            roles.add(roleService.getRoleByName("ROLE_USER"));
        }
        user.setRoleList(roles);
        userService.updateUser(user);
        return "redirect:/admin";
    }


    @GetMapping("/addNewUser")
    public String addUser(Model model) {
        User user = new User();
        model.addAttribute("user",user);
        return "add_new_user";
    }
    @PostMapping(value = "/addNewUser")
    public String postAddUser(@ModelAttribute("user") @Valid User user,
                              @RequestParam(required=false) String roleAdmin,
                              @RequestParam(required=false) String roleUser, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/add_new_user";
        }
        List<Role> roles = new LinkedList<>();
        if (roleAdmin != null && roleAdmin.equals("ROLE_ADMIN")) {
            roles.add(roleService.getRoleByName("ROLE_ADMIN"));
        }
        if (roleUser != null && roleUser.equals("ROLE_USER")) {
            roles.add(roleService.getRoleByName("ROLE_USER"));
        }
        user.setRoleList(roles);
        userService.saveUser(user);
        return "redirect:/admin";
    }
    @RequestMapping("/deleteUser")
    public String deleteUser(@RequestParam("empId")long id) {
        userService.deleteUser(id);
        return  "redirect:/admin";
    }
}
