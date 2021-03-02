import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class DBservice {
	
		private static Connection con;
	
		public DBservice() {
			String url= "jdbc:mysql://localhost:3306/bankdb";
			String username="bank";
			String password="securepassword";
		Connection con;
		try {
			con = DriverManager.getConnection(url, username, password);
			DBservice.con = con;
			System.out.println("************** Connected to Database! *************************");
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			con = null;
			e.printStackTrace();
		}
	}
	
	
/****************************************************************************************************************/	
	int addAccount(String name, String address, int amount, int accNo)
	{
		int userId=-1;	
		try
		{
			// Add customer
			con.setAutoCommit(false);
			String addCustSql = "INSERT INTO Account (name, address, amount, accNo) VALUES (?,?,?,?)";
			PreparedStatement addCust = con.prepareStatement(addCustSql, Statement.RETURN_GENERATED_KEYS);
			addCust.setString(1,name);
			addCust.setString(2,address);
			addCust.setInt(3,amount);
			addCust.setInt(4,accNo);
			addCust.executeUpdate();
			/*ResultSet addCustResults = addCust.getGeneratedKeys();
			if(addCustResults.next())
			{
				userId = addCustResults.getInt(1);
				
			}*/
			con.commit();
		}
		catch(SQLException ex)
		{
			try {
				con.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Error !"+ex.getMessage());
		}
		return userId;
	}
/*****************************************************************************************************/
	public int getBalance()throws SQLException {
		int balance=0 ;
		try
		{
			// get balance
			
			con.setAutoCommit(false);
			String addCustSql = "SELECT amount FROM account WHERE accNo=(SELECT MAX(accNo) FROM account);";
			PreparedStatement addCust = con.prepareStatement(addCustSql);
			//addCust.setInt(1,accno);
			ResultSet resultSet = addCust.executeQuery();
			while(resultSet.next()) {
				balance = resultSet.getInt("amount");
			}
			con.commit();
			
		}
		catch(SQLException ex)
		{
			try {
				con.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Error !"+ex.getMessage());
		}
		return balance;
		
	}

/*********************************************************************************************************************/
	public int setBalance(int accno)throws SQLException {
		int balance = -1;
		try
		{
			// Add balance
			con.setAutoCommit(false);
			String addCustSql = "UPDATE Account SET amount = ? WHERE accNo = ?";
			PreparedStatement addCust = con.prepareStatement(addCustSql, Statement.RETURN_GENERATED_KEYS);
			addCust.setInt(1, accno);
			addCust.executeUpdate();
			ResultSet addCustResults = addCust.getGeneratedKeys();
			if(addCustResults.next())
			{
				balance = addCustResults.getInt(1);
				
			}
			con.commit();
			return balance;
		}
		catch(SQLException ex)
		{
			try {
				con.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Error !"+ex.getMessage());
		}
		return balance;
	}
/*************************************************************************************************************/
	int addTransaction(String tType, java.sql.Date localDate, int amount, int tAccNo)
	{
		int transactionId=-1;	
		try
		{
			// Add transaction
			con.setAutoCommit(false);
			String addTranSql = "INSERT INTO transactions(tType, tDate, amount, tAccNo) VALUES (?,?,?,?)";
			PreparedStatement addTran = con.prepareStatement(addTranSql, Statement.RETURN_GENERATED_KEYS);
	 		addTran.setString(1,tType);
			addTran.setDate(2,localDate);
			addTran.setInt(3,amount);
			addTran.setInt(4,tAccNo);
			addTran.executeUpdate();
			ResultSet addTranResults = addTran.getGeneratedKeys();
			if(addTranResults.next())
			{
				transactionId = addTranResults.getInt(1);
				
			}
			con.commit();
			return transactionId;
		}
		catch(SQLException ex)
		{
			try {
				con.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Error !"+ex.getMessage());
		}
		return transactionId;
	}

/***************************************************************************************************************/
	public int getMaxAccNo()throws SQLException {
		int accountNumber = 0;
		con.setAutoCommit(false);
		String addCustSql = "SELECT MAX(accNo) FROM account";
		PreparedStatement addCust = con.prepareStatement(addCustSql);
		//addCust.setInt(1,accno);
		ResultSet resultSet = addCust.executeQuery();
		while(resultSet.next()) {
		 accountNumber = resultSet.getInt("max(accNo)");
		 break;
		}
		con.commit();
		return accountNumber;
		
	}
	
/***************************************************************************************************************/
	// update customer
	boolean updateAccount(int accNo, int amount) {
		boolean success = false;
	try	{
		con.setAutoCommit(false);
			String updateSql = "UPDATE Account SET amount = ? WHERE accNo = ?";
			PreparedStatement updateBalance = con.prepareStatement(updateSql);
			updateBalance.setInt(1,amount);
			updateBalance.setInt(2,accNo);
			updateBalance.executeUpdate();
			success = true;
			con.commit();
			return success;
		}
	catch(SQLException ex)
	{
		try {
			con.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Error !"+ex.getMessage());
	}
		return success;
	}

/*****************************************************************************************/
// delete customer
		boolean deleteAccount(int accNo) {
		boolean success = false;
	try	{
		con.setAutoCommit(false);
			String deleteSql = "DELETE FROM Account WHERE accNo = ?";
			PreparedStatement deleteAcc = con.prepareStatement(deleteSql);
			deleteAcc.setInt(1,accNo);
			deleteAcc.executeUpdate();
			success = true;
			con.commit();
		}
	catch(SQLException ex)
	{
		try {
			con.rollback();
		} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println("Error !"+ex.getMessage());
	}
		return success;
	}
/*****************************************************************************************************/	
	// delete transaction
	boolean deleteTransaction(int accNo) {
		boolean success = false;
	try	{
		con.setAutoCommit(false);
			String deleteSql = "DELETE FROM Transactions WHERE tAccNo = ?";
			PreparedStatement deleteAcc = con.prepareStatement(deleteSql);
			deleteAcc.setInt(1,accNo);
			deleteAcc.executeUpdate();
			success = true;
			con.commit();
	}
	catch(SQLException ex)
	{
		try {
			con.rollback();
		} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
	System.out.println("Error !"+ex.getMessage());
}
		return success;
	}
/************************************************************************************************************/	
	public void getTrans(int accno, int notxns)throws SQLException {
		int balance=0 ;
		try
		{
			// get transactions for one acccNo
			
			con.setAutoCommit(false);
			String addCustSql = "SELECT * FROM transactions WHERE TAccNo = ?";
			PreparedStatement addCust = con.prepareStatement(addCustSql);
			//addCust.setInt(1,accno);
			ResultSet resultSet = addCust.executeQuery();
			while(resultSet.next()) {
				balance = resultSet.getInt("amount");
				System.out.println(resultSet.getString("tType")+" "+resultSet.getInt("amount")+" "+resultSet.getDate("tDate"));
			}
			con.commit();
			
		}
		catch(SQLException ex)
		{
			try {
				con.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Error !"+ex.getMessage());
		}
	
		
	}	

	
	}


