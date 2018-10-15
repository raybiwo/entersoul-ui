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

import core.dto.MstJenisBarangDto;
import pagevmd.NavigationVmd;
import util.JsonUtil;
import util.RestResponse;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class JenisBarangEditVmd extends NavigationVmd{
	private List<MstJenisBarangDto> listJenisBarang = new ArrayList<>();
	
	private MstJenisBarangDto mstJenisBarang = new MstJenisBarangDto();
	
	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	private final String WS_URL = "http://localhost:8080/boot-ui";
	
	@Init
	public void load(){
		mstJenisBarang = (MstJenisBarangDto) Sessions.getCurrent().getAttribute("obj");
		String uriJenisBarangAll = WS_URL + "/jenisbarang/all";
		
		RestResponse restResponse = new RestResponse();
		
		BaseVmd jenisBarangBs = new BaseVmd();
		restResponse = jenisBarangBs.callWs(uriJenisBarangAll, null, HttpMethod.GET);
		try {
			listJenisBarang = JsonUtil.mapJsonToListObject(restResponse.getContents(), MstJenisBarangDto.class);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@Command("back")
	@NotifyChange({"includeSrc","p"})
	public void back(){
		Sessions.getCurrent().setAttribute("obj", mstJenisBarang);
		Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
		inc.setSrc("/master/JenisBarang/jenisBarang.zul");
	}
	
	@Command("save")
	public void save(){
		if (mstJenisBarang.getKodeJenisBarang()!=null) {
			String urisave = WS_URL + "/jenisbarang";
			RestResponse restResponse = new RestResponse();
			BaseVmd bs = new BaseVmd();
			restResponse = bs.callWs(urisave, mstJenisBarang, HttpMethod.POST);
			Clients.showNotification(restResponse.getMessage(), Clients.NOTIFICATION_TYPE_INFO, null, null, 1500);
			Sessions.getCurrent().setAttribute("obj", mstJenisBarang);
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/master/JenisBarang/jenisBarang.zul");
		}
	}
	
	public List<MstJenisBarangDto> getListJenisBarang() {
		return listJenisBarang;
	}
	public void setListJenisBarang(List<MstJenisBarangDto> listJenisBarang) {
		this.listJenisBarang = listJenisBarang;
	}
	public MstJenisBarangDto getMstJenisBarang() {
		return mstJenisBarang;
	}
	public void setMstJenisBarang(MstJenisBarangDto mstJenisBarang) {
		this.mstJenisBarang = mstJenisBarang;
	}
	public static Logger getLogger() {
		return logger;
	}
	public static void setLogger(Logger logger) {
		JenisBarangEditVmd.logger = logger;
	}
	public String getWS_URL() {
		return WS_URL;
	}
}
