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

import core.dto.TrxPenjualanDtlDto;
import core.dto.TrxPenjualanHdrDto;
import pagevmd.NavigationVmd;
import util.BaseUri;
import util.JsonUtil;
import util.RestResponse;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class PenjualanVmd extends NavigationVmd{
	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	private BaseUri uri = new BaseUri();
	private final String WS_URL = uri.BaseUriParam();
	private final NumberFormat frmt = new DecimalFormat("0000");
	
	SimpleDateFormat formatter = new SimpleDateFormat("ddMMyy");
	Date tanggal = new Date();
	
	private TrxPenjualanHdrDto trxPenjualanHdr = new TrxPenjualanHdrDto();
	private List<TrxPenjualanHdrDto> listPenjualanHdr = new ArrayList<>();
	
	private TrxPenjualanDtlDto trxPenjualanDtl = new TrxPenjualanDtlDto();
	private List<TrxPenjualanDtlDto> listPenjualanDtl = new ArrayList<>();
	
	private boolean readonly = false;
	
	private String noNota;
	
	private int indexHdr=0;
	
	@Init
	public void load(){
		String uri = WS_URL + "/penjualanhdr/all";
		RestResponse restResponse = new RestResponse();
		BaseVmd bs = new BaseVmd();
		
		restResponse = bs.callWs(uri,null,HttpMethod.GET);
		try {
			listPenjualanHdr =  JsonUtil.mapJsonToListObject(restResponse.getContents(), TrxPenjualanHdrDto.class);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Unable to convert JSON!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		setIndexHdr(listPenjualanHdr.size()+1);
		setNoNota("INC"+formatter.format(getTanggal())+frmt.format(getIndexHdr()));
	}
	
	@Command("add")
	@NotifyChange({"includeSrc","p"})
	public void add(){
		TrxPenjualanHdrDto trxPenjualanHdr = new TrxPenjualanHdrDto();
		trxPenjualanHdr.setNotaPenjualan(getNoNota());
		Sessions.getCurrent().setAttribute("header", trxPenjualanHdr);
		Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
		inc.setSrc("/transaksi/penjualan/penjualanedit.zul");
	}
	
	@Command("edit")
	@NotifyChange({"includeSrc","p"})
	public void edit(){
		if (trxPenjualanHdr.getNotaPenjualan()==null) {
			Messagebox.show("Pilih data yang akan diedit");
		} else {
			Sessions.getCurrent().setAttribute("header", trxPenjualanHdr);
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/transaksi/penjualan/penjualanedit.zul");
		}
	}
	
	@Command("delete")
	@NotifyChange({"includeSrc","p"})
	public void delete(){
		if (trxPenjualanHdr.getNotaPenjualan()==null) {
			Messagebox.show("Pilih data yang akan dihapus");
		} else {
			String uriDelete = WS_URL + "/penjualanhdr/delete/"+trxPenjualanHdr.getNotaPenjualan();
			RestResponse restResponse = new RestResponse();
			BaseVmd bs = new BaseVmd();
			
			restResponse = bs.callWs(uriDelete, null, HttpMethod.DELETE);
			listPenjualanHdr.remove(trxPenjualanHdr);
			BindUtils.postNotifyChange(null, null, PenjualanVmd.this, "listPenjualanHdr");
			
			Clients.showNotification(restResponse.getMessage(), Clients.NOTIFICATION_TYPE_INFO, null, null, 2000);
			Sessions.getCurrent().setAttribute("header", trxPenjualanHdr);
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/transaksi/penjualan/penjualan.zul");
		}
	}
	

	public TrxPenjualanHdrDto getTrxPenjualanHdr() {
		return trxPenjualanHdr;
	}
	
	public void setTrxPenjualanHdr(TrxPenjualanHdrDto trxPenjualanHdr) {
		this.trxPenjualanHdr = trxPenjualanHdr;
	}

	public List<TrxPenjualanHdrDto> getListPenjualanHdr() {
		return listPenjualanHdr;
	}
	
	public void setListPenjualanHdr(List<TrxPenjualanHdrDto> listPenjualanHdr) {
		this.listPenjualanHdr = listPenjualanHdr;
	}

	public TrxPenjualanDtlDto getTrxPenjualanDtl() {
		return trxPenjualanDtl;
	}

	public void setTrxPenjualanDtl(TrxPenjualanDtlDto trxPenjualanDtl) {
		this.trxPenjualanDtl = trxPenjualanDtl;
	}

	public List<TrxPenjualanDtlDto> getListPenjualanDtl() {
		return listPenjualanDtl;
	}

	public void setListPenjualanDtl(List<TrxPenjualanDtlDto> listPenjualanDtl) {
		this.listPenjualanDtl = listPenjualanDtl;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		PenjualanVmd.logger = logger;
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

	public int getIndexHdr() {
		return indexHdr;
	}

	public void setIndexHdr(int indexHdr) {
		this.indexHdr = indexHdr;
	}

	public String getNoNota() {
		return noNota;
	}

	public void setNoNota(String noNota) {
		this.noNota = noNota;
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

	public NumberFormat getFrmt() {
		return frmt;
	}
}
