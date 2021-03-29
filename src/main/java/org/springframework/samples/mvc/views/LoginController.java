package org.springframework.samples.mvc.views;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sign-in/*")
public class LoginController {

    /*private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
            .createEntityManagerFactory("HibernateJPA");*/

    @GetMapping("/")
    public String loginPage() {
        return "loginPage";
    }

    /*@PostMapping("/")
    public String checkCredentials(@RequestParam String username, @RequestParam String password, Model model) throws IOException {

        *//*String query;

        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction entityTransaction = null;
        try{
            List<User> users;
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();

            query = "SELECT u FROM User u WHERE u.username = :username AND u.password = :password";
            users = entityManager.createQuery(query, User.class).setParameter("username", username).setParameter("password", password).getResultList();

            if (!users.isEmpty()){
                User user = users.get(0);
                model.addAttribute("username", user.getUsername());
                model.addAttribute("role",user.getRole().getName());

                    return "/home";

            }

            entityTransaction.commit();

        } catch (Exception exception) {

            // If there is an exception rollback changes
            if (entityTransaction != null) {
                entityTransaction.rollback();
            }
            exception.printStackTrace();

        } finally {
            // Close EntityManager
            entityManager.close();
        }*//*
        return "loginPage";
    }*/
}
