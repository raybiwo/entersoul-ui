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
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Include;
//import org.zkoss.zul.Messagebox;

import org.zkoss.zul.Messagebox;

import core.dto.MstBarangDto;
import core.dto.MstLoginDto;
import core.dto.TrxPenjualanDtlDto;
import core.dto.TrxPenjualanHdrDto;
import pagevmd.NavigationVmd;
import util.JsonUtil;
import util.RestResponse;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class PenjualanEditVmd extends NavigationVmd {
	private final String WS_URL = "http://localhost:8080/boot-core";
	private final NumberFormat frmt = new DecimalFormat("0000");

	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);

	private List<TrxPenjualanDtlDto> listIndexDetail = new ArrayList<>();
	private List<TrxPenjualanHdrDto> listIndexHeader = new ArrayList<>();
	private List<TrxPenjualanDtlDto> listDetail = new ArrayList<>();
	private TrxPenjualanDtlDto trxPenjualanDtl = new TrxPenjualanDtlDto();

	private List<MstBarangDto> listBarang = new ArrayList<>();
	private MstBarangDto mstBarangDto = new MstBarangDto();
	private MstBarangDto msBarang = new MstBarangDto();

	private TrxPenjualanHdrDto trxPenjualanHdr = new TrxPenjualanHdrDto();

	private MstLoginDto mstLoginDto = new MstLoginDto();

	private boolean readonly = false;
	private boolean visible = true;
	private boolean statusPopUp = false;

	private String kodebarang;
	private String idDetail;

	private int jumlah;
	private int qty;
	private int diskon;
	private int stok = 0;
	private int hargaSatuan = 0;
	private int indexDtl = 0;

	@Init
	public void load() {

		trxPenjualanHdr = (TrxPenjualanHdrDto) Sessions.getCurrent()
				.getAttribute("header");

		mstLoginDto = (MstLoginDto) Sessions.getCurrent().getAttribute("user");

		// TrxPenjualanDtlDto trxPenjualanDtl = new TrxPenjualanDtlDto();
		String kode = trxPenjualanHdr.getNotaPenjualan();
		trxPenjualanHdr.setIdKaryawan(mstLoginDto.getIdKaryawan());

		String uriIndexDtl = WS_URL + "/penjualandtl/all";
		String uriDetail = WS_URL + "/penjualandtl/getByNota/" + kode;
		String uriBarang = WS_URL + "/barang/all";

		RestResponse restResponse = new RestResponse();
		BaseVmd bs = new BaseVmd();

		try {
			restResponse = bs.callWs(uriIndexDtl, null, HttpMethod.GET);
			listIndexDetail = JsonUtil.mapJsonToListObject(
					restResponse.getContents(), TrxPenjualanDtlDto.class);

			restResponse = bs.callWs(uriDetail, null, HttpMethod.GET);
			listDetail = JsonUtil.mapJsonToListObject(
					restResponse.getContents(), TrxPenjualanDtlDto.class);

			restResponse = bs.callWs(uriBarang, null, HttpMethod.GET);
			listBarang = JsonUtil.mapJsonToListObject(
					restResponse.getContents(), MstBarangDto.class);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		setIndexDtl(listIndexDetail.size() + 1);
	}

	@Command("back")
	public void back() {
		Include inc = (Include) Executions.getCurrent().getDesktop()
				.getPage("index").getFellow("mainInclude");
		inc.setSrc("/transaksi/penjualan/penjualan.zul");
	}

	@Command("save")
	@NotifyChange("trxPenjualanHdr")
	public void save() {
		trxPenjualanHdr.setIdKaryawan(mstLoginDto.getIdKaryawan());

		System.out.println("========= ID Karyawan   	========= : "
				+ trxPenjualanHdr.getIdKaryawan());
		System.out.println("========= Nama Karyawan 	========= :"
				+ trxPenjualanHdr.getNamaKaryawan());
		System.out.println("========= Nota Penjualan 	========= : "
				+ trxPenjualanHdr.getNotaPenjualan());
		System.out.println("========= Harga Total       ========= : "
				+ trxPenjualanHdr.getHargaTotal());
		System.out.println("========= Tanggal Transaksi ========= : "
				+ trxPenjualanHdr.getTanggalTransaksi() + "\n");

		if (trxPenjualanHdr.getNotaPenjualan() == null) {
			Messagebox.show("Tidak dapat disimpan, silahkan masukkan data!");
		} else {
			String uriSave = WS_URL + "/penjualanhdr";
			RestResponse restResponse = new RestResponse();
			BaseVmd bs = new BaseVmd();

			trxPenjualanHdr.setDetail(listDetail);
			trxPenjualanHdr.setBarang(listBarang);

			restResponse = bs.callWs(uriSave, trxPenjualanHdr, HttpMethod.POST);

			Clients.showNotification(restResponse.getMessage(),
					Clients.NOTIFICATION_TYPE_INFO, null, null, 1500);
			Include inc = (Include) Executions.getCurrent().getDesktop()
					.getPage("index").getFellow("mainInclude");
			inc.setSrc("/transaksi/penjualan/penjualan.zul");
		}
	}

	@Command("addDetail")
	@NotifyChange({ "statusPopUp", "trxPenjualanDtl", "jumlah", "mstBarang",
			"stok", "idDetail" })
	public void addDetail() {
		setStatusPopUp(true);
		trxPenjualanDtl = new TrxPenjualanDtlDto();

		setIdDetail("ID" + frmt.format(getIndexDtl()));

		setJumlah(0);
		setStok(0);
	}

	@Command("delDetail")
	@NotifyChange({ "trxPenjualanDtl", "listDetail" })
	public void delDetail() {
		if (trxPenjualanDtl.getIdDetail() == null) {
			Messagebox.show("Pilih data yang akan didelete");
		} else {
			listDetail.remove(trxPenjualanDtl);
		}
	}

	@Command("saveDetail")
	@NotifyChange({ "listDetail", "statusPopUp", "trxPenjualanHdr" })
	public void saveDetail() {
		if (msBarang.getKodeBarang() == null) {
			Messagebox
					.show("Tidak bisa disave, silahkan masukkan data terlebih dahulu");
		} else {
			trxPenjualanDtl.setKodeBarang(msBarang.getKodeBarang());
			trxPenjualanDtl.setNamaBarang(msBarang.getNamaBarang());
			trxPenjualanDtl.setIdDetail(getIdDetail());
			trxPenjualanDtl
					.setNotaPenjualan(trxPenjualanHdr.getNotaPenjualan());

			setKodebarang(trxPenjualanDtl.getKodeBarang());
			setQty(trxPenjualanDtl.getQuantity().intValue());
			System.out.println("kode barang : " + getKodebarang());
			System.out.println("kode barang : " + getQty());

			if (kodebarang != null) {
				for (MstBarangDto bar : listBarang) {
					if (getKodebarang() == bar.getKodeBarang()) {
						System.out.println("");
						setStok(bar.getStokBarang().intValue() - getQty());
						BigDecimal bigStok = new BigDecimal(stok);
						bar.setStokBarang(bigStok);
					} else {
						System.out.println("");
					}
				}
			}

			listDetail.add(trxPenjualanDtl);

			int hasil = 0;
			for (TrxPenjualanDtlDto detail : listDetail) {
				hasil = hasil + detail.getJumlah().intValue();

				/*
				 * System.out.println("========= ID detail   ========= : " +
				 * detail.getIdDetail());
				 * System.out.println("========= Kode Barang ========= :" +
				 * detail.getKodeBarang());
				 * System.out.println("========= Nama Barang ========= : " +
				 * detail.getNamaBarang());
				 * System.out.println("========= No nota     ========= : " +
				 * detail.getNoNota());
				 * System.out.println("========= Nota Penjualan ====== : " +
				 * detail.getNotaPenjualan());
				 * System.out.println("========= Diskon      ========= : " +
				 * detail.getDiskon());
				 * System.out.println("========= Quantity    ========= :" +
				 * detail.getQuantity());
				 * System.out.println("========= Jumlah      ========= : " +
				 * detail.getJumlah());
				 * System.out.println("========= Harga Satuan========= :" +
				 * detail.getHarga() + "\n");
				 */
			}

			/*
			 * for (MstBarangDto brg : listBarang) {
			 * System.out.println("========= Kode Barang ========= : " +
			 * brg.getKodeBarang());
			 * System.out.println("========= Nama Barang ========= : " +
			 * brg.getNamaBarang());
			 * System.out.println("========= Kode Merk   ========= : " +
			 * brg.getKodeMerk());
			 * System.out.println("========= Nama Merk   ========= : " +
			 * brg.getNamaMerk());
			 * System.out.println("========= Kode Supplier  ====== : " +
			 * brg.getKodeSupplier());
			 * System.out.println("========= Nama Supplier======== : " +
			 * brg.getNamaSupplier());
			 * System.out.println("========= Jenis Barang========= : " +
			 * brg.getKodeJenisBarang());
			 * System.out.println("========= Stok Barang ========= : " +
			 * brg.getStokBarang() + "\n"); }
			 * 
			 * System.out.println("========= ID Karyawan   	========= : " +
			 * trxPenjualanHdr.getIdKaryawan());
			 * System.out.println("========= Nama Karyawan 	========= :" +
			 * trxPenjualanHdr.getNamaKaryawan());
			 * System.out.println("========= Nota Penjualan 	========= : " +
			 * trxPenjualanHdr.getNotaPenjualan());
			 * System.out.println("========= Harga Total       ========= : " +
			 * trxPenjualanHdr.getHargaTotal());
			 * System.out.println("========= Tanggal Transaksi ========= : " +
			 * trxPenjualanHdr.getTanggalTransaksi() + "\n");
			 */
			BigDecimal hargaTotal = new BigDecimal(hasil);
			trxPenjualanHdr.setHargaTotal(hargaTotal);
			setStatusPopUp(false);

			indexDtl = indexDtl + 1;
		}
	}

	@Command("total")
	@NotifyChange({ "statusPopUp", "jumlah" })
	public void total() {
		setStatusPopUp(true);
		int Quantity = trxPenjualanDtl.getQuantity().intValue();
		int stokbrg = msBarang.getStokBarang().intValue();

		if (Quantity > stokbrg) {
			Messagebox.show("Quantity tidak boleh lebih dari stok barang");
		} else if (Quantity == 0) {
			Messagebox.show("Stok Barang telah habis");
		}

		if (trxPenjualanDtl.getHarga() != null
				&& trxPenjualanDtl.getQuantity() != null
				&& trxPenjualanDtl.getDiskon() == null) {
			jumlah = trxPenjualanDtl.getHarga().intValue()
					* trxPenjualanDtl.getQuantity().intValue();
			BigDecimal tempJumlah = new BigDecimal(jumlah);
			trxPenjualanDtl.setJumlah(tempJumlah);
		} else if (trxPenjualanDtl.getHarga() != null
				&& trxPenjualanDtl.getQuantity() != null
				&& trxPenjualanDtl.getDiskon() != null) {
			jumlah = trxPenjualanDtl.getHarga().intValue()
					* trxPenjualanDtl.getQuantity().intValue()
					- (trxPenjualanDtl.getHarga().intValue()
							* trxPenjualanDtl.getQuantity().intValue()
							* trxPenjualanDtl.getDiskon().intValue() / 100);
			BigDecimal tempJumlah = new BigDecimal(jumlah);
			trxPenjualanDtl.setJumlah(tempJumlah);
		}

	}

	@Command("tampilStok")
	@NotifyChange({ "stok" })
	public void tampilStok() {
		if (msBarang.getKodeBarang() != null) {
			setStok(msBarang.getStokBarang().intValue());
		}
	}

	@Command("backDetail")
	@NotifyChange("statusPopUp")
	public void backDetail() {
		setStatusPopUp(false);
	}

	@Command("print")
	public void print() {
		Clients.evalJavaScript("window.open('"
				+ "http://localhost:8080/boot-report/frameset?__report=/report/trPenjualan.rptdesign&prLink=http://localhost:8080/boot-core/penjualandtl/getByNota/"
				+ trxPenjualanHdr.getNotaPenjualan()
				+ "&prLink1=http://localhost:8080/boot-core/penjualanhdr/getOneList/"
				+ trxPenjualanHdr.getNotaPenjualan()
				+ "','','top=0,left=0,height=800,width=1024,scrollbars=1,resizable=1')");
	}

	public NumberFormat getFrmt() {
		return frmt;
	}

	public String getIdDetail() {
		return idDetail;
	}

	public void setIdDetail(String idDetail) {
		this.idDetail = idDetail;
	}

	public List<TrxPenjualanDtlDto> getListIndexDetail() {
		return listIndexDetail;
	}

	public void setListIndexDetail(List<TrxPenjualanDtlDto> listIndexDetail) {
		this.listIndexDetail = listIndexDetail;
	}

	public int getIndexDtl() {
		return indexDtl;
	}

	public void setIndexDtl(int indexDtl) {
		this.indexDtl = indexDtl;
	}

	public int getHargaSatuan() {
		return hargaSatuan;
	}

	public void setHargaSatuan(int hargaSatuan) {
		this.hargaSatuan = hargaSatuan;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public String getKodebarang() {
		return kodebarang;
	}

	public void setKodebarang(String kodebarang) {
		this.kodebarang = kodebarang;
	}

	public MstBarangDto getMsBarang() {
		return msBarang;
	}

	public void setMsBarang(MstBarangDto msBarang) {
		this.msBarang = msBarang;
	}

	public int getStok() {
		return stok;
	}

	public void setStok(int stok) {
		this.stok = stok;
	}

	public MstLoginDto getMstLoginDto() {
		return mstLoginDto;
	}

	public void setMstLoginDto(MstLoginDto mstLoginDto) {
		this.mstLoginDto = mstLoginDto;
	}

	public int getDiskon() {
		return diskon;
	}

	public void setDiskon(int diskon) {
		this.diskon = diskon;
	}

	public int getJumlah() {
		return jumlah;
	}

	public void setJumlah(int jumlah) {
		this.jumlah = jumlah;
	}

	public static Logger getLogger() {
		return logger;
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

	public static void setLogger(Logger logger) {
		PenjualanEditVmd.logger = logger;
	}

	public List<TrxPenjualanDtlDto> getListDetail() {
		return listDetail;
	}

	public void setListDetail(List<TrxPenjualanDtlDto> listDetail) {
		this.listDetail = listDetail;
	}

	public TrxPenjualanDtlDto getTrxPenjualanDtl() {
		return trxPenjualanDtl;
	}

	public void setTrxPenjualanDtl(TrxPenjualanDtlDto trxPenjualanDtl) {
		this.trxPenjualanDtl = trxPenjualanDtl;
	}

	public TrxPenjualanHdrDto getTrxPenjualanHdr() {
		return trxPenjualanHdr;
	}

	public void setTrxPenjualanHdr(TrxPenjualanHdrDto trxPenjualanHdr) {
		this.trxPenjualanHdr = trxPenjualanHdr;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
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

	public List<TrxPenjualanHdrDto> getListIndexHeader() {
		return listIndexHeader;
	}

	public void setListIndexHeader(List<TrxPenjualanHdrDto> listIndexHeader) {
		this.listIndexHeader = listIndexHeader;
	}
}
