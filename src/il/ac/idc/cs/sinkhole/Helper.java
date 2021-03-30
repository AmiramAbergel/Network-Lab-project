package il.ac.idc.cs.sinkhole;

public class Helper { 
    public static int getBit(byte inputByte, int position, int amount) {
        int andMask = 0;
        for (int i = 0; i < amount; i++) {
            andMask = (andMask << 1) + 1;
        }
        andMask = andMask << (8 - position - amount);
        int result = inputByte & andMask;
        return result >> (8 - position - amount);
    }

    public static byte setBit(byte inputByte, int position, int amount, int value) {
        value = value << (8 - position - amount);
        int andMask = 0;
        for (int i = 0; i < amount; i++) {
            andMask = (andMask << 1) + 1;
        }
        andMask = andMask << (8 - position - amount);
        byte newValue = (byte) ((int) inputByte & ~andMask | (value & andMask));
        
        return newValue;
    }

    public static int getNumber(byte[] arr) {
        int result = 0;
        for (byte b : arr) {
            result = result << 8;
            result += (int)b & 0xff;
        }
        return result;
    }

    public static void showBytesAsBits(byte[] dnsQueryBytes) {
        int counter = 0;
        for (byte b : dnsQueryBytes) {
            System.out.println(counter++);
            System.out.println(Integer.toBinaryString(b & 255 | 256).substring(1));
            if (counter > 100)
                break;
        }
    }
    
    public static boolean notPointer(byte[] data,int indx) {
        boolean check = (Helper.getBit(data[indx], 0, 2) == 0);
        return check;
    }
    
    public static int getPointerIndex(byte[] data,int indx) {
    	byte[] pointerBytes = { data[indx], data[indx + 1] };
        pointerBytes[0] = Helper.setBit(pointerBytes[0], 0, 2, 0);
        return Helper.getNumber(pointerBytes);
    }
    
    public static int getResultFlag(byte[] data) {
    	return getBit(data[2], 0, 1);
    	
    }
    public static int getAuthorityFlag(byte[] data) {
    	return getBit(data[2], 5, 1);
    	
    }
    public static int getRecursiveAvFlag(byte[] data) {
    	return getBit(data[2], 7, 1);
    	
    }
    public static int getRecursiveDsFlag(byte[] data) {
    	return getBit(data[3], 0, 1);
    	
    }
    public static int getReturnCodeFlag(byte[] data) {
    	return getBit(data[3], 4, 4);
    	
    }
    
    public static int getQuestions(byte[] data) {
    	byte[] questionBytes = { data[4], data[5] };
    	return getNumber(questionBytes);
    }
    public static int getAnswers(byte[] data) {
    	byte[] answerBytes = { data[6], data[7] };
    	return getNumber(answerBytes);
    }
    public static int getAuthority(byte[] data) {
    	byte[] authorityBytes = { data[8], data[9] };
    	return getNumber(authorityBytes);
    }
    
}
