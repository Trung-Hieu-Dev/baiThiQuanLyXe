import java.util.ArrayList;
import java.util.List;

public class quanly_XE {
  List<XE> dsXe = new ArrayList<>();

  public void themXe(XE xe) {
    if (dsXe.size() <= 0) {
      dsXe.add(xe);
    } else {
      for (int i = 0; i < dsXe.size(); i++) {
        if (!dsXe.get(i).getMaXe().equals(xe.getMaXe())) {
          dsXe.add(xe);
        }
      }
    }
  }

  public void xoaXe(String maXe) {
    for (int i = 0; i < dsXe.size(); i++) {
      if (dsXe.get(i).getMaXe().equals(maXe)) {
        dsXe.remove(i);
      }
    }
  }
}
