package sdk;

import org.stellar.sdk.KeyPair;
import org.ethereum.util.RLP;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.json.simple.JSONObject;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import net.sf.json.JSONSerializer;
import io.github.novacrypto.base58.Base58;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.math.BigInteger;

public class Transaction {
	TransactionHeader H;
	TransactionBody B;

	public Transaction() {
		this.H = new TransactionHeader();
		this.B = new TransactionBody();
	}

	public void Sign(String seed, String sequence_id, String networkId) {
		KeyPair kp = KeyPair.fromSecretSeed(seed);
		this.B.sequence_id = new BigInteger(sequence_id);
		this.B.fee = Integer.toString(this.B.operations.size()*10000);
		System.out.println(this.B.fee);
		try{
			this.B.source = kp.getAccountId();
			this.H.hash = MakeHash();
			this.H.signature = GetSignature(kp, networkId.getBytes("UTF-8"));
		}
		catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private String GetSignature(KeyPair kp, byte[] networkId) throws UnsupportedEncodingException{
		byte[] c = ByteBuffer.allocate(networkId.length+this.H.hash.getBytes("UTF-8").length).put(networkId).put(this.H.hash.getBytes("UTF-8")).array();
		byte[] b = kp.sign(c);
		return Base58.base58Encode(b);
	}

	private String MakeHash() throws NoSuchAlgorithmException, IOException {
		byte[] encoded = null;
		encoded = RLP.encode(this.B.toArray());

		return Base58.base58Encode(DoubleSha256(encoded));
	}

	private byte[] DoubleSha256(byte[] msg) throws NoSuchAlgorithmException {
        MessageDigest md1 = MessageDigest.getInstance("SHA-256");
        md1.update(msg);
        MessageDigest md2 = MessageDigest.getInstance("SHA-256");
		md2.update(md1.digest());

		return md2.digest();
    }

	public net.sf.json.JSONObject ToJson() {
		net.sf.json.JSONObject jsonData = net.sf.json.JSONObject.fromObject(JSONSerializer.toJSON(this));
		
		return jsonData;
	}

	public void AddOperation(Operation op) {
		this.B.operations.add(op);
	}
}

class TransactionBody {
	String source;
	String fee;
	BigInteger sequence_id;
	ArrayList<Operation> operations;

	TransactionBody() {
		operations = new ArrayList<Operation>();
	}
	
	Object[] toArray() {
		ArrayList<Object> array = new ArrayList<Object>();
		for (Operation op : this.operations){
			array.add(op.toArray());
		}
		Object[] opArray = array.toArray(new Object[array.size()]);
		Object[] dataArray = {this.source, this.fee, this.sequence_id, opArray};

		return dataArray;
	}
}
		
class TransactionHeader {
	String version;
	String created;
	String hash;
	String signature;
	
	public TransactionHeader(){
		this.version = "1";
	}
}
