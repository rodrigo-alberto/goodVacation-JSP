package agency.vacation.good.daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import agency.vacation.good.connection.ConnectionBd;
import agency.vacation.good.models.Destiny;
import agency.vacation.good.utils.SystemAlerts;

public class DestinyDao {
	public boolean createDestiny(Destiny destiny, Part part) {
		String sql = "INSERT INTO destiny (`nameDestiny`, image, city) VALUES (?, ?, ?)";
		
		try {
			PreparedStatement pst = ConnectionBd.getConnection().prepareStatement(sql);
			InputStream is = part.getInputStream();
			
			pst.setString(1, destiny.getName());
			pst.setBlob(2, is);
			pst.setString(3, destiny.getCity());
			pst.execute();
			pst.close();		
			
		} catch (SQLException e) {
			SystemAlerts.printAlertMessage(SystemAlerts.REGISTRY_ERROR);
			SystemAlerts.printAlertException(e);
			return false;
		} catch (IOException e) {
			SystemAlerts.printAlertMessage(SystemAlerts.REGISTRY_ERROR);
			SystemAlerts.printAlertException(e);
			return false;
		}
		
		SystemAlerts.printAlertMessage(SystemAlerts.REGISTRY_SUCCESS);
		return true;
	}
	
	public ArrayList<Destiny> getAllDestinys() {
		String sql = "SELECT * FROM destiny";
		ArrayList<Destiny> arrDestinys = new ArrayList<Destiny>();
		Blob blob = null;
		
		try {
			PreparedStatement pst = ConnectionBd.getConnection().prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			
			while (rs.next()) {
				Destiny destiny = new Destiny();
				blob = rs.getBlob("image");
				
				destiny.setIdDestiny(rs.getInt("idDestiny"));
				destiny.setName(rs.getString("nameDestiny"));
				destiny.setImage(blob.getBytes(1, (int) blob.length()));
				destiny.setImageBase64(destiny.getImage());
				destiny.setCity(rs.getString("city"));
				arrDestinys.add(destiny);
			}
			pst.close();
			rs.close();
		
		} catch (SQLException e) {
			SystemAlerts.printAlertException(e);
		}
		
		return arrDestinys;
	}
	
	public Destiny getDestinyById(int idDestiny) {
		String sql = "SELECT * FROM destiny WHERE idDestiny = ?";
		Destiny outputDestiny = new Destiny();
		Blob blob = null;
		
		try {
			PreparedStatement pst = ConnectionBd.getConnection().prepareStatement(sql);
			ResultSet rs;
			
			pst.setInt(1, idDestiny);
			rs = pst.executeQuery();
			
			while (rs.next()) {
				blob = rs.getBlob("image");
				
				outputDestiny.setIdDestiny(rs.getInt("idDestiny"));
				outputDestiny.setName(rs.getString("nameDestiny"));
				outputDestiny.setImage(blob.getBytes(1, (int) blob.length()));
				outputDestiny.setImageBase64(outputDestiny.getImage());
				outputDestiny.setCity(rs.getString("city"));			
			}
			pst.close();
			rs.close();
		
		} catch (SQLException e) {
			SystemAlerts.printAlertException(e);
		} 
		
		return outputDestiny;
	}
	
	public boolean updateDestiny(int idDestiny, Destiny destiny, Part part) {
		 String sql = "UPDATE destiny SET nameDestiny = ?, image = ?, city = ? WHERE idDestiny = ?";
	        
	        try {
	        	PreparedStatement pst = ConnectionBd.getConnection().prepareStatement(sql);
	        	InputStream is = part.getInputStream();

	        	pst.setString(1, destiny.getName());
	            pst.setBlob(2, is);
	            pst.setString(3, destiny.getCity());
	            pst.setInt(4, idDestiny);
	            pst.execute();
	            pst.close();          
	            
	        } catch (SQLException e) {
	        	SystemAlerts.printAlertMessage(SystemAlerts.UPDATE_ERROR);
				SystemAlerts.printAlertException(e);
				return false;
	        }catch (IOException e) {
				SystemAlerts.printAlertMessage(SystemAlerts.REGISTRY_ERROR);
				SystemAlerts.printAlertException(e);
				return false;
			}
			
	        SystemAlerts.printAlertMessage(SystemAlerts.UPDATE_SUCCESS);
	        return true;
	}
	
	public boolean deleteDestiny(int idDestiny) {
		String sql = "DELETE FROM destiny WHERE idDestiny = ?";
		
		try {
			PreparedStatement pst = ConnectionBd.getConnection().prepareStatement(sql);
			pst.setInt(1, idDestiny);
			pst.execute();
			pst.close();
			
		} catch (SQLIntegrityConstraintViolationException e) {
			SystemAlerts.printAlertMessage(SystemAlerts.FOREIGN_KEY_RESTRICTION);
			return false;
		}catch (SQLException  ex) {
			SystemAlerts.printAlertMessage(SystemAlerts.DELETE_ERROR);
			SystemAlerts.printAlertException(ex);
			return false;
		}
		
		SystemAlerts.printAlertMessage(SystemAlerts.DELETE_SUCCESS);
		return true;
	}
}
