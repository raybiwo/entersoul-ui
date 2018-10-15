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

import core.dto.MstMerkBarangDto;
import pagevmd.NavigationVmd;
import util.JsonUtil;
import util.RestResponse;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class MerkBarangVmd extends NavigationVmd{
	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	private final String WS_URL = "http://localhost:8080/boot-ui";
	private final NumberFormat frmt = new DecimalFormat("000");
	
	private MstMerkBarangDto mstMerkBarang = new MstMerkBarangDto();
	private List<MstMerkBarangDto> listMerkBarang = new ArrayList<>();
	private boolean readonly = false;
	
	private String kodeMerk;
	
	private long indexHdr=0;
	
	@Init
	public void load(){
		String uri = WS_URL + "/merkbarang/all";
		RestResponse restResponse = new RestResponse();
		BaseVmd bs = new BaseVmd();
		
		restResponse = bs.callWs(uri,null,HttpMethod.GET);
		try {
			listMerkBarang =  JsonUtil.mapJsonToListObject(restResponse.getContents(), MstMerkBarangDto.class);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Unable to convert JSON!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		setIndexHdr(listMerkBarang.size());
		setKodeMerk("KM"+frmt.format(getIndexHdr()));
	}
	
	@Command("add")
	@NotifyChange({"includeSrc","p"})
	public void add(){
		MstMerkBarangDto mstMerkBarang = new MstMerkBarangDto();
		mstMerkBarang.setKodeMerk(getKodeMerk());
		Sessions.getCurrent().setAttribute("obj", mstMerkBarang);
		Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
		inc.setSrc("/master/merkBarang/merkBarangedit.zul");
	}
	
	@Command("edit")
	@NotifyChange({"includesrc","p"})
	public void edit(){
		if (mstMerkBarang.getKodeMerk()==null) {
			Messagebox.show("Pilih data yang akan di edit");
		} else {
			Sessions.getCurrent().setAttribute("obj", mstMerkBarang);
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/master/merkBarang/merkBarangedit.zul");
		}
	}
	
	@Command("delete")
	public void delete(){
		if (mstMerkBarang.getKodeMerk()==null) {
			Messagebox.show("Pilih data yang akan didelete");
		}else{
			String uriSave = WS_URL + "/merkbarang/delete/"+mstMerkBarang.getKodeMerk();
			RestResponse restResponse = new RestResponse();
			BaseVmd bs = new BaseVmd();
			
			restResponse = bs.callWs(uriSave, null, HttpMethod.DELETE);
			listMerkBarang.remove(mstMerkBarang);
			BindUtils.postNotifyChange(null, null, MerkBarangVmd.this, "listMerkBarang");
			Clients.showNotification(restResponse.getMessage(), Clients.NOTIFICATION_TYPE_INFO, null, null, 1500);
			
			Sessions.getCurrent().setAttribute("obj", mstMerkBarang);
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/master/merkBarang/merkBarang.zul");
		}
	}
	
	@Command("print")
	public void print(){
		if (mstMerkBarang.getKodeMerk()==null) {
			Clients.evalJavaScript("window.open('"
					+ "http://localhost:8080/boot-report/frameset?__report=/report/mstMerkBarang.rptdesign&prLink=http://localhost:8080/boot-core/merkbarang/all"
					+ "','','top=0,left=0,height=800,width=1024,scrollbars=1,resizable=1')");
		} else {
			Clients.evalJavaScript("window.open('"
					+ "http://localhost:8080/boot-report/frameset?__report=/report/mstMerkBarang.rptdesign&prLink=http://localhost:8080/boot-core/merkbarang/getOneList/"+mstMerkBarang.getKodeMerk()
					+ "','','top=0,left=0,height=800,width=1024,scrollbars=1,resizable=1')");
		}
	}
	
	public static Logger getLogger() {
		return logger;
	}
	public static void setLogger(Logger logger) {
		MerkBarangVmd.logger = logger;
	}
	public MstMerkBarangDto getMstMerkBarang() {
		return mstMerkBarang;
	}
	public void setMstMerkBarang(MstMerkBarangDto mstMerkBarang) {
		this.mstMerkBarang = mstMerkBarang;
	}
	public List<MstMerkBarangDto> getListMerkBarang() {
		return listMerkBarang;
	}
	public void setListMerkBarang(List<MstMerkBarangDto> listMerkBarang) {
		this.listMerkBarang = listMerkBarang;
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

	public String getKodeMerk() {
		return kodeMerk;
	}

	public void setKodeMerk(String kodeMerk) {
		this.kodeMerk = kodeMerk;
	}

	public long getIndexHdr() {
		return indexHdr;
	}

	public void setIndexHdr(long indexHdr) {
		this.indexHdr = indexHdr;
	}
}
