package com.letv.cdn.receiver.model;

/**
 * CDN节点与省份id对应关系model
 * 
 * @author sunyan
 *
 */
public class NPMappingModel {
	
	private String cdnid;
	private String nodename;
	private int active;
	private int nodetype;
	private String country;
	private String provinceid;
	private String areaid;
	private String nodeispid;
	private String nginxconn;
	private String nginxband;
	private String nowNodeband;
	private String maxNodeBand;
	private String usage;

	public String getCdnid() {
		return cdnid;
	}

	public void setCdnid(String cdnid) {
		this.cdnid = cdnid;
	}

	public String getNodename() {
		return nodename;
	}

	public void setNodename(String nodename) {
		this.nodename = nodename;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public int getNodetype() {
		return nodetype;
	}

	public void setNodetype(int nodetype) {
		this.nodetype = nodetype;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvinceid() {
		return provinceid;
	}

	public void setProvinceid(String provinceid) {
		this.provinceid = provinceid;
	}

	public String getAreaid() {
		return areaid;
	}

	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}

	public String getNodeispid() {
		return nodeispid;
	}

	public void setNodeispid(String nodeispid) {
		this.nodeispid = nodeispid;
	}

	public String getNginxconn() {
		return nginxconn;
	}

	public void setNginxconn(String nginxconn) {
		this.nginxconn = nginxconn;
	}

	public String getNginxband() {
		return nginxband;
	}

	public void setNginxband(String nginxband) {
		this.nginxband = nginxband;
	}

	public String getNowNodeband() {
		return nowNodeband;
	}

	public void setNowNodeband(String nowNodeband) {
		this.nowNodeband = nowNodeband;
	}

	public String getMaxNodeBand() {
		return maxNodeBand;
	}

	public void setMaxNodeBand(String maxNodeBand) {
		this.maxNodeBand = maxNodeBand;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

}
