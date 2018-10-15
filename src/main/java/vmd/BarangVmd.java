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
import core.dto.MstBarangDto;
import util.JsonUtil;
import util.RestResponse;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class BarangVmd extends NavigationVmd {
	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	private final String WS_URL = "http://localhost:8080/boot-ui";
	private final NumberFormat frmt = new DecimalFormat("000");

	private MstBarangDto mstBarang = new MstBarangDto();
	private List<MstBarangDto> listBarang = new ArrayList<>();

	private boolean readonly = false;

	private String kodeBrg;
	private String search;

	private long indexHdr = 0;

	@Init
	public void load() {
		String uri = WS_URL + "/barang/allLengkap";
		RestResponse restResponse = new RestResponse();
		BaseVmd bs = new BaseVmd();

		try {
			restResponse = bs.callWs(uri, null, HttpMethod.GET);
			listBarang = JsonUtil.mapJsonToListObject(
					restResponse.getContents(), MstBarangDto.class);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Unable to convert JSON!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		setIndexHdr(restResponse.getTotalRecords() + 1);
		setKodeBrg("BRG-" + frmt.format(getIndexHdr()));
	}

	@Command("add")
	@NotifyChange({ "includeSrc", "p" })
	public void add() {
		System.out
				.println("======================================================================="
						+ listBarang.size() + 1);
		MstBarangDto mstBarang = new MstBarangDto();
		mstBarang.setKodeBarang(getKodeBrg());
		Sessions.getCurrent().setAttribute("obj", mstBarang);
		Include inc = (Include) Executions.getCurrent().getDesktop()
				.getPage("index").getFellow("mainInclude");
		inc.setSrc("/master/barang/barangedit.zul");
	}

	@Command("edit")
	@NotifyChange({ "includesrc", "p" })
	public void edit() {
		if (mstBarang.getKodeBarang() == null) {
			Messagebox.show("Pilih data yang akan di edit");
		} else {
			Sessions.getCurrent().setAttribute("obj", mstBarang);
			Include inc = (Include) Executions.getCurrent().getDesktop()
					.getPage("index").getFellow("mainInclude");
			inc.setSrc("/master/barang/barangedit.zul");
		}
	}

	@Command("delete")
	@NotifyChange({ "includeSrc", "p" })
	public void delete() {
		if (mstBarang.getKodeBarang() == null) {
			Messagebox.show("Pilih data yang akan didelete");
		} else {
			String uriSave = WS_URL + "/barang/delete/"
					+ mstBarang.getKodeBarang();
			RestResponse restResponse = new RestResponse();
			BaseVmd bs = new BaseVmd();

			restResponse = bs.callWs(uriSave, null, HttpMethod.DELETE);
			listBarang.remove(mstBarang);
			BindUtils
					.postNotifyChange(null, null, BarangVmd.this, "listBarang");
			Clients.showNotification(restResponse.getMessage(),
					Clients.NOTIFICATION_TYPE_INFO, null, null, 1500);

			Sessions.getCurrent().setAttribute("obj", mstBarang);
			Include inc = (Include) Executions.getCurrent().getDesktop()
					.getPage("index").getFellow("mainInclude");
			inc.setSrc("/master/barang/barang.zul");
		}
	}

	@Command("print")
	public void print() {
		System.out.println("=========================================="
				+ mstBarang.getKodeBarang());
		if (mstBarang.getKodeBarang() == null) {
			Clients.evalJavaScript("window.open('"
					+ "http://localhost:8080/boot-report/frameset?__report=/report/mstBarang.rptdesign&prLink=http://localhost:8080/boot-core/barang/allLengkap"
					+ "','','top=0,left=0,height=800,width=1024,scrollbars=1,resizable=1')");
		} else {
			Clients.evalJavaScript("window.open('"
					+ "http://localhost:8080/boot-report/frameset?__report=/report/mstBarang.rptdesign&prLink=http://localhost:8080/boot-core/barang/getOneList/"
					+ mstBarang.getKodeBarang()
					+ "','','top=0,left=0,height=800,width=1024,scrollbars=1,resizable=1')");
		}
	}

	@Command("search")
	@NotifyChange("listBarang")
	public void search() {
		String uriSearch = WS_URL + "/barang/search/" + getSearch();
		//String uriSearchAll = WS_URL + "/barang/search/" + getSearch();
		RestResponse restResponse = new RestResponse();
		BaseVmd bs = new BaseVmd();
		
		System.out.println("======================================"+getSearch());

		try {
				restResponse = bs.callWs(uriSearch, null, HttpMethod.GET);
				listBarang = JsonUtil.mapJsonToListObject(
						restResponse.getContents(), MstBarangDto.class);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Unable to convert JSON!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public String getWS_URL() {
		return WS_URL;
	}

	public NumberFormat getFrmt() {
		return frmt;
	}

	public MstBarangDto getMstBarang() {
		return mstBarang;
	}

	public void setMstBarang(MstBarangDto mstBarang) {
		this.mstBarang = mstBarang;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		BarangVmd.logger = logger;
	}

	public List<MstBarangDto> getListBarang() {
		return listBarang;
	}

	public void setListBarang(List<MstBarangDto> listBarang) {
		this.listBarang = listBarang;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public long getIndexHdr() {
		return indexHdr;
	}

	public void setIndexHdr(long indexHdr) {
		this.indexHdr = indexHdr;
	}

	public String getKodeBrg() {
		return kodeBrg;
	}

	public void setKodeBrg(String kodeBrg) {
		this.kodeBrg = kodeBrg;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}
}
