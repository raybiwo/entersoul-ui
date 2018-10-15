package vmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zul.Messagebox;

import core.dto.MstLoginDto;
import util.RestResponse;
import util.BaseUri;
import util.JsonUtil;
import vmd.BaseVmd;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class LoginVmd {
	private String username;
	private String password;
	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	private BaseUri uri = new BaseUri();
	private final String WS_URL = uri.BaseUriParam();

	@Command("login")
	@NotifyChange({ "username", "password" })
	public void login() {
		
		if (username != null && password != null) {
			MstLoginDto mstLoginDto = new MstLoginDto();
			mstLoginDto.setUsername(username);
			mstLoginDto.setPassword(password);
			String uri = WS_URL + "/login/login";
			
			RestResponse restResponse = new RestResponse();
			BaseVmd bs = new BaseVmd();
			restResponse = bs.callWs(uri, mstLoginDto, HttpMethod.POST);
			Sessions.getCurrent().setAttribute("user", mstLoginDto);
			
			if (restResponse.getContents() == null) {
				Messagebox.show("Password dan username salah! coba ingat ingat lagi");
				setUsername(null);
				setPassword(null);
			} else {
				try {
					mstLoginDto = JsonUtil.mapJsonToSingleObject(
							restResponse.getContents(), MstLoginDto.class);
				} catch (Exception e) {
					System.out.println("Unable to Convert JSON!");
					logger.error(e.getMessage());
					e.printStackTrace();
				}
				Sessions.getCurrent().setAttribute("user", mstLoginDto);
				Executions.sendRedirect("/index.zul");
			}
		} else {
			Messagebox.show("Harap diisi terlebih dahulu");
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
