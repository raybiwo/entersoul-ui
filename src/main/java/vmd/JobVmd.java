package vmd;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Include;
import org.zkoss.zul.Messagebox;

import core.dto.MstJobDto;
import pagevmd.NavigationVmd;
import util.JsonUtil;
import util.RestResponse;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class JobVmd extends NavigationVmd{
	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	private final String WS_URL = "http://localhost:8080/boot-ui";
	private final NumberFormat frmt = new DecimalFormat("000");
	
	private MstJobDto mstJob = new MstJobDto();
	private List<MstJobDto> listJob = new ArrayList<>();
	private boolean readonly = false;
	
	private String kodeJob;
	
	private long indexHdr=0;
	
	@Init
	public void load(){
		String uri = WS_URL + "/job/all";
		RestResponse restResponse = new RestResponse();
		BaseVmd bs = new BaseVmd();
		
		restResponse = bs.callWs(uri,null,HttpMethod.GET);
		try {
			listJob =  JsonUtil.mapJsonToListObject(restResponse.getContents(), MstJobDto.class);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Unable to convert JSON!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		setIndexHdr(listJob.size()+1);
		setKodeJob("ID"+frmt.format(getIndexHdr()));
	}
	
	@Command("add")
	@NotifyChange({"includeSrc","p"})
	public void add(){
		MstJobDto mstJob = new MstJobDto();
		mstJob.setIdJob(getKodeJob());
		Sessions.getCurrent().setAttribute("obj", mstJob);
		Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
		inc.setSrc("/master/job/jobEdit.zul");
	}
	
	@Command("edit")
	@NotifyChange({"includesrc","p"})
	public void edit(){
		if (mstJob.getIdJob()==null) {
			Messagebox.show("Pilih data yang akan di edit");
		} else {
			Sessions.getCurrent().setAttribute("obj", mstJob);
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/master/job/jobEdit.zul");
		}
	}
	
	@Command("delete")
	public void delete(){
		if (mstJob.getIdJob()==null) {
			Messagebox.show("Pilih data yang akan didelete");
		}else{
			String uriSave = WS_URL + "/job/delete/"+mstJob.getIdJob();
			RestResponse restResponse = new RestResponse();
			BaseVmd bs = new BaseVmd();
			
			restResponse = bs.callWs(uriSave, null, HttpMethod.DELETE);
			listJob.remove(mstJob);
			BindUtils.postNotifyChange(null, null, JobVmd.this, "listJob");
			Clients.showNotification(restResponse.getMessage(), Clients.NOTIFICATION_TYPE_INFO, null, null, 1500);
			
			Sessions.getCurrent().setAttribute("obj", mstJob);
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/master/job/job.zul");
		}
	}
	
	@Command("print")
	public void print(){
		if (mstJob.getIdJob()==null) {
			Clients.evalJavaScript("window.open('"
					+ "http://localhost:8080/boot-report/frameset?__report=/report/mstJob.rptdesign&prLink=http://localhost:8080/boot-core/job/all"
					+ "','','top=0,left=0,height=800,width=1024,scrollbars=1,resizable=1')");
		} else {
			Clients.evalJavaScript("window.open('"
					+ "http://localhost:8080/boot-report/frameset?__report=/report/mstJob.rptdesign&prLink=http://localhost:8080/boot-core/job/getOneList/"+mstJob.getIdJob()
					+ "','','top=0,left=0,height=800,width=1024,scrollbars=1,resizable=1')");
		}
	}
	
	public static Logger getLogger() {
		return logger;
	}
	public static void setLogger(Logger logger) {
		JobVmd.logger = logger;
	}
	public MstJobDto getMstJob() {
		return mstJob;
	}
	public void setMstJob(MstJobDto mstJob) {
		this.mstJob = mstJob;
	}
	public List<MstJobDto> getListJob() {
		return listJob;
	}
	public void setListJob(List<MstJobDto> listJob) {
		this.listJob = listJob;
	}
	public boolean isReadonly() {
		return readonly;
	}
	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}
	public String getWS_URL() {
		return WS_URL;
	}

	public long getIndexHdr() {
		return indexHdr;
	}

	public void setIndexHdr(long indexHdr) {
		this.indexHdr = indexHdr;
	}

	public String getKodeJob() {
		return kodeJob;
	}

	public void setKodeJob(String kodeJob) {
		this.kodeJob = kodeJob;
	}

	public NumberFormat getFrmt() {
		return frmt;
	}
}
