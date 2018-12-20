package vmd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zul.Include;
import org.zkoss.zul.Messagebox;

import core.dto.DeploymentDto;
import core.dto.TesterKPIDto;
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
	

	List<TesterKPIDto> testerKPIDtos = new ArrayList<TesterKPIDto>();
	TesterKPIDto testerKPIDto1 = new TesterKPIDto();
	
	@Init
	public void load(){
		String uri = WS_URL + "/tester/all";
		RestResponse restResponse = new RestResponse();
		BaseVmd bs = new BaseVmd();
		
		try {
			restResponse = bs.callWs(uri,null,HttpMethod.GET);
//			listDeploymentDto =  JsonUtil.mapJsonToListObject(restResponse.getContents(), DeploymentDto.class);
		} catch (Exception e) {
			System.out.println("Unable to convert JSON!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
//		setIndexHdr(listDeploymentDto.size()+1);
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
			//java.io.File myFile = new java.io.File(media.getName());
			
//			myFile.renameTo("D:\\NNH - XL\\Hackaton\\KPI July 2017\\"+media.getName());
//			f.getPath();
			//get percentage of test execution status (Ontrack/Delay)
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
			//Messagebox.show("File Uploaded: " + this.getData("D:\\NNH - XL\\Hackaton\\KPI July 2017\\Pre-prod Testing issues (Jan-June).csv"));
			Messagebox.show(""+this.getTxt());
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
	
	@Command("submit")
	@NotifyChange({"includeSrc","p"})
	public void submit() {
		//Messagebox.show(" "+txt);
//		Messagebox.show("First Drop Your File");
		TesterKPICount objTC = new TesterKPICount();
		String[][] testerKPI = objTC.getResult();
		String result = "";
		String semiResult[][] = new String[testerKPI.length][testerKPI[0].length+1]; //>> testerKPI[0].length+1 because testerKPI value without FTR
//		String finalResult[][] =new String[testerKPI.length][testerKPI[0].length+1];
		
		//COUNT TOTAL
		CalculateTotalKPI objTotal = new CalculateTotalKPI();
		String[] testerList = {"AYUNI UCHA","NELY HUDA DINI","SINARINO WICAKSONO","GILANG RIZKI GINANJAR","ANGGITA PRETTYSARI","MUHAMMAD ARDI YUDHAPRAWIRA","MUHAMMAD BAIHAQI","ANY LUTHFATUNNISA","HANIFAH TRI ASTUTI","AHMAD PRABAWA","NURUL DINI INDRIYANI","VITHA RISTA POSTALIA","REINHART BINSAR HAMONANGAN","RYAN ILHAM","PUTRI WIDIASTUTI"};
        String[][] agileList = {{"AXISNET","NELY HUDA DINI"},{"PREPAID SONIC","PUTRI WIDIASTUTI"},{"PREPAID BATMAN","SINARINO WICAKSONO"},{"XWG","ANGGITA PRETTYSARI"},{"JUMANJI","MUHAMMAD ARDI YUDHAPRAWIRA"},{"TOKO XL","MUHAMMAD BAIHAQI"},{"ENTERPRISE","ANY LUTHFATUNNISA"},{"POSTPAID","HANIFAH TRI ASTUTI"},{"AXIS","NURUL DINI INDRIYANI"}};
        String[][][] rules = { {{"TS","10"},{"SD","20"},{"RD","20"},{"RIT","20"},{"SS","10"},{"FTR","10"},{"AI","10"}}, {{"TS","20"},{"SD","20"},{"RD","20"},{"RIT","20"},{"SS","10"},{"FTR","10"},{"AI","0"}} };
		
		for (int i = 0; i < testerKPI.length; i++) {
			for (int j = 0; j < testerKPI[i].length-1; j++) {
				semiResult[i][j] = testerKPI[i][j];
			}
		}
		
		//insert FTR value
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
		
		
		//COUNT TOTAL
		String[][] total = objTotal.calculateKPI(rules, semiResult, agileList, testerList);
		
//		//ADD TOTAL VALUE
//        for (int i = 0; i < finalResult.length; i++) {
//			for (int j = 0; j < total.length; j++) {
//				if(finalResult[i][0].equals(total[j][0].toUpperCase())) {
//					finalResult[i][finalResult.length-1] = total[j][1];
//				}
//			}
//		}
		
		for(int i=0; i<total.length; i++){
			TesterKPIDto testerKPIDto = new TesterKPIDto();
            result = result + (i+1)+". ";
            testerKPIDto.setName(testerKPI[i][0]);
            testerKPIDto.setValueExecution(testerKPI[i][1]);
            testerKPIDto.setValueScenario(testerKPI[i][2]);
            testerKPIDto.setValueDefect(testerKPI[i][3]);
            testerKPIDto.setValueRIT(testerKPI[i][4]);
            testerKPIDto.setValueSharing(testerKPI[i][5]);
            testerKPIDto.setFtr(this.getFtr());
            testerKPIDto.setValueAgile(testerKPI[i][6]);
            testerKPIDto.setValueTotal(total[i][8]);
            
            System.out.println("name : "+testerKPIDto.getName());
            System.out.println("setValueExecution : "+testerKPIDto.getValueExecution());
            System.out.println("setValueScenario : "+testerKPIDto.getValueScenario());
            System.out.println("setValueDefect : "+testerKPIDto.getValueDefect());
//			System.out.print((i+1)+". ");
//            for(int j=0; j<testerKPI[i].length; j++){
//            	result = result + testerKPI[i][j];
////                System.out.print(testerKPI[i][j]);
//            }
//            result = result + "\n";
//            System.out.println("");
            testerKPIDtos.add(testerKPIDto);
        }
		
		BindUtils.postNotifyChange(null, null, TesterVmd.this, "testerKPIDtos");
		
		Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
		inc.setSrc("/tester/tester.zul");
		Messagebox.show("KPI Generated!"+this.getFtr());
	}
	
	

	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}
	
	public List<TesterKPIDto> getTesterKPIDtos() {
		return testerKPIDtos;
	}

	public void setTesterKPIDtos(List<TesterKPIDto> testerKPIDtos) {
		this.testerKPIDtos = testerKPIDtos;
	}

	public TesterKPIDto getTesterKPIDto1() {
		return testerKPIDto1;
	}

	public void setTesterKPIDto1(TesterKPIDto testerKPIDto1) {
		this.testerKPIDto1 = testerKPIDto1;
	}

	public String getFtr() {
		return ftr;
	}

	public void setFtr(String ftr) {
		this.ftr = ftr;
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
