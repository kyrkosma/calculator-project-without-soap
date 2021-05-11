package org.springframework.samples.mvc.views;

import com.google.gson.Gson;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/*")
public class MyRestController {

    /*@PersistenceContext
    EntityManager entityManager;*/

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
            .createEntityManagerFactory("HibernateJPA");

    //Caused problems with rollback, couldn't filter table
    /*@Autowired
    private EntityManager entityManager;*/

    @RequestMapping(value = "getDB", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json;charset=UTF-8")
    public String getDB() {
        String result = getCalculationTable();
        return result;
    }

    @RequestMapping(value = "filterDB", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String filterDB(@RequestParam String username, @RequestParam String operation, @RequestParam String value1, @RequestParam(defaultValue = "empty") String value2) {
        if(value1.equals("add")){ value1 = "+"; }
        String result;

        if (username.equals("all")){
            if (operation.equals("all")){
                result = getCalculationTable();
            } else {
                result = filterCalculationTable(operation, value1, value2);
            }
        } else {
            result = filterCalculationTableByUser(username, operation, value1, value2);
        }

        return result;
    }

    @RequestMapping(value = "sendData2", method = RequestMethod.POST,
                    consumes = "application/json", produces = "application/json")
    public DTO sendData2(@RequestBody String string){

        System.out.println("json body : " + string);
        //String to JSONObject
        JSONObject json = new JSONObject(string);
        System.out.println(json);

        float num1;
        String operation;
        float num2;
        float result = -666;

        String userName;
        List<String> userNames = new ArrayList<>();
        boolean foundUser = false;

        try {
            userNames = getUsernamesFromDB();
            userName = json.getString("username");

            for(String u : userNames){
                if (u.equals(userName)){
                    foundUser = true;
                    break;
                }
            }

            if (!foundUser){
                return new DTO("Specified user does not exist.");
            }

            num1 = json.getFloat("num1");
            operation = json.getString("operation");
            num2 = json.getFloat("num2");

            if (operation.equals("+")) result = num1 + num2;
            else if (operation.equals("-")) result = num1 - num2;
            else if (operation.equals("*")) result = num1 * num2;
            else if (operation.equals("/")) result = num1 / num2;

            if(operation.equals("+") || operation.equals("-") || operation.equals("*") || operation.equals("/")){
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                String strDate = dateFormat.format(date);
                String strTime = timeFormat.format(date);
                addCalculationToDB(num1, operation, num2, result, strDate, new Date(), new Date(), strTime, new Date(), userName);
                return new DTO("Success.", result);
            } else {
                return new DTO("Failed: There is something wrong with the data you are trying to send.");
            }

        } catch (Exception e){
            return new DTO("Failed: There is something wrong with the data you are trying to send.");
        }
    }

    @RequestMapping(value = "sendData", method = RequestMethod.POST)
    @ResponseBody
    public String sendData(@RequestParam String username, @RequestParam float num1, @RequestParam String op, @RequestParam float num2){
        System.out.println("------------------");
        System.out.println(num1);
        System.out.println(op);
        System.out.println(num2);
        System.out.println("------------------");

        String operation = op;

        if (op.equals("add")){
            operation = "+";
        }

        List<String> userNames = new ArrayList<>();
        boolean foundUser = false;

        userNames = getUsernamesFromDB();

        for(String u : userNames){
            if (u.equals(username)){
                foundUser = true;
                break;
            }
        }

        if (!foundUser){
            return "Specified user does not exist.";
        }

        float result = -666;

        if (operation.equals("+")) result = num1 + num2;
        else if (operation.equals("-")) result = num1 - num2;
        else if (operation.equals("*")) result = num1 * num2;
        else if (operation.equals("/")) result = num1 / num2;

        if(operation.equals("+") || operation.equals("-") || operation.equals("*") || operation.equals("/")){
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            String strDate = dateFormat.format(date);
            String strTime = timeFormat.format(date);
            addCalculationToDB(num1, operation, num2, result, strDate, new Date(), new Date(), strTime, new Date(), username);
            return "Success. The result is " + result;
        } else {
            return "Failed: There is something wrong with the data you are trying to send.";
        }
    }

    public String getCalculationTable(){

        String result = null;
        // The EntityManager class allows operations such as create, read, update, delete
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        // Used to issue transactions on the EntityManager
        EntityTransaction entityTransaction = null;

        try {

            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();

            List<Calculation> calculations = entityManager.createQuery( "from Calculation", Calculation.class ).getResultList();

            /*org.json.JSONArray json = new org.json.JSONArray(calculations);
            org.json.JSONObject historyTable = new org.json.JSONObject();
            System.out.println("JSON STRING");
            System.out.println(json.toString());
            for (int i=0; i<json.length(); i++) {
                org.json.JSONObject jsonObject = new org.json.JSONObject(json.getJSONObject(i));
                historyTable.put(String.valueOf(i), jsonObject);
            }*/

            Gson g = new Gson();
            result = g.toJson(calculations);


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

        return result;
    }

    public void addCalculationToDB(float firstNumber, String operation, float secondNumber, float apotelesma, String hm,
                                   java.util.Date hmer,
                                   java.util.Date hmeromhnia, String wra_,
                                   java.util.Date hmerom, String userName) {

        // The EntityManager class allows operations such as create, read, update, delete
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        // Used to issue transactions on the EntityManager
        EntityTransaction entityTransaction = null;

        try {
            // Get transaction and start
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();

            // Create and set values for new Calculation Obj
            Calculation calculation = new Calculation();
            calculation.setFirstNumber(firstNumber);
            calculation.setOperation(operation);
            calculation.setSecondNumber(secondNumber);
            calculation.setApotelesma(apotelesma);
            calculation.setHm(hm);
            calculation.setHmer(hmer);
            calculation.setHmeromhnia(hmeromhnia);
            calculation.setWra_(wra_);
            calculation.setHmerom(hmerom);
            calculation.setUserName(userName);

            // Save the customer object
            entityManager.persist(calculation);
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
    }

    public String filterCalculationTable(String operation, String value1, String value2){
        String queryJPA = null;
        String result = null;
        // The EntityManager class allows operations such as create, read, update, delete
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        // Used to issue transactions on the EntityManager
        EntityTransaction entityTransaction = null;

        try {
            List<Calculation> calculations = null;
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();

            if (value2.equals("empty")) {
                if(operation.equals("operation")) {
                    queryJPA = "SELECT c FROM Calculation c WHERE c.operation = :firstValue";
                    calculations = entityManager.createQuery(queryJPA, Calculation.class).setParameter("firstValue", value1).getResultList();
                } else if (operation.equals("first")){
                    queryJPA = "SELECT c FROM Calculation c WHERE c.firstNumber = :firstValue";
                    calculations = entityManager.createQuery(queryJPA, Calculation.class).setParameter("firstValue", Float.parseFloat(value1)).getResultList();
                } else if (operation.equals("second")){
                    queryJPA = "SELECT c FROM Calculation c WHERE c.secondNumber = :firstValue";
                    calculations = entityManager.createQuery(queryJPA, Calculation.class).setParameter("firstValue", Float.parseFloat(value1)).getResultList();
                } else if (operation.equals("result")){
                    queryJPA = "SELECT c FROM Calculation c WHERE c.apotelesma = :firstValue";
                    calculations = entityManager.createQuery(queryJPA, Calculation.class).setParameter("firstValue", Float.parseFloat(value1)).getResultList();
                }
                System.out.println("-----------------------------------------------");
                System.out.println("NO DATE DETECTED");
                System.out.println("-----------------------------------------------");
            } else {
                if(operation.equals("date")) {
                    /*Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(value1);
                    Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(value2);*/
                    queryJPA = "SELECT c FROM Calculation c WHERE TRUNC(c.hmeromhnia) >= TO_DATE(:firstValue,'yyyy-MM-dd') AND TRUNC(c.hmeromhnia) <= TO_DATE(:secondValue,'yyyy-MM-dd')";
                    calculations = entityManager.createQuery(queryJPA, Calculation.class).setParameter("firstValue", value1).setParameter("secondValue", value2).getResultList();
                    System.out.println("-----------------------------------------------");
                    System.out.println("DATE DETECTED");
                    System.out.println(value1);
                    System.out.println(value2);
                    System.out.println("-----------------------------------------------");
                }
            }

            Gson g = new Gson();
            result = g.toJson(calculations);

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

        return result;
    }

    public String filterCalculationTableByUser(String userName, String operation, String value1, String value2){
        String queryJPA = null;
        String result = null;
        // The EntityManager class allows operations such as create, read, update, delete
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        // Used to issue transactions on the EntityManager
        EntityTransaction entityTransaction = null;

        try {
            List<Calculation> calculations = null;
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();

            if (value2.equals("empty")) {
                if(operation.equals("operation")) {
                    queryJPA = "SELECT c FROM Calculation c WHERE c.operation = :firstValue AND c.userName = :userValue";
                    calculations = entityManager.createQuery(queryJPA, Calculation.class).setParameter("firstValue", value1).setParameter("userValue", userName).getResultList();
                } else if (operation.equals("first")){
                    queryJPA = "SELECT c FROM Calculation c WHERE c.firstNumber = :firstValue AND c.userName = :userValue";
                    calculations = entityManager.createQuery(queryJPA, Calculation.class).setParameter("firstValue", Float.parseFloat(value1)).setParameter("userValue", userName).getResultList();
                } else if (operation.equals("second")){
                    queryJPA = "SELECT c FROM Calculation c WHERE c.secondNumber = :firstValue AND c.userName = :userValue";
                    calculations = entityManager.createQuery(queryJPA, Calculation.class).setParameter("firstValue", Float.parseFloat(value1)).setParameter("userValue", userName).getResultList();
                } else if (operation.equals("result")){
                    queryJPA = "SELECT c FROM Calculation c WHERE c.apotelesma = :firstValue AND c.userName = :userValue";
                    calculations = entityManager.createQuery(queryJPA, Calculation.class).setParameter("firstValue", Float.parseFloat(value1)).setParameter("userValue", userName).getResultList();
                } else if (operation.equals("all")){
                    queryJPA = "SELECT c FROM Calculation c WHERE c.userName = :userValue";
                    calculations = entityManager.createQuery(queryJPA, Calculation.class).setParameter("userValue", userName).getResultList();
                }
                System.out.println("-----------------------------------------------");
                System.out.println("NO DATE DETECTED");
                System.out.println("-----------------------------------------------");
            } else {
				/*Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(value1);
				Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(value2);*/
                queryJPA = "SELECT c FROM Calculation c WHERE TRUNC(c.hmeromhnia) >= TO_DATE(:firstValue,'yyyy-MM-dd') AND TRUNC(c.hmeromhnia) <= TO_DATE(:secondValue,'yyyy-MM-dd') AND c.userName = :userValue";
                calculations = entityManager.createQuery(queryJPA, Calculation.class).setParameter("firstValue", value1).setParameter("secondValue", value2).setParameter("userValue", userName).getResultList();
                System.out.println("-----------------------------------------------");
                System.out.println("DATE DETECTED");
                System.out.println(value1);
                System.out.println(value2);
                System.out.println("-----------------------------------------------");
            }

            Gson g = new Gson();
            result = g.toJson(calculations);

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

        return result;
    }

    public class DTO {

        private String request;
        private float result;

        public DTO(String request) {
            this.request = request;
        }

        public DTO(String request, float result) {
            this.request = request;
            this.result = result;
        }

        public String getRequest() {
            return request;
        }

        public void setRequest(String request) {
            this.request = request;
        }

        public float getResult() {
            return result;
        }

        public void setResult(float result) {
            this.result = result;
        }
    }

    public List<String> getUsernamesFromDB(){

        String result = null;
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction entityTransaction = null;

        List<User> users;
        List<String> userNames = new ArrayList<>();

        try {

            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();

            users = entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();

            for (User u: users) {
                userNames.add(u.getUsername());
                System.out.println(u.getUsername());
            }

            entityTransaction.commit();

        } catch (Exception exception) {

            if (entityTransaction != null) {
                entityTransaction.rollback();
            }
            exception.printStackTrace();

        } finally {
            entityManager.close();
        }
        return userNames;
    }

}
