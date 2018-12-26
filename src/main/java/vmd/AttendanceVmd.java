package vmd;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;

import core.dto.AttendanceDto;
import util.BaseUri;
import util.JsonUtil;
import util.RestResponse;


@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AttendanceVmd {
	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	private BaseUri uri = new BaseUri();
	private final String WS_URL = uri.BaseUriParam();
	
	private AttendanceDto attendanceDto = new AttendanceDto();
	private List<AttendanceDto> listAttendanceDto = new ArrayList<>();
	
	
	@Init
	public void load(){
		String uri = WS_URL + "/attendance/all";
		RestResponse restResponse = new RestResponse();
		BaseVmd bs = new BaseVmd();
		
		try {
			restResponse = bs.callWs(uri,null,HttpMethod.GET);
			System.out.println("check response : "+restResponse.getContents());
			listAttendanceDto = JsonUtil.mapJsonToListObject(restResponse.getContents(), AttendanceDto.class);
		} catch (Exception e) {
			System.out.println("Unable to convert JSON!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		//setIndexHdr(listMemberDto.size()+1);
	}
	
	
	public AttendanceDto getAttendanceDto() {
		return attendanceDto;
	}
	public void setMemberDto(AttendanceDto attendanceDto) {
		this.attendanceDto = attendanceDto;
	}
	public List<AttendanceDto> getListAttendanceDto() {
		return listAttendanceDto;
	}
	public void setListMemberDto(List<AttendanceDto> listAttendanceDto) {
		this.listAttendanceDto = listAttendanceDto;
	}
	
	
}
