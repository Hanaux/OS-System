
public class CPU {
	//declaration
	public enum ERegister{
		eIR,
		eAC,
		
		eCS,
		eDS,
		eSS,
		eHS,
		
		ePC,
		eSP, //stack의 top을 말함. 
		
		eSR,
		eIN,
		
		eMAR,
		eMBR
	}

	public class Register{
		protected short value;
		public short getValue() {
			return this.value;
		}
		public void setValue(short value) {
			this.value = value;
		}
	}
	
	private class IR extends Register {
		public short getOpCode() {return (short)(this.value >> 8);}
		public short getOperand() {return (short)(this.value & 0x00FF);}
	}
	private enum EOpCode{
		eHalt,	//0x00
		eLDC,	//0x01
		eLDA,	//0x02
		eSTA,	//0x03
		eADDA,	//0x04
		eADDC,	//0x05
		eSUBA,	//0x06
		eSUBC,	//0x07
		eMULA,	//0x08
		eMULC,	//0x09
		eDIVA,	//0x0A
		eDIVC,	//0x0B
		eANDA,	//0x0C
		eANDC,	//0x0D
		eNOTA,	//0x0E
		eNOTC,	//0x0F
		eJMPZ,	//0x10
		eJMPBZ,	//0x11
		eJMP	//0x12

	}
	private class CU{

		public boolean isZero(Register sr) {
			if ((short)(sr.getValue() & 0x8000)==0) {				// bitwise and
				return false;
			} else {
				return true; // 서로 같음.
			}
		}
		
		public boolean isBZ(Register sr) {
			if ((short)(sr.getValue() & 0x4000)==0) { //어 이 조건임 ㅇㅇㅇ 
				return false;
			} else {
				return true; //이 조건 아니에요 점프 넘어가주세요
			}
		}
	}
	private class ALU{
		short aluVal;
		short retVal;

		public void add(short value) {
			this.aluVal+=value;
		}
		public void store(short value) {
			this.aluVal = value;
		}
		public void sub(short value) {
			this.aluVal-=value;
		}
		public void mul(short value) {
			this.aluVal*=value;
		}
		public void div(short value) {	
			this.aluVal/=value;
		}
		public short getAluVal() {
			this.retVal = aluVal;
			initVal();
			return this.retVal;
		}
		public void and(short value) {
			this.aluVal&=value;
		}
		public void not(short value) {
			this.aluVal^=value;
		}
		public void initVal() { //만들어준거
			this.aluVal=0;
		}
	
	}

	
	//components
	private ALU alu;
	private CU cu;
	
	private Register registers[];
	
	//associations
	private Memory memory;
	private Loader loader;

	//status
	private boolean bPowerOn;
	private boolean isPowerOn() {return this.bPowerOn;}
	public void setPowerOn() {
		this.bPowerOn=true;
		this.run();
	}
	public void shutDown() {this.bPowerOn=false;}
	
	//instructions
	private void Halt() {
		System.out.println(this.registers[ERegister.ePC.ordinal()].getValue());
		System.out.println(this.registers[ERegister.eSP.ordinal()].getValue());
		System.out.println(this.registers[ERegister.eAC.ordinal()].getValue());
		shutDown();
		}
	
