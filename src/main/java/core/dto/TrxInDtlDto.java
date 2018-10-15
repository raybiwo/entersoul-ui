package core.dto;

import java.math.BigDecimal;

public class TrxInDtlDto {
	private String kodeDetailMasuk;
	private String noInvoice;
	private String kodeBarang;
	private String namaBarang;
	private BigDecimal qtyMasuk;
	private BigDecimal subtotalHargaMasuk;
	public String getKodeDetailMasuk() {
		return kodeDetailMasuk;
	}
	public void setKodeDetailMasuk(String kodeDetailMasuk) {
		this.kodeDetailMasuk = kodeDetailMasuk;
	}
	public String getNoInvoice() {
		return noInvoice;
	}
	public void setNoInvoice(String noInvoice) {
		this.noInvoice = noInvoice;
	}
	public String getKodeBarang() {
		return kodeBarang;
	}
	public void setKodeBarang(String kodeBarang) {
		this.kodeBarang = kodeBarang;
	}
	public String getNamaBarang() {
		return namaBarang;
	}
	public void setNamaBarang(String namaBarang) {
		this.namaBarang = namaBarang;
	}
	public BigDecimal getQtyMasuk() {
		return qtyMasuk;
	}
	public void setQtyMasuk(BigDecimal qtyMasuk) {
		this.qtyMasuk = qtyMasuk;
	}
	public BigDecimal getSubtotalHargaMasuk() {
		return subtotalHargaMasuk;
	}
	public void setSubtotalHargaMasuk(BigDecimal subtotalHargaMasuk) {
		this.subtotalHargaMasuk = subtotalHargaMasuk;
	}
}
