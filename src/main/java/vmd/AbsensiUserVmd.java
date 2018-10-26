package vmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Sessions;

import core.dto.UsersDto;
import util.BaseUri;

public class AbsensiUserVmd {
	
	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	private BaseUri uri = new BaseUri();
	private final String WS_URL = uri.BaseUriParam();
	
	private UsersDto usersDto = new UsersDto();
	
	@Init
	public void load() {
		usersDto = (UsersDto) Sessions.getCurrent()
				.getAttribute("header");
	}

}
