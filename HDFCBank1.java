import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import allXceptions.MyExceptionHandler;
public class HDFCBank1 implements Bank,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DBservice database = new DBservice();
	
	//private List<Account> accountList = new ArrayList<Account>();
	@Override
	public String openAccount(String name, String address, int amount) throws InsufficientBalanceException{
	
		if(amount < Bank.MIN_BALANCE)
			throw new InsufficientBalanceException("Min balance required to open Account is "+Bank.MIN_BALANCE);
		int accountNumber = 11111;
		try {
			int i = database.getMaxAccNo();
			if(i != 0) 
				accountNumber = i+1;
		} catch (SQLException ex) {
			/*// TODO Auto-generated catch block
			e.printStackTrace();*/
			MyExceptionHandler.handleException("projName","Module 1", ex);
		}
		
		database.addAccount(name, address, amount, accountNumber);
		
		return "Opened Account! yey Your account number is " + accountNumber;
	}

	@Override
	public int withdraw(int accno, int amount) throws InsufficientBalanceException, InvalidAccountException {
		/*Account acct = searchAccount(accno);
		if(acct==null)
			throw new InvalidAccountException("Invalid Account Number");
		int oldBalance = acct.getBalance();
		if(oldBalance-amount > Bank.MIN_BALANCE) {
			acct.setBalance(oldBalance-amount);
			Transaction txn = new Transaction(new Date(),"DEBIT",amount);
			acct.getTxns().add(txn);*/
			try {
				
				int ac = database.getBalance();
				//System.out.println(ac);
				if(ac-amount > Bank.MIN_BALANCE) {
				database.updateAccount(accno, ac-amount);
				java.sql.Date date;
				LocalDate d = java.time.LocalDate.now();
				date = new java.sql.Date(d.getYear(), d.getMonthValue(), d.getDayOfMonth());
				database.addTransaction("Debit",date, amount, accno);
				}
			else {
			throw new InsufficientBalanceException("Min balance requires is"+Bank.MIN_BALANCE);
			}
		 }catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
		 }
		try {
			return database.getBalance();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	private Account searchAccount(int accno2) {
		// TODO Auto-generated method stub
		for(Account acct : accountList) {
			if(acct.getAccno()==accno2)
				return acct;
		}
		return null;
	}

	@Override
	public int deposit(int accno, int amount) throws InvalidAccountException {
		/*Account acct = searchAccount(accno);
		if(acct==null)
			throw new InvalidAccountException("Invalid Account Number");
		int oldBalance = acct.getBalance();
		acct.setBalance(oldBalance+amount);
		Transaction txn = new Transaction(new Date(),"CREDIT",amount);
		acct.getTxns().add(txn);
		int amt = oldBalance+amount;
		database.updateAccount(accno, amt);
		java.sql.Date date;
		LocalDate d = java.time.LocalDate.now();
		date = new java.sql.Date(d.getYear(), d.getMonthValue(), d.getDayOfMonth());
		database.addTransaction("Credit",date, amount, accno);
		return acct.getBalance();*/
		try {
			
			int ac = database.getBalance();
			//System.out.println(ac);
			if(ac+amount > Bank.MIN_BALANCE) {
			database.updateAccount(accno, ac+amount);
			java.sql.Date date;
			LocalDate d = java.time.LocalDate.now();
			date = new java.sql.Date(d.getYear(), d.getMonthValue(), d.getDayOfMonth());
			database.addTransaction("CREDIT",date, amount, accno);
			}
		else {
		throw new InsufficientBalanceException("Min balance requires is"+Bank.MIN_BALANCE);
		}
	 }catch (SQLException e) {
// TODO Auto-generated catch block
e.printStackTrace();
	 } catch (InsufficientBalanceException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		return database.getBalance();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return 0;
	}

	@Override
	public int transfer(int accfrom, int accto, int amount)
			throws InvalidAccountException, InsufficientBalanceException {     
		int newbalance = withdraw(accfrom,amount);
		try {
			deposit(accto,amount);			
		}catch(Exception ex) {
			deposit(accfrom,amount);			
		}
		return newbalance;

	}

	@Override
	public int closeAccount(int accno) throws InvalidAccountException {
	/*	Account acct = searchAccount(accno);
		System.out.println(acct.getAccno());
		if(acct==null)
			throw new InvalidAccountException("Invalid Account Number");
		accountList.remove(acct);*/
			database.deleteAccount(accno);
			database.deleteTransaction(accno);
			
		return 0;
	}

	@Override
	public String printRecentTransaction(int accno, int notxns) throws InvalidAccountException {
		// TODO Auto-generated method stub
		/*Account acct = searchAccount(accno);
		if(acct==null) throw new InvalidAccountException("Invalid Account Number");
		List<Transaction> txns = acct.getTxns();
		Collections.sort(txns,(txn1,txn2)->{return txn2.getDate().compareTo(txn1.getDate());});
		StringBuilder builder = new StringBuilder();
		for(int i=0; i < notxns; i++) {
			builder.append(txns.get(i).toString());
		}
		return builder.toString();*/
		return null;
	}

}