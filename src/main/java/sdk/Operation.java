package sdk;

public class Operation {
	public OperationHeader H;
	public OperationBody B;

	public Operation() {}

	public Operation(String type, String amount, String target) {
		this.H = new OperationHeader(type);
		if (type.equals("create-account")) {
			this.B = new CreateAccount(amount, target, "");
		}
		if (type.equals("payment")) {
			this.B = new Payment(amount, target);
		}
	}
	public Object[] toArray(){
		Object[] array = {this.H.toArray(), this.B.toArray()};
		return array;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Operation)){
			return false;
		}
		Operation op = (Operation)obj;
		if ((H.equals(op.H)) && (B.equals(op.B))){
			return true;
		}
		else {
			return false;
		}
	}
}

class OperationHeader {
	public String type;
	
	public OperationHeader(String type) {
		this.type = type;
	}
	public String[] toArray() {
		String[] array = {this.type};
		return array;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof OperationHeader))
			return false;
		OperationHeader opHeader = (OperationHeader)obj;
		if (type.equals(opHeader.type))
			return true;
		else
			return false; 
	}
}

interface OperationBody {
	abstract String[] toArray();
}

class CreateAccount implements OperationBody {
	public String target = "";
	public String amount = "";
	public String linked = "";

	public CreateAccount(String amount, String target, String linked) {
		this.target = target;
		this.amount = amount;
		this.linked = linked;
	}
	public String[] toArray() {
		String[] array = {this.target, this.amount, this.linked};
		return array;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof CreateAccount))
			return false;
		CreateAccount opCreateAccount = (CreateAccount)obj;
		if ((target.equals(opCreateAccount.target)) && (amount.equals(opCreateAccount.amount)) && (linked.equals(opCreateAccount.linked)))
			return true;
		else
			return false; 
	}
}

class Payment implements OperationBody {
	public String amount = "";
	public String target = "";

	public Payment(String amount, String target) {
		this.amount = amount;
		this.target = target;
	}
	public String[] toArray(){
		String[] array = {this.target, this.amount};
		return array;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Payment))
			return false;
		Payment opPayment = (Payment)obj;
		if ((target.equals(opPayment.target)) && (amount.equals(opPayment.amount)))
			return true;
		else
			return false; 
	}
}
