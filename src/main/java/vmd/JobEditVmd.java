package vmd;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Include;

import core.dto.MstJobDto;
import pagevmd.NavigationVmd;
import util.JsonUtil;
import util.RestResponse;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class JobEditVmd extends NavigationVmd{
	private List<MstJobDto> listJob = new ArrayList<>();
	
	private MstJobDto mstJob = new MstJobDto();
	
	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	private final String WS_URL = "http://localhost:8080/boot-ui";
	
	@Init
	public void load(){
		mstJob = (MstJobDto) Sessions.getCurrent().getAttribute("obj");
		String uriJobAll = WS_URL + "/job/all";
		
		RestResponse restResponse = new RestResponse();
		
		BaseVmd jobBs = new BaseVmd();
		restResponse = jobBs.callWs(uriJobAll, null, HttpMethod.GET);
		try {
			listJob = JsonUtil.mapJsonToListObject(restResponse.getContents(), MstJobDto.class);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@Command("back")
	@NotifyChange({"includeSrc","p"})
	public void back(){
		Sessions.getCurrent().setAttribute("obj", mstJob);
		Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
		inc.setSrc("/master/job/job.zul");
	}
	
	@Command("save")
	public void save(){
		if (mstJob.getIdJob()!=null) {
			String urisave = WS_URL + "/job";
			RestResponse restResponse = new RestResponse();
			BaseVmd bs = new BaseVmd();
			restResponse = bs.callWs(urisave, mstJob, HttpMethod.POST);
			Clients.showNotification(restResponse.getMessage(), Clients.NOTIFICATION_TYPE_INFO, null, null, 1500);
			Sessions.getCurrent().setAttribute("obj", mstJob);
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/master/job/job.zul");
		}
	}
	
	public List<MstJobDto> getListJob() {
		return listJob;
	}
	public void setListJob(List<MstJobDto> listJob) {
		this.listJob = listJob;
	}
	public MstJobDto getMstJob() {
		return mstJob;
	}
	public void setMstJob(MstJobDto mstJob) {
		this.mstJob = mstJob;
	}
	public static Logger getLogger() {
		return logger;
	}
	public static void setLogger(Logger logger) {
		JobEditVmd.logger = logger;
	}
	public String getWS_URL() {
		return WS_URL;
	}
}
