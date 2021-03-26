package org.springframework.samples.mvc.views;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/sign-in/*")
public class LoginController {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
            .createEntityManagerFactory("HibernateJPA");

    @GetMapping("/")
    public String loginPage() {

        return "loginPage";

    }

    @GetMapping("/sign-out")
    public String handleLogout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if(session.getAttribute("username") != null) {
            session.invalidate();
        }
        return "loginPage";

    }

    @PostMapping("check")
    public String checkCredentials(@RequestParam String username, @RequestParam String password, Model model, HttpServletRequest request) throws IOException {

        String query;

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

                HttpSession session = request.getSession();
                session.setMaxInactiveInterval(30);

                System.out.println(session.getAttribute("User"));

                session.setAttribute("username", user.getUsername());
                session.setAttribute("user", user);

                System.out.println("--------------------------");
                System.out.println("Session ID");
                System.out.println(session.getId());
                System.out.println("Session Creation Time");
                System.out.println(session.getCreationTime());
                System.out.println("Is this a new session?");
                System.out.println(session.isNew());
                System.out.println("Last time accessed");
                System.out.println(session.getLastAccessedTime());
                System.out.println("--------------------------");

                if(user.getRole().getName().equals("user")){
                    return "/home";
                } else if (user.getRole().getName().equals("admin")){
                    return "/views/history";
                }

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
        }

        return "loginPage";

    }
}
