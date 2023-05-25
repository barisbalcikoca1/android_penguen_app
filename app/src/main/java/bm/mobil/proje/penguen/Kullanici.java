package bm.mobil.proje.penguen;

public class Kullanici implements Comparable<Kullanici>{
    private  String j_ad;
    private String j_mail;
    private int j_skor;

    public Kullanici(){}
    public Kullanici(String ad, String mail, int skor){
        j_ad=ad;
        j_mail=mail;
        j_skor=skor;
    }
    public String getJ_ad() { return j_ad; }
    public void setJ_ad(String adi) {
        this.j_ad = adi;
    }
    public String getJ_mail() { return j_mail; }
    public void setJ_mail(String mail) {
        this.j_mail = mail;
    }
    public int getJ_skor() { return j_skor; }
    public void setJ_skor(int skor) {
        this.j_skor = skor;
    }

    @Override
    public int compareTo(Kullanici kullanici) {
        if(this.j_skor>kullanici.getJ_skor())
            return -1;
        else if(kullanici.getJ_skor()>this.j_skor)
            return 1;
        else
            return 0;
    }
}
