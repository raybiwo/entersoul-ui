package vmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.dto.DeploymentDto;
import util.BaseUri;

public class DeploymentEditVmd {
	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	private BaseUri uri = new BaseUri();
	private final String WS_URL = uri.BaseUriParam();
	
	private DeploymentDto deploymentDto = new DeploymentDto();

	public DeploymentDto getDeploymentDto() {
		return deploymentDto;
	}

	public void setDeploymentDto(DeploymentDto deploymentDto) {
		this.deploymentDto = deploymentDto;
	}
}
