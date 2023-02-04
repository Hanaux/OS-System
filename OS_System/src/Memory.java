import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Memory {
	private short memory[];
	private CPU cpu;
		
	public Memory(CPU cpu) {
		this.memory = new short[512];
		this.cpu = cpu;
	}

	
	public short load(short mar) {
		return this.memory[mar];
	}
	
	public void store(short mar, short mbr) {
		this.memory[mar] = mbr;
	}


	public short allocate(int i) { // 어차피 우린 1개만 해서 ㄱㅊ
		// TODO Auto-generated method stub
		return 0;
	}

}
