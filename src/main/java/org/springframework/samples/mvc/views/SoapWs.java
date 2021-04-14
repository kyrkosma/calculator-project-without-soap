package org.springframework.samples.mvc.views;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.xml.bind.annotation.XmlElement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

// For the SOAP Response to work correctly when returning an Object
// In the Response classes you need to define the original Calculation.class and not the one produced by WsImport
@WebService()
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public class SoapWs {

    public static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
            .createEntityManagerFactory("HibernateJPA");

    @WebMethod()
    public List<Calculation> soapGetDb(){
        List<Calculation> calculations = UtilSoapWs.getInstance().getTable();
        return calculations;
    }

    @WebMethod()
    public List<Calculation> soapFilterDb(
            @WebParam(name = "filterBy") @XmlElement(required = true) String operation,
            @WebParam(name = "equals") @XmlElement(required = true) String value1){
        List<Calculation> calculations = UtilSoapWs.getInstance().filterTable(operation, value1, "empty");
        return calculations;
    }

    @WebMethod()
    public List<Calculation> soapFilterDbByDate(
            @WebParam(name = "fromDate") @XmlElement(required = true) String value1,
            @WebParam(name = "toDate") @XmlElement(required = true) String value2){
        List<Calculation> calculations = UtilSoapWs.getInstance().filterTable("date", value1, value2);
        return calculations;
    }

    @WebMethod()
    public String soapCalculate(
            @WebParam(name = "firstNumber") float num1,
            @WebParam(name = "operation") String operation,
            @WebParam(name = "secondNumber") float num2){
        return UtilSoapWs.getInstance().doCalculation(num1, operation, num2);
    }



}
