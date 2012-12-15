package model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.dbutils.DbUtils;

/**
 * Servlet implementation class LisaAdminAlluv
 */
public class LisaAdminAlluv extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LisaAdminAlluv() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		 System.out.println("lisa alluv @" + id);
		 
		 List<RiigiAdminYksus> yksused = null;
			try {
				 
				yksused = vaataYksuseid(id);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}

			request.setAttribute("yksused", yksused);
			request.getRequestDispatcher("LisaAdminAlluv.jsp").forward(request, response);
	}
	
private List<RiigiAdminYksus> vaataYksuseid(int id) throws SQLException {
		
		List<RiigiAdminYksus> yksused = new ArrayList<RiigiAdminYksus>();
		
		Connection conn = DriverManager
				.getConnection("jdbc:hsqldb:file:x://ITK//Java//Veebirakendused//DB//Projekt;shutdown=true");

		PreparedStatement ps = null;
		ResultSet rset = null;
		try {
			 ps = conn.prepareStatement("select * from RIIGI_ADMIN_YKSUS "
					+ "where "
					 	+ "suletud is null "
					+ "AND "
						+ "riigi_admin_yksus_ID not in "
							+ "(select "
								+ "distinct(riigi_admin_yksuse_alluva_id) "
							+ "from "
								+"ADMIN_ALlUVUS "
							+ "where "
								+ "suletud is null"
							+") "
							+"AND "
								+ "riigi_admin_yksuse_liik_id in "
									+ "(select "
										+"riigi_admin_yksuse_alluva_liik_id "
									+ "from "
										+ "VOIMALIK_ALLUVUS "
									+ "where "
										+ "VOIMALIK_ALLUVUS.riigi_admin_yksuse_liik_id = "
											+ "(select "
												+ "riigi_admin_yksuse_liik_id "
											+ "from "
												+ "RIIGI_ADMIN_YKSUS "
											+ "where "
												+ "RIIGI_ADMIN_YKSUS.riigi_admin_yksus_ID = ?"
											+") limit 1"
									+")"
							);
			ps.setInt(1, id);
			
			rset = ps.executeQuery();
			RiigiAdminYksus yksus = new RiigiAdminYksus();
			
			while (rset.next()) {
				yksus.setId(rset.getInt(1));
				yksus.setAvaja(rset.getString(2));
				yksus.setAvatud(rset.getDate(3));
				yksus.setMuutja(rset.getString(4));
				yksus.setMuudetud(rset.getDate(5));
				yksus.setSulgeja(rset.getString(6));
				yksus.setSuletud(rset.getDate(7));
				yksus.setKood(rset.getString(8));
				yksus.setNimetus(rset.getString(9));
				yksus.setKommentaar(rset.getString(10));
				yksus.setAlates(rset.getDate(11));
				yksus.setKuni(rset.getDate(12));
				yksus.setRiigi_admin_yksuse_liik_id(rset.getInt(13));
				System.out.println(rset.getInt(1));
				
				yksused.add(yksus);
			}

			return yksused;
			
		} finally {
			DbUtils.closeQuietly(rset);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(conn);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String alluv_id[] = request.getParameterValues("alluv_id");
		int id = Integer.parseInt(request.getParameter("id"));
		   if (alluv_id != null) 
			   
		   {
			   System.out.println(request.getParameter("id") + " uued alluvad: ");
			   Connection conn = null;
			   PreparedStatement ps = null;
			   ResultSet rset = null;					
				 
			   for (int i = 0; i < alluv_id.length; i++) 
		      {
		    	  try {
	    		  conn = DriverManager
	    					.getConnection("jdbc:hsqldb:file:x://ITK//Java//Veebirakendused//DB//Projekt;shutdown=true");

	    		  ps = conn.prepareStatement("INSERT INTO ADMIN_ALLUVUS("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, alates, kuni, kommentaar, riigi_admin_yksuse_id, riigi_admin_yksuse_alluva_id) VALUES("
				+ "'admin', TODAY, 'admin', TODAY, NULL, NULL, TODAY, NULL, NULL, ?, ?);");
				  ps.setInt(1, id);
				  ps.setInt(2, Integer.parseInt(alluv_id[i]));
					
					 int rowCount = ps.executeUpdate();
					 System.out.println(rowCount + " rows updated!");
					} catch (SQLException e) {
						throw new RuntimeException(e);
					} finally {
					 DbUtils.closeQuietly(conn);
					 DbUtils.closeQuietly(ps);
					 DbUtils.closeQuietly(rset);
					 }
		      }
		   }
		   else System.out.println ("<b>none<b>");
		   String redirectURL = "V2?ID=" + id;
		   response.sendRedirect(redirectURL);
	}
}
