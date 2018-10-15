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

import core.dto.MstKaryawanDto;
import pagevmd.NavigationVmd;
import util.JsonUtil;
import util.RestResponse;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class KaryawanVmd extends NavigationVmd{
	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	private final String WS_URL =  "http://localhost:8080/boot-ui";
	private final NumberFormat frmt = new DecimalFormat("000");
	
	private MstKaryawanDto mstKaryawan = new MstKaryawanDto();
	private List<MstKaryawanDto> listKaryawan = new ArrayList<>();
	
	private String kodeKry;
	
	private long indexHdr=0;
	
	@Init
	public void load(){
		String uri = WS_URL + "/karyawan/all";
		RestResponse restResponse = new RestResponse();
		BaseVmd bs = new BaseVmd();
		
		restResponse = bs.callWs(uri,null,HttpMethod.GET);
		try {
			listKaryawan =  JsonUtil.mapJsonToListObject(restResponse.getContents(), MstKaryawanDto.class);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Unable to convert JSON!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		setIndexHdr(listKaryawan.size()+1);
		setKodeKry("IDK-"+frmt.format(getIndexHdr()));
	}
	
	@Command("add")
	@NotifyChange({"includeSrc","p"})
	public void add(){
		MstKaryawanDto mstKaryawan = new MstKaryawanDto();
		mstKaryawan.setIdKaryawan(getKodeKry());
		Sessions.getCurrent().setAttribute("obj", mstKaryawan);
		Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
		inc.setSrc("/master/karyawan/karyawanEdit.zul");
	}
	
	@Command("edit")
	@NotifyChange({"includesrc","p"})
	public void edit(){
		if (mstKaryawan.getIdJob()==null) {
			Messagebox.show("Pilih data yang akan di edit");
		} else {
			Sessions.getCurrent().setAttribute("obj", mstKaryawan);
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/master/karyawan/karyawanEdit.zul");
		}
	}
	
	@Command("delete")
	public void delete(){
		if (mstKaryawan.getIdJob()==null) {
			Messagebox.show("Pilih data yang akan didelete");
		}else{
			String uriSave = WS_URL + "/karyawan/delete/"+mstKaryawan.getIdKaryawan();
			RestResponse restResponse = new RestResponse();
			BaseVmd bs = new BaseVmd();
			
			restResponse = bs.callWs(uriSave, null, HttpMethod.DELETE);
			listKaryawan.remove(mstKaryawan);
			BindUtils.postNotifyChange(null, null, KaryawanVmd.this, "listKaryawan");
			Clients.showNotification(restResponse.getMessage(), Clients.NOTIFICATION_TYPE_INFO, null, null, 2000);
			
			Sessions.getCurrent().setAttribute("obj", mstKaryawan);
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/master/karyawan/karyawan.zul");
		}
	}
	
	@Command("print")
	public void print(){
		if (mstKaryawan.getIdKaryawan()==null) {
			Clients.evalJavaScript("window.open('"
					+ "http://localhost:8080/boot-report/frameset?__report=/report/mstKaryawan.rptdesign&prLink=http://localhost:8080/boot-core/karyawan/all"
					+ "','','top=0,left=0,height=800,width=1024,scrollbars=1,resizable=1')");
		} else {
			Clients.evalJavaScript("window.open('"
					+ "http://localhost:8080/boot-report/frameset?__report=/report/mstKaryawan.rptdesign&prLink=http://localhost:8080/boot-core/karyawan/getOneList/"+mstKaryawan.getIdKaryawan()
					+ "','','top=0,left=0,height=800,width=1024,scrollbars=1,resizable=1')");
		}
	}
	
	public static Logger getLogger() {
		return logger;
	}
	public static void setLogger(Logger logger) {
		KaryawanVmd.logger = logger;
	}
	public MstKaryawanDto getMstKaryawan() {
		return mstKaryawan;
	}
	public void setMstKaryawan(MstKaryawanDto mstKaryawan) {
		this.mstKaryawan = mstKaryawan;
	}
	public List<MstKaryawanDto> getListKaryawan() {
		return listKaryawan;
	}
	public void setListKaryawan(List<MstKaryawanDto> listKaryawan) {
		this.listKaryawan = listKaryawan;
	}
	public String getWS_URL() {
		return WS_URL;
	}

	public NumberFormat getFrmt() {
		return frmt;
	}

	public String getKodeKry() {
		return kodeKry;
	}

	public void setKodeKry(String kodeKry) {
		this.kodeKry = kodeKry;
	}

	public long getIndexHdr() {
		return indexHdr;
	}

	public void setIndexHdr(long indexHdr) {
		this.indexHdr = indexHdr;
	}
}
