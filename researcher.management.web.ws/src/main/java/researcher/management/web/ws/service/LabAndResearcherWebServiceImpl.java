package researcher.management.web.ws.service;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jws.WebService;

import researcher.management.web.ws.model.*;
/**
 * This web service class manages Labs and Researchers by providing methods
 * to create, delete, and link labs with researchers.
 */
@WebService(serviceName="LabAndResearcherWebServiceImpl",targetNamespace = "http://service.ws.web.management.researcher/")
public class LabAndResearcherWebServiceImpl implements LabAndResearcherWebService{
	// Database connection properties
	   private final String DB_URL="jdbc:postgresql://localhost:5432/postgres";
	   private final String DB_USER = "postgres";
	   private final String DB_PASS = "root";
	   // Static block to load PostgreSQL driver
	   private Map<String, Researcher> researchers = new HashMap<String,Researcher>();
		static {
		    try {
		        Class.forName("org.postgresql.Driver");
		    } catch (ClassNotFoundException e) {
		        e.printStackTrace();
		    }
		}
		 /**
	     * Creates a new researcher in the database.
	     *
	     * @param researcher The researcher object containing the researcher's information.
	     * @return true if the researcher was successfully created, false otherwise.
	     */
	   public boolean createResearcher(Researcher researcher) {
		   try {
			Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
			String sql = "INSERT INTO researchers(id_res,res_name,res_email,pub_count,lab_name) values(?,?,?,?,?)";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, researcher.getId());
			preparedStatement.setString(2, researcher.getName());
			preparedStatement.setString(3, researcher.getEmail());
			preparedStatement.setInt(4, researcher.getTotal_article());
			preparedStatement.setString(5, researcher.getLabName());
			int affectedRows = preparedStatement.executeUpdate();
			return affectedRows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	    }
	   /**
	     * Links a lab with a researcher in the database.
	     *
	     * @param labName The name of the lab to link with the researcher.
	     * @param researcherId The ID of the researcher to link with the lab.
	     * @return true if the lab and researcher were successfully linked, false otherwise.
	     */
		public boolean linkLabWithResearcher(String labName, String researcherId) {
			try {
				Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
				String sql = "UPDATE researchers SET lab_name = ? WHERE id_res = ?";
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, labName);
				preparedStatement.setString(2, researcherId);
				int affectedRows = preparedStatement.executeUpdate();
				return affectedRows > 0;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		 /**
	     * Deletes a researcher from the database.
	     *
	     * @param researcherId The ID of the researcher to delete.
	     * @return true if the researcher was successfully deleted, false otherwise.
	     */
		public boolean deleteResearcher(String researcherId) {
		
		    try {
		    	Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
		        String sql = "DELETE FROM researchers WHERE id_res = ?";
		        PreparedStatement preparedStatement = connection.prepareStatement(sql);
		        preparedStatement.setString(1, researcherId);
		        int affectedRows = preparedStatement.executeUpdate();
		        return affectedRows > 0;
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return false;
		   
			}
		    
		}
		 /**
	     * Retrieves a list of researchers associated with a specific lab from the database.
	     *
	     * @param labName The name of the lab for which to retrieve the associated researchers.
	     * @return A list of researchers associated with the specified lab.
	     */
		public List<Researcher> getResearcherByLab(String labName) {
	        
	    	/*return researchers.values().stream()
	                .filter(researcher -> labName.equals(researcher.getLabName()))
	                .collect(Collectors.toList());*/
	    	 List<Researcher> researchers = new ArrayList<Researcher>();
	         try {
	        	 Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
	             String sql = "SELECT * FROM researchers WHERE lab_name = ?";
	             PreparedStatement preparedStatement = connection.prepareStatement(sql);
	             preparedStatement.setString(1, labName);
	             ResultSet resultSet = preparedStatement.executeQuery();

	             while (resultSet.next()) {
	                 Researcher researcher = new Researcher();
	                 researcher.setId(resultSet.getString("id_res"));
	                 researcher.setEmail(resultSet.getString("res_email"));
	                 researcher.setName(resultSet.getString("res_name"));
	                 researcher.setTotal_article(resultSet.getInt("pub_count"));
	                 researcher.setLabName(resultSet.getString("lab_name"));
	                 researchers.add(researcher);
	             }
	         } catch (SQLException e) {
	             e.printStackTrace();
	         }
	         return researchers;
	    	
	    }
		/**
	     * Creates a new lab in the database.
	     *
	     * @param lab The lab object containing the lab's information.
	     * @return true if the lab was successfully created, false otherwise.
	     */
		public boolean createLab(Lab lab) {
		     /*   if (labs.containsKey(lab.getName())) {
		            return false;
		        }
		        labs.put(lab.getName(), lab);
		        return true; */
				try{
					Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
					String sql = "INSERT INTO labs(lab_name,describe) VALUES(?,?)";
					PreparedStatement preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, lab.getName());
					preparedStatement.setString(2, lab.getDescription());
					int affectedRows = preparedStatement.executeUpdate();
					return affectedRows > 0;
				}catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
		    }
		/**
	     * Deletes a lab from the database, and unlinks all associated researchers.
	     *
	     * @param labName The name of the lab to delete.
	     * @return true if the lab was successfully deleted, false otherwise.
	     */
			public boolean deleteLab(String labName) {
				 try  {
				        // First, unlink all researchers associated with the lab.
					    Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
				        String unlinkResearchersSql = "UPDATE researchers SET lab_name = NULL WHERE lab_name = ?";
				        PreparedStatement unlinkResearchersStatement = connection.prepareStatement(unlinkResearchersSql);
				        unlinkResearchersStatement.setString(1, labName);
				        unlinkResearchersStatement.executeUpdate();

				        // Then, delete the lab.
				        String deleteLabSql = "DELETE FROM labs WHERE lab_name = ?";
				        PreparedStatement deleteLabStatement = connection.prepareStatement(deleteLabSql);
				        deleteLabStatement.setString(1, labName);
				        int affectedRows = deleteLabStatement.executeUpdate();
				        return affectedRows > 0;
				    } catch (SQLException e) {
				        e.printStackTrace();
				        return false;
				    }
			 }
			  /**
		     * Retrieves a lab by its name from the database.
		     *
		     * @param name The name of the lab to retrieve.
		     * @return A Lab object containing the lab's information if found, otherwise a new empty Lab object.
		     */
		    public Lab getLabByName(String name) {
		        //return labs.get(name);
		    	Lab  lab = null;
		    	try {
					Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
					String sql = "SELECT*FROM labs WHERE lab_name = ?";
					PreparedStatement preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, name);
					ResultSet resultSet = preparedStatement.executeQuery();
					if(resultSet.next()) {
						lab = new Lab();
						lab.setName(resultSet.getString("lab_name"));
						lab.setDescription(resultSet.getString("describe"));
					}else {
							lab = new Lab(); // 创建一个新的空 Lab 对象，而不是返回 null
				            lab.setName("Not found");
				            lab.setDescription("No lab found with the given name.");
					}
				} catch (SQLException e) {
					 e.printStackTrace();
				}
		    	return lab;
		    }

		
		public static void main(String[] args) {
		   LabAndResearcherWebServiceImpl labWebService = new LabAndResearcherWebServiceImpl();
		   Lab	lab = new Lab("French Nantional Research Center","First test local");
		   labWebService.createLab(lab);
		   Researcher researcher = new Researcher("1","Michel","724915929@gmail.com","French Nantional Research Center",15);
		   LabAndResearcherWebServiceImpl researcherWebServiceImpl = new LabAndResearcherWebServiceImpl();
		   System.out.println(researcherWebServiceImpl.createResearcher(researcher));
	}

}
