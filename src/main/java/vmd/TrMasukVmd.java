package vmd;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import core.dto.TrxInDtlDto;
import core.dto.TrxInHdrDto;
import pagevmd.NavigationVmd;
import util.JsonUtil;
import util.RestResponse;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class TrMasukVmd extends NavigationVmd {
	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	private final String WS_URL = "http://localhost:8080/boot-ui";
	private final NumberFormat frmt = new DecimalFormat("0000");

	SimpleDateFormat formatter = new SimpleDateFormat("ddMMyy");
	Date tanggal = new Date();

	private TrxInHdrDto trxInHdr = new TrxInHdrDto();
	private List<TrxInHdrDto> listTrxInHdr = new ArrayList<>();

	private TrxInDtlDto trxInDtl = new TrxInDtlDto();
	private List<TrxInDtlDto> listTrxInDtl = new ArrayList<>();

	private boolean readonly = false;
	
	private String noInv;
	
	private int indexHdr=0;

	@Init
	public void load() {
		String uri = WS_URL + "/trxinhdr/allLengkap";
		RestResponse restResponse = new RestResponse();
		BaseVmd bs = new BaseVmd();

		restResponse = bs.callWs(uri, null, HttpMethod.GET);
		try {
			listTrxInHdr = JsonUtil.mapJsonToListObject(
					restResponse.getContents(), TrxInHdrDto.class);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Unable to convert JSON!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		setIndexHdr(listTrxInHdr.size()+1);
		setNoInv("INV"+formatter.format(getTanggal())+frmt.format(getIndexHdr()));
	}

	@Command("add")
	@NotifyChange({ "includeSrc", "p" })
	public void add() {
		TrxInHdrDto trxInHdr = new TrxInHdrDto();
		Sessions.getCurrent().setAttribute("header", trxInHdr);
		trxInHdr.setNoInvoice(getNoInv());
		Include inc = (Include) Executions.getCurrent().getDesktop()
				.getPage("index").getFellow("mainInclude");
		inc.setSrc("/transaksi/masuk/trmasukedit.zul");
	}

	@Command("edit")
	@NotifyChange({ "includesrc", "p" })
	public void edit() {
		if (trxInHdr.getNoInvoice() == null) {
			Messagebox.show("Pilih data yang akan di edit");
		} else {
			Sessions.getCurrent().setAttribute("header", trxInHdr);
			Include inc = (Include) Executions.getCurrent().getDesktop()
					.getPage("index").getFellow("mainInclude");
			inc.setSrc("/transaksi/masuk/trmasukedit.zul");
		}
	}

	@Command("delete")
	@NotifyChange({"includeSrc","p"})
	public void delete() {
		if (trxInHdr.getNoInvoice() == null) {
			Messagebox.show("Pilih data yang akan didelete");
		} else {
			String uriDelete = WS_URL + "/trxinhdr/delete/"+trxInHdr.getNoInvoice();
			RestResponse restResponse = new RestResponse();
			BaseVmd bs = new BaseVmd();

			restResponse = bs.callWs(uriDelete, null, HttpMethod.DELETE);
			listTrxInHdr.remove(trxInHdr);
			BindUtils.postNotifyChange(null, null, TrMasukVmd.this, "listTrxInHdr");			
			
			Clients.showNotification(restResponse.getMessage(),
					Clients.NOTIFICATION_TYPE_INFO, null, null, 1500);
			Sessions.getCurrent().setAttribute("header", trxInHdr);
			Include inc = (Include) Executions.getCurrent().getDesktop()
					.getPage("index").getFellow("mainInclude");
			inc.setSrc("/transaksi/masuk/trmasuk.zul");
		}
	}
	
	public SimpleDateFormat getFormatter() {
		return formatter;
	}

	public void setFormatter(SimpleDateFormat formatter) {
		this.formatter = formatter;
	}

	public Date getTanggal() {
		return tanggal;
	}

	public void setTanggal(Date tanggal) {
		this.tanggal = tanggal;
	}

	public TrxInHdrDto getTrxInHdr() {
		return trxInHdr;
	}

	public void setTrxInHdr(TrxInHdrDto trxInHdr) {
		this.trxInHdr = trxInHdr;
	}

	public List<TrxInHdrDto> getListTrxInHdr() {
		return listTrxInHdr;
	}

	public void setListTrxInHdr(List<TrxInHdrDto> listTrxInHdr) {
		this.listTrxInHdr = listTrxInHdr;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		TrMasukVmd.logger = logger;
	}

	public TrxInDtlDto getTrxInDtl() {
		return trxInDtl;
	}

	public void setTrxInDtl(TrxInDtlDto trxInDtl) {
		this.trxInDtl = trxInDtl;
	}

	public List<TrxInDtlDto> getListTrxInDtl() {
		return listTrxInDtl;
	}

	public void setListTrxInDtl(List<TrxInDtlDto> listTrxInDtl) {
		this.listTrxInDtl = listTrxInDtl;
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

	public int getIndexHdr() {
		return indexHdr;
	}

	public void setIndexHdr(int indexHdr) {
		this.indexHdr = indexHdr;
	}

	public String getNoInv() {
		return noInv;
	}

	public void setNoInv(String noInv) {
		this.noInv = noInv;
	}
}
