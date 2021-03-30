package org.springframework.samples.mvc.views;

import com.google.gson.Gson;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/*")
public class MyRestController {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
            .createEntityManagerFactory("HibernateJPA");

    @RequestMapping(value = "getDB", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json;charset=UTF-8")
    public String getDB() {
        String result = getCalculationTable();
        return result;
    }

    @RequestMapping(value = "filterDB", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String filterDB(@RequestParam String operation, @RequestParam String value1, @RequestParam(defaultValue = "empty") String value2) {
        if(value1.equals("add")){ value1 = "+"; }
        String result = filterCalculationTable(operation, value1, value2);
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

        try {
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
                DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
                String strDate = dateFormat.format(date);
                String strTime = timeFormat.format(date);
                addCalculationToDB(num1, operation, num2, result, strDate, new Date(), new Date(), strTime, new Date());
                return new DTO("success(json)", result);
            } else {
                return new DTO("fail(json)");
            }

        } catch (Exception e){
            return new DTO("error(json)");
        }
    }

    @RequestMapping(value = "sendData", method = RequestMethod.POST)
    @ResponseBody
    public String sendData(@RequestParam float num1, @RequestParam String operation, @RequestParam float num2){
        System.out.println("------------------");
        System.out.println(num1);
        System.out.println(operation);
        System.out.println(num2);
        System.out.println("------------------");

        float result = -666;

        if (operation.equals("+")) result = num1 + num2;
        else if (operation.equals("-")) result = num1 - num2;
        else if (operation.equals("*")) result = num1 * num2;
        else if (operation.equals("/")) result = num1 / num2;

        if(operation.equals("+") || operation.equals("-") || operation.equals("*") || operation.equals("/")){
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
            String strDate = dateFormat.format(date);
            String strTime = timeFormat.format(date);
            addCalculationToDB(num1, operation, num2, result, strDate, new Date(), new Date(), strTime, new Date());
            return "success(params)";
        } else {
            return "failed";
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


            org.json.JSONArray json = new org.json.JSONArray(calculations);
            org.json.JSONObject historyTable = new org.json.JSONObject();
            System.out.println("JSON STRING");
            System.out.println(json.toString());
            for (int i=0; i<json.length(); i++) {
                org.json.JSONObject jsonObject = new org.json.JSONObject(json.getJSONObject(i));
                historyTable.put(String.valueOf(i), jsonObject);
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

    public void addCalculationToDB(float firstNumber, String operation, float secondNumber, float apotelesma, String hmeromhnia_,
                                   java.util.Date hmeromhnia__,
                                   java.util.Date hmeromhnia, String wra_,
                                   java.util.Date hmerom) {

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
            calculation.setHmeromhnia_(hmeromhnia_);
            calculation.setHmeromhnia__(hmeromhnia__);
            calculation.setHmeromhnia(hmeromhnia);
            calculation.setWra_(wra_);
            calculation.setHmerom(hmerom);

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

}
