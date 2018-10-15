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
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Include;

import pagevmd.NavigationVmd;
import util.JsonUtil;
import util.RestResponse;
import core.dto.MstCustomerDto;

public class CustomerEditVmd extends NavigationVmd{
	private List<MstCustomerDto> listCustomer = new ArrayList<>();
	
	private MstCustomerDto mstCustomer = new MstCustomerDto();
	
	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	private final String WS_URL = "http://localhost:8080/boot-ui";
	
	@Init
	public void load(){
		mstCustomer = (MstCustomerDto) Sessions.getCurrent().getAttribute("obj");
		String uriCustomerAll = WS_URL + "/customer/all";
		
		RestResponse restResponse = new RestResponse();
		
		BaseVmd customerBs = new BaseVmd();
		restResponse = customerBs.callWs(uriCustomerAll, null, HttpMethod.GET);
		try {
			listCustomer = JsonUtil.mapJsonToListObject(restResponse.getContents(), MstCustomerDto.class);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@Command("back")
	@NotifyChange({"includeSrc","p"})
	public void back(){
		Sessions.getCurrent().setAttribute("obj", mstCustomer);
		Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
		inc.setSrc("/master/customer/customer.zul");
	}
	
	@Command("save")
	public void save(){
		if (mstCustomer.getIdCustomer()!=null) {
			String urisave = WS_URL + "/customer";
			RestResponse restResponse = new RestResponse();
			BaseVmd bs = new BaseVmd();
			restResponse = bs.callWs(urisave, mstCustomer, HttpMethod.POST);
			Clients.showNotification(restResponse.getMessage(), Clients.NOTIFICATION_TYPE_INFO, null, null, 1500);
			Sessions.getCurrent().setAttribute("obj", mstCustomer);
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/master/customer/customer.zul");
		}
	}
	
	public List<MstCustomerDto> getListCustomer() {
		return listCustomer;
	}
	public void setListCustomer(List<MstCustomerDto> listCustomer) {
		this.listCustomer = listCustomer;
	}
	public MstCustomerDto getMstCustomer() {
		return mstCustomer;
	}
	public void setMstCustomer(MstCustomerDto mstCustomer) {
		this.mstCustomer = mstCustomer;
	}
	public static Logger getLogger() {
		return logger;
	}
	public static void setLogger(Logger logger) {
		CustomerEditVmd.logger = logger;
	}
	public String getWS_URL() {
		return WS_URL;
	}
}
