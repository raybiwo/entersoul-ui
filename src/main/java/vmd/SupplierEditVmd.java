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

import core.dto.MstSupplierDto;
import pagevmd.NavigationVmd;
import util.JsonUtil;
import util.RestResponse;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class SupplierEditVmd extends NavigationVmd{
	private List<MstSupplierDto> listSupplier = new ArrayList<>();
	
	private MstSupplierDto mstSupplier = new MstSupplierDto();
	
	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	private final String WS_URL = "http://localhost:8080/boot-ui";
	
	@Init
	public void load(){
		mstSupplier = (MstSupplierDto) Sessions.getCurrent().getAttribute("obj");
		String uriSupplierAll = WS_URL + "/supplier/all";
		
		RestResponse restResponse = new RestResponse();
		
		BaseVmd supplierBs = new BaseVmd();
		restResponse = supplierBs.callWs(uriSupplierAll, null, HttpMethod.GET);
		try {
			listSupplier = JsonUtil.mapJsonToListObject(restResponse.getContents(), MstSupplierDto.class);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@Command("back")
	@NotifyChange({"includeSrc","p"})
	public void back(){
		Sessions.getCurrent().setAttribute("obj", mstSupplier);
		Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
		inc.setSrc("/master/supplier/supplier.zul");
	}
	
	@Command("save")
	public void save(){
		if (mstSupplier.getKodeSupplier()!=null) {
			String urisave = WS_URL + "/supplier";
			RestResponse restResponse = new RestResponse();
			BaseVmd bs = new BaseVmd();
			restResponse = bs.callWs(urisave, mstSupplier, HttpMethod.POST);
			Clients.showNotification(restResponse.getMessage(), Clients.NOTIFICATION_TYPE_INFO, null, null, 1500);
			Sessions.getCurrent().setAttribute("obj", mstSupplier);
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/master/supplier/supplier.zul");
		}
	}

	public List<MstSupplierDto> getListSupplier() {
		return listSupplier;
	}

	public void setListSupplier(List<MstSupplierDto> listSupplier) {
		this.listSupplier = listSupplier;
	}

	public MstSupplierDto getMstSupplier() {
		return mstSupplier;
	}

	public void setMstSupplier(MstSupplierDto mstSupplier) {
		this.mstSupplier = mstSupplier;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		SupplierEditVmd.logger = logger;
	}

	public String getWS_URL() {
		return WS_URL;
	}
}
