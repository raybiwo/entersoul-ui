package vmd;

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

import core.dto.MstBarangDto;
import core.dto.MstJenisBarangDto;
import core.dto.MstMerkBarangDto;
import core.dto.MstSupplierDto;
import pagevmd.NavigationVmd;
import util.JsonUtil;
import util.RestResponse;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class BarangEditVmd extends NavigationVmd{
	private List<MstSupplierDto> listSupplier = new ArrayList<>();
	private List<MstJenisBarangDto> listJenisBarang = new ArrayList<>();
	private List<MstMerkBarangDto> listMerkBarang = new ArrayList<>();
	
	private MstMerkBarangDto mstMerkBarang = new MstMerkBarangDto();
	private MstSupplierDto mstSupplier = new MstSupplierDto();
	private MstJenisBarangDto mstJenisBarang = new MstJenisBarangDto();
	private MstBarangDto mstBarang = new MstBarangDto();
	
	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	private final String WS_URL = "http://localhost:8080/boot-ui";
	
	@Init
	public void load(){
		mstBarang = (MstBarangDto) Sessions.getCurrent().getAttribute("obj");
		String uriJenisBarangAll = WS_URL + "/jenisbarang/all";
		String uriSupplierAll = WS_URL + "/supplier/all";
		String uriMerkAll = WS_URL + "/merkbarang/all";
		
		RestResponse restResponse = new RestResponse();
		
		BaseVmd supplierBs = new BaseVmd();
		restResponse = supplierBs.callWs(uriSupplierAll, null, HttpMethod.GET);
		try {
			listSupplier = JsonUtil.mapJsonToListObject(restResponse.getContents(), MstSupplierDto.class);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		BaseVmd jenisBarangBs = new BaseVmd();
		restResponse = jenisBarangBs.callWs(uriJenisBarangAll, null, HttpMethod.GET);
		try {
			listJenisBarang = JsonUtil.mapJsonToListObject(restResponse.getContents(), MstJenisBarangDto.class);
			for (MstJenisBarangDto qr : listJenisBarang) {
				System.out.println("=========="+qr.getKodeJenisBarang());
				System.out.println("=========="+qr.getNamaJenisBarang());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		BaseVmd MerkBs = new BaseVmd();
		restResponse = MerkBs.callWs(uriMerkAll, null, HttpMethod.GET);
		try {
			listMerkBarang = JsonUtil.mapJsonToListObject(restResponse.getContents(), MstMerkBarangDto.class);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@Command("back")
	@NotifyChange({"includeSrc","p"})
	public void back(){
		Sessions.getCurrent().setAttribute("obj", mstBarang);
		Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
		inc.setSrc("/master/barang/barang.zul");
	}
	
	@Command("save")
	public void save(){
		if (mstSupplier.getKodeSupplier()!=null && mstJenisBarang.getKodeJenisBarang()!=null && mstMerkBarang.getKodeMerk()!=null) {
			mstBarang.setKodeSupplier(mstSupplier.getKodeSupplier());
			mstBarang.setKodeJenisBarang(mstJenisBarang.getKodeJenisBarang());
			mstBarang.setKodeMerk(mstMerkBarang.getKodeMerk());
		}

		if (mstBarang.getKodeBarang()!=null) {
			String urisave = WS_URL + "/barang";
			RestResponse restResponse = new RestResponse();
			BaseVmd bs = new BaseVmd();
			restResponse = bs.callWs(urisave, mstBarang, HttpMethod.POST);
			Clients.showNotification(restResponse.getMessage(), Clients.NOTIFICATION_TYPE_INFO, null, null, 1500);
			Sessions.getCurrent().setAttribute("obj", mstBarang);
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/master/barang/barang.zul");
		}
	}
	
	public List<MstMerkBarangDto> getListMerkBarang() {
		return listMerkBarang;
	}

	public void setListMerkBarang(List<MstMerkBarangDto> listMerkBarang) {
		this.listMerkBarang = listMerkBarang;
	}

	public MstMerkBarangDto getMstMerkBarang() {
		return mstMerkBarang;
	}

	public void setMstMerkBarang(MstMerkBarangDto mstMerkBarang) {
		this.mstMerkBarang = mstMerkBarang;
	}

	public List<MstJenisBarangDto> getListJenisBarang() {
		return listJenisBarang;
	}

	public void setListJenisBarang(List<MstJenisBarangDto> listJenisBarang) {
		this.listJenisBarang = listJenisBarang;
	}

	public MstJenisBarangDto getMstJenisBarang() {
		return mstJenisBarang;
	}

	public void setMstJenisBarang(MstJenisBarangDto mstJenisBarang) {
		this.mstJenisBarang = mstJenisBarang;
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
		BarangEditVmd.logger = logger;
	}
	public String getWS_URL() {
		return WS_URL;
	}
}
