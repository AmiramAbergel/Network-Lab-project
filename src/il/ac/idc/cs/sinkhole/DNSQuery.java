package il.ac.idc.cs.sinkhole;

public class DNSQuery {
	/**
	 *data -the trimmed bytes from the packet
	 *curr - the current position of array data
	 *flag - an object contain all flags and amount */
    private byte[] data;
    private int curr;
    private Flags flag;
    
    /**
     *Question Section*/ 
    private Question question;
    
    /**
     *Resource  Records*/ 
    private ResourceRecord[] answers;
    private ResourceRecord[] authorities;
    

    
    public DNSQuery(byte[] data) {
        this.data = data;
        this.curr = 0;
        this.flag = new Flags();
        headerSection();
        questionSection();
        rrSection();
        this.curr = 0;
        
    }
    
    private void headerSection() {
        this.flag.initFlags(this.data);
        this.flag.initAmount(this.data);;
        this.curr += DEF.HEAD;
    }

    private void questionSection() {
        String questionName = readNameFromByte(this.curr, true);
        byte[] questionTypeBytes = { this.data[this.curr], this.data[this.curr + 1] };
        int questionType = Helper.getNumber(questionTypeBytes);
        this.curr += DEF.TYPE;
        this.curr += DEF.CLASS;
        this.question = new Question(questionName, questionType);
    }

    private void rrSection() {
        // read answer + authority
    	int answers = this.flag.getAnswers();
    	int authurties = this.flag.getAuthorities();
        if (answers > 0) {
            this.answers = new ResourceRecord[answers];
            initRRs(this.answers);
        }
        if (authurties > 0) {
            this.authorities = new ResourceRecord[authurties];
            initRRs(this.authorities);
        }
    }

    public byte[] getData() {
        return this.data;
    }
    
    
    public Question getQuestion() {
        return this.question;
    }
    
    public Flags getflag() {
        return this.flag;
    }
    
    public ResourceRecord getAnswerRR(int indx) {
        return this.answers[indx];
    }
    
    public ResourceRecord getAuthorityRR(int indx) {
        return this.authorities[indx];
    }


    public void setFlag(String flagName, int value) {
        this.data = this.flag.setFlag(this.data, flagName, value);
    }


   


    private void initRRs(ResourceRecord[] arr) {
        for (int i = 0; i < arr.length; i++) {
            String rrName = readNameFromByte(this.curr, true);
            byte[] rrTypes = { this.data[this.curr], this.data[this.curr + 1] };
            int rrType = Helper.getNumber(rrTypes);
            this.curr += DEF.TYPE;
            this.curr += DEF.CLASS;
            this.curr += DEF.TTL;
            byte[] rdLen = { this.data[this.curr], this.data[this.curr + 1] };
            int rdDataLen = Helper.getNumber(rdLen);
            this.curr += DEF.RDL;
            String rdData = "";
            if (rrType == 2) {
                rdData = readNameFromByte(this.curr, false);
            }
            this.curr += rdDataLen;
            arr[i] = new ResourceRecord(rrName, rrType, rdData);
        }
    }

    



    private String readNameFromByte(int startingByteIdx, boolean advanceCursor) {
        StringBuilder result = new StringBuilder();
        int currentByteIdx = startingByteIdx;
        int bytesToRead = this.data[currentByteIdx];
        boolean readFromBytes = true;
        while (readFromBytes) {
        	//true if not pointer
        	boolean notPointer = Helper.notPointer(this.data, currentByteIdx);
        	if(notPointer) {
        		bytesToRead = this.data[currentByteIdx];
                currentByteIdx++;
                for (int i = 0; i < bytesToRead; i++) {
                    char c = (char) this.data[currentByteIdx];
                    result.append(c);
                    currentByteIdx++;
                }
                if (this.data[currentByteIdx] != 0) {
                    result.append('.');
                } else {
                    currentByteIdx++;
                    readFromBytes = false;
                }
        	}
        	else {
                result.append(readNameFromByte(Helper.getPointerIndex(this.data, currentByteIdx), false));
                currentByteIdx += 2;
                readFromBytes = false;
        	}
        	
        }

        if (advanceCursor) {
            this.curr += currentByteIdx - startingByteIdx;
        }

        return result.toString();
    }


}
