﻿package model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.dbutils.DbUtils;
import org.hsqldb.server.Servlet;

public class DatabaseDAO extends Servlet {

	private static final long serialVersionUID = 1L;

	private Connection connection;
	private Statement statement;

	final String connectionString = "jdbc:hsqldb:file:${user.home}/i377/Team09d/db;shutdown=true";
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			connection = DriverManager.getConnection(connectionString);
			statement = connection.createStatement();
			createTables();
			insertTestData();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			DbUtils.closeQuietly(statement);
			DbUtils.closeQuietly(connection);
		}
		response.getWriter().println("great success!");
		String redirectURL = "./?loodud=jah";
	    response.sendRedirect(redirectURL);
	}

	@Override
	public void init() throws ServletException {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public void createTables() throws SQLException {
		createTableRiigiAdminYksuseLiik();
		createTableRiigiAdminYksus();
		createTableAdminAlluvus();
		createTableVoimalikAlluvus();
	}

	public void insertTestData() throws SQLException {		
		insertTestDataRiigiAdminYksuseLiik();
		insertTestDataRiigiAdminYksus();
		insertTestDataAdminAlluvus();
		insertTestDataVoimalikAlluvus();		
	}

	

	private void createTableRiigiAdminYksuseLiik() throws SQLException {
		statement.executeUpdate("DROP SCHEMA PUBLIC CASCADE");
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS RIIGI_ADMIN_YKSUSE_LIIK ("
				+ "riigi_admin_yksuse_liik_id INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) NOT NULL,"
				+ "avaja                VARCHAR(32) NOT NULL,"
				+ "avatud               DATE NOT NULL,"
				+ "muutja               VARCHAR(32) NOT NULL,"
				+ "muudetud             DATE NOT NULL,"
				+ "sulgeja              VARCHAR(32),"
				+ "suletud              DATE,"
				+ "kood                 VARCHAR(10) NOT NULL,"
				+ "nimetus              VARCHAR(100) NOT NULL,"
				+ "kommentaar           LONGVARCHAR,"
				+ "alates               DATE NOT NULL,"
				+ "kuni                 DATE,"
				+ "PRIMARY KEY (riigi_admin_yksuse_liik_id));");
	}

	private void insertTestDataRiigiAdminYksuseLiik() throws SQLException {
		statement.executeUpdate("INSERT INTO RIIGI_ADMIN_YKSUSE_LIIK("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, kood, nimetus, kommentaar, alates, kuni) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, 'R', 'Riik', '', TODAY, NULL);");
		statement.executeUpdate("INSERT INTO RIIGI_ADMIN_YKSUSE_LIIK("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, kood, nimetus, kommentaar, alates, kuni) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, 'M', 'Maakond', 'Suurim haldusüksus riigis', TODAY, NULL);");
		statement.executeUpdate("INSERT INTO RIIGI_ADMIN_YKSUSE_LIIK("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, kood, nimetus, kommentaar, alates, kuni) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, 'L', 'Linn', NULL, TODAY, NULL);");
		statement.executeUpdate("INSERT INTO RIIGI_ADMIN_YKSUSE_LIIK("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, kood, nimetus, kommentaar, alates, kuni) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, 'V', 'Vald', NULL, TODAY, NULL);");
		statement.executeUpdate("INSERT INTO RIIGI_ADMIN_YKSUSE_LIIK("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, kood, nimetus, kommentaar, alates, kuni) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, 'K', 'Küla', NULL, TODAY, NULL);");
		statement.executeUpdate("INSERT INTO RIIGI_ADMIN_YKSUSE_LIIK("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, kood, nimetus, kommentaar, alates, kuni) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, 'T', 'Talu', NULL, TODAY, NULL);");
	}

	private void createTableVoimalikAlluvus() throws SQLException {
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS VOIMALIK_ALLUVUS ("
				+ "voimalik_alluvus_id  INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) NOT NULL,"
				+ "avaja                VARCHAR(32) NOT NULL,"
				+ "avatud               DATE NOT NULL,"
				+ "muutja               VARCHAR(32) NOT NULL,"
				+ "muudetud             DATE NOT NULL,"
				+ "sulgeja              VARCHAR(32),"
				+ "suletud              DATE,"
				+ "riigi_admin_yksuse_liik_id INTEGER NOT NULL,"
				+ "riigi_admin_yksuse_alluva_liik_id INTEGER NOT NULL,"
				+ "kommentaar           LONGVARCHAR,"
				+ "alates               DATE NOT NULL,"
				+ "kuni                 DATE,"
				+ "PRIMARY KEY (voimalik_alluvus_id), "
				+ "FOREIGN KEY (riigi_admin_yksuse_liik_id) REFERENCES RIIGI_ADMIN_YKSUSE_LIIK ON DELETE RESTRICT,"
				+ "FOREIGN KEY (riigi_admin_yksuse_alluva_liik_id) REFERENCES RIIGI_ADMIN_YKSUSE_LIIK ON DELETE RESTRICT);");
	}
	
	private void insertTestDataVoimalikAlluvus() throws SQLException {
		statement.executeUpdate("INSERT INTO VOIMALIK_ALLUVUS("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, riigi_admin_yksuse_liik_id, riigi_admin_yksuse_alluva_liik_id, kommentaar, alates, kuni) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, 1, 2, 'Maakond allub riigile', TODAY, NULL);");
		statement.executeUpdate("INSERT INTO VOIMALIK_ALLUVUS("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, riigi_admin_yksuse_liik_id, riigi_admin_yksuse_alluva_liik_id, kommentaar, alates, kuni) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, 2, 3, 'Linn allub maakonnale', TODAY, NULL);");
		statement.executeUpdate("INSERT INTO VOIMALIK_ALLUVUS("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, riigi_admin_yksuse_liik_id, riigi_admin_yksuse_alluva_liik_id, kommentaar, alates, kuni) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, 2, 4, 'Vald allub maakonnale', TODAY, NULL);");
		statement.executeUpdate("INSERT INTO VOIMALIK_ALLUVUS("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, riigi_admin_yksuse_liik_id, riigi_admin_yksuse_alluva_liik_id, kommentaar, alates, kuni) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, 4, 5, 'Küla allub vallale', TODAY, NULL);");
		statement.executeUpdate("INSERT INTO VOIMALIK_ALLUVUS("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, riigi_admin_yksuse_liik_id, riigi_admin_yksuse_alluva_liik_id, kommentaar, alates, kuni) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, 5, 6, 'Talu allub külale', TODAY, NULL);");
	}
	
	private void createTableRiigiAdminYksus() throws SQLException {
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS RIIGI_ADMIN_YKSUS ("
				+ "riigi_admin_yksus_ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) NOT NULL,"
				+ "avaja                VARCHAR(32) NOT NULL,"
				+ "avatud               DATE NOT NULL,"
				+ "muutja               VARCHAR(32) NOT NULL,"
				+ "muudetud             DATE NOT NULL,"
				+ "sulgeja              VARCHAR(32),"
				+ "suletud              DATE,"
				+ "kood					VARCHAR(20),"
				+ "nimetus              VARCHAR(100),"
				+ "kommentaar           LONGVARCHAR,"
				+ "alates               DATE NOT NULL,"
				+ "kuni                 DATE,"
				+ "riigi_admin_yksuse_liik_id INTEGER NOT NULL,"
				+ "PRIMARY KEY (riigi_admin_yksus_ID), "
				+ "FOREIGN KEY (riigi_admin_yksuse_liik_id) REFERENCES RIIGI_ADMIN_YKSUSE_LIIK ON DELETE RESTRICT);");
	}
		
	private void insertTestDataRiigiAdminYksus() throws SQLException {
		statement.executeUpdate("INSERT INTO RIIGI_ADMIN_YKSUS("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, kood, nimetus, kommentaar, alates, kuni, riigi_admin_yksuse_liik_id) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, 'R1', 'Eesti Vabariik', '"
				+ "Eesti Vabariik on riik Põhja-Euroopas. "
				+ "Eesti piirneb põhjas üle Soome lahe Soome Vabariigiga, läänes üle Läänemere Rootsi Kuningriigiga, lõunas Läti Vabariigiga ja idas Vene Föderatsiooniga. "
				+ "Eesti pindala on tänapäeval 45 227 ruutkilomeetrit.', TODAY, NULL, 1); ");
		
		statement.executeUpdate("INSERT INTO RIIGI_ADMIN_YKSUS("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, kood, nimetus, kommentaar, alates, kuni, riigi_admin_yksuse_liik_id) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, 'M1', 'Viljandi maakond', '"
				+ "Viljandimaa piirneb läänes Pärnu, põhjas Järva, kirdes Jõgevamaaga, idas Tartu ja kagus Valga maakonnaga ning lõunas Lätiga.', TODAY, NULL, 2); ");
		
		statement.executeUpdate("INSERT INTO RIIGI_ADMIN_YKSUS("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, kood, nimetus, kommentaar, alates, kuni, riigi_admin_yksuse_liik_id) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, 'L1', 'Viljandi linn', "
				+ "'Linn Lõuna-Eestis. Viljandi on Viljandi maakonna halduskeskus. Linn asub Sakala kõrgustikul, Viljandi järve kaldal. Viljandist on Tallinnasse 164 km, Tartusse 81 km ja Pärnusse 97 km.', TODAY, NULL, 3); ");
		
		statement.executeUpdate("INSERT INTO RIIGI_ADMIN_YKSUS("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, kood, nimetus, kommentaar, alates, kuni, riigi_admin_yksuse_liik_id) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, 'V1', 'Viiratsi vald', 'Asub Viljandi linnast idas ja ulatub Viljandist Võrtsjärveni.', TODAY, NULL, 4); ");
		
		statement.executeUpdate("INSERT INTO RIIGI_ADMIN_YKSUS("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, kood, nimetus, kommentaar, alates, kuni, riigi_admin_yksuse_liik_id) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, 'K1', 'Verilaske küla', 'Asub ca 1 km kaugusel Viiratsi alevikust, Väluste teeääres.', TODAY, NULL, 5); ");
		
		statement.executeUpdate("INSERT INTO RIIGI_ADMIN_YKSUS("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, kood, nimetus, kommentaar, alates, kuni, riigi_admin_yksuse_liik_id) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, 'T1', 'Ärma talu', 'Asub Abja-Paluojast mõne kilomeetri kaugusel ja kuulub Ilveste suguvõsale juba 1763. aastast alates. Selle pindala on 82 hektarit.', TODAY, NULL, 6); ");
		statement.executeUpdate("INSERT INTO RIIGI_ADMIN_YKSUS("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, kood, nimetus, kommentaar, alates, kuni, riigi_admin_yksuse_liik_id) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, 'M2', 'Harju maakond', NULL, TODAY, NULL, 2); ");
		statement.executeUpdate("INSERT INTO RIIGI_ADMIN_YKSUS("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, kood, nimetus, kommentaar, alates, kuni, riigi_admin_yksuse_liik_id) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, 'M3', 'Tartu maakond', NULL, TODAY, NULL, 2); ");
		statement.executeUpdate("INSERT INTO RIIGI_ADMIN_YKSUS("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, kood, nimetus, kommentaar, alates, kuni, riigi_admin_yksuse_liik_id) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, 'L2', 'Tallinn', 'Eesti pealinn', TODAY, NULL, 3); ");
		statement.executeUpdate("INSERT INTO RIIGI_ADMIN_YKSUS("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, kood, nimetus, kommentaar, alates, kuni, riigi_admin_yksuse_liik_id) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, 'L3', 'Tartu', 'Haridusmeka', TODAY, NULL, 3); ");
		statement.executeUpdate("INSERT INTO RIIGI_ADMIN_YKSUS("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, kood, nimetus, kommentaar, alates, kuni, riigi_admin_yksuse_liik_id) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, 'V2', 'Rae vald', NULL, TODAY, NULL, 4); ");
	}
	
	private void createTableAdminAlluvus() throws SQLException {
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS ADMIN_ALLUVUS  ("
				+ "admin_alluvus_id     INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) NOT NULL,"
				+ "avaja                VARCHAR(32) NOT NULL,"
				+ "avatud               DATE NOT NULL,"
				+ "muutja               VARCHAR(32) NOT NULL,"
				+ "muudetud             DATE NOT NULL,"
				+ "sulgeja              VARCHAR(32),"
				+ "suletud              DATE,"
				+ "alates               DATE NOT NULL,"
				+ "kuni                 DATE,"
				+ "kommentaar           LONGVARCHAR,"
				+ "riigi_admin_yksuse_id	INTEGER NOT NULL,"
				+ "riigi_admin_yksuse_alluva_id	INTEGER NOT NULL,"
				+ "PRIMARY KEY (admin_alluvus_id),"
				+ "FOREIGN KEY (riigi_admin_yksuse_id) REFERENCES RIIGI_ADMIN_YKSUS ON DELETE RESTRICT,"
				+ "FOREIGN KEY (riigi_admin_yksuse_alluva_id) REFERENCES RIIGI_ADMIN_YKSUS ON DELETE RESTRICT);");
	}
	
	private void insertTestDataAdminAlluvus() throws SQLException {
		statement.executeUpdate("INSERT INTO ADMIN_ALLUVUS("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, alates, kuni, kommentaar, riigi_admin_yksuse_id, riigi_admin_yksuse_alluva_id) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, TODAY, NULL, 'Viljandi maakond allub Eesti Vabariigile', 1, 2);");
		statement.executeUpdate("INSERT INTO ADMIN_ALLUVUS("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, alates, kuni, kommentaar, riigi_admin_yksuse_id, riigi_admin_yksuse_alluva_id) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, TODAY, NULL, 'Viljandi linn allub Viljandi maakonnale', 2, 3);");
		statement.executeUpdate("INSERT INTO ADMIN_ALLUVUS("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, alates, kuni, kommentaar, riigi_admin_yksuse_id, riigi_admin_yksuse_alluva_id) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, TODAY, NULL, 'Viiratsi vald allub Viljandi maakonnale', 2, 4);");
		statement.executeUpdate("INSERT INTO ADMIN_ALLUVUS("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, alates, kuni, kommentaar, riigi_admin_yksuse_id, riigi_admin_yksuse_alluva_id) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, TODAY, NULL, 'Verilaske küla allub Viiratsi vallale', 4, 5);");
		statement.executeUpdate("INSERT INTO ADMIN_ALLUVUS("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, alates, kuni, kommentaar, riigi_admin_yksuse_id, riigi_admin_yksuse_alluva_id) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, TODAY, NULL, 'Ärma talu allub Verilaske külale', 5, 6);");
		statement.executeUpdate("INSERT INTO ADMIN_ALLUVUS("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, alates, kuni, kommentaar, riigi_admin_yksuse_id, riigi_admin_yksuse_alluva_id) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, TODAY, NULL, 'Harju maakond allub Eesti Vabariigile', 1, 7);");	
		statement.executeUpdate("INSERT INTO ADMIN_ALLUVUS("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, alates, kuni, kommentaar, riigi_admin_yksuse_id, riigi_admin_yksuse_alluva_id) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, TODAY, NULL, 'Tartu maakond allub Eesti Vabariigile', 1, 8);");	
		statement.executeUpdate("INSERT INTO ADMIN_ALLUVUS("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, alates, kuni, kommentaar, riigi_admin_yksuse_id, riigi_admin_yksuse_alluva_id) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, TODAY, NULL, 'Tartu linn allub Tartu maakonnale', 8, 10);");		
		statement.executeUpdate("INSERT INTO ADMIN_ALLUVUS("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, alates, kuni, kommentaar, riigi_admin_yksuse_id, riigi_admin_yksuse_alluva_id) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, TODAY, NULL, 'Tallinn allub Harju maakonnale', 7,9);");	
		statement.executeUpdate("INSERT INTO ADMIN_ALLUVUS("
				+ "avaja, avatud, muutja, muudetud, sulgeja, suletud, alates, kuni, kommentaar, riigi_admin_yksuse_id, riigi_admin_yksuse_alluva_id) VALUES("
				+ "'Mart Potter', TODAY, 'Mart Potter', TODAY, NULL, NULL, TODAY, NULL, 'Rae vald allub Harju maakonnale', 7, 11);");	
	
	}
}