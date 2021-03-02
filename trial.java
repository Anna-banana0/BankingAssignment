import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class trial implements Bank,Serializable {
	private List<Account> accountList = new ArrayList<Account>();
	private int accno=11111;
	@Override
	public String openAccount(String name, String address, int amount) throws InsufficientBalanceException {
		if(amount < Bank.MIN_BALANCE)
			throw new InsufficientBalanceException("Min balance required to open Account is "+Bank.MIN_BALANCE);
		Account acct = new Account(accno++,name, address,amount);
		accountList.add(acct);
		return "HDFC Thanks u for opening acc new accno is "+acct.getAccno();
	}

	@Override
	public int withdraw(int accno, int amount) throws InsufficientBalanceException, InvalidAccountException {
		Account acct = searchAccount(accno);
		if(acct==null)
			throw new InvalidAccountException("Yo");
		int oldBalance = acct.getBalance();
		if(oldBalance-amount > Bank.MIN_BALANCE) {
			acct.setBalance(oldBalance-amount);
			Transaction txn = new Transaction(new Date(),"DEBIT",amount);
			acct.getTxns().add(txn);
		}else {
			throw new InsufficientBalanceException("Min balance requires is"+Bank.MIN_BALANCE);
		}
		return acct.getBalance();
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
		Account acct = searchAccount(accno);
		if(acct==null)
			throw new InvalidAccountException("Yo");
		int oldBalance = acct.getBalance();
		acct.setBalance(oldBalance+amount);
		Transaction txn = new Transaction(new Date(),"CREDIT",amount);
		acct.getTxns().add(txn);
		return acct.getBalance();
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
		Account acct = searchAccount(accno);
		if(acct==null)
			throw new InvalidAccountException("Yo");
		accountList.remove(acct);
		return acct.getBalance();
	}

	@Override
	public String printRecentTransaction(int accno, int notxns) throws InvalidAccountException {
		// TODO Auto-generated method stub
		Account acct = searchAccount(accno);
		if(acct==null) throw new InvalidAccountException("Yo");
		List<Transaction> txns = acct.getTxns();
		Collections.sort(txns,(txn1,txn2)->{return txn2.getDate().compareTo(txn1.getDate());});
		StringBuilder builder = new StringBuilder();
		for(int i=0; i < notxns; i++) {
			builder.append(txns.get(i).toString());
		}
		return builder.toString();
	}

}
