package core.dto;

public class MstLoginDto {
	private String idKaryawan;
	private String password;
	private String username;
	private String namaKaryawan;
	public String getNamaKaryawan() {
		return namaKaryawan;
	}
	public void setNamaKaryawan(String namaKaryawan) {
		this.namaKaryawan = namaKaryawan;
	}
	public String getIdKaryawan() {
		return idKaryawan;
	}
	public void setIdKaryawan(String idKaryawan) {
		this.idKaryawan = idKaryawan;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
