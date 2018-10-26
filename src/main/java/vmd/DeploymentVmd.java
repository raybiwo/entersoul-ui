package vmd;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import core.dto.DeploymentDto;
import util.BaseUri;
import util.JsonUtil;
import util.RestResponse;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class DeploymentVmd {
	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	private BaseUri uri = new BaseUri();
	private final String WS_URL = uri.BaseUriParam();
	
	SimpleDateFormat formatter = new SimpleDateFormat("ddMMyy");
	Date tanggal = new Date();
	
	private DeploymentDto deploymentDto = new DeploymentDto();
	private List<DeploymentDto> listDeploymentDto = new ArrayList<>();
	
	private int indexHdr=0;
	
	private boolean statusPopUp = false;
	private int indexDtl = 0;

	@Init
	public void load(){
		String uri = WS_URL + "/deployment/all";
		RestResponse restResponse = new RestResponse();
		BaseVmd bs = new BaseVmd();
		
		try {
			restResponse = bs.callWs(uri,null,HttpMethod.GET);
			listDeploymentDto =  JsonUtil.mapJsonToListObject(restResponse.getContents(), DeploymentDto.class);
		} catch (Exception e) {
			System.out.println("Unable to convert JSON!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		setIndexHdr(listDeploymentDto.size()+1);
	}
	
	@Command("delete")
	@NotifyChange({"includeSrc","p"})
	public void delete(){
		if (deploymentDto.getDepName()==null) {
			Messagebox.show("Pilih data yang akan dihapus");
		} else {
			String uriDelete = WS_URL + "/deployment/delete/"+deploymentDto.getId();
			RestResponse restResponse = new RestResponse();
			BaseVmd bs = new BaseVmd();
			
			restResponse = bs.callWs(uriDelete, null, HttpMethod.DELETE);
			listDeploymentDto.remove(deploymentDto);
			BindUtils.postNotifyChange(null, null, DeploymentVmd.this, "listDeploymentDto");
			
			Clients.showNotification(restResponse.getMessage(), Clients.NOTIFICATION_TYPE_INFO, null, null, 2000);
			Sessions.getCurrent().setAttribute("header", deploymentDto);
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/deployment/deployment.zul");
		}
	}
	
	@Command("add")
	@NotifyChange({ "statusPopUp", "deploymentDto"})
	public void add(){
		setStatusPopUp(true);
		deploymentDto = new DeploymentDto();
//		Sessions.getCurrent().setAttribute("deployment", deploymentDto);
//		Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
//		inc.setSrc("/deployment/deploymentedit.zul");
	}
	
	@Command("cancel")
	@NotifyChange("statusPopUp")
	public void backDetail() {
		setStatusPopUp(false);
	}
	
	@Command("save")
	@NotifyChange({ "listDeploymentDto", "statusPopUp", "deploymentDto" })
	public void save() {
//		Messagebox.show("check data " +deploymentDto.getDepName());
		
		String uriSave = WS_URL + "/deployment";
//		String uriAll = WS_URL + "/deployment/all";
		RestResponse restResponse = new RestResponse();
		BaseVmd bs = new BaseVmd();
		
		restResponse = bs.callWs(uriSave, deploymentDto, HttpMethod.POST);
		listDeploymentDto.add(deploymentDto);
		
		Clients.showNotification(restResponse.getMessage(),
				Clients.NOTIFICATION_TYPE_INFO, null, null, 1500);
		
		setStatusPopUp(false);
//		Include inc = (Include) Executions.getCurrent().getDesktop()
//				.getPage("index").getFellow("mainInclude");
//		inc.setSrc("/deployment/deployment.zul");
//		try {
//			restResponse = bs.callWs(uriAll,null,HttpMethod.GET);
//			listDeploymentDto =  JsonUtil.mapJsonToListObject(restResponse.getContents(), DeploymentDto.class);
//		} catch (Exception e) {
//			System.out.println("Unable to convert JSON!");
//			logger.error(e.getMessage());
//			e.printStackTrace();
//		}
		
		setIndexHdr(listDeploymentDto.size()+1);
	}
	
	public int getIndexDtl() {
		return indexDtl;
	}

	public void setIndexDtl(int indexDtl) {
		this.indexDtl = indexDtl;
	}
	
	public boolean isStatusPopUp() {
		return statusPopUp;
	}

	public void setStatusPopUp(boolean statusPopUp) {
		this.statusPopUp = statusPopUp;
	}

	public Date getTanggal() {
		return tanggal;
	}

	public void setTanggal(Date tanggal) {
		this.tanggal = tanggal;
	}

	public DeploymentDto getDeploymentDto() {
		return deploymentDto;
	}

	public void setDeploymentDto(DeploymentDto deploymentDto) {
		this.deploymentDto = deploymentDto;
	}

	public List<DeploymentDto> getListDeploymentDto() {
		return listDeploymentDto;
	}

	public void setListDeploymentDto(List<DeploymentDto> listDeploymentDto) {
		this.listDeploymentDto = listDeploymentDto;
	}

	public int getIndexHdr() {
		return indexHdr;
	}

	public void setIndexHdr(int indexHdr) {
		this.indexHdr = indexHdr;
	}
}
