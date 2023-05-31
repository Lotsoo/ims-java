/*
 * To change this license header, choose License Headers in Projeke gas
Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.controller_aset;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import koneksi.Koneksi;
import sun.security.rsa.RSACore;
import view.Asset;
import view.ListEmployee;
import view.Welcome;
import view.SignUp;

/**
 *
 * @author user
 */
public class model_aset implements controller_aset {

    private String filename;
    String nameFile;

    @Override
    public void Input(Asset aset) throws SQLException {

        try {
            Connection con = koneksi.Koneksi.getKoneksi();

            String sql = "insert into aset values(?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement prepare = con.prepareStatement(sql);

            prepare.setInt(1, Integer.valueOf(aset.txtId.getText()));
            prepare.setString(2, aset.txtAsset.getText());
            prepare.setInt(3, Integer.valueOf(aset.txtQuantity.getText()));
            prepare.setString(4, (String) aset.cbCondition.getSelectedItem());
            prepare.setString(5, String.valueOf(aset.Date.getDate()));
            prepare.setString(6, aset.txtphoto.getText());
            prepare.setInt(7, Integer.valueOf(aset.txtPrice.getText()));
            prepare.executeUpdate();
            Reset(aset);
            

            JOptionPane.showMessageDialog(null, "Data has been inputted");
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Failed to input data" + e);
        } finally {
            Tampil(aset);
            aset.setLebarKolom();
        }
    }

    @Override
    public void Edit(Asset aset) throws SQLException {
        try {
            Connection con = koneksi.Koneksi.getKoneksi();
            String sql = "update aset set asset=?, quantity=?, kondisi=?, date_upload=?, photo=?, price=? where id_asset=?";
            PreparedStatement prepare = con.prepareStatement(sql);

            prepare.setInt(7, Integer.valueOf(aset.txtId.getText()));
            prepare.setString(1, aset.txtAsset.getText());
            prepare.setInt(2, Integer.valueOf(aset.txtQuantity.getText()));
            prepare.setString(3, (String) aset.cbCondition.getSelectedItem());
            prepare.setString(4, String.valueOf(aset.Date.getDate()));
            prepare.setString(5, aset.txtphoto.getText());
            prepare.setInt(6, Integer.valueOf(aset.txtPrice.getText()));
            prepare.executeUpdate();

            JOptionPane.showMessageDialog(null, "Changed");
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            Tampil(aset);
            aset.setLebarKolom();
            Reset(aset);
        }
    }

    @Override
    public void Reset(Asset aset) throws SQLException {
        aset.txtAsset.setText("");
        aset.txtQuantity.setText("");
        aset.Date.setDate(null);
        aset.cbCondition.setSelectedIndex(0);
        aset.txtphoto.setText(".");
        aset.txtPrice.setText("");

    }

    @Override
    public void Delete(Asset aset) throws SQLException {
        int row = aset.tbl.getSelectedRow();
        if (row != -1) {

            String NOMER = aset.tbl.getValueAt(row, 0).toString();
            String sql = "DELETE FROM aset WHERE id_asset='" + NOMER + "'";

            String resetno = "ALTER TABLE aset DROP id_asset";
            String consecutivenumbers = "ALTER TABLE aset ADD id_asset INT(3) NOT NULL AUTO_INCREMENT PRIMARY KEY FIRST";

            try {
                Connection con = koneksi.Koneksi.getKoneksi();
                con.createStatement().execute(sql);
                con.createStatement().execute(resetno);
                con.createStatement().execute(consecutivenumbers);

                JOptionPane.showMessageDialog(null, "Deleted");
                Tampil(aset);
                aset.setLebarKolom();
                Reset(aset);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ERROR" + e);
            }
        }
    }

