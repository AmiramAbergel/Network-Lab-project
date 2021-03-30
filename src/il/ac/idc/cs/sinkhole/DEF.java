package il.ac.idc.cs.sinkhole;

public class DEF {
	/** The server port. */
	public static final int SERVERPORT = 5300;
	/** The default  DNS port. */
	public static final int DNSPORT = 53;
	/** The buffer size . */
	public static final int BUFFSIZE = 512;
	/** The maximum iteration that can be done. */
	public static final int MAXITERATIONS = 16;
	/* taken from RFC-1035 for the header of a DNS */
	/** The header size. */
    public static final int HEAD = 12;
    /** TYPE size. */
    public static final int TYPE = 2;
    /** CLASS size. */
    public static final int CLASS = 2;
    /** RR TTL size. */
    public static final int TTL = 4;
    /** RR RDL LENGTH. */
    public static final int RDL = 2;
    public DEF() {
    	
    }
}
