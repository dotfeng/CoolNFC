package cn.edu.zju.coolnfc.crypto;

public class CRC32Calculator {
	public static byte[] CRC32(byte[] arg) {
		int crc = 0xFFFFFFFF; // initial contents of LFBSR
		int poly = 0xEDB88320; // reverse polynomial

		for (byte b : arg) {
			int temp = (crc ^ b) & 0xff;

			// read 8 bits one at a time
			for (int i = 0; i < 8; i++) {
				if ((temp & 1) == 1)
					temp = (temp >>> 1) ^ poly;
				else
					temp = (temp >>> 1);
			}
			crc = (crc >>> 8) ^ temp;
		}
		return integerToByteArray(crc);
	}

	public static byte[] integerToByteArray(int i) {
		byte[] result = new byte[4];

		result[3] = (byte) (i >> 24);
		result[2] = (byte) (i >> 16);
		result[1] = (byte) (i >> 8);
		result[0] = (byte) (i >> 0);

		return result;
	}
}