	private void LDC() {
		// IR.operand -> MBR
		this.registers[ERegister.eMBR.ordinal()].setValue(
				((CPU.IR) this.registers[ERegister.eIR.ordinal()]).getOperand());
		//MBR -> AC
		this.registers[ERegister.eAC.ordinal()].setValue(this.registers[ERegister.eMBR.ordinal()].getValue());
	}
	private void LDA() {
		// IR.operand -> MAR, 주소 SP값 더해야 실제주소가 됨
		this.registers[ERegister.eMAR.ordinal()].setValue(
				(short) (((CPU.IR) this.registers[ERegister.eIR.ordinal()]).getOperand()
				+this.registers[ERegister.eSP.ordinal()].getValue()));
		// memory.load(MAR) -> MBR
		this.registers[ERegister.eMBR.ordinal()].setValue(
				this.memory.load(this.registers[ERegister.eMAR.ordinal()].getValue()));
		// MBR -> AC
		this.registers[ERegister.eAC.ordinal()].setValue(this.registers[ERegister.eMBR.ordinal()].getValue());
	}
	private void STA() {
		//IR.operand -> MAR, 주소 SP값 더해야 실제주소가 됨
		this.registers[ERegister.eMAR.ordinal()].setValue(
				(short) (((CPU.IR) this.registers[ERegister.eIR.ordinal()]).getOperand()
				+this.registers[ERegister.eSP.ordinal()].getValue()));
		//AC -> MBR
		this.registers[ERegister.eMBR.ordinal()].setValue(this.registers[ERegister.eAC.ordinal()].getValue());
		// memory.store(MAR, MBR)
		this.memory.store(
				this.registers[ERegister.eMAR.ordinal()].getValue(),
				this.registers[ERegister.eMBR.ordinal()].getValue());
	}
	private void ADDA() {
		// AC -> ALU
		this.alu.store(this.registers[ERegister.eAC.ordinal()].getValue());//LDA하면 AC에 있는 값이 지워지기 때문에
		//IR.operand -> MAR
		this.LDA();
		//this.add(this.registers[ERegister.eAC.ordinal()].getValue());
		this.alu.add(this.registers[ERegister.eAC.ordinal()].getValue());
		this.registers[ERegister.eAC.ordinal()].setValue(this.alu.getAluVal());
	}
	private void ADDC() {
		this.alu.store(this.registers[ERegister.eAC.ordinal()].getValue());//LDA하면 AC에 있는 값이 지워지기 때문에
		//IR.operand -> MAR
		this.LDC();
		//this.add(this.registers[ERegister.eAC.ordinal()].getValue());
		this.alu.add(this.registers[ERegister.eAC.ordinal()].getValue());
		this.registers[ERegister.eAC.ordinal()].setValue(this.alu.getAluVal());
	}
	private void SUBA() {
		this.alu.store(this.registers[ERegister.eAC.ordinal()].getValue());
		this.LDA();
		this.alu.sub(this.registers[ERegister.eAC.ordinal()].getValue());
		this.registers[ERegister.eAC.ordinal()].setValue(this.alu.getAluVal());
//		setSR();
	}
	private void SUBC() {
		this.alu.store(this.registers[ERegister.eAC.ordinal()].getValue());//LDA하면 AC에 있는 값이 지워지기 때문에
		//IR.operand -> MAR
		this.LDC();
		//this.add(this.registers[ERegister.eAC.ordinal()].getValue());
		this.alu.sub(this.registers[ERegister.eAC.ordinal()].getValue());
		this.registers[ERegister.eAC.ordinal()].setValue(this.alu.getAluVal());
//		setSR();
	}
	private void MULA() {
		this.alu.store(this.registers[ERegister.eAC.ordinal()].getValue());
		this.LDA();
		this.alu.mul(this.registers[ERegister.eAC.ordinal()].getValue());
		this.registers[ERegister.eAC.ordinal()].setValue(this.alu.getAluVal());
	}
	private void MULC() {
		this.alu.store(this.registers[ERegister.eAC.ordinal()].getValue());
		this.LDC();
		this.alu.mul(this.registers[ERegister.eAC.ordinal()].getValue());
		this.registers[ERegister.eAC.ordinal()].setValue(this.alu.getAluVal());
	}
	private void DIVA() {
		this.alu.store(this.registers[ERegister.eAC.ordinal()].getValue());
		this.LDA();
		this.alu.div(this.registers[ERegister.eAC.ordinal()].getValue());
		this.registers[ERegister.eAC.ordinal()].setValue(this.alu.getAluVal());
	}
	private void DIVC() {
		this.alu.store(this.registers[ERegister.eAC.ordinal()].getValue());
		this.LDC();
		this.alu.div(this.registers[ERegister.eAC.ordinal()].getValue());
		this.registers[ERegister.eAC.ordinal()].setValue(this.alu.getAluVal());
	}
	private void ANDA() {
		this.alu.store(this.registers[ERegister.eAC.ordinal()].getValue());
		this.LDA();
		this.alu.and(this.registers[ERegister.eAC.ordinal()].getValue());
		this.registers[ERegister.eAC.ordinal()].setValue(this.alu.getAluVal());
	}
	private void ANDC() {
		this.alu.store(this.registers[ERegister.eAC.ordinal()].getValue());
		this.LDC();
		this.alu.and(this.registers[ERegister.eAC.ordinal()].getValue());
		this.registers[ERegister.eAC.ordinal()].setValue(this.alu.getAluVal());
	}
	private void NOTA() {
		this.alu.store(this.registers[ERegister.eAC.ordinal()].getValue());
		this.LDA();
		this.alu.not(this.registers[ERegister.eAC.ordinal()].getValue());
		this.registers[ERegister.eAC.ordinal()].setValue(this.alu.getAluVal());
	}
	private void NOTC() {
		this.alu.store(this.registers[ERegister.eAC.ordinal()].getValue());
		this.LDC();
		this.alu.not(this.registers[ERegister.eAC.ordinal()].getValue());
		this.registers[ERegister.eAC.ordinal()].setValue(this.alu.getAluVal());
	}
	private void JMPZ() { //  a, b  a==b
		if(this.cu.isZero(this.registers[ERegister.eSR.ordinal()])) {
			//ir.operand -> PC
			this.registers[ERegister.ePC.ordinal()].setValue(
					((CPU.IR) this.registers[ERegister.eIR.ordinal()]).getOperand());
		}else plusPC();
		
	}
	private void JMPBZ() { //a, b a<b
		short retVal = this.registers[ERegister.eAC.ordinal()].getValue();
		if(retVal<=0) this.registers[ERegister.eSR.ordinal()].setValue((short)0x4000);
		else this.registers[ERegister.eSR.ordinal()].setValue((short)0x0000);
		
		if(this.cu.isBZ(this.registers[ERegister.eSR.ordinal()])) {
			//ir.operand -> PC
			this.registers[ERegister.ePC.ordinal()].setValue(
					((CPU.IR) this.registers[ERegister.eIR.ordinal()]).getOperand());
		} else plusPC();
	}
	private void JMP() {
		this.registers[ERegister.ePC.ordinal()].setValue(
				((CPU.IR) this.registers[ERegister.eIR.ordinal()]).getOperand());
	}

