import java.util.Scanner;

public class Process {
	public enum EState{
		eReady,
		eRunning,
		eWaiting
	}
	private class PCB{
		private EState eState;
		private int id;
		private CPU.Register registers[];
		
	}
	
	private class CodeSegment{
		private short codeList[];
	}
	

}
