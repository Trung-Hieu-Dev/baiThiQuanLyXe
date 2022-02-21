public class XE {
  private String maXe;
  private String tenXe;
  private String nhaSanXuat;
  private int soLuong;

  public XE(String maXe, String tenXe, String nhaSanXuat, int soLuong) {
    this.maXe = maXe;
    this.tenXe = tenXe;
    this.nhaSanXuat = nhaSanXuat;
    this.soLuong = soLuong;
  }

  public String getMaXe() {
    return maXe;
  }

  public void setMaXe(String maXe) {
    this.maXe = maXe;
  }

  public String getTenXe() {
    return tenXe;
  }

  public void setTenXe(String tenXe) {
    this.tenXe = tenXe;
  }

  public String getNhaSanXuat() {
    return nhaSanXuat;
  }

  public void setNhaSanXuat(String nhaSanXuat) {
    this.nhaSanXuat = nhaSanXuat;
  }

  public int getSoLuong() {
    return soLuong;
  }

  public void setSoLuong(int soLuong) {
    this.soLuong = soLuong;
  }
}