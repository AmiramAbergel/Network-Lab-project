package il.ac.idc.cs.sinkhole;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

public class BlockList {
	/**
	 *blockList - a hash set of strings  */
	private HashSet<String> blockList;
	
	
	public BlockList() {
        this.blockList = new HashSet<>();
    }
	public BlockList(String path) {
		this();
        loadFile(path);
    }
	public boolean isValid(DNSQuery data) {

        String dom = data.getQuestion().name();
        return !this.blockList.contains(dom);

    }

    private void loadFile(String path) {
        try {
            File blockFile = new File(path);
            Scanner read = new Scanner(blockFile);
            while (read.hasNextLine()) {
                String dom = read.nextLine();
                blockList.add(dom);
            }
            read.close();
        } catch (FileNotFoundException e) {
            System.err.printf("An Error Occurred While Loading File %s not found\n", path);
        }
    }
}
