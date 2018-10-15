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

import pagevmd.NavigationVmd;
import util.JsonUtil;
import util.RestResponse;
import core.dto.MstKaryawanDto;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class KaryawanEditVmd extends NavigationVmd{
	private List<MstKaryawanDto> listKaryawan = new ArrayList<>();
	
	private MstKaryawanDto mstKaryawan = new MstKaryawanDto();
	
	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	private final String WS_URL = "http://localhost:8080/boot-ui";
	
	@Init
	public void load(){
		mstKaryawan = (MstKaryawanDto) Sessions.getCurrent().getAttribute("obj");
		String uriKaryawanAll = WS_URL + "/karyawan/all";
		
		RestResponse restResponse = new RestResponse();
		
		BaseVmd karywanBs = new BaseVmd();
		restResponse = karywanBs.callWs(uriKaryawanAll, null, HttpMethod.GET);
		try {
			listKaryawan = JsonUtil.mapJsonToListObject(restResponse.getContents(), MstKaryawanDto.class);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@Command("back")
	@NotifyChange({"includeSrc","p"})
	public void back(){
		Sessions.getCurrent().setAttribute("obj", mstKaryawan);
		Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
		inc.setSrc("/master/karyawan/karyawan.zul");
	}
	
	@Command("save")
	public void save(){
		if (mstKaryawan.getIdKaryawan()!=null) {
			String urisave = WS_URL + "/karyawan";
			
			RestResponse restResponse = new RestResponse();
			BaseVmd bs = new BaseVmd();
			
			restResponse = bs.callWs(urisave, mstKaryawan, HttpMethod.POST);
			Clients.showNotification(restResponse.getMessage(), Clients.NOTIFICATION_TYPE_INFO, null, null, 1500);
			Sessions.getCurrent().setAttribute("obj", mstKaryawan);
			
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/master/karyawan/karyawan.zul");
		}
	}
	
	public List<MstKaryawanDto> getListKaryawan() {
		return listKaryawan;
	}
	public void setListKaryawan(List<MstKaryawanDto> listKaryawan) {
		this.listKaryawan = listKaryawan;
	}
	public MstKaryawanDto getMstKaryawan() {
		return mstKaryawan;
	}
	public void setMstKaryawan(MstKaryawanDto mstKaryawan) {
		this.mstKaryawan = mstKaryawan;
	}
	public static Logger getLogger() {
		return logger;
	}
	public static void setLogger(Logger logger) {
		KaryawanEditVmd.logger = logger;
	}
	public String getWS_URL() {
		return WS_URL;
	}
}
