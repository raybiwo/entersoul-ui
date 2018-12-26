package vmd;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.select.annotation.VariableResolver;

import core.dto.MemberDto;
import pagevmd.NavigationVmd;


@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class MemberEditVmd extends NavigationVmd{

	private List<MemberDto> listMember = new ArrayList<>();
	private MemberDto memberDto = new MemberDto();
//	
//	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
//	private final String WS_URL = "http://localhost:8086/boot-ui";
//	
	
	
	public List<MemberDto> getListMember() {
		return listMember;
	}
	public void setListMember(List<MemberDto> listMember) {
		this.listMember = listMember;
	}
	public MemberDto getMemberDto() {
		return memberDto;
	}
	public void setMemberDto(MemberDto memberDto) {
		this.memberDto = memberDto;
	}
	
}
