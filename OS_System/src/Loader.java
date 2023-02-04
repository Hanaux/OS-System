import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Loader {
//	private FileManager fileManager; 없는 걸로 치고
	private Memory memory; // 원래는 memory manager가 되야함. 

//	private short currentAddress;
	private Exe exe;
	public Loader(Memory memory) {
		this.memory = memory;
	}
	public void loadProcess(String fileName) {
		this.exe = new Exe();
		Scanner scanner;
		try {
			scanner = new Scanner(new File("exe/"+fileName));
			this.exe.load(scanner);
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
//	class Process{ // OS의 입장에서 n개의 process가 실행될 거 아냐, process가 각각의id를 가지고 있고 메모리를 또 점유하고 있음.
//		//process마다 start address등 이 있어야 함.
//		//
//		static final short sizeHeader = 4;
//		static final short indexPC = 0;
//		static final short indexSP = 1;
//		
//		private short startAddress;
//		private short sizeData, sizeCode;
////		private short PC, SP;
//		
//		private void loadHeader(Scanner scanner) {
//			this.sizeData = scanner.nextShort(16);
//			this.sizeCode = scanner.nextShort(16);
//			
//			this.startAddress = memory.allocate(sizeHeader/2+this.sizeData+this.sizeCode);
////			this.startAddress = 0;
//			cpu.setPC(startAddress + sizeHeader/2); //=4
//			cpu.setSP(startAddress + sizeHeader/2 + this.sizeCode);
//		}
//		private void loadBody(Scanner scanner) {
//			//code segment
//			short currentAddress = (short) (this.startAddress + sizeHeader/2);
//			while(scanner.hasNext()) {
//				memory.store(currentAddress, scanner.nextShort(16));//확인한번해오기
//				currentAddress++;
//			}
//			//data segment : 이미 함. 메모리 allocate를 할 때. memory가 확보 해둠.
		
			

}
