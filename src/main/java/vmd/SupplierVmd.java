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

import pagevmd.NavigationVmd;
import util.JsonUtil;
import util.RestResponse;
import core.dto.MstSupplierDto;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class SupplierVmd extends NavigationVmd{
	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	private final String WS_URL = "http://localhost:8080/boot-ui";
	private final NumberFormat frmt = new DecimalFormat("000");
	
	private MstSupplierDto mstSupplier = new MstSupplierDto();
	private List<MstSupplierDto> listSupplier = new ArrayList<>();
	private boolean readonly = false;
	
	private String kodeSp;
	
	private long indexHdr=0;
	
	@Init
	public void load(){
		String uri = WS_URL + "/supplier/all";
		RestResponse restResponse = new RestResponse();
		BaseVmd bs = new BaseVmd();
		
		restResponse = bs.callWs(uri,null,HttpMethod.GET);
		try {
			listSupplier =  JsonUtil.mapJsonToListObject(restResponse.getContents(), MstSupplierDto.class);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Unable to convert JSON!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		setIndexHdr(listSupplier.size()+1);
		setKodeSp("KS"+frmt.format(getIndexHdr()));
	}
	
	@Command("add")
	@NotifyChange({"includeSrc","p"})
	public void add(){
		MstSupplierDto mstSupplier = new MstSupplierDto();
		mstSupplier.setKodeSupplier(getKodeSp());
		Sessions.getCurrent().setAttribute("obj", mstSupplier);
		Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
		inc.setSrc("/master/supplier/supplierEdit.zul");
	}
	
	@Command("edit")
	@NotifyChange({"includesrc","p"})
	public void edit(){
		if (mstSupplier.getKodeSupplier()==null) {
			Messagebox.show("Pilih data yang akan di edit");
		} else {
			Sessions.getCurrent().setAttribute("obj", mstSupplier);
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/master/supplier/supplierEdit.zul");
		}
	}
	
	@Command("delete")
	public void delete(){
		if (mstSupplier.getKodeSupplier()==null) {
			Messagebox.show("Pilih data yang akan didelete");
		}else{
			String uriSave = WS_URL + "/supplier/delete/"+mstSupplier.getKodeSupplier();
			RestResponse restResponse = new RestResponse();
			BaseVmd bs = new BaseVmd();
			
			restResponse = bs.callWs(uriSave, null, HttpMethod.DELETE);
			listSupplier.remove(mstSupplier);
			BindUtils.postNotifyChange(null, null, SupplierVmd.this, "listSupplier");
			Clients.showNotification(restResponse.getMessage(), Clients.NOTIFICATION_TYPE_INFO, null, null, 1500);
			
			Sessions.getCurrent().setAttribute("obj", mstSupplier);
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/master/supplier/supplier.zul");
		}
	}
	
	@Command("print")
	public void print(){
		if (mstSupplier.getKodeSupplier()==null) {
			Clients.evalJavaScript("window.open('"
					+ "http://localhost:8080/boot-report/frameset?__report=/report/mstSupplier.rptdesign&prLink=http://localhost:8080/boot-core/supplier/all"
					+ "','','top=0,left=0,height=800,width=1024,scrollbars=1,resizable=1')");
		} else {
			Clients.evalJavaScript("window.open('"
					+ "http://localhost:8080/boot-report/frameset?__report=/report/mstSupplier.rptdesign&prLink=http://localhost:8080/boot-core/supplier/getOneList/"+mstSupplier.getKodeSupplier()
					+ "','','top=0,left=0,height=800,width=1024,scrollbars=1,resizable=1')");
		}
	}
	
	public static Logger getLogger() {
		return logger;
	}
	public static void setLogger(Logger logger) {
		SupplierVmd.logger = logger;
	}
	public MstSupplierDto getMstSupplier() {
		return mstSupplier;
	}
	public void setMstSupplier(MstSupplierDto mstSupplier) {
		this.mstSupplier = mstSupplier;
	}
	public List<MstSupplierDto> getListSupplier() {
		return listSupplier;
	}
	public void setListSupplier(List<MstSupplierDto> listSupplier) {
		this.listSupplier = listSupplier;
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

	public String getKodeSp() {
		return kodeSp;
	}

	public void setKodeSp(String kodeSp) {
		this.kodeSp = kodeSp;
	}

	public long getIndexHdr() {
		return indexHdr;
	}

	public void setIndexHdr(long indexHdr) {
		this.indexHdr = indexHdr;
	}
}