	private void plusPC() {
		this.registers[ERegister.ePC.ordinal()].setValue((short)(this.registers[ERegister.ePC.ordinal()].getValue()+1));
	}
	
	//constructor
	public CPU() {
		this.alu = new ALU();
		this.cu = new CU();
		this.registers = new Register[ERegister.values().length];
		for(ERegister eRegister: ERegister.values()) {//register만듦, IR 따로 만듦
			this.registers[eRegister.ordinal()] = new Register();
		}
		this.registers[ERegister.eIR.ordinal()] = new IR();
	}
	public void associate(Memory memory, Loader loader) {
		this.memory = memory;
		this.loader = loader;
	}
	
	private void fetch() {
		//PC->MAR
		this.registers[ERegister.eMAR.ordinal()].setValue(this.registers[ERegister.ePC.ordinal()].getValue());
		// memory.load(MAR)
		this.registers[ERegister.eMBR.ordinal()].setValue(
		this.memory.load(this.registers[ERegister.eMAR.ordinal()].getValue()));
		//MBR -> IR
		this.registers[ERegister.eIR.ordinal()].setValue(this.registers[ERegister.eMBR.ordinal()].getValue());
	}
	private void decode() {
	
	}
	
	private void execute() {
		switch(EOpCode.values()[((IR)this.registers[ERegister.eIR.ordinal()]).getOpCode()]) {
		case eHalt:
			this.Halt();
			break;
		case eLDC:
			this.LDC();
			plusPC();
			break;
		case eLDA:
			this.LDA();
			plusPC();
			break;
		case eSTA:
			this.STA();
			plusPC();
			break;
		case eADDA:
			this.ADDA();
			plusPC();
			break;
		case eADDC:
			this.ADDC();
			plusPC();
			break;
		case eSUBA:
			this.SUBA();
			plusPC();
			break;
		case eSUBC:
			this.SUBC();
			plusPC();
			break;
		case eMULA:
			this.MULA();
			plusPC();
			break;
		case eMULC:
			this.MULC();
			plusPC();
			break;
		case eDIVA:
			this.DIVA();
			plusPC();
			break;
		case eDIVC:
			this.DIVC();
			plusPC();
			break;
		case eANDA:
			this.ANDA();
			plusPC();
			break;
		case eANDC:
			this.ANDC();
			plusPC();
			break;
		case eNOTA:
			this.NOTA();
			plusPC();
			break;
		case eNOTC:
			this.NOTC();
			plusPC();
			break;
		case eJMPZ:
			this.JMPZ();
			break;
		case eJMPBZ:
			this.JMPBZ();
			break;
		case eJMP:
			this.JMP();
			break;
		default:
			break;
		}
	}
	
	private void checkInterrupt() {
	}
	
	public void run() {
		while(isPowerOn()) {
			this.fetch();
			this.decode();
			this.execute();
			this.checkInterrupt();
		}
	}
	
	public static void main(String args[]) {
		CPU cpu = new CPU();
		Memory memory = new Memory(cpu);
		Loader loader = new Loader(memory);
		
		cpu.associate(memory, loader);
		loader.loadProcess("exe1");
		cpu.setPowerOn();
	}
	public void setPC(int i) {
		this.registers[ERegister.ePC.ordinal()].setValue((short)(i));
	}
	public void setSP(int i) {
		this.registers[ERegister.eSP.ordinal()].setValue((short)i);
		
	}
}
