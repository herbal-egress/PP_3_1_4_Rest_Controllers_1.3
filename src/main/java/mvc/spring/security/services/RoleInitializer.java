package mvc.spring.security.services;

import mvc.spring.security.entities.Role;
import mvc.spring.security.entities.User;
import mvc.spring.security.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Проверка наличия ролей в базе данных!!! Просто для автоматизации добавил.

@Component
public class RoleInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
               createRoleIfNotExists("ROLE_ADMIN");
        createRoleIfNotExists("ROLE_USER");
    }

    private void createRoleIfNotExists(String roleName) {
        if (roleRepository.findByName(roleName) == null) {
            roleRepository.save(new Role(roleName));
        }
    }
}