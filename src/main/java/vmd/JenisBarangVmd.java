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
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Include;
import org.zkoss.zul.Messagebox;

import core.dto.MstJenisBarangDto;
import pagevmd.NavigationVmd;
import util.JsonUtil;
import util.RestResponse;

public class JenisBarangVmd extends NavigationVmd{
	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	private final String WS_URL = "http://localhost:8080/boot-ui";
	private final NumberFormat frmt = new DecimalFormat("000");
	
	private MstJenisBarangDto mstJenisBarang = new MstJenisBarangDto();
	private List<MstJenisBarangDto> listJenisBarang = new ArrayList<>();
	private boolean readonly = false;
	
	private String kodeJenis;
	
	private long indexHdr=0;
	
	@Init
	public void load(){
		String uri = WS_URL + "/jenisbarang/all";
		RestResponse restResponse = new RestResponse();
		BaseVmd bs = new BaseVmd();
		
		restResponse = bs.callWs(uri,null,HttpMethod.GET);
		try {
			listJenisBarang =  JsonUtil.mapJsonToListObject(restResponse.getContents(), MstJenisBarangDto.class);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Unable to convert JSON!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		setIndexHdr(listJenisBarang.size());
		setKodeJenis("KJ"+frmt.format(getIndexHdr()));
	}
	
	@Command("add")
	@NotifyChange({"includeSrc","p"})
	public void add(){
		MstJenisBarangDto mstJenisBarangDto = new MstJenisBarangDto();
		mstJenisBarangDto.setKodeJenisBarang(getKodeJenis());
		Sessions.getCurrent().setAttribute("obj", mstJenisBarangDto);
		Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
		inc.setSrc("/master/JenisBarang/jenisBarangEdit.zul");
	}
	
	@Command("edit")
	@NotifyChange({"includesrc","p"})
	public void edit(){
		if (mstJenisBarang.getKodeJenisBarang()==null) {
			Messagebox.show("Pilih data yang akan di edit");
		} else {
			Sessions.getCurrent().setAttribute("obj", mstJenisBarang);
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/master/JenisBarang/jenisBarangEdit.zul");
		}
	}
	
	@Command("delete")
	public void delete(){
		if (mstJenisBarang.getKodeJenisBarang()==null) {
			Messagebox.show("Pilih data yang akan didelete");
		}else{
			String uriSave = WS_URL + "/jenisbarang/delete/"+mstJenisBarang.getKodeJenisBarang();
			RestResponse restResponse = new RestResponse();
			BaseVmd bs = new BaseVmd();
			
			restResponse = bs.callWs(uriSave, null, HttpMethod.DELETE);
			listJenisBarang.remove(mstJenisBarang);
			BindUtils.postNotifyChange(null, null, JenisBarangVmd.this, "listJenisBarang");
			Clients.showNotification(restResponse.getMessage(), Clients.NOTIFICATION_TYPE_INFO, null, null, 1500);
			
			Sessions.getCurrent().setAttribute("obj", mstJenisBarang);
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/master/JenisBarang/jenisBarang.zul");
		}
	}
	
	@Command("print")
	public void print(){
		if (mstJenisBarang.getKodeJenisBarang()==null) {
			Clients.evalJavaScript("window.open('"
					+ "http://localhost:8080/boot-report/frameset?__report=/report/mstJenisBarang.rptdesign&prLink=http://localhost:8080/boot-core/jenisbarang/all"
					+ "','','top=0,left=0,height=800,width=1024,scrollbars=1,resizable=1')");
		} else {
			Clients.evalJavaScript("window.open('"
					+ "http://localhost:8080/boot-report/frameset?__report=/report/mstJenisBarang.rptdesign&prLink=http://localhost:8080/boot-core/jenisbarang/getOneList/"+mstJenisBarang.getKodeJenisBarang()
					+ "','','top=0,left=0,height=800,width=1024,scrollbars=1,resizable=1')");
		}
	}
	
	public static Logger getLogger() {
		return logger;
	}
	public static void setLogger(Logger logger) {
		JenisBarangVmd.logger = logger;
	}
	public MstJenisBarangDto getMstJenisBarang() {
		return mstJenisBarang;
	}
	public void setMstJenisBarang(MstJenisBarangDto mstJenisBarang) {
		this.mstJenisBarang = mstJenisBarang;
	}
	public List<MstJenisBarangDto> getListJenisBarang() {
		return listJenisBarang;
	}
	public void setListJenisBarang(List<MstJenisBarangDto> listJenisBarang) {
		this.listJenisBarang = listJenisBarang;
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

	public String getKodeJenis() {
		return kodeJenis;
	}

	public void setKodeJenis(String kodeJenis) {
		this.kodeJenis = kodeJenis;
	}

	public NumberFormat getFrmt() {
		return frmt;
	}
	
	
}
