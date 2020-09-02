package it.polito.tdp.formulaone.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.formulaone.model.Arco;
import it.polito.tdp.formulaone.model.Race;
import it.polito.tdp.formulaone.model.Season;

public class FormulaOneDAO {

	public List<Season> getAllSeasons() {
		String sql = "SELECT year, url FROM seasons ORDER BY year";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			List<Season> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new Season(rs.getInt("year"), rs.getString("url")));
			}
			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Integer> getAnno() {
		String sql = "SELECT year FROM seasons ORDER BY year";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			List<Integer> list = new ArrayList<>();
			while (rs.next()) {
				list.add(rs.getInt("year"));
			}
			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	

	public List <Race> getGare (Integer x, Map<Integer, Race> idMap) {
		String sql = "SELECT raceId AS id, YEAR AS anno, ROUND AS r, circuitId AS cid, NAME AS nome, url " + 
				"FROM races r " + 
				"WHERE YEAR = ? ";
		
		List <Race> result = new ArrayList<Race>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, x);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				Race r = new Race(res.getInt("id"), res.getInt("anno"), res.getInt("r"),
								res.getInt("cid"), res.getString("nome"), res.getString("url"));
				idMap.put(res.getInt("raceId"), r); 
					result.add(r);
			}
			
			conn.close();
			return result;
			
		}  catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List <Arco> getArchi(Integer x, Map <Integer, Race> idMap) {
		String sql = "SELECT r1.raceId AS r1, r2.raceId AS r2, COUNT(DISTINCT(re.driverId)) AS peso " + 
				"FROM races r1, races r2, results re, results re2 " + 
				"WHERE r1.raceId = re.raceId " + 
				"AND r2.raceId = re2.raceId " + 
				"AND r1.raceId > r2.raceId " + 
				"AND re.driverId = re2.driverId " + 
				"AND re.statusId = 1 " + 
				"AND re2.statusId = 1 " +
				"AND r1.year = ? " + 
				"AND r2.year = ? " + 
				"GROUP by r1, r2 ";
		
		List <Arco> result = new ArrayList<Arco>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, x);
			st.setInt(2, x);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				Race r1 = idMap.get(res.getInt("r1"));
				Race r2 = idMap.get(res.getInt("r2"));
				
				if (r1!= null && r2 != null) {
					result.add(new Arco(r1, r2, res.getInt("peso")));
				}
			}
			
			conn.close();
			return result;
		}  catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}
}

