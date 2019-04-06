package sdk;

import org.junit.Test;
import static org.junit.Assert.*;

public class TransactionTest {
	@Test
	public void testSignature() { 
		Transaction tx = makeTestTx();
		String signature = "nU46BuF6f1PUUCoHoy3EXMxdibvRC6ZYyzLPsr4aNJYJnDDvSdcn52Qf9CGy5R9UbkMgW6mdKGwrHNvd3oCoRsp";
		assertEquals(signature, tx.H.signature);
	}

	@Test
	public void testFromJson1() {
		Transaction tx1 = makeTestTx();
		String json = "{ \"B\": { \"fee\": \"20000\","+
					"\"operations\": [ { \"B\": { \"amount\": \"100\","+
					"\"target\": \"GB3AOQD2M5AKMNWUP2HFCVQYFQNGTAFTJ24BHZ56ONSGGOXMG3EBO6OE\"},"+
					"\"H\": {\"type\": \"payment\"}},"+
					"{ \"B\": { \"amount\": \"1000000\",\"linked\": \"\","+
					"\"target\": \"GD54SAKFHJ2QSBLEHZIQV3UWQ42OD6VQ6HKF6TN6F72US3AUQNDSONEV\"},"+
					"\"H\": {\"type\": \"create-account\"}}],"+
					"\"sequence_id\": 1,"+
					"\"source\": \"GAG5EESGOZIHTKK5N2NBHX25EWRC3S3TWZT7RMCSBX65A3KTJKILQKCF\"},"+
 					"\"H\": { \"created\": \"\","+
					"\"signature\": \"nU46BuF6f1PUUCoHoy3EXMxdibvRC6ZYyzLPsr4aNJYJnDDvSdcn52Qf9CGy5R9UbkMgW6mdKGwrHNvd3oCoRsp\","+
					"\"version\": \"1\"}}";
		Transaction tx2= new Transaction();
		tx2.FromJson(json);
		assertTrue(tx1.equals(tx2));
	}

	@Test
	public void testFromJson2() {
		Transaction tx1 = makeTestTx();
		String json = tx1.ToJson();
		Transaction tx2 = new Transaction();
		tx2.FromJson(json);
		assertTrue(tx1.equals(tx2));
	}

	@Test
	public void testSenderCheck() {}

	public Transaction makeTestTx() {
		Operation op1 = new Operation("payment", "100", "GB3AOQD2M5AKMNWUP2HFCVQYFQNGTAFTJ24BHZ56ONSGGOXMG3EBO6OE");
		Operation op2 = new Operation("create-account", "1000000", "GD54SAKFHJ2QSBLEHZIQV3UWQ42OD6VQ6HKF6TN6F72US3AUQNDSONEV");
		Transaction tx = new Transaction();
		tx.AddOperation(op1);
		tx.AddOperation(op2);
		tx.Sign("SDJLIFJ3PMT22C2IZAR4PY2JKTGPTACPX2NMT5NPERC2SWRWUE4HWOEE", "1", "sebak-test-network");
		return tx;
	}
}
