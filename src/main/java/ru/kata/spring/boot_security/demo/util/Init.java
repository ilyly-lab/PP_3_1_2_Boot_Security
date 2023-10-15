package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.services.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedList;
import java.util.List;


@Component
public class Init {
    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;

    @Autowired
    public Init(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void initializedDataBase() {
        roleService.save(new Role("ROLE_ADMIN"));
        roleService.save(new Role("ROLE_USER"));
        List<Role> adminRole = new LinkedList<>();
        List<Role> userRole = new LinkedList<>();
        List<Role> allRoles = new LinkedList<>();
        adminRole.add(roleService.getRoleByName("ROLE_ADMIN"));
        userRole.add(roleService.getRoleByName("ROLE_USER"));
        allRoles.add(roleService.getRoleByName("ROLE_ADMIN"));
        allRoles.add(roleService.getRoleByName("ROLE_USER"));
        userService.saveUser(new User( "Admin","Adminov", "@admin.yandex", "100" , adminRole));
        userService.saveUser(new User( "User", "Userov", "@user.gmail", "100", userRole));
        userService.saveUser(new User("Trans", "Transov", "@trans.rambler", "100", allRoles));
    }
}
