package il.ac.idc.cs.sinkhole;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class SinkholeServer {
	/**
	 *cSocket a client datagram Socket for udp protocol
	 *dSocket a server datagram Socket 
	 *cPacket a client datagram Packet used by Socket
	 *dPacket a server datagram Packet used by Socket
	 *dQuery the DNS Query to be sent to the server
	 *blockList a blockList object contain all names of blocked domain**/
	private DatagramSocket cSocket;
    private DatagramSocket sSocket;
    private DatagramPacket cPacket;
    private DatagramPacket sPacket;
    private DNSQuery 	   dQuery ;
    private BlockList      blockList;
    
    /**
     *cIp a client Ip address send to server in the end
     *cPort a client port number 
     *foundRes true if we received response false otherwise
     *nameServ root dns name server
     *path  a path to blockList file if exists
     **/
    private InetAddress cIp;
    private int cPort;
    private boolean foundRes;
    private String nameServ;
    private String path;
    
  public static void main(String[] args) {
  	SinkholeServer sinkHole = new SinkholeServer();
  	if (args.length > 0) {
  		sinkHole.path = args[0];
  	}
  	else {
  		sinkHole.path = null;
  	}
  	System.out.println("OUR SERVER is now listening on port 5300");
    sinkHole.run();
  }
    
    public SinkholeServer() {
        try {
            this.cSocket = new DatagramSocket();
            this.sSocket = new DatagramSocket(DEF.SERVERPORT);
        } catch (SocketException e) {
            System.err.printf("error occurred while trying to open a new socket: %s\n", e);
        }
    }
    
    public void run() {
    	if (this.path != null) {
    		this.blockList = new BlockList(this.path);
    	}
        while (true) {
            // init 
        	this.init();
            DNSQuery clientQuery = new DNSQuery(this.cPacket.getData());
            if (this.blockList != null) {
                boolean isValidQuery = this.blockList.isValid(clientQuery);
                if (!isValidQuery) {
                    this.dQuery = clientQuery;
                    this.sPacket = this.cPacket;
                    this.dQuery.setFlag("RCODE", 3);
                    this.dQuery.setFlag("QR", 1);
                    this.foundRes = true;
                }
            }
        	// retrieve a random root dns name-server
        	this.nameServ = this.getRoot();
            //stop when iterative 16 times
            int i = 0;
            while (i < DEF.MAXITERATIONS && !this.foundRes) {
                this.sendPacket();
                this.getResponse();
                byte[] data = Arrays.copyOfRange(this.sPacket.getData(), 0, this.sPacket.getLength());
                this.dQuery = new DNSQuery(data);
                int errorCode = this.dQuery.getflag().getCode();
                if (errorCode == 3) {
                	this.foundRes = true;
                }
                else if (errorCode == 0) {
                    if (this.dQuery.getflag().getAnswers() > 0) {
                        this.foundRes = true;
                    }
                    else if (this.dQuery.getflag().getAuthorities() > 0) {
                        ResourceRecord rr = this.dQuery.getAuthorityRR(0);
                        this.nameServ = rr.getRDData();
                    }
                }
            }
            this.sendResponse();
            i++;
        }
    }

    
    private void init() {
    	this.cPacket = receivePacket();
    	this.cIp = this.cPacket.getAddress();
    	this.cPort = this.cPacket.getPort();
    	this.foundRes = false;
    	this.dQuery = null;
    	this.sPacket = null;
    	
    }
    
    private DatagramPacket receivePacket() {
        byte[] buff = new byte[DEF.BUFFSIZE];
        DatagramPacket ClientPacket = new DatagramPacket(buff, DEF.BUFFSIZE);
        try {
            this.sSocket.receive(ClientPacket);
        } catch (IOException e) {
            System.err.printf("IO Error Occurred While Receiving Packet: %s\n", e);
        }
        return ClientPacket;
    }

    private String getRoot() {
        Random random = new Random();
        char rrs = (char) ((int) 'a' + random.nextInt(13));
        return String.format("%c.root-servers.net", rrs);
    }
    
    private void sendPacket() {
    	try {
			this.cPacket.setAddress(InetAddress.getByName(this.nameServ));
			this.cPacket.setPort(DEF.DNSPORT);
	        this.cSocket.send(this.cPacket);
		} catch (UnknownHostException e) {
			System.err.printf("An Unknown Host Exception Occurred While Setting Address: %s\n", e);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.printf("IO Error Occurred While Sending Packet: %s\n", e);
			e.printStackTrace();
		}
        
    }
    private void getResponse() {
    	byte[] buff = new byte[DEF.BUFFSIZE];
    	this.sPacket = new DatagramPacket(buff, DEF.BUFFSIZE);
        try {
			this.cSocket.receive(this.sPacket);
		} catch (IOException e) {
			System.err.printf("IO Error Occurred While Reviving Response: %s\n", e);
			e.printStackTrace();
		}
    }
    private void sendResponse() {
    	if (this.foundRes) {
            this.dQuery.setFlag("AA", 0);
            this.dQuery.setFlag("RA", 1);
            this.sPacket.setData(this.dQuery.getData());
            this.sPacket.setAddress(this.cIp);
            this.sPacket.setPort(this.cPort);
            try {
				this.sSocket.send(this.sPacket);
			} catch (IOException e) {
				System.err.printf("IO Error Occurred While Sending Response: %s\n", e);
				e.printStackTrace();
			}
            System.out.println("The Response Was Sent Successfully");
        }
    }
     
}
