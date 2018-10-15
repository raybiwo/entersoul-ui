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

import core.dto.MstTokoDto;
import pagevmd.NavigationVmd;
import util.JsonUtil;
import util.RestResponse;
 
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class TokoVmd extends NavigationVmd{
	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	private final String WS_URL = "http://localhost:8080/boot-ui";
	private final NumberFormat frmt = new DecimalFormat("000");
	
	private MstTokoDto mstToko = new MstTokoDto();
	private List<MstTokoDto> listToko = new ArrayList<>();
	private boolean readonly = false;
	
	private String kodeToko;
	
	private long indexHdr=0;
	
	@Init
	public void load(){
		String uri = WS_URL + "/toko/all";
		RestResponse restResponse = new RestResponse();
		BaseVmd bs = new BaseVmd();
		
		restResponse = bs.callWs(uri,null,HttpMethod.GET);
		try {
			listToko =  JsonUtil.mapJsonToListObject(restResponse.getContents(), MstTokoDto.class);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Unable to convert JSON!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		setIndexHdr(listToko.size()+1);
		setKodeToko("KT"+frmt.format(getIndexHdr()));
	}
	
	@Command("add")
	@NotifyChange({"includeSrc","p"})
	public void add(){
		MstTokoDto mstToko = new MstTokoDto();
		mstToko.setKodeToko(getKodeToko());
		Sessions.getCurrent().setAttribute("obj", mstToko);
		Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
		inc.setSrc("/master/toko/tokoEdit.zul");
	}
	
	@Command("edit")
	@NotifyChange({"includesrc","p"})
	public void edit(){
		if (mstToko.getKodeToko()==null) {
			Messagebox.show("Pilih data yang akan di edit");
		} else {
			Sessions.getCurrent().setAttribute("obj", mstToko);
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/master/toko/tokoEdit.zul");
		}
	}
	
	@Command("delete")
	public void delete(){
		if (mstToko.getKodeToko()==null) {
			Messagebox.show("Pilih data yang akan didelete");
		}else{
			String uriSave = WS_URL + "/toko/delete/"+mstToko.getKodeToko();
			RestResponse restResponse = new RestResponse();
			BaseVmd bs = new BaseVmd();
			
			restResponse = bs.callWs(uriSave, null, HttpMethod.DELETE);
			listToko.remove(mstToko);
			BindUtils.postNotifyChange(null, null, TokoVmd.this, "listToko");
			Clients.showNotification(restResponse.getMessage(), Clients.NOTIFICATION_TYPE_INFO, null, null, 1500);
			
			Sessions.getCurrent().setAttribute("obj", mstToko);
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/master/toko/toko.zul");
		}
	}
	
	@Command("print")
	public void print(){
		if (mstToko.getKodeToko()==null) {
			Clients.evalJavaScript("window.open('"
					+ "http://localhost:8080/boot-report/frameset?__report=/report/mstToko.rptdesign&prLink=http://localhost:8080/boot-core/toko/all"
					+ "','','top=0,left=0,height=800,width=1024,scrollbars=1,resizable=1')");
		} else {
			Clients.evalJavaScript("window.open('"
					+ "http://localhost:8080/boot-report/frameset?__report=/report/mstToko.rptdesign&prLink=http://localhost:8080/boot-core/toko/getOneList/"+mstToko.getKodeToko()
					+ "','','top=0,left=0,height=800,width=1024,scrollbars=1,resizable=1')");
		}
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		TokoVmd.logger = logger;
	}

	public MstTokoDto getMstToko() {
		return mstToko;
	}

	public void setMstToko(MstTokoDto mstToko) {
		this.mstToko = mstToko;
	}

	public List<MstTokoDto> getListToko() {
		return listToko;
	}

	public void setListToko(List<MstTokoDto> listToko) {
		this.listToko = listToko;
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

	public NumberFormat getFrmt() {
		return frmt;
	}

	public String getKodeToko() {
		return kodeToko;
	}

	public void setKodeToko(String kodeToko) {
		this.kodeToko = kodeToko;
	}

	public long getIndexHdr() {
		return indexHdr;
	}

	public void setIndexHdr(long indexHdr) {
		this.indexHdr = indexHdr;
	}
}
