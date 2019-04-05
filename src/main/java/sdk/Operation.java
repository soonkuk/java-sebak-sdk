package sdk;

public class Operation {
	OperationHeader H;
	OperationBody B;

	public Operation(String type, String amount, String target, String linked) {
		this.H = new OperationHeader(type);
		if (type == "create-account") {
			this.B = new CreateAccount(amount, target, linked);
		}
		if (type == "payment") {
			this.B = new Payment(amount, target);
		}
	}
	Object[] toArray(){
		Object[] array = {this.H.toArray(), this.B.toArray()};
		return array;
	}
}

class OperationHeader {
	String type;
	
	OperationHeader(String type) {
		this.type = type;
	}
	String[] toArray() {
		String[] array = {this.type};
		return array;
	}
}

interface OperationBody {
	abstract String[] toArray();
}

class CreateAccount implements OperationBody {
	private String target;
	private String amount;
	private String linked;

	CreateAccount(String amount, String target, String linked) {
		this.target = target;
		this.amount = amount;
		this.linked = linked;
	}
	public String[] toArray() {
		String[] array = {this.target, this.amount, this.linked};
		return array;
	}
}

class Payment implements OperationBody {
	private String amount;
	private String target;

	Payment(String amount, String target) {
		this.amount = amount;
		this.target = target;
	}
	public String[] toArray(){
		String[] array = {this.target, this.amount};
		return array;
	}
}
