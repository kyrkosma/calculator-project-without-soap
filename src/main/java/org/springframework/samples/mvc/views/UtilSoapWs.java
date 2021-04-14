package org.springframework.samples.mvc.views;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UtilSoapWs extends SoapWs {

    public static UtilSoapWs getInstance(){
        return new UtilSoapWs();
    }

    /*private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
            .createEntityManagerFactory("HibernateJPA");*/

    public List<Calculation> getTable(){

        List<Calculation> calculations = null;
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction entityTransaction = null;

        try {

            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();

            calculations = entityManager.createQuery( "from Calculation", Calculation.class ).getResultList();

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

        return calculations;
    }

    public List<Calculation> filterTable(String operation, String value1, String value2){
        String queryJPA = null;
        List<Calculation> calculations = null;
        // The EntityManager class allows operations such as create, read, update, delete
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        // Used to issue transactions on the EntityManager
        EntityTransaction entityTransaction = null;

        try {

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

        return calculations;
    }

    public String doCalculation(float num1, String operation, float num2){
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
            return "success";
        } else {
            return "failed";
        }
    }

    public void addCalculationToDB(float firstNumber, String operation, float secondNumber, float apotelesma, String hm,
                                    java.util.Date hmer,
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
            calculation.setHm(hm);
            calculation.setHmer(hmer);
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
}
