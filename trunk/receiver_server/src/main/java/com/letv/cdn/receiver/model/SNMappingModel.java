package com.letv.cdn.receiver.model;

/**
 * serverip与CDN节点id对应关系model
 * 
 * @author sunyan
 *
 */
public class SNMappingModel{
    
    private String host0;
    private String host1;
    private String host2;// serverip
    private String cdnid; // cdn编号
    private String ename; // cdn节点名称
    private String cname;
    private String diskid;
    private int active;
    
    public String getHost0() {
    
        return host0;
    }
    
    public String getHost1() {
    
        return host1;
    }
    
    public void setHost1(String host1) {
    
        this.host1 = host1;
    }
    
    public String getHost2() {
    
        return host2;
    }
    
    public void setHost2(String host2) {
    
        this.host2 = host2;
    }
    
    public void setHost0(String host0) {
    
        this.host0 = host0;
    }
    
    public String getCdnid() {
    
        return cdnid;
    }
    
    public void setCdnid(String cdnid) {
    
        this.cdnid = cdnid;
    }
    
    public String getEname() {
    
        return ename;
    }
    
    public void setEname(String ename) {
    
        this.ename = ename;
    }
    
    public String getCname() {
    
        return cname;
    }
    
    public void setCname(String cname) {
    
        this.cname = cname;
    }
    
    public String getDiskid() {
    
        return diskid;
    }
    
    public void setDiskid(String diskid) {
    
        this.diskid = diskid;
    }
    
    public int getActive() {
    
        return active;
    }
    
    public void setActive(int active) {
    
        this.active = active;
    }
    
}
