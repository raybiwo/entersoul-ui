package core.dto;

import java.math.BigDecimal;

public class TrxPenjualanDtlDto {
	private String idDetail;
	private String kodeDetail;
	
	private String kodeBarang;
	private String namaBarang;
	
	private String notaPenjualan;
	private String noNota;
	
	private BigDecimal diskon;
	private BigDecimal harga;
	private BigDecimal jumlah;
	private BigDecimal quantity;
	
	public String getKodeDetail() {
		return kodeDetail;
	}
	public void setKodeDetail(String kodeDetail) {
		this.kodeDetail = kodeDetail;
	}
	public String getNamaBarang() {
		return namaBarang;
	}
	public void setNamaBarang(String namaBarang) {
		this.namaBarang = namaBarang;
	}
	public String getNoNota() {
		return noNota;
	}
	public void setNoNota(String noNota) {
		this.noNota = noNota;
	}
	public String getIdDetail() {
		return idDetail;
	}
	public void setIdDetail(String idDetail) {
		this.idDetail = idDetail;
	}
	public String getKodeBarang() {
		return kodeBarang;
	}
	public void setKodeBarang(String kodeBarang) {
		this.kodeBarang = kodeBarang;
	}
	public String getNotaPenjualan() {
		return notaPenjualan;
	}
	public void setNotaPenjualan(String notaPenjualan) {
		this.notaPenjualan = notaPenjualan;
	}
	public BigDecimal getDiskon() {
		return diskon;
	}
	public void setDiskon(BigDecimal diskon) {
		this.diskon = diskon;
	}
	public BigDecimal getHarga() {
		return harga;
	}
	public void setHarga(BigDecimal harga) {
		this.harga = harga;
	}
	public BigDecimal getJumlah() {
		return jumlah;
	}
	public void setJumlah(BigDecimal jumlah) {
		this.jumlah = jumlah;
	}
	public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
}
