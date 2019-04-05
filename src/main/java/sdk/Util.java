package sdk;

public class Util {
	public static String ByteArrayToHexString(byte[] bytes){
		StringBuilder sb = new StringBuilder();
		for(byte b : bytes){
			sb.append(String.format("%02X", b&0xff));
		}

		return sb.toString();
	}
}
