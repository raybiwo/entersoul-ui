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
import core.dto.MstCustomerDto;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class CustomerVmd extends NavigationVmd{
	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	private final String WS_URL = "http://localhost:8080/boot-ui";
	private final NumberFormat frmt = new DecimalFormat("00");
	
	private MstCustomerDto mstCustomer = new MstCustomerDto();
	private List<MstCustomerDto> listCustomer = new ArrayList<>();
	
	private boolean readonly = false;
	
    private String kodeCst;
	
	private long indexHdr=0;
	
	@Init
	public void load(){
		String uri = WS_URL + "/customer/all";
		RestResponse restResponse = new RestResponse();
		BaseVmd bs = new BaseVmd();
		
		restResponse = bs.callWs(uri,null,HttpMethod.GET);
		try {
			listCustomer =  JsonUtil.mapJsonToListObject(restResponse.getContents(), MstCustomerDto.class);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Unable to convert JSON!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		setIndexHdr(listCustomer.size()+1);
		setKodeCst("ID"+frmt.format(getIndexHdr()));
	}
	
	@Command("add")
	@NotifyChange({"includeSrc","p"})
	public void add(){
		MstCustomerDto mstCustomer = new MstCustomerDto();
		mstCustomer.setIdCustomer(getKodeCst());
		Sessions.getCurrent().setAttribute("obj", mstCustomer);
		Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
		inc.setSrc("/master/customer/customeredit.zul");
	}
	
	@Command("edit")
	@NotifyChange({"includesrc","p"})
	public void edit(){
		if (mstCustomer.getIdCustomer()==null) {
			Messagebox.show("Pilih data yang akan di edit");
		} else {
			Sessions.getCurrent().setAttribute("obj", mstCustomer);
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/master/customer/customeredit.zul");
		}
	}
	
	@Command("delete")
	public void delete(){
		if (mstCustomer.getIdCustomer()==null) {
			Messagebox.show("Pilih data yang akan didelete");
		}else{
			String uriSave = WS_URL + "/customer/delete/"+mstCustomer.getIdCustomer();
			RestResponse restResponse = new RestResponse();
			BaseVmd bs = new BaseVmd();
			
			restResponse = bs.callWs(uriSave, null, HttpMethod.DELETE);
			listCustomer.remove(mstCustomer);
			BindUtils.postNotifyChange(null, null, CustomerVmd.this, "listCustomer");
			Clients.showNotification(restResponse.getMessage(), Clients.NOTIFICATION_TYPE_INFO, null, null, 1500);
			
			Sessions.getCurrent().setAttribute("obj", mstCustomer);
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/master/customer/customer.zul");
		}
	}
	
	@Command("print")
	public void print(){
		if (mstCustomer.getIdCustomer()==null) {
			Clients.evalJavaScript("window.open('"
					+ "http://localhost:8080/boot-report/frameset?__report=/report/mstCustomer.rptdesign&prLink=http://localhost:8080/boot-core/customer/all"
					+ "','','top=0,left=0,height=800,width=1024,scrollbars=1,resizable=1')");
		} else {
			Clients.evalJavaScript("window.open('"
					+ "http://localhost:8080/boot-report/frameset?__report=/report/mstCustomer.rptdesign&prLink=http://localhost:8080/boot-core/customer/getOneList/"+mstCustomer.getIdCustomer()
					+ "','','top=0,left=0,height=800,width=1024,scrollbars=1,resizable=1')");
		}
	}
	
	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		CustomerVmd.logger = logger;
	}

	public MstCustomerDto getMstCustomer() {
		return mstCustomer;
	}

	public void setMstCustomer(MstCustomerDto mstCustomer) {
		this.mstCustomer = mstCustomer;
	}

	public List<MstCustomerDto> getListCustomer() {
		return listCustomer;
	}

	public void setListCustomer(List<MstCustomerDto> listCustomer) {
		this.listCustomer = listCustomer;
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

	public long getIndexHdr() {
		return indexHdr;
	}

	public void setIndexHdr(long indexHdr) {
		this.indexHdr = indexHdr;
	}

	public NumberFormat getFrmt() {
		return frmt;
	}

	public String getKodeCst() {
		return kodeCst;
	}

	public void setKodeCst(String kodeCst) {
		this.kodeCst = kodeCst;
	}
}
