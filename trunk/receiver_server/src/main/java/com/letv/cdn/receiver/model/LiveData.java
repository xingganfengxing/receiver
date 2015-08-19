package com.letv.cdn.receiver.model;
public class LiveData{
    String ptime;
    Long visit;
    Long bw;
    
    public LiveData(String ptime,Long visit, Long bw) {
        this.ptime = ptime;
        this.visit = visit;
        this.bw = bw;
    }
    
    public LiveData(Long visit, Long bw) {
        this.visit = visit;
        this.bw = bw;
    }
    
    public int compareBw(Long ob) throws Exception {
        if (ob != null && this.bw != null) {
            return bw.compareTo(ob);
        } else {
            throw new Exception("参数为NULL");
        }
    }
    public int compareVisit(Long ob) throws Exception {
        if (ob != null && this.visit != null) {
            return visit.compareTo(ob);
        } else {
            throw new Exception("参数为NULL");
        }
    }
    public Long getVisit() {
        return visit;
    }
    public void setVisit(Long visit) {
        this.visit = visit;
    }
    public Long getBw() {
        return bw;
    }
    public void setBw(Long bw) {
        this.bw = bw;
    }
    
    
    
    
    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }
    
    public String toString(){
        return this.ptime+"\t"+this.visit+"\t"+this.bw;
    }

//    public static void main(String[] args) {
//          try {
//            Long ob = 100L;
//            
//              LiveData live = new LiveData(24L,23L);
//              int i =   live.compareBw(ob);
//             System.out.println(i);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        
//    }
}
