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
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zul.Include;
import org.zkoss.zul.Messagebox;

import core.dto.UsersDto;
import util.BaseUri;
import util.JsonUtil;
import util.RestResponse;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class UsersVmd {
	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	private BaseUri uri = new BaseUri();
	private final String WS_URL = uri.BaseUriParam();
	private final NumberFormat frmt = new DecimalFormat("0000");
	
	SimpleDateFormat formatter = new SimpleDateFormat("ddMMyy");
	Date tanggal = new Date();
	
	private UsersDto usersDto = new UsersDto();
	private List<UsersDto> listUsersDto = new ArrayList<>();
	
	private boolean readonly = false;
	
	private String noNota;
	
	private int indexHdr=0;
	
	@Init
	public void load(){
		String uri = WS_URL + "/users/all";
		RestResponse restResponse = new RestResponse();
		BaseVmd bs = new BaseVmd();
		
		restResponse = bs.callWs(uri,null,HttpMethod.GET);
		try {
			listUsersDto = JsonUtil.mapJsonToListObject(restResponse.getContents(), UsersDto.class);
		} catch (Exception e) {
			System.out.println("Unable to convert JSON!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		setIndexHdr(listUsersDto.size()+1);
	}
	
	@Command("edit")
	@NotifyChange({"includeSrc","p"})
	public void edit(){
		if (usersDto.getNik()==null) {
			Messagebox.show("Pilih data yang akan diedit");
		} else {
			Sessions.getCurrent().setAttribute("header", usersDto);
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/transaksi/penjualan/penjualanedit.zul");
		}
	}

	public int getIndexHdr() {
		return indexHdr;
	}

	public void setIndexHdr(int indexHdr) {
		this.indexHdr = indexHdr;
	}

	public UsersDto getUsersDto() {
		return usersDto;
	}

	public void setUsersDto(UsersDto usersDto) {
		this.usersDto = usersDto;
	}

	public List<UsersDto> getListUsersDto() {
		return listUsersDto;
	}

	public void setListUsersDto(List<UsersDto> listUsersDto) {
		this.listUsersDto = listUsersDto;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public String getNoNota() {
		return noNota;
	}

	public void setNoNota(String noNota) {
		this.noNota = noNota;
	}

	public NumberFormat getFrmt() {
		return frmt;
	}

}
