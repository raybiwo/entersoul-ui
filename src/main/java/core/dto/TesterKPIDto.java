package core.dto;

public class TesterKPIDto {
	private String name;
	private String valueExecution;
	private String valueScenario;
	private String valueDefect;
	private String valueRIT;
	private String valueSharing;
	private String ftr;
	private String valueAgile;
	private String valueTotal;
	
	public String getValueTotal() {
		return valueTotal;
	}
	public void setValueTotal(String valueTotal) {
		this.valueTotal = valueTotal;
	}
	public String getValueAgile() {
		return valueAgile;
	}
	public void setValueAgile(String valueAgile) {
		this.valueAgile = valueAgile;
	}
	public String getFtr() {
		return ftr;
	}
	public void setFtr(String ftr) {
		this.ftr = ftr;
	}
	public String getValueSharing() {
		return valueSharing;
	}
	public void setValueSharing(String valueSharing) {
		this.valueSharing = valueSharing;
	}
	public String getValueRIT() {
		return valueRIT;
	}
	public void setValueRIT(String valueRIT) {
		this.valueRIT = valueRIT;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValueExecution() {
		return valueExecution;
	}
	public void setValueExecution(String valueExecution) {
		this.valueExecution = valueExecution;
	}
	public String getValueScenario() {
		return valueScenario;
	}
	public void setValueScenario(String valueScenario) {
		this.valueScenario = valueScenario;
	}
	public String getValueDefect() {
		return valueDefect;
	}
	public void setValueDefect(String valueDefect) {
		this.valueDefect = valueDefect;
	}
}
