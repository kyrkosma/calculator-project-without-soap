package org.springframework.samples.mvc.views;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.google.gson.Gson;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/views/*")
public class ViewsController {

	private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
			.createEntityManagerFactory("HibernateJPA");

	@GetMapping("/home")
	public String showHomepage(HttpSession session) {
		System.out.println("Created at: " + session.getCreationTime());
		System.out.println("Last accessed at: " + session.getLastAccessedTime());
		return "home";
	}

	@GetMapping("/history")
	public String showHistory() {
			return "/views/history";
	}

	@GetMapping("/getHistory")
	@ResponseBody
	public void getHistory(HttpServletResponse response) throws IOException, SQLException {

		String result = getCalculationTable();

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		// Get the printwriter object from response to write the required json object to the output stream
		PrintWriter out = response.getWriter();
		// Perform the following, it will return your json object
		out.print(result);
		out.flush();

	}

	@GetMapping("submitToDB")
	@ResponseBody
	public void submitToDB(@RequestParam String num1, @RequestParam String operation, @RequestParam String num2, HttpServletResponse response) throws IOException, SQLException {
		float result = 98765;
		if (operation.equals("+")) result = Float.parseFloat(num1) + Float.parseFloat(num2);
		else if (operation.equals("-")) result = Float.parseFloat(num1) - Float.parseFloat(num2);
		else if (operation.equals("*")) result = Float.parseFloat(num1) * Float.parseFloat(num2);
		else if (operation.equals("/")) result = Float.parseFloat(num1) / Float.parseFloat(num2);

		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
		String strDate = dateFormat.format(date);
		String strTime = timeFormat.format(date);

		float firstNumber = Float.parseFloat(num1);
		float secondNumber = Float.parseFloat(num2);
		addCalculationToDB(firstNumber, operation, secondNumber,result, strDate, new Date(), new Date(), strTime, new Date());

		response.getWriter().write(String.valueOf(result));
	}

	@GetMapping("searchDB")
	@ResponseBody
	public void searchDB(@RequestParam String operation, @RequestParam String value1, @RequestParam String value2, HttpServletResponse response) throws IOException, SQLException {

		String result = getFilteredCalculationTable(operation, value1, value2);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(result);
		out.flush();

	}

	public Connection getConnectionToDB(){
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe",
					"system",
					"1234"
			);
			return conn;
		} catch (Exception e){
			return null;
		}
	}

	public String getDBGreeting(Connection conn){
		Statement stmt;
		String query = "SELECT * FROM EMP";
		try{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query); /* get the result table */
			rs.next();	/* go to first row */
			return rs.getString("NAME");

		} catch (SQLException throwables) {
			throwables.printStackTrace();
			return "";
		}
	}

	public JSONObject getDBTable(Connection conn) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		String query = "SELECT * FROM CALCULATIONHISTORY";
		try{
			int id = 0;

			JSONArray rowArray = new JSONArray();
			JSONObject historyTable = new JSONObject();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			while(rs.next()){
				id += 1;
				JSONObject row = new JSONObject();
				row.put("firstNumber", rs.getString("FIRST_NUMBER"));
				row.put("operation", rs.getString("OPERATION"));
				row.put("secondNumber", rs.getString("SECOND_NUMBER"));
				row.put("apotelesma", rs.getString("APOTELESMA"));
				row.put("hmeromhnia", rs.getString("HMEROMHNIA_"));
				row.put("wra", rs.getString("WRA_"));
				/*rowArray.add(row);*/
				historyTable.put(String.valueOf(id),row);
			}
			System.out.println(historyTable);
			return historyTable;

		} catch (SQLException throwables) {
			throwables.printStackTrace();
			return null;
		} finally {
			rs.close();
			conn.close();
		}
	}

	public void commitToDB(String num1, String operation, String num2, float result, Connection conn) throws SQLException {

		conn.setAutoCommit(false);
		String updateStatement = "INSERT INTO CALCULATIONHISTORY  VALUES (id_generator.nextval, ?,?,?,?,?,TO_DATE(?,'dd/MM/yyyy'),SYSDATE, ?, SYSTIMESTAMP)";
		PreparedStatement pstmt = conn.prepareStatement(updateStatement);
		String r = "random";

		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
		String strDate = dateFormat.format(date);
		String strTime = timeFormat.format(date);
		System.out.println("Converted String: " + strDate);
		System.out.println("Converted String: " + strTime);

		pstmt.setFloat(1, Float.parseFloat(num1));
		pstmt.setString(2, operation);
		pstmt.setFloat(3, Float.parseFloat(num2));
		pstmt.setFloat(4, result);
		pstmt.setString(5, strDate);
		pstmt.setString(6, strDate);
		pstmt.setString(7, strTime);

		int n = pstmt.executeUpdate();
		System.out.println("n is equal " + n);
		conn.commit();
	}

	public JSONObject getFilteredTable(String operation, String value1, String value2, Connection conn) throws SQLException {
		System.out.println(operation + " ---- " + value1);
		System.out.println(operation + " ---- " + value2);
		ResultSet rs = null;
		String query = "";
		 if (value2.equals("empty")) {
			query = "SELECT * FROM CALCULATIONHISTORY WHERE " + operation + " = '" + value1 + "'";
			 System.out.println("-----------------------------------------------");
			 System.out.println("NO DATE DETECTED");
			 System.out.println("-----------------------------------------------");
		} else {
			 query = "SELECT * FROM CALCULATIONHISTORY WHERE " + operation + " >= TO_DATE('" + value1 + "', 'yyyy-MM-dd') AND " + operation + "<= TO_DATE('" + value2 + "', 'yyyy-MM-dd')";
			 System.out.println("-----------------------------------------------");
			 System.out.println("DATE DETECTED");
			 System.out.println("-----------------------------------------------");
			 System.out.println(query);
			 System.out.println("-----------------------------------------------");
		 }
		PreparedStatement pstmt = conn.prepareStatement(query);

		System.out.println("++++++++++++++++++++++++++++++++++++++++");
		System.out.println(value1);
		System.out.println("++++++++++++++++++++++++++++++++++++++++");
		System.out.println(value2);
		System.out.println("++++++++++++++++++++++++++++++++++++++++");

		try{
			int id = 0;
			JSONObject historyTable = new JSONObject();
			rs = pstmt.executeQuery();

			System.out.println(rs);

			while(rs.next()){
				id += 1;
				JSONObject row = new JSONObject();
				row.put("firstNumber", rs.getString("FIRST_NUMBER"));
				row.put("operation", rs.getString("OPERATION"));
				row.put("secondNumber", rs.getString("SECOND_NUMBER"));
				row.put("apotelesma", rs.getString("APOTELESMA"));
				row.put("hmeromhnia", rs.getString("HMEROMHNIA_"));
				row.put("wra", rs.getString("WRA_"));
				/*rowArray.add(row);*/
				historyTable.put(String.valueOf(id),row);
				System.out.println(historyTable);
			}
			System.out.println("----------------"+id+"-------------");
			System.out.println(historyTable);
			return historyTable;

		} catch (SQLException throwables) {
			throwables.printStackTrace();
			System.out.println("Error at filter history table!");
			return null;
		} finally {
			rs.close();
			conn.close();
		}
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

	public String getFilteredCalculationTable(String operation, String value1, String value2){
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
				if(operation.equals("OPERATION")) {
					queryJPA = "SELECT c FROM Calculation c WHERE c.operation = :firstValue";
					calculations = entityManager.createQuery(queryJPA, Calculation.class).setParameter("firstValue", value1).getResultList();
				} else if (operation.equals("FIRST_NUMBER")){
					queryJPA = "SELECT c FROM Calculation c WHERE c.firstNumber = :firstValue";
					calculations = entityManager.createQuery(queryJPA, Calculation.class).setParameter("firstValue", Float.parseFloat(value1)).getResultList();
				} else if (operation.equals("SECOND_NUMBER")){
					queryJPA = "SELECT c FROM Calculation c WHERE c.secondNumber = :firstValue";
					calculations = entityManager.createQuery(queryJPA, Calculation.class).setParameter("firstValue", Float.parseFloat(value1)).getResultList();
				} else if (operation.equals("APOTELESMA")){
					queryJPA = "SELECT c FROM Calculation c WHERE c.apotelesma = :firstValue";
					calculations = entityManager.createQuery(queryJPA, Calculation.class).setParameter("firstValue", Float.parseFloat(value1)).getResultList();
				}
				System.out.println("-----------------------------------------------");
				System.out.println("NO DATE DETECTED");
				System.out.println("-----------------------------------------------");
			} else {
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

	public void createUsersAndRoles(){
		EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
		EntityTransaction entityTransaction = null;

		try {
			entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();

			Role user = new Role();
			Role admin = new Role();
			User user_ = new User();
			User admin_ = new User();
			user_.setUsername("marioskyrkos1");
			user_.setPassword("1234567");
			user_.setHMEROMHNIA(new java.util.Date());
			user_.setIsActive(1);
			user_.setRole(user);
			admin_.setUsername("marioskyrkos2");
			admin_.setPassword("1234567");
			admin_.setHMEROMHNIA(new java.util.Date());
			admin_.setIsActive(0);
			admin_.setRole(admin);

			entityManager.persist(user);
			entityManager.persist(admin);
			entityManager.persist(user_);
			entityManager.persist(admin_);
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
