import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Loader {
//	private FileManager fileManager; ���� �ɷ� ġ��
	private Memory memory; // ������ memory manager�� �Ǿ���. 

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
//	class Process{ // OS�� ���忡�� n���� process�� ����� �� �Ƴ�, process�� ������id�� ������ �ְ� �޸𸮸� �� �����ϰ� ����.
//		//process���� start address�� �� �־�� ��.
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
//				memory.store(currentAddress, scanner.nextShort(16));//Ȯ���ѹ��ؿ���
//				currentAddress++;
//			}
//			//data segment : �̹� ��. �޸� allocate�� �� ��. memory�� Ȯ�� �ص�.
		
			

}
