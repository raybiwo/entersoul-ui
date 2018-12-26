package vmd;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFileChooser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zhtml.Fileupload;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Include;
import org.zkoss.zul.Messagebox;

import core.dto.DeploymentDto;
import core.dto.KPITesterDto;
import core.dto.MemberDto;
import core.dto.TesterKPIDto;
import core.dto.UsernameListDto;
import util.BaseUri;
import util.JsonUtil;
import util.RestResponse;
import vmd.testerFunctions.CalculateTotalKPI;
import vmd.testerFunctions.TesterGetValue;
import vmd.testerFunctions.TesterKPICount;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class TesterVmd {
	Media media;

	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	private BaseUri uri = new BaseUri();
	private final String WS_URL = uri.BaseUriParam();

	private String txt = "";
	private String ftr = "";
	private String monthPeriode = "June";
	private String yearPeriode = "" + YearMonth.now().getYear();
	private String saveVisible = "false";
	

	private String ExecutionContains = "";
	private String TestScenarioContains = "";
	private String RejectedDefectContains = "";
	private String RITContains = "";
	private String SharingSessionContains = "";
	private String AgileInvolvementContains = "";

	private String fileTestExec = "";
	private String fileTestScen = "";
	private String fileReject = "";
	private String fileRIT = "";
	private String fileSharing = "";
	private String fileAgile = "";

	List<UsernameListDto> unameDtos = new ArrayList<UsernameListDto>();
	List<KPITesterDto> testerKPIDtos = new ArrayList<KPITesterDto>();
	KPITesterDto testerKPIDto1 = new KPITesterDto();
	UsernameListDto unameDto1 = new UsernameListDto();

	private KPITesterDto kpiTesterDto = new KPITesterDto();
	private List<KPITesterDto> listKpiTesterDto = new ArrayList<>();
	private MemberDto memberDto = new MemberDto();
	private List<MemberDto> listMemberDto = new ArrayList<>();
	
	
	
	

	

	@Init
	public void load() {
		String uri = WS_URL + "/tester/all";
		RestResponse restResponse = new RestResponse();
		BaseVmd bs = new BaseVmd();
//		List<String> testlit = new ArrayList<>();
		try {
			restResponse = bs.callWs(uri, null, HttpMethod.GET);
			listKpiTesterDto = JsonUtil.mapJsonToListObject(restResponse.getContents(), KPITesterDto.class);
//			for (KPITesterDto kpiTesterDto : listKpiTesterDto) {
//				testlit.add(kpiTesterDto.getKodeKaryawan());
//			}
		} catch (Exception e) {
			System.out.println("Unable to convert JSON!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
//		setIndexHdr(listDeploymentDto.size()+1);
	}

	@Command("doTryUp")
	public void doTryUp(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) throws IOException {
		Object obj = ctx.getTriggerEvent();
//		Messagebox.show(ctx.getComponent().getId());
		if (obj instanceof UploadEvent) {
			UploadEvent upev = (UploadEvent) obj;
			Media med = null;
			Scanner scanner = null;
			InputStream is = null;

			med = upev.getMedia();

			if (med.getContentType().equalsIgnoreCase("application/vnd.ms-excel")) {

				try {
					is = med.getStreamData();
					scanner = new Scanner(is);
					switch (ctx.getComponent().getId()) {
					case "upExDev":
						this.fileTestExec = med.getName();
						while (scanner.hasNextLine()) {
							if(ExecutionContains.equals("")) {
								ExecutionContains = ExecutionContains + scanner.nextLine();
							}else {
								ExecutionContains = ExecutionContains + "\n" + scanner.nextLine();
							}
						}
						break;
					case "upTestScen":
						this.fileTestScen = med.getName();
						while (scanner.hasNextLine()) {
							TestScenarioContains = TestScenarioContains + scanner.nextLine() + "\n";
						}
						break;
					case "upRejecDef":
						this.fileReject = med.getName();
						while (scanner.hasNextLine()) {
							RejectedDefectContains = RejectedDefectContains + scanner.nextLine() + "\n";
						}
						break;
					case "upRit":
						this.fileRIT = med.getName();
						while (scanner.hasNextLine()) {
							RITContains = RITContains + scanner.nextLine() + "\n";
						}
						break;
					case "upSharing":
						this.fileSharing = med.getName();
						while (scanner.hasNextLine()) {
							SharingSessionContains = SharingSessionContains + scanner.nextLine() + "\n";
						}
						break;
					case "upAgile":
						this.fileAgile = med.getName();
						while (scanner.hasNextLine()) {
							AgileInvolvementContains = AgileInvolvementContains + scanner.nextLine() + "\n";
						}
						break;
					default:
						Messagebox.show("Internal Service Error: 500", "ERROR", 1, Messagebox.ERROR);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					scanner.close();
					is.close();
				}
//				Messagebox.show("File Uploaded " + med.getName(), "INFO", 1, Messagebox.INFORMATION);
				Clients.showNotification(med.getName(), Clients.NOTIFICATION_TYPE_INFO, null, null, 2000);
			} else {
//				Messagebox.show("Wrong file format, Please try again", "INFO", 1, Messagebox.INFORMATION);
				Clients.showNotification("Wrong file format, Please try again", Clients.NOTIFICATION_TYPE_WARNING, null, null, 2000);
			}

//			Messagebox.show(ExecutionContains);
		} else {
//			Messagebox.show("Unknown Error", "ERROR", 1, Messagebox.ERROR);
			Clients.showNotification("Unknown Error", Clients.NOTIFICATION_TYPE_ERROR, null, null, 2000);
		}

	}

	@Command("doUpload")
	public void doUpload(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) throws IOException {
		TesterGetValue objtgv = new TesterGetValue();
		UploadEvent upEvent = null;
		Object objUploadEvent = ctx.getTriggerEvent();
		if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
			upEvent = (UploadEvent) objUploadEvent;
		}
		if (upEvent != null) {

//			media = Fileupload.get(); 
//			File f = new File(media.getName()); //show dialogue box (browse file)
//			InputStream inputStream= media.getStreamData();
//	        OutputStream out;
//			out = new FileOutputStream(f);
//			byte buf[]=new byte[1024];
//		    int len;
//		    while((len=inputStream.read(buf))>0)
//		    out.write(buf,0,len);
//		    out.close();
//		    inputStream.close();
//	        
//
//	        String filepath = f.getAbsolutePath();
////	        mdbConverter mdb = new mdbConverter();
//
//	        Messagebox.show(filepath);
//	        }
			upEvent.getMedia();
			this.txt = (upEvent.getMedia().getName());
			// java.io.File myFile = new java.io.File(media.getName());

//			myFile.renameTo("D:\\NNH - XL\\Hackaton\\KPI July 2017\\"+media.getName());
//			f.getPath();
			// get percentage of test execution status (Ontrack/Delay)
//			String datafile = objtgv.getData("D:\\NNH - XL\\Hackaton\\KPI July 2017\\Pre-prod Testing issues (Jan-June).csv");
//			String[][] testExecution = objtgv.getTestingStatus(datafile);
//			String result = "";
//			for(int i=0; i<testExecution.length; i++){
//	            for(int j=0; j<2; j++){
//	                result = result+testExecution[i][j];
//	            }
//	            result = result+"\n";
//	        }
//			Messagebox.show("Result: \n"+result);
			// Messagebox.show("File Uploaded: " + this.getData("D:\\NNH - XL\\Hackaton\\KPI
			// July 2017\\Pre-prod Testing issues (Jan-June).csv"));
			Messagebox.show("" + this.getTxt());
		}
	}

	@Command
	public void doGenerateKPI() {
		media = Fileupload.get();
//		if (media != null)
//			Filedownload.save(media);
//		else
//			Messagebox.show("First Drop Your File");
//		

		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Save Log File as");
		chooser.setSelectedFile(new File("AttendenceBug.html"));

	}

	@Command("generate")
	@NotifyChange({ "includeSrc", "p" })
	public void generate() {
		/*
		 * if (!ExecutionContains.equals("") && !TestScenarioContains.equals("") &&
		 * !RejectedDefectContains.equals("") && !RITContains.equals("") &&
		 * !SharingSessionContains.equals("") && !AgileInvolvementContains.equals("") &&
		 * !ftr.equals("")) {
		 */

			// GET MEMBER LIST
			MemberVmd objMemberVmd = new MemberVmd();
			listMemberDto = objMemberVmd.getAll();
			
			//get member tester count
			int tempcount = 0;
			if (listMemberDto.size() > 0) {
				for (MemberDto memberDto : listMemberDto) {
					if(memberDto.getManagement().equals("TESTER")) {
						tempcount++;
					}
				}
			}
			
			//initiate list tester
			String[] testerList = new String[tempcount];
			String[][] tstrIncludeUname = new String[tempcount][2];
			
			if (listMemberDto.size() > 0) {
				//get tester name
				int temp = 0;
//				testerList = new String[listMemberDto.size()];
//				for (String[] strings : tstrIncludeUname) {
//					testerList[temp] = memberDto.getName();
//					tstrIncludeUname[temp][0] = memberDto.getUsername();
//					tstrIncludeUname[temp][1] = memberDto.getName();
//					temp++;
//				}
				for (MemberDto memberDto : listMemberDto) {
					if(memberDto.getManagement().equals("TESTER")){
						testerList[temp] = memberDto.getName();
						tstrIncludeUname[temp][0] = memberDto.getUsername();
						tstrIncludeUname[temp][1] = memberDto.getName();
						temp++;
					}
				}
			}
			// Messagebox.show(" "+txt);
//		Messagebox.show("First Drop Your File");
			
			//Count KPI
			TesterKPICount objTC = new TesterKPICount();
			String[][] testerKPI = objTC.getResult(testerList, ExecutionContains);
			String result = "";
			String semiResult[][] = new String[testerKPI.length][testerKPI[0].length + 1]; // >> testerKPI[0].length+1
																							// because testerKPI value
																							// without FTR
//		String finalResult[][] =new String[testerKPI.length][testerKPI[0].length+1];

			// COUNT TOTAL
			CalculateTotalKPI objTotal = new CalculateTotalKPI();
//		testerList = {"AYUNI UCHA","NELY HUDA DINI","SINARINO WICAKSONO","GILANG RIZKI GINANJAR","ANGGITA PRETTYSARI","MUHAMMAD ARDI YUDHAPRAWIRA","MUHAMMAD BAIHAQI","ANY LUTHFATUNNISA","HANIFAH TRI ASTUTI","AHMAD PRABAWA","NURUL DINI INDRIYANI","VITHA RISTA POSTALIA","REINHART BINSAR HAMONANGAN","RYAN ILHAM","PUTRI WIDIASTUTI"};
			String[][] agileList = { { "AXISNET", "NELY HUDA DINI" }, { "PREPAID SONIC", "PUTRI WIDIASTUTI" },
					{ "PREPAID BATMAN", "SINARINO WICAKSONO" }, { "XWG", "ANGGITA PRETTYSARI" },
					{ "JUMANJI", "MUHAMMAD ARDI YUDHAPRAWIRA" }, { "TOKO XL", "MUHAMMAD BAIHAQI" },
					{ "ENTERPRISE", "ANY LUTHFATUNNISA" }, { "POSTPAID", "HANIFAH TRI ASTUTI" },
					{ "AXIS", "NURUL DINI INDRIYANI" } };
			String[][][] rules = {
					{ { "TS", "10" }, { "SD", "20" }, { "RD", "20" }, { "RIT", "20" }, { "SS", "10" }, { "FTR", "10" },
							{ "AI", "10" } },
					{ { "TS", "20" }, { "SD", "20" }, { "RD", "20" }, { "RIT", "20" }, { "SS", "10" }, { "FTR", "10" },
							{ "AI", "0" } } };

			for (int i = 0; i < testerKPI.length; i++) {
				for (int j = 0; j < testerKPI[i].length - 1; j++) {
					semiResult[i][j] = testerKPI[i][j];
				}
			}

			// insert FTR value
			for (int i = 0; i < semiResult.length; i++) {
				semiResult[i][6] = this.getFtr();
				semiResult[i][7] = testerKPI[i][6];
			}

//		//insert semiResult(exclude total)
//		for (int i = 0; i < semiResult.length; i++) {
//			for (int j = 0; j < semiResult[i].length; j++) {
//				finalResult[i][j] = semiResult[i][j];
//			}
//		}

			// COUNT TOTAL
			String[][] total = objTotal.calculateKPI(rules, semiResult, agileList, testerList);

//		//ADD TOTAL VALUE
//        for (int i = 0; i < finalResult.length; i++) {
//			for (int j = 0; j < total.length; j++) {
//				if(finalResult[i][0].equals(total[j][0].toUpperCase())) {
//					finalResult[i][finalResult.length-1] = total[j][1];
//				}
//			}
//		}

			for (int i = 0; i < total.length; i++) {
				KPITesterDto testerKPIDto = new KPITesterDto();
				UsernameListDto unameDto = new UsernameListDto();
				
				result = result + (i + 1) + ". ";
				for (int j = 0; j < tstrIncludeUname.length; j++) {
					if(testerKPI[i][0].equals(tstrIncludeUname[j][1])) {
						testerKPIDto.setKodeKaryawan(tstrIncludeUname[j][0]);
						unameDto.setUsername(tstrIncludeUname[j][0]);
						unameDto.setName(testerKPI[i][0]);
					}
				}
				unameDto.setResultKPI(total[i][8]);
				
//				testerKPIDto.setKodeKaryawan(testerKPI[i][0]);
				testerKPIDto.setTestExec(testerKPI[i][1]); unameDto.setTestExec(testerKPI[i][1]);
				testerKPIDto.setScenDesign(testerKPI[i][2]); unameDto.setScenDesign(testerKPI[i][2]); 
				testerKPIDto.setRejectDefect(testerKPI[i][3]); unameDto.setRejectDefect(testerKPI[i][3]);
				testerKPIDto.setRit(testerKPI[i][4]); unameDto.setRit(testerKPI[i][4]);
				testerKPIDto.setSharingSession(testerKPI[i][5]); unameDto.setSharingSession(testerKPI[i][5]);
				testerKPIDto.setTestQuality(this.getFtr()); unameDto.setTestQuality(this.getFtr());
				testerKPIDto.setAgileInvolvement(testerKPI[i][6]); unameDto.setAgileInvolvement(testerKPI[i][6]);
				testerKPIDto.setTotal(total[i][8]);
				testerKPIDto.setKpiPeriode(monthPeriode+"-"+yearPeriode); unameDto.setKpiPeriode(monthPeriode+"-"+yearPeriode);
				
				
//            System.out.println("name : "+testerKPIDto.getName());
//            System.out.println("setValueExecution : "+testerKPIDto.getValueExecution());
//            System.out.println("setValueScenario : "+testerKPIDto.getValueScenario());
//            System.out.println("setValueDefect : "+testerKPIDto.getValueDefect());
//			System.out.print((i+1)+". ");
//            for(int j=0; j<testerKPI[i].length; j++){
//            	result = result + testerKPI[i][j];
////                System.out.print(testerKPI[i][j]);
//            }
//            result = result + "\n";
//            System.out.println("");
				testerKPIDtos.add(testerKPIDto);
				unameDtos.add(unameDto);
				this.setSaveVisible("true");
			}

			BindUtils.postNotifyChange(null, null, TesterVmd.this, "testerKPIDtos");

			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/tester/tester.zul");
//			Messagebox.show("KPI Generated!"+unameDtos.size() + " " + unameDtos.get(0).getName());
			Clients.showNotification("KPI Generated!", Clients.NOTIFICATION_TYPE_INFO, null, null, 2000);

		/*
		 * } else { Messagebox.show("Please fill any form!"); }
		 */
	}

	@Command("save")
	public void save() {
		String uri = WS_URL + "/tester/save";
		RestResponse restResponse = new RestResponse();
		BaseVmd bs = new BaseVmd();
		
		if (testerKPIDtos.size() > 0) {		
			try {
				for (KPITesterDto kpiTesterDto : testerKPIDtos) {
					restResponse = bs.callWs(uri, kpiTesterDto, HttpMethod.POST);
				}
//				Messagebox.show("Success to save KPI " + testerKPIDtos.size());
				Clients.showNotification("Success to save KPI!", Clients.NOTIFICATION_TYPE_INFO, null, null, 2000);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Unable to call WS!");
				Messagebox.show("Unable to save KPI " + testerKPIDtos.size());
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		
		}
//		Messagebox.show("testerKPIDtos.size : "+ testerKPIDtos.size());
	}

	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	public List<KPITesterDto> getTesterKPIDtos() {
		return testerKPIDtos;
	}

	public void setTesterKPIDtos(List<KPITesterDto> testerKPIDtos) {
		this.testerKPIDtos = testerKPIDtos;
	}

	public KPITesterDto getTesterKPIDto1() {
		return testerKPIDto1;
	}

	public void setTesterKPIDto1(KPITesterDto testerKPIDto1) {
		this.testerKPIDto1 = testerKPIDto1;
	}

	public String getFtr() {
		return ftr;
	}

	public void setFtr(String ftr) {
		this.ftr = ftr;
	}

	public String getFileTestExec() {
		return fileTestExec;
	}

	public void setFileTestExec(String fileTestExec) {
		this.fileTestExec = fileTestExec;
	}

	public String getFileTestScen() {
		return fileTestScen;
	}

	public void setFileTestScen(String fileTestScen) {
		this.fileTestScen = fileTestScen;
	}

	public String getFileReject() {
		return fileReject;
	}

	public void setFileReject(String fileReject) {
		this.fileReject = fileReject;
	}

	public String getFileRIT() {
		return fileRIT;
	}

	public void setFileRIT(String fileRIT) {
		this.fileRIT = fileRIT;
	}

	public String getFileSharing() {
		return fileSharing;
	}

	public void setFileSharing(String fileSharing) {
		this.fileSharing = fileSharing;
	}

	public String getFileAgile() {
		return fileAgile;
	}

	public void setFileAgile(String fileAgile) {
		this.fileAgile = fileAgile;
	}

	public KPITesterDto getKpiTesterDto() {
		return kpiTesterDto;
	}

	public void setKpiTesterDto(KPITesterDto kpiTesterDto) {
		this.kpiTesterDto = kpiTesterDto;
	}

	public List<KPITesterDto> getListKpiTesterDto() {
		return listKpiTesterDto;
	}

	public void setListKpiTesterDto(List<KPITesterDto> listKpiTesterDto) {
		this.listKpiTesterDto = listKpiTesterDto;
	}

	public MemberDto getMemberDto() {
		return memberDto;
	}

	public void setMemberDto(MemberDto memberDto) {
		this.memberDto = memberDto;
	}

	public List<MemberDto> getListMemberDto() {
		return listMemberDto;
	}

	public void setListMemberDto(List<MemberDto> listMemberDto) {
		this.listMemberDto = listMemberDto;
	}
	
	public List<UsernameListDto> getUnameDtos() {
		return unameDtos;
	}

	public void setUnameDtos(List<UsernameListDto> unameDtos) {
		this.unameDtos = unameDtos;
	}

	public UsernameListDto getUnameDto1() {
		return unameDto1;
	}

	public void setUnameDto1(UsernameListDto unameDto1) {
		this.unameDto1 = unameDto1;
	}

	public String getMonthPeriode() {
		return monthPeriode;
	}

	public void setMonthPeriode(String monthPeriode) {
		this.monthPeriode = monthPeriode;
	}

	public String getYearPeriode() {
		return yearPeriode;
	}

	public void setYearPeriode(String yearPeriode) {
		this.yearPeriode = yearPeriode;
	}

	public String getSaveVisible() {
		return saveVisible;
	}

	public void setSaveVisible(String saveVisible) {
		this.saveVisible = saveVisible;
	}
	
//	@Command
//	public void getFilename() {
//		UploadEvent upEvent = null;
//		upEvent.getMedia();
////		media = Fileupload.get(); 
////		if (media != null)
////			Filedownload.save(media);
////		else
////			Messagebox.show("First Drop Your File");
////		
//	}
}
