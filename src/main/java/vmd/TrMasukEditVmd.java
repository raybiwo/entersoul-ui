package vmd;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Include;
import org.zkoss.zul.Messagebox;

import pagevmd.NavigationVmd;
import util.JsonUtil;
import util.RestResponse;
import core.dto.MstBarangDto;
import core.dto.MstSupplierDto;
import core.dto.TrxInDtlDto;
import core.dto.TrxInHdrDto;

public class TrMasukEditVmd extends NavigationVmd {
	private final String WS_URL = "http://localhost:8080/boot-ui";
	private final NumberFormat frmt = new DecimalFormat("0000");

	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);

	private List<TrxInDtlDto> listIndexDetail = new ArrayList<>();
	private List<TrxInHdrDto> listIndexHeader = new ArrayList<>();
	private List<TrxInDtlDto> listDetail = new ArrayList<>();
	private List<MstSupplierDto> listSupplier = new ArrayList<>();
	private List<MstBarangDto> listBarang = new ArrayList<>();
	
	private TrxInDtlDto trxInDtl = new TrxInDtlDto();
	private TrxInHdrDto trxInHdr = new TrxInHdrDto();
	private MstBarangDto mstBarangDto = new MstBarangDto();
	private MstBarangDto mstBarang = new MstBarangDto();
	private MstSupplierDto mstSupplier = new MstSupplierDto();

	private String idDetail;
	private String kodebarang;

	private boolean visible = true;
	private boolean statusPopUp = false;
	
	private int indexDtl=0;
	private int jumlah=0;
	private int qty;
	private int stok;
	private int hargaSatuan;

	@Init
	public void load() {
		trxInHdr = (TrxInHdrDto) Sessions.getCurrent().getAttribute("header");

		String kode = trxInHdr.getNoInvoice();
		
		String uriIndexDtl = WS_URL + "/trxindtl/all"; 
		String uriDetail = WS_URL + "/trxindtl/getByInv/" + kode;
		String uriBarang = WS_URL + "/barang/all";
		String uriSupplier = WS_URL + "/supplier/all";

		RestResponse restResponse = new RestResponse();
		BaseVmd bs = new BaseVmd();

		try {
			restResponse = bs.callWs(uriSupplier, null, HttpMethod.GET);
			listSupplier = JsonUtil.mapJsonToListObject(
					restResponse.getContents(), MstSupplierDto.class);
			
			restResponse = bs.callWs(uriIndexDtl, null, HttpMethod.GET);
			listIndexDetail = JsonUtil.mapJsonToListObject(
					restResponse.getContents(), TrxInDtlDto.class);
			
			restResponse = bs.callWs(uriDetail, null, HttpMethod.GET);
			listDetail = JsonUtil.mapJsonToListObject(
					restResponse.getContents(), TrxInDtlDto.class);

			restResponse = bs.callWs(uriBarang, null, HttpMethod.GET);
			listBarang = JsonUtil.mapJsonToListObject(
					restResponse.getContents(), MstBarangDto.class);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		setIndexDtl(listIndexDetail.size()+1);
	}

	@Command("back")
	@NotifyChange({ "includeSrc", "p" })
	public void back() {
		Sessions.getCurrent().setAttribute("obj", trxInDtl);
		Include inc = (Include) Executions.getCurrent().getDesktop()
				.getPage("index").getFellow("mainInclude");
		inc.setSrc("/transaksi/masuk/trmasuk.zul");
	}

	@Command("save")
	@NotifyChange("trxInHdr")
	public void save() {
		if (trxInHdr.getNoInvoice()==null) {
			Messagebox.show("Tidak dapat disimpan, silahkan masukkan data!");
		} else {
			String uriSave= WS_URL + "/trxinhdr";
			RestResponse restResponse = new RestResponse();
			BaseVmd bs = new BaseVmd();
			
			trxInHdr.setDetail(listDetail);
			trxInHdr.setBarang(listBarang);
			
			restResponse = bs.callWs(uriSave, trxInHdr, HttpMethod.POST);
			
			Clients.showNotification(restResponse.getMessage(), Clients.NOTIFICATION_TYPE_INFO, null, null, 1500);
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/transaksi/masuk/trmasuk.zul");
		}
	}
	
	@Command("addDetail")
	@NotifyChange({"statusPopUp","trxInDtl","jumlah","mstBarang","stok","idDetail","hargaSatuan"})
	public void addDetail() {
		setStatusPopUp(true);
		trxInDtl = new TrxInDtlDto();

		setIdDetail("ID"+frmt.format(getIndexDtl()));
		
		setHargaSatuan(0); 
		setJumlah(0);
		setStok(0);
	}
	
	@Command("delDetail")
	@NotifyChange({"trxInDtl","listDetail"})
	public void delDetail(){		
		if(trxInDtl.getKodeDetailMasuk() == null){
			Messagebox.show("Pilih data yang akan didelete");
		} else {
			listDetail.remove(trxInDtl);
		}
	}
	
	@Command("saveDetail")
	@NotifyChange({ "listDetail", "statusPopUp", "trxInHdr" })
	public void saveDetail() {
		if (mstBarang.getKodeBarang()==null) {
			Messagebox.show("Tidak bisa disave, silahkan masukkan data terlebih dahulu");
		} else {
			trxInDtl.setKodeBarang(mstBarang.getKodeBarang());
			trxInDtl.setNamaBarang(mstBarang.getNamaBarang());
			trxInDtl.setKodeDetailMasuk(getIdDetail());
			trxInDtl.setNoInvoice(trxInHdr.getNoInvoice());
			
			setKodebarang(trxInDtl.getKodeBarang());
			setQty(trxInDtl.getQtyMasuk().intValue());
			
			if(kodebarang!=null){
				for (MstBarangDto bar : listBarang) {
					if (getKodebarang() == bar.getKodeBarang()) {
						System.out.println("");
						setStok(bar.getStokBarang().intValue()+getQty());
						BigDecimal bigStok = new BigDecimal(stok);
						bar.setStokBarang(bigStok);
					} else {
						System.out.println("");
					}
				}
			}
			
			listDetail.add(trxInDtl);

			int hasil = 0;
			for (TrxInDtlDto detail : listDetail) {
				hasil = hasil + detail.getSubtotalHargaMasuk().intValue();
			}
			BigDecimal hargaTotal = new BigDecimal(hasil);
			trxInHdr.setTotalHargaMasuk(hargaTotal);
			setStatusPopUp(false);
			
			indexDtl = indexDtl+1;
		}
	}
	
	@Command("total")
	@NotifyChange({"statusPopUp","jumlah"})
	public void total() {
		setStatusPopUp(true);
		if (trxInDtl.getQtyMasuk()!=null && getHargaSatuan()!=0) {
			setJumlah(getHargaSatuan()*trxInDtl.getQtyMasuk().intValue());
			BigDecimal tempJumlah = new BigDecimal(getJumlah());
			trxInDtl.setSubtotalHargaMasuk(tempJumlah);
		} else {
			setJumlah(getHargaSatuan());
			BigDecimal tempJumlah = new BigDecimal(getJumlah());
			trxInDtl.setSubtotalHargaMasuk(tempJumlah);
		}
	}
	
	@Command("tampilStok")
	@NotifyChange({"stok"})
	public void tampilStok(){
		if(mstBarang.getKodeBarang()!=null){
			setStok(mstBarang.getStokBarang().intValue());
		}
	}
	
	@Command("tampilkodeSupplier")
	@NotifyChange({"trxInHdr"})
	public void tampilkodeSupplier(){
		if(mstSupplier.getKodeSupplier()!=null){
			trxInHdr.setKodeSupplier(mstSupplier.getKodeSupplier());
		}
	}
	
	@Command("print")
	public void print() {
		Clients.evalJavaScript("window.open('"
				+ "http://localhost:8080/boot-report/frameset?__report=/report/transaksiMasuk.rptdesign&prLink=http://localhost:8080/boot-core/trxindtl/getByInv/"
				+ trxInHdr.getNoInvoice()
				+ "&prLink1=http://localhost:8080/boot-core/trxinhdr/getOneList/"
				+ trxInHdr.getNoInvoice()
				+ "','','top=0,left=0,height=800,width=1024,scrollbars=1,resizable=1')");
	}
	
	@Command("backDetail")
	@NotifyChange("statusPopUp")
	public void backDetail() {
		setStatusPopUp(false);
	}
	
	public String getKodebarang() {
		return kodebarang;
	}

	public void setKodebarang(String kodebarang) {
		this.kodebarang = kodebarang;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		TrMasukEditVmd.logger = logger;
	}

	public List<TrxInDtlDto> getListIndexDetail() {
		return listIndexDetail;
	}

	public void setListIndexDetail(List<TrxInDtlDto> listIndexDetail) {
		this.listIndexDetail = listIndexDetail;
	}

	public List<TrxInHdrDto> getListIndexHeader() {
		return listIndexHeader;
	}

	public void setListIndexHeader(List<TrxInHdrDto> listIndexHeader) {
		this.listIndexHeader = listIndexHeader;
	}

	public List<TrxInDtlDto> getListDetail() {
		return listDetail;
	}

	public void setListDetail(List<TrxInDtlDto> listDetail) {
		this.listDetail = listDetail;
	}

	public TrxInDtlDto getTrxInDtl() {
		return trxInDtl;
	}

	public void setTrxInDtl(TrxInDtlDto trxInDtl) {
		this.trxInDtl = trxInDtl;
	}

	public List<MstBarangDto> getListBarang() {
		return listBarang;
	}

	public void setListBarang(List<MstBarangDto> listBarang) {
		this.listBarang = listBarang;
	}

	public MstBarangDto getMstBarangDto() {
		return mstBarangDto;
	}

	public void setMstBarangDto(MstBarangDto mstBarangDto) {
		this.mstBarangDto = mstBarangDto;
	}

	public MstBarangDto getMstBarang() {
		return mstBarang;
	}

	public void setMstBarang(MstBarangDto mstBarang) {
		this.mstBarang = mstBarang;
	}

	public TrxInHdrDto getTrxInHdr() {
		return trxInHdr;
	}

	public void setTrxInHdr(TrxInHdrDto trxInHdr) {
		this.trxInHdr = trxInHdr;
	}

	public boolean isStatusPopUp() {
		return statusPopUp;
	}

	public void setStatusPopUp(boolean statusPopUp) {
		this.statusPopUp = statusPopUp;
	}

	public String getWS_URL() {
		return WS_URL;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public NumberFormat getFrmt() {
		return frmt;
	}

	public int getIndexDtl() {
		return indexDtl;
	}

	public void setIndexDtl(int indexDtl) {
		this.indexDtl = indexDtl;
	}

	public String getIdDetail() {
		return idDetail;
	}

	public void setIdDetail(String idDetail) {
		this.idDetail = idDetail;
	}

	public int getJumlah() {
		return jumlah;
	}

	public void setJumlah(int jumlah) {
		this.jumlah = jumlah;
	}

	public int getStok() {
		return stok;
	}

	public void setStok(int stok) {
		this.stok = stok;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public int getHargaSatuan() {
		return hargaSatuan;
	}

	public void setHargaSatuan(int hargaSatuan) {
		this.hargaSatuan = hargaSatuan;
	}

	public List<MstSupplierDto> getListSupplier() {
		return listSupplier;
	}

	public void setListSupplier(List<MstSupplierDto> listSupplier) {
		this.listSupplier = listSupplier;
	}

	public MstSupplierDto getMstSupplier() {
		return mstSupplier;
	}

	public void setMstSupplier(MstSupplierDto mstSupplier) {
		this.mstSupplier = mstSupplier;
	}
}
