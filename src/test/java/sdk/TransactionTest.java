package sdk;

import org.junit.Test;
import static org.junit.Assert.*;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import org.stellar.sdk.KeyPair;

public class TransactionTest {
	@Test
	public void testSignature() throws UnsupportedEncodingException, NoSuchAlgorithmException, IOException {
		Operation op1 = new Operation("payment", "100", "GB3AOQD2M5AKMNWUP2HFCVQYFQNGTAFTJ24BHZ56ONSGGOXMG3EBO6OE", "");
		Operation op2 = new Operation("create-account", "1000000", "GD54SAKFHJ2QSBLEHZIQV3UWQ42OD6VQ6HKF6TN6F72US3AUQNDSONEV", "");
		Transaction tx = new Transaction();
		tx.AddOperation(op1);
		tx.AddOperation(op2);
		tx.Sign("SDJLIFJ3PMT22C2IZAR4PY2JKTGPTACPX2NMT5NPERC2SWRWUE4HWOEE", "1", "sebak-test-network");

		String signature = "nU46BuF6f1PUUCoHoy3EXMxdibvRC6ZYyzLPsr4aNJYJnDDvSdcn52Qf9CGy5R9UbkMgW6mdKGwrHNvd3oCoRsp";
		assertEquals(signature, tx.H.signature);
	}
}

 	@Test
	public void testOperationType()

	@Test
	public void testOperationCount()

	@Test
	public void testToJson()

	@Test
	public void testSenderCheck()


