import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

public class QuanLyXe extends JFrame {
  private JPanel bangDieuKhien;
  private JTextField maXe;
  private JTextField tenXe;
  private JTextField soLuong;
  private JButton themXe;
  private JButton xoaXe;
  private JButton luuXe;
  private JButton thoatChuongTrinh;
  private JButton xoaTrang;
  private JComboBox nhaSanXuat;
  private JTable hienThiDuLieu;
  private String nhaSanXuatDaChon;

  quanly_XE qlx = new quanly_XE();
  DefaultTableModel tableModel = (DefaultTableModel) hienThiDuLieu.getModel();
  InputVerifier validate = new ValidateInput();

  public QuanLyXe() {
    luuXe.setEnabled(false);
    xoaXe.setEnabled(false);
    taoBangHienThi();
    nhaSanXuat.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        nhaSanXuatDaChon = nhaSanXuat.getSelectedItem().toString();
      }
    });
    themXe.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String maXeThem = "";
        String tenXeThem = "";
        int soLuongThem = 0;
        String nhaSanXuatThem = nhaSanXuatDaChon;

        if (Objects.equals(nhaSanXuatDaChon, "Chọn nhà sản xuất") || nhaSanXuatDaChon == null || maXe == null || tenXe == null || soLuong == null) {
          JOptionPane.showMessageDialog(themXe, "Vui lòng điền đầy đủ thông tin.");
        } else {
          if (validate.verify(maXe)) {
            maXeThem = maXe.getText();
          } else {
            JOptionPane.showMessageDialog(themXe, "Mã xe không được là chữ cái.");
          }

          if (validate.verify(tenXe)) {
            JOptionPane.showMessageDialog(themXe, "Tên xe không được là chữ số.");
          } else {
            tenXeThem = tenXe.getText();
          }

          if (validate.verify(soLuong)) {
            soLuongThem = Integer.parseInt(soLuong.getText());
          } else {
            JOptionPane.showMessageDialog(themXe, "Số lượng không được là chữ cái.");
          }
        }

        if (validate.verify(maXe) && !validate.verify(tenXe) && validate.verify(soLuong)) {
          if (soLuongThem == 0) {
            JOptionPane.showMessageDialog(themXe, "Số lượng không được bằng 0.");
          } else {
            try {
              PreparedStatement stmt = ConnectDb.getMySQLConnection().prepareStatement("SELECT maXe FROM car " +
                  "WHERE maXe = ?");
              stmt.setLong(1, Long.parseLong(maXeThem));
              ResultSet rs = stmt.executeQuery();
              boolean isExist = false;
              while (rs.next()) {
                isExist = true;
              }

              if (!isExist) {
                XE xeThem = new XE(maXeThem, tenXeThem, nhaSanXuatThem, soLuongThem);

                qlx.themXe(xeThem);

                themXeVaoDb(xeThem);
              } else {
                JOptionPane.showMessageDialog(themXe, "Mã xe đã tồn tại.");
              }
            } catch (Exception ex) {
              ex.printStackTrace();
            }
          }
        }
      }
    });
    xoaTrang.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        maXe.setEnabled(true);
        themXe.setEnabled(true);
        xoaChuDaNhap();
      }
    });
    hienThiDuLieu.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        luuXe.setEnabled(true);
        xoaXe.setEnabled(true);
        maXe.setEnabled(false);
        themXe.setEnabled(false);
        int indexHangDaChon = hienThiDuLieu.getSelectedRow();

        maXe.setText(tableModel.getValueAt(indexHangDaChon, 0).toString());
        tenXe.setText(tableModel.getValueAt(indexHangDaChon, 1).toString());
        nhaSanXuat.setSelectedItem(tableModel.getValueAt(indexHangDaChon, 2).toString());
        soLuong.setText(tableModel.getValueAt(indexHangDaChon, 3).toString());
      }
    });
    thoatChuongTrinh.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int xacNhanThoat = JOptionPane.showConfirmDialog(xoaXe, "Bạn đang tiến hành thoát chương trình. Bạn có chắc chắn không?");
        if (xacNhanThoat == 0) {
          System.exit(0);
        }
      }
    });
    xoaXe.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int maXeCanXoa = Integer.parseInt(maXe.getText());
        int xacNhanXoa = JOptionPane.showConfirmDialog(xoaXe, "Bạn đang tiến hành xoá dữ liệu mã xe " + maXeCanXoa +
            ". Bạn có chắc chắn không?");
        if (xacNhanXoa == 0) {
          xoaDuLieuDb(maXeCanXoa);
        }
      }
    });
    luuXe.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int maXeCanSua = Integer.parseInt(maXe.getText());
        String tenXeCanSua = tenXe.getText();
        String soLuongCanSua = soLuong.getText();
        if (validate.verify(soLuong)) {
          soLuongCanSua = soLuong.getText();
          int xacNhanSua = JOptionPane.showConfirmDialog(luuXe,
              "Bạn đang tiến hành cập nhật dữ liệu mã xe " + maXeCanSua + ". Bạn có chắc chắn không?");
          if (xacNhanSua == 0) {
            if (Integer.parseInt(soLuongCanSua) != 0) {
              suaDuLieuDb(maXeCanSua, tenXeCanSua, Integer.parseInt(soLuongCanSua));
            } else {
              JOptionPane.showMessageDialog(luuXe, "Số lượng không được bằng 0.");
            }
          }
        } else {
          JOptionPane.showMessageDialog(luuXe, "Số lượng không được là chữ cái.");
        }
      }
    });
  }

  private void themXeVaoDb(XE xeCanThem) {
    try {
      String themXeStmt = "INSERT INTO car (maXe, tenXe, nhaSanXuat, soLuong) VALUES (?, ?, ?, ?)";

      PreparedStatement preparedStatement = ConnectDb.getMySQLConnection().prepareStatement(themXeStmt);

      preparedStatement.setString(1, xeCanThem.getMaXe());
      preparedStatement.setString(2, xeCanThem.getTenXe());
      preparedStatement.setString(3, xeCanThem.getNhaSanXuat());
      preparedStatement.setInt(4, xeCanThem.getSoLuong());
      preparedStatement.execute();

      ConnectDb.getMySQLConnection().close();

      capNhatBangHienThi();

      JOptionPane.showMessageDialog(themXe, "Đã thêm xe thành công!");

      xoaChuDaNhap();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static class ValidateInput extends InputVerifier {
    @Override
    public boolean verify(JComponent input) {
      String text = ((JTextField) input).getText();
      try {
        Integer.parseInt(text);
      } catch (NumberFormatException e) {
        return false;
      }
      return true;
    }
  }

  private void xoaChuDaNhap() {
    maXe.setText("");
    tenXe.setText("");
    nhaSanXuat.setSelectedIndex(0);
    soLuong.setText("");
  }

  private void capNhatBangHienThi() {
    try {
      Statement stmt = ConnectDb.getMySQLConnection().createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM car");

      tableModel.setRowCount(0);

      while (rs.next()) {
        String maXeHienThi = rs.getString("maXe").toString();
        String tenXeHienThi = rs.getString("tenXe").toString();
        String nhaSanXuatHienThi = rs.getString("nhaSanXuat").toString();
        String soLuongHienThi = String.valueOf(rs.getInt("soLuong"));

        String[] data = {maXeHienThi, tenXeHienThi, nhaSanXuatHienThi, soLuongHienThi};
        tableModel.addRow(data);
      }

      ConnectDb.getMySQLConnection().close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void taoBangHienThi() {
    try {
      Statement stmt = ConnectDb.getMySQLConnection().createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM car");

      tableModel.addColumn("Mã xe");
      tableModel.addColumn("Tên xe");
      tableModel.addColumn("Số lượng");
      tableModel.addColumn("Nhà sản xuất");

      while (rs.next()) {
        String maXeHienThi = rs.getString("maXe").toString();
        String tenXeHienThi = rs.getString("tenXe").toString();
        String nhaSanXuatHienThi = rs.getString("nhaSanXuat").toString();
        String soLuongHienThi = String.valueOf(rs.getInt("soLuong"));

        String[] data = {maXeHienThi, tenXeHienThi, nhaSanXuatHienThi, soLuongHienThi};
        tableModel.addRow(data);
      }

      ConnectDb.getMySQLConnection().close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void xoaDuLieuDb(int maXeCanXoa) {
    try {
      String xoaXeStmt = "DELETE FROM car WHERE maXe = ?";

      PreparedStatement preparedStatement = ConnectDb.getMySQLConnection().prepareStatement(xoaXeStmt);

      preparedStatement.setInt(1, maXeCanXoa);
      preparedStatement.execute();

      ConnectDb.getMySQLConnection().close();

      capNhatBangHienThi();

      JOptionPane.showMessageDialog(themXe, "Đã xoá thành công xe có mã số " + maXeCanXoa + "!");

      xoaChuDaNhap();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void suaDuLieuDb(int maXeCanSua, String tenXeCanSua, int soLuongCanSua) {
    try {
      String xoaXeStmt = "UPDATE car SET tenXe=?, soLuong=?, nhaSanXuat=? WHERE maXe=?";

      PreparedStatement preparedStatement = ConnectDb.getMySQLConnection().prepareStatement(xoaXeStmt);

      preparedStatement.setString(1, tenXeCanSua);
      preparedStatement.setInt(2, soLuongCanSua);
      preparedStatement.setString(3, nhaSanXuatDaChon);
      preparedStatement.setInt(4, maXeCanSua);
      preparedStatement.execute();

      ConnectDb.getMySQLConnection().close();

      capNhatBangHienThi();

      JOptionPane.showMessageDialog(themXe, "Đã cập nhật thành công xe có mã số " + maXeCanSua + "!");

      xoaChuDaNhap();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static void main(String[] args) {
    QuanLyXe q = new QuanLyXe();
    q.setContentPane(q.bangDieuKhien);
    q.setTitle("Chương trình quản lý xe");
    q.setSize(500, 350);
    q.setVisible(true);
    q.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}
