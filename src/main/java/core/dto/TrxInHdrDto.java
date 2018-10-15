package core.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class TrxInHdrDto {
	private String noInvoice;
	private String kodeSupplier;
	private String namaSupplier;
	private Date tglKeluar;
	private BigDecimal totalHargaMasuk;
	private List<TrxInDtlDto> detail;
	private List<MstBarangDto> barang;
	
	public List<TrxInDtlDto> getDetail() {
		return detail;
	}
	public void setDetail(List<TrxInDtlDto> detail) {
		this.detail = detail;
	}
	public List<MstBarangDto> getBarang() {
		return barang;
	}
	public void setBarang(List<MstBarangDto> barang) {
		this.barang = barang;
	}
	public String getNoInvoice() {
		return noInvoice;
	}
	public void setNoInvoice(String noInvoice) {
		this.noInvoice = noInvoice;
	}
	public String getKodeSupplier() {
		return kodeSupplier;
	}
	public void setKodeSupplier(String kodeSupplier) {
		this.kodeSupplier = kodeSupplier;
	}
	public String getNamaSupplier() {
		return namaSupplier;
	}
	public void setNamaSupplier(String namaSupplier) {
		this.namaSupplier = namaSupplier;
	}
	public Date getTglKeluar() {
		return tglKeluar;
	}
	public void setTglKeluar(Date tglKeluar) {
		this.tglKeluar = tglKeluar;
	}
	public BigDecimal getTotalHargaMasuk() {
		return totalHargaMasuk;
	}
	public void setTotalHargaMasuk(BigDecimal totalHargaMasuk) {
		this.totalHargaMasuk = totalHargaMasuk;
	}
}
