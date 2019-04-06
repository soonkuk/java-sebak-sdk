package sdk;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.security.MessageDigest;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.nio.ByteBuffer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import io.github.novacrypto.base58.Base58;
import org.ethereum.util.RLP;
import org.stellar.sdk.KeyPair;

public class Transaction {
	public TransactionHeader H;
	public TransactionBody B;

	public Transaction() {
		this.H = new TransactionHeader();
		this.B = new TransactionBody();
	}

	public void Sign(String seed, String sequence_id, String networkId) {
		KeyPair kp = KeyPair.fromSecretSeed(seed);
		this.B.sequence_id = new BigInteger(sequence_id);
		this.B.fee = Integer.toString(this.B.operations.size()*10000);
		try{
			this.B.source = kp.getAccountId();
			String hash = MakeHash();
			this.H.signature = GetSignature(kp, hash, networkId.getBytes("UTF-8"));
		}
		catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public String GetSignature(KeyPair kp, String hash, byte[] networkId) throws UnsupportedEncodingException{
		byte[] c = ByteBuffer.allocate(networkId.length+hash.getBytes("UTF-8").length).put(networkId).put(hash.getBytes("UTF-8")).array();
		byte[] b = kp.sign(c);
		return Base58.base58Encode(b);
	}

	public String MakeHash() throws NoSuchAlgorithmException, IOException {
		byte[] encoded = null;
		encoded = RLP.encode(this.B.toArray());

		return Base58.base58Encode(DoubleSha256(encoded));
	}

	public byte[] DoubleSha256(byte[] msg) throws NoSuchAlgorithmException {
        MessageDigest md1 = MessageDigest.getInstance("SHA-256");
        md1.update(msg);
        MessageDigest md2 = MessageDigest.getInstance("SHA-256");
		md2.update(md1.digest());

		return md2.digest();
    }

	public void AddOperation(Operation op) {
		this.B.operations.add(op);
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Transaction)) {
			return false;
		}
		Transaction tx = (Transaction)obj;
		if ((H.equals(tx.H)) && (B.equals(tx.B)))
			return true;
		else
			return false; 
	}

	public String ToJson() {
		var mapper = new ObjectMapper();
		String jsonStr = "";
		try {
			jsonStr = mapper.writeValueAsString(this);
		}
		catch (JsonProcessingException e) {
			System.out.println(e.getMessage());
		}

		//System.out.println(jsonStr);
		return jsonStr;
	}

	public void FromJson(String json) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			var	mapper1 = new ObjectMapper();
			map = mapper1.readValue(json, new TypeReference<HashMap<String,Object>>(){});
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
		ArrayList<Operation> ops = new ArrayList<Operation>();
		HashMap<String, Object> txHeader = (HashMap<String,Object>)map.get("H");
		HashMap<String, Object> txBody = (HashMap<String,Object>)map.get("B");
		ArrayList<Object> objArray = (ArrayList<Object>)(txBody.get("operations"));
		for (Object op:objArray) {
			HashMap<String, Object> c = (HashMap<String, Object>)op;
			HashMap<String, String> opHeader = (HashMap<String, String>)(c.get("H"));
			HashMap<String, String> opBody = (HashMap<String, String>)(c.get("B"));
			Operation temp = new Operation();
			String opType = ((String)(opHeader.get("type")));

			if (opType.equals("payment")) {
				temp = new Operation((String)(opHeader.get("type")), (String)(opBody.get("amount")), (String)(opBody.get("target")), "");
			} else if (opType.equals("create-account")) { 
				temp = new Operation((String)(opHeader.get("type")), (String)(opBody.get("amount")), (String)(opBody.get("target")), (String)(opBody.get("linked")));
			}
			ops.add(temp);
		}
		byte[] seq = ByteBuffer.allocate(4).putInt((int)txBody.get("sequence_id")).array();
		this.H.created = (String)txHeader.get("created");
		this.H.signature = (String)txHeader.get("signature");
		this.B.source = (String)txBody.get("source");
		this.B.fee = (String)txBody.get("fee");
		this.B.sequence_id = new BigInteger(seq);
		this.B.operations = ops;
	}
}

class TransactionBody {
	public String source = "";
	public String fee = "";
	public BigInteger sequence_id;
	public ArrayList<Operation> operations;

	public TransactionBody() {
		operations = new ArrayList<Operation>();
	}
	
	public Object[] toArray() {
		ArrayList<Object> array = new ArrayList<Object>();
		for (Operation op : this.operations){
			array.add(op.toArray());
		}
		Object[] opArray = array.toArray(new Object[array.size()]);
		Object[] dataArray = {this.source, this.fee, this.sequence_id, opArray};

		return dataArray;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof TransactionBody))
			return false;
		TransactionBody txBody = (TransactionBody)obj;
		if ((source.equals(txBody.source)) && (fee.equals(txBody.fee)) && (sequence_id.equals(txBody.sequence_id))){
			if (operations.size()==txBody.operations.size()){
				for (int i=0 ; i<operations.size() ; i++) {
					Operation op1 = operations.get(i);
					Operation op2 = (txBody.operations).get(i);
					boolean b = op1.equals(op2);
					if (!b)
						return false;
				}
				return true;
			}
		}
		return false; 
	}
}
		
class TransactionHeader {
	public String version = "";
	public String created = "";
	public String signature = "";
	
	public TransactionHeader(){
		this.version = "1";
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof TransactionHeader))
			return false;
		TransactionHeader txHeader = (TransactionHeader)obj;
		if ((version.equals(txHeader.version)) && (created.equals(txHeader.created)) && (signature.equals(txHeader.signature)))
			return true;
		else
			return false; 
	}
}
