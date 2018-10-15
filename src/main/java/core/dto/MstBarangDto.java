package core.dto;

import java.math.BigDecimal;

public class MstBarangDto {
	private String kodeBarang;
	private String namaBarang;
	
	private String kodeJenisBarang;
	private String namaJenisBarang;
	
	private String kodeMerk;
	private String namaMerk;
	
	private String kodeSupplier;
	private String namaSupplier;
	
	private BigDecimal stokBarang;

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

	public String getKodeJenisBarang() {
		return kodeJenisBarang;
	}

	public void setKodeJenisBarang(String kodeJenisBarang) {
		this.kodeJenisBarang = kodeJenisBarang;
	}

	public String getNamaJenisBarang() {
		return namaJenisBarang;
	}

	public void setNamaJenisBarang(String namaJenisBarang) {
		this.namaJenisBarang = namaJenisBarang;
	}

	public String getKodeMerk() {
		return kodeMerk;
	}

	public void setKodeMerk(String kodeMerk) {
		this.kodeMerk = kodeMerk;
	}

	public String getNamaMerk() {
		return namaMerk;
	}

	public void setNamaMerk(String namaMerk) {
		this.namaMerk = namaMerk;
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

	public BigDecimal getStokBarang() {
		return stokBarang;
	}

	public void setStokBarang(BigDecimal stokBarang) {
		this.stokBarang = stokBarang;
	}
}
