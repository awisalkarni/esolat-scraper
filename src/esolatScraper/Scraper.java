package esolatScraper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {
	
	static String[] zones = {"JHR04","JHR03","JHR02","JHR01","KDH06","KDH05","KDH04","KDH03","KDH02","KDH01","KTN03","KTN01"
			,"MLK01","NGS02","NGS01","PHG06","PHG05","PHG03","PHG02","PHG01","PRK07","PRK05","PRK04","PRK02","PRK01","PLS01","PNG01"
			,"SBH09","SBH08","SBH07","SBH06","SBH05","SBH04","SBH03","SBH02","SBH01","SWK08","SWK07","SWK06","SWK05","SWK04","SWK03","SWK02","SWK01","TRG04"
			,"TRG03","TRG02","TRG01","WLY02","SGR01","SGR02", "SGR03", "SGR04", "PHG04","PRK06","PRK03"};
	static String[] months = {"01","02","03","04","05","06","07","08","09","10","11","12"};

	public static void main(String[] args) {
		System.out.println("Started parsing...");
		for (int i=0; i< zones.length; i++) {
			for (int j=0; j<months.length; j++){
				try {
					parseData(zones[i], months[j]);
					System.out.println("Location:"+zones[i] + " Bulan:"+months[j]);
				}catch(Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		
//		for (int i=0; i< zones.length; i++) {
////			for (int j=0; j<months.length; j++){
//				parseData(zones[i], "01");
//				System.out.println("Location:"+zones[i] + " Bulan:"+"01");
////			}
//		}
		

	}
	
	public static void parseData(String zone, String bulan){
		try {
			String url = "http://www.e-solat.gov.my/web/waktusolat.php?zone="+zone+"&state=Kuala%20Lumpur&year=2014&jenis=year&bulan="+bulan+"&LG=BM";
			Document doc = Jsoup.connect(url).get();
//			System.out.println(doc.toString());
			Elements tablesLain = doc.select("tr[bgcolor=#EDE6F5]");
			Elements tablesJumaat = doc.select("tr[bgcolor=#5CDAE5]");
			for (Element waktu : tablesLain) {
//				System.out.println(waktu.text());
				
				String[] waktuSpilt = waktu.text().split(" ");
				String tarikh = waktuSpilt[0]+"-"+waktuSpilt[1]+"-"+"2014";
				String hari = waktuSpilt[2];
				String imsak = waktuSpilt[3];
				String subuh = waktuSpilt[4];
				String syuruk = waktuSpilt[5];
				String zohor = waktuSpilt[6];
				String asar = waktuSpilt[7];
				String maghrib = waktuSpilt[8];
				String isyak = waktuSpilt[9];
				savedatabase(zone, tarikh, hari, imsak, subuh, syuruk, zohor, asar, maghrib, isyak);
			}
			
			for (Element waktu : tablesJumaat) {
//				System.out.println(waktu.text());
				String[] waktuSpilt = waktu.text().split(" ");
				String tarikh = waktuSpilt[0]+"-"+waktuSpilt[1]+"-"+"2014";
				String hari = waktuSpilt[2];
				String imsak = waktuSpilt[3];
				String subuh = waktuSpilt[4];
				String syuruk = waktuSpilt[5];
				String zohor = waktuSpilt[6];
				String asar = waktuSpilt[7];
				String maghrib = waktuSpilt[8];
				String isyak = waktuSpilt[9];
				savedatabase(zone, tarikh, hari, imsak, subuh, syuruk, zohor, asar, maghrib, isyak);
			}
			System.out.println("harilain:"+tablesLain.size()+" tablesjumaat:"+tablesJumaat.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void savedatabase(String zone, String tarikh, String hari, String imsak, String subuh, String syuruk
			,String zohor, String asar, String maghrib, String isyak){
		 
	        Connection con = null;
	        PreparedStatement stmt = null;
	        try {
	        	Class.forName("com.mysql.jdbc.Driver");
	            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/esolat_scrape", "root", "");
	            stmt = con.prepareStatement("INSERT INTO esolat_scrape.solat(Zone, Tarikh, Hari, Imsak, Subuh,"
	            		+ "Syuruk,Zohor, Asar, Maghrib, Isyak ) VALUES (?,?,?,?,?,?,?,?,?,?)");
	            stmt.setString(1, zone);
	            stmt.setString(2, tarikh);
	            stmt.setString(3, hari);
	            stmt.setString(4, imsak);
	            stmt.setString(5, subuh);
	            stmt.setString(6, syuruk);
	            stmt.setString(7, zohor);
	            stmt.setString(8, asar);
	            stmt.setString(9, maghrib);
	            stmt.setString(10, isyak);
	            stmt.executeUpdate();
//	            con.commit();
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            if (stmt != null) {
	                try {
	                   stmt.close();
	                } catch (SQLException ex) {
	                }
	            }
	            if (con != null) {
	                try {
	                   con.close();                    
	                } catch (SQLException ex) {
	                }
	            }
	        }
	}

}
