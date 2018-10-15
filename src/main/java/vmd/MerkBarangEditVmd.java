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

import core.dto.MstMerkBarangDto;
import pagevmd.NavigationVmd;
import util.JsonUtil;
import util.RestResponse;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class MerkBarangEditVmd extends NavigationVmd{
	private List<MstMerkBarangDto> listMerkBarang = new ArrayList<>();
	
	private MstMerkBarangDto mstMerkBarang = new MstMerkBarangDto();
	
	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	private final String WS_URL = "http://localhost:8080/boot-ui";
	
	@Init
	public void load(){
		mstMerkBarang = (MstMerkBarangDto) Sessions.getCurrent().getAttribute("obj");
		String uriMerkBarangAll = WS_URL + "/merkbarang/all";
		
		RestResponse restResponse = new RestResponse();
		
		BaseVmd merkBarangBs = new BaseVmd();
		restResponse = merkBarangBs.callWs(uriMerkBarangAll, null, HttpMethod.GET);
		try {
			listMerkBarang = JsonUtil.mapJsonToListObject(restResponse.getContents(), MstMerkBarangDto.class);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@Command("back")
	@NotifyChange({"includeSrc","p"})
	public void back(){
		Sessions.getCurrent().setAttribute("obj", mstMerkBarang);
		Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
		inc.setSrc("/master/merkBarang/merkBarang.zul");
	}
	
	@Command("save")
	public void save(){
		if (mstMerkBarang.getKodeMerk()!=null) {
			String urisave = WS_URL + "/merkbarang";
			RestResponse restResponse = new RestResponse();
			BaseVmd bs = new BaseVmd();
			restResponse = bs.callWs(urisave, mstMerkBarang, HttpMethod.POST);
			Clients.showNotification(restResponse.getMessage(), Clients.NOTIFICATION_TYPE_INFO, null, null, 1500);
			Sessions.getCurrent().setAttribute("obj", mstMerkBarang);
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/master/merkBarang/merkBarang.zul");
		}
	}
	
	public List<MstMerkBarangDto> getListMerkBarang() {
		return listMerkBarang;
	}
	public void setListMerkBarang(List<MstMerkBarangDto> listMerkBarang) {
		this.listMerkBarang = listMerkBarang;
	}
	
	public MstMerkBarangDto getMstMerkBarang() {
		return mstMerkBarang;
	}

	public void setMstMerkBarang(MstMerkBarangDto mstMerkBarang) {
		this.mstMerkBarang = mstMerkBarang;
	}

	public static Logger getLogger() {
		return logger;
	}
	public static void setLogger(Logger logger) {
		MerkBarangEditVmd.logger = logger;
	}
	public String getWS_URL() {
		return WS_URL;
	}
	
	
}