    @Override
    public void Save(Asset aset) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void Print(Asset aset) throws SQLException {
        MessageFormat header = new MessageFormat("Report");
        MessageFormat footer = new MessageFormat("page-{0}");
        try {
            aset.tbl.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (java.awt.print.PrinterException e) {
            System.err.format("cannot print %$%n", e.getMessage());
        }
    }
/*
    @Override
    public void Export(Asset aset) throws SQLException {
        try {
            JasperPrint jp = JasperFillManager.fillReport(getClass().getResourceAsStream("report1.jasper"), null, Koneksi.getKoneksi());
            JasperViewer.viewReport(jp, false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
*/
    @Override
    public void KlikTable(Asset aset) throws SQLException {
        try {
            int pilih = aset.tbl.getSelectedRow();
            if (pilih == -1) {
                return;
            }
            aset.txtId.setText(aset.tblmodel.getValueAt(pilih, 0).toString());
            aset.txtAsset.setText(aset.tblmodel.getValueAt(pilih, 1).toString());
            aset.txtQuantity.setText(aset.tblmodel.getValueAt(pilih, 2).toString());
            aset.cbCondition.setSelectedItem(aset.tblmodel.getValueAt(pilih, 3).toString());
            aset.txtphoto.setText(aset.tblmodel.getValueAt(pilih, 5).toString());
            aset.txtPrice.setText(aset.tblmodel.getValueAt(pilih, 6).toString());
            
         
             
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void Enter(Asset aset) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void Tampil(Asset aset) throws SQLException {
        aset.tblmodel.getDataVector().removeAllElements();
        aset.tblmodel.fireTableDataChanged();

        try {
            Connection con = koneksi.Koneksi.getKoneksi();
            Statement stt = con.createStatement();
            String sql = "select id_asset, asset, quantity, kondisi, date_upload, photo, concat('Rp', format(price, 0)) from aset";
            ResultSet res = stt.executeQuery(sql);
            while (res.next()) {
                Object[] ob = new Object[8];
                ob[0] = res.getString(1);
                ob[1] = res.getString(2);
                ob[2] = res.getString(3);
                ob[3] = res.getString(4);
                ob[4] = res.getString(5);
                ob[5] = res.getString(6);
                ob[6] = res.getString(7);
                aset.tblmodel.addRow(ob);

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void Upload_foto(Asset aset) throws SQLException {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        ImageIcon icon = new ImageIcon(f.toString());
        Image image = icon.getImage().getScaledInstance(aset.LabelImage.getWidth(), aset.LabelImage.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon ic = new ImageIcon(image);
        aset.LabelImage.setIcon(ic);
        filename = f.getAbsolutePath();
        aset.txtphoto.setText(filename);

        String newpath = "C:\\Users\\astse\\Documents\\test";
        File directory = new File(newpath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File sourceFile = null;
        File destinationFile = null;
        String exstension = filename.substring(filename.lastIndexOf('.') + 1);
        sourceFile = new File(filename);
        Date tanggal_update = new Date();
        String tampilan = "dd-MM-yyyy";
        SimpleDateFormat fm = new SimpleDateFormat(tampilan);
        String tanggal = String.valueOf(fm.format(tanggal_update));
        destinationFile = new File(newpath + "/Uploaded-" + tanggal.toString() + "." + exstension);
        aset.txtphoto.setText("Uploaded-" + tanggal.toString() + "." + exstension);
        try {
            Files.copy(sourceFile.toPath(), destinationFile.toPath());
        } catch (IOException ex) {
            Logger.getLogger(model_aset.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SortByName(Asset aset) throws SQLException {
        aset.tblmodel.getDataVector().removeAllElements();
        aset.tblmodel.fireTableDataChanged();
        if (aset.cbSort.getSelectedItem().equals("Asset")) {
            try {
                Connection con = koneksi.Koneksi.getKoneksi();
                Statement stt = con.createStatement();
                String sql = "select * from aset order by asset asc";
                ResultSet res = stt.executeQuery(sql);
                while (res.next()) {
                    Object[] ob = new Object[8];
                    ob[0] = res.getString(1);
                    ob[1] = res.getString(2);
                    ob[2] = res.getString(3);
                    ob[3] = res.getString(4);
                    ob[4] = res.getString(5);
                    ob[5] = res.getString(6);
                    ob[6] = res.getString(7);
                    aset.tblmodel.addRow(ob);

                }
            } catch (Exception e) {
                System.out.println(e);
            }
        } else if (aset.cbSort.getSelectedItem().equals("Date")) {
            try {
                Connection con = koneksi.Koneksi.getKoneksi();
                Statement stt = con.createStatement();
                String sql = "select * from aset order by date asc";
                ResultSet res = stt.executeQuery(sql);
                while (res.next()) {
                    Object[] ob = new Object[8];
                    ob[0] = res.getString(1);
                    ob[1] = res.getString(2);
                    ob[2] = res.getString(3);
                    ob[3] = res.getString(4);
                    ob[4] = res.getString(5);
                    ob[5] = res.getString(6);
                    ob[6] = res.getString(7);
                    aset.tblmodel.addRow(ob);

                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }

    public void tampilSearch(Asset aset) throws SQLException {
        aset.tblmodel.getDataVector().removeAllElements();
        aset.tblmodel.fireTableDataChanged();

        try {
            if (aset.txtAsset.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Data cannot be empty", "Message", JOptionPane.ERROR_MESSAGE);
            } else {
                Connection con = koneksi.Koneksi.getKoneksi();
                Statement stt = con.createStatement();
                String sql = "select * from aset where asset='" + aset.txtSrcTab.getText() + "'";
                ResultSet res = stt.executeQuery(sql);
                while (res.next()) {
                    Object[] ob = new Object[8];
                    ob[0] = res.getString(1);
                    ob[1] = res.getString(2);
                    ob[2] = res.getString(3);
                    ob[3] = res.getString(4);
                    ob[4] = res.getString(5);
                    ob[5] = res.getString(6);
                    ob[6] = res.getString(7);
                    aset.tblmodel.addRow(ob);

                 Reset(aset);   
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void AutoNumber(Asset aset) throws SQLException {
        try {
            Connection con = koneksi.Koneksi.getKoneksi();
            Statement stt = con.createStatement();
            String sql = "SELECT MAX(id_asset) FROM aset";
            ResultSet res = stt.executeQuery(sql);

            while (res.next()) {
                int a = res.getInt(1);
                aset.txtId.setText(Integer.toString(a + 1));
            }

        } catch (Exception ex) {
            Logger.getLogger(model_aset.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void Export(Asset aset) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void tampilGambar(Asset aset) throws SQLException {
        
    }

    @Override
    public void register(SignUp sgu) throws SQLException {
        
        
       try {
            if (sgu.txtusername.getText().equals("") || sgu.txtpassword.getPassword().equals("")
                    || sgu.txtnama.getText().equals("")) {
                JOptionPane.showMessageDialog(sgu, "Data Cannot Be Empty", "Message", JOptionPane.ERROR_MESSAGE);
                sgu.hapuslayar();
            } else if (sgu.txtpassword.getText() == null ? sgu.txtketikpassword.getText() != null : !sgu.txtpassword.getText().equals(sgu.txtketikpassword.getText())) {
                JOptionPane.showMessageDialog(null, "Password Doesn't Match");
            } else {
                Connection con = Koneksi.getKoneksi();
                Statement st = con.createStatement();
                String simpan = "insert into user values ('" + sgu.txtusername.getText() + "', '"
                        + String.valueOf(sgu.txtpassword.getPassword()) + "','" + sgu.txtnama.getText() + "')";
                st = con.createStatement();
                int SA = st.executeUpdate(simpan);

                JOptionPane.showMessageDialog(sgu, "Registration Successful");
                sgu.setVisible(false);
                new Welcome().setVisible(true);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(sgu, "This Account already exist / Duplicate Account ", "Message",
                    JOptionPane.WARNING_MESSAGE);
            sgu.hapuslayar();

        }
    }

    @Override
    public void TampilList(ListEmployee le) throws SQLException {
              try {
            Connection con = koneksi.Koneksi.getKoneksi();
            Statement stt = con.createStatement();
            String sql = "select * from user";
            ResultSet res = stt.executeQuery(sql);
            while (res.next()) {
                Object[] ob = new Object[5];
                ob[0] = res.getString(1);
                ob[1] = res.getString(2);
                ob[2] = res.getString(3);
                ob[3] = res.getString(4);
                le.tblmodel.addRow(ob);

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
