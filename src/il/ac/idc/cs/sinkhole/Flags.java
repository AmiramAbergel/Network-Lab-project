package il.ac.idc.cs.sinkhole;

public class Flags {
	
	/**
	 *res -flag if got response
	 *auth - flag for authorityAnswer
	 *recAv - flag if recursion available
	 *recDes - flag if recursion desired
	 *code - the return code*/
    private int res;
    private int auth;
    private int recAv;
    private int recDes;
    private int code;
	/**
	 *q -amount of questions
	 *aut - amount of authority
	 *ans -amount of answer */
//    private int q;
    private int aut;
    private int ans;
    public Flags() {
    	this.res = 0;
    	this.auth = 0;
    	this.recAv = 0;
    	this.recDes = 0;
    	this.code = 0;
    }
    
    public int getAnswers() {
    	return this.ans;
    }
    public int getAuthorities() {
    	return this.aut;
    }
    public int getResult() {
    	return this.res;
    }
    public int getAuthority() {
    	return this.auth;
    }
    public int getRecursiveAvailable() {
    	return this.recAv;
    }
    public int getRecursiveDesired() {
    	return this.recDes;
    }
    public int getCode() {
    	return this.code;
    }
    
    
    public byte[] setFlag(byte[] data,String flagName, int value) {
        byte newByte = 0;
        switch (flagName) {
            case "QR":
                newByte = Helper.setBit(data[2], 0, 1, value);
                data[2] = newByte;
                break;
            case "RD":
                newByte = Helper.setBit(data[2], 7, 1, value);
                data[2] = newByte;
                break;
            case "RA":
                newByte = Helper.setBit(data[3], 0, 1, value);
                data[3] = newByte;
                break;
            case "AA":
                newByte = Helper.setBit(data[2], 5, 1, value);
                data[2] = newByte;
                break;
            case "RCODE":
                newByte = Helper.setBit(data[3], 4, 4, value);
                data[3] = newByte;
                break;
        }
        return data;
    }
    public void initFlags(byte[] data) {
        this.res = Helper.getResultFlag(data);
        this.auth = Helper.getAuthorityFlag(data);
        this.recAv = Helper.getRecursiveAvFlag(data);
        this.recDes = Helper.getRecursiveDsFlag(data);
        this.code = Helper.getReturnCodeFlag(data);
    }
    
    public void initAmount(byte[] data) {
//        this.q = Util.getQuestions(data);
        this.ans = Helper.getAnswers(data);
        this.aut = Helper.getAuthority(data);
    }
    
}
