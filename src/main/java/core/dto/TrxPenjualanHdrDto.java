package core.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class TrxPenjualanHdrDto {
	private String notaPenjualan;
	private BigDecimal hargaTotal;
	private String idKaryawan;
	private String namaKaryawan;
	private Date tanggalTransaksi;
	private List<TrxPenjualanDtlDto> detail;
	private List<MstBarangDto> barang;
	
	
	
	public List<MstBarangDto> getBarang() {
		return barang;
	}
	public void setBarang(List<MstBarangDto> barang) {
		this.barang = barang;
	}
	public List<TrxPenjualanDtlDto> getDetail() {
		return detail;
	}
	public void setDetail(List<TrxPenjualanDtlDto> detail) {
		this.detail = detail;
	}
	public String getNotaPenjualan() {
		return notaPenjualan;
	}
	public void setNotaPenjualan(String notaPenjualan) {
		this.notaPenjualan = notaPenjualan;
	}
	public BigDecimal getHargaTotal() {
		return hargaTotal;
	}
	public void setHargaTotal(BigDecimal hargaTotal) {
		this.hargaTotal = hargaTotal;
	}
	public String getIdKaryawan() {
		return idKaryawan;
	}
	public void setIdKaryawan(String idKaryawan) {
		this.idKaryawan = idKaryawan;
	}
	public String getNamaKaryawan() {
		return namaKaryawan;
	}
	public void setNamaKaryawan(String namaKaryawan) {
		this.namaKaryawan = namaKaryawan;
	}
	public Date getTanggalTransaksi() {
		return tanggalTransaksi;
	}
	public void setTanggalTransaksi(Date tanggalTransaksi) {
		this.tanggalTransaksi = tanggalTransaksi;
	}
}
