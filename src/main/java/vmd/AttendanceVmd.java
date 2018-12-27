package vmd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Fileupload;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;

import core.dto.AttendanceDto;
import core.dto.MemberDto;
import util.BaseUri;
import util.JsonUtil;
import util.RestResponse;


@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AttendanceVmd {
	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	private BaseUri uri = new BaseUri();
	private final String WS_URL = uri.BaseUriParam();
	
	private AttendanceDto attendanceDto = new AttendanceDto();
	private AttendanceDto attendanceDto1 = new AttendanceDto();
	private List<AttendanceDto> listAttendanceDto = new ArrayList<>();
	
	private MemberDto memberDto = new MemberDto();
	private List<MemberDto> listMemberDto = new ArrayList<>();
	
	private String username;
	
	@Init
	public void load(){
		String uri = WS_URL + "/attendance/allWithMember";
		RestResponse restResponse = new RestResponse();
		BaseVmd bs = new BaseVmd();
		
		String uriMember = WS_URL + "/member/allWithMember";
		RestResponse restResponseMember = new RestResponse();
		BaseVmd bsMember = new BaseVmd();
//		
		try {
			restResponse = bs.callWs(uri,null,HttpMethod.GET);
			System.out.println("check response : "+restResponse.getContents());
			listAttendanceDto = JsonUtil.mapJsonToListObject(restResponse.getContents(), AttendanceDto.class);
			
			restResponseMember = bsMember.callWs(uriMember,null,HttpMethod.GET);
			System.out.println("check response : "+restResponseMember.getContents());
			listMemberDto = JsonUtil.mapJsonToListObject(restResponseMember.getContents(), MemberDto.class);
			
		} catch (Exception e) {
			System.out.println("Unable to convert JSON!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		//setIndexHdr(listMemberDto.size()+1);
	}
	
	
	@Command("select")
	@NotifyChange("listAttendanceDto")
	public void select() {
		Clients.showNotification(username, Clients.NOTIFICATION_TYPE_INFO, null, null, 2000);
		String uri = WS_URL + "/attendance/allWithName";
		RestResponse restResponse = new RestResponse();
		BaseVmd bs = new BaseVmd();
		attendanceDto1.setNik(username);
		
		try {
			restResponse = bs.callWs(uri,attendanceDto1,HttpMethod.POST);
			System.out.println("check response : "+restResponse.getContents());
			listAttendanceDto = JsonUtil.mapJsonToListObject(restResponse.getContents(), AttendanceDto.class);
			
//			restResponseMember = bsMember.callWs(uriMember,null,HttpMethod.GET);
//			System.out.println("check response : "+restResponseMember.getContents());
//			listMemberDto = JsonUtil.mapJsonToListObject(restResponseMember.getContents(), MemberDto.class);
			
		} catch (Exception e) {
			System.out.println("Unable to convert JSON!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
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
	public void setListAttendanceDto(List<AttendanceDto> listAttendanceDto) {
		this.listAttendanceDto = listAttendanceDto;
	}


	public MemberDto getMemberDto() {
		return memberDto;
	}


	public void setMemberDto(MemberDto memberDto) {
		this.memberDto = memberDto;
	}


	public List<MemberDto> getListMemberDto() {
		return listMemberDto;
	}


	public void setListMemberDto(List<MemberDto> listMemberDto) {
		this.listMemberDto = listMemberDto;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
