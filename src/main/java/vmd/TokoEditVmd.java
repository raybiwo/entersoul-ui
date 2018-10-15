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

import core.dto.MstTokoDto;
import pagevmd.NavigationVmd;
import util.JsonUtil;
import util.RestResponse;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class TokoEditVmd extends NavigationVmd{
	private List<MstTokoDto> listToko =  new ArrayList<>();
	
	private MstTokoDto mstToko = new MstTokoDto();
	
	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	private final String WS_URL = "http://localhost:8080/boot-ui";
	
	@Init
	public void load(){
		mstToko = (MstTokoDto) Sessions.getCurrent().getAttribute("obj");
		String uriTokoAll = WS_URL + "/toko/all";
		
		RestResponse restResponse = new RestResponse();
		
		BaseVmd tokoBs = new BaseVmd();
		restResponse = tokoBs.callWs(uriTokoAll, null, HttpMethod.GET);
		try {
			listToko = JsonUtil.mapJsonToListObject(restResponse.getContents(), MstTokoDto.class);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@Command("back")
	@NotifyChange({"includeSrc","p"})
	public void back(){
		Sessions.getCurrent().setAttribute("obj", mstToko);
		Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
		inc.setSrc("/master/toko/toko.zul");
	}
	
	@Command("save")
	public void save(){
		if (mstToko.getKodeToko()!=null) {
			String urisave = WS_URL + "/toko";
			RestResponse restResponse = new RestResponse();
			BaseVmd bs = new BaseVmd();
			restResponse = bs.callWs(urisave, mstToko, HttpMethod.POST);
			Clients.showNotification(restResponse.getMessage(), Clients.NOTIFICATION_TYPE_INFO, null, null, 1500);
			Sessions.getCurrent().setAttribute("obj", mstToko);
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/master/toko/toko.zul");
		}
	}

	public List<MstTokoDto> getListToko() {
		return listToko;
	}

	public void setListToko(List<MstTokoDto> listToko) {
		this.listToko = listToko;
	}

	public MstTokoDto getMstToko() {
		return mstToko;
	}

	public void setMstToko(MstTokoDto mstToko) {
		this.mstToko = mstToko;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		TokoEditVmd.logger = logger;
	}

	public String getWS_URL() {
		return WS_URL;
	}
}
