
import java.util.Scanner;
import java.sql.*;
import java.io.*;
import java.lang.*;
import java.nio.*;
import java.util.*;
import java.text.SimpleDateFormat;
public class CSCI3170Proj {

	// CHANGE START
	public static String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/Rdb1";
	public static String dbUsername = "Random1";
	public static String dbPassword = "CSCI3170kplee7";
	// CHANGE END
	public static int ans;
	public static Connection connectToOracle(){
		Connection con = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
		} catch (ClassNotFoundException e){
			System.out.println("[Error]: Java MySQL DB Driver not found!!");
			System.exit(0);
		} catch (SQLException e){
			//System.out.println(e);
		}
		return con;
	}

	
	public static void main(String[] args) throws IOException, Exception{
		Scanner menuAns = new Scanner(System.in);
		String answer;
		int condition;
		int table = 2;
		String sql;
		Statement stmt;
		PreparedStatement pstmt;
		String [] attribute;
		//System.out.println("Welcome to the mission design system!");
		System.out.println();
		while(true){
			try{
				Connection mySQLDB = connectToOracle();
				Connection mySQLDB1 = connectToOracle();
				if(mySQLDB == null){
					answer = "0";
					System.out.println("[Error]: Database connection failed, system exit");
				}else{
					//CHANGE STARTS
					do {
					System.out.println("welcome! who are you?");
					System.out.println("1. an administrator");
					System.out.println("2. a passenger");
					System.out.println("3. a driver");
					System.out.println("4. none of the above");
					System.out.println("please enter [1 - 4].");
					answer = menuAns.nextLine();
					ans = Integer.parseInt(answer);
					switch (ans) {
						case 1:		do{
									System.out.println("administrator, what would you like to do?");
									System.out.println("1. create tables");
									System.out.println("2. delete tables");
									System.out.println("3. load data");
									System.out.println("4. check data");
									System.out.println("5. go back");
									System.out.println("please enter [1 - 5].");
									answer = menuAns.nextLine();
									ans = Integer.parseInt(answer);
									switch (ans) {
										case 1:		if ( table <= 1)
													{
														System.out.println("tables already created");
														condition = 200;
														break;
													}
													else 
													{
													sql =	"CREATE TABLE drivers(DID int unsigned primary key,Name varchar(30) not null,VID char(6) not null);";
													stmt = mySQLDB.createStatement();
													stmt.executeUpdate(sql);
													sql =	"CREATE TABLE vehicles(VID char(6) primary key,Model varchar(30) not null,Model_Year int(4) unsigned,Seats int unsigned);";
													stmt.executeUpdate(sql);		
													sql =	"CREATE TABLE passengers( PID int unsigned primary key,Name varchar(30) not null);";
													stmt.executeUpdate(sql);		
													sql	=	"CREATE TABLE trips(TID int unsigned primary key,DID int unsigned not null,PID int unsigned not null,Start DATETIME,End DATETIME,Fee int unsigned,Rating int Default '0');";
															// check rating between 0 to 5 by java coding
													stmt.executeUpdate(sql);		
													sql =	"CREATE TABLE request(RID int unsigned primary key auto_increment,PID int unsigned ,Model_Year int(4) unsigned,Model varchar(30) not null,no_of_passenger int unsigned not null,taken varchar(3) not null);";
												
													stmt.executeUpdate(sql);
													table = 1;
													condition = 200;
													System.out.println("processing...done! tables are created!");
													break;
													}
										case 2:		
													sql =			"drop tables if exists drivers;";
													stmt = mySQLDB.createStatement();
													stmt.executeUpdate(sql);
													
													sql =			"drop tables if exists vehicles;";
													stmt = mySQLDB.createStatement();
													stmt.executeUpdate(sql);
													
													sql	=			"drop tables if exists passengers;";
													stmt = mySQLDB.createStatement();
													stmt.executeUpdate(sql);
													
													sql	=			"drop tables if exists trips;";
													stmt = mySQLDB.createStatement();
													stmt.executeUpdate(sql);
													
													sql	=			"drop tables if exists request;";
													stmt = mySQLDB.createStatement();
													stmt.executeUpdate(sql);
													
													table =	10;
													condition = 200;
													System.out.println("Deleted");
													
													break;
										case 3:		System.out.println("please enter the folder path");
													Scanner path = new Scanner(System.in);
													String foldername = path.nextLine();
													
													 
													
													sql = "LOAD DATA LOCAL INFILE '"+foldername+"/drivers.csv' into table drivers Fields terminated by ',';";
													pstmt = mySQLDB1.prepareStatement(sql);
													pstmt.executeUpdate();
													sql = "LOAD DATA LOCAL INFILE '"+foldername+"/passengers.csv' into table passengers Fields terminated by ',';";
													pstmt = mySQLDB1.prepareStatement(sql);
													pstmt.executeUpdate();
													sql = "LOAD DATA LOCAL INFILE '"+foldername+"/vehicles.csv' into table vehicles Fields terminated by ',';";
													pstmt = mySQLDB1.prepareStatement(sql);
													pstmt.executeUpdate();
													
													sql = "LOAD DATA LOCAL INFILE '"+foldername+"/trips.csv' into table trips Fields terminated by ',';";
													pstmt = mySQLDB1.prepareStatement(sql);
													pstmt.executeUpdate();
														/*try{
															String data;
															BufferedReader br = new BufferedReader(new FileReader(file);
															while((data = br.readLine()) != null){
																attribute = data.split(",");
																
																if (v.contains("\\drivers.csv")){
																	sql = "insert into drivers values(?,?,?);";
																	pstmt.setInt(1, Integer.parseInt(attribute[0]));
																	pstmt.setString(2, attribute[1]);
																	pstmt.setString(3, attribute[2]);
																	pstmt.executeUpdate();
																}
																else if (v.contains("\\vehicles.csv")){
																	sql = "insert into vehicles values(?,?,?,?);";
																	pstmt.setString(1, attribute[0]);
																	pstmt.setString(2, attribute[1]);
																	pstmt.setInt(3, Integer.parseInt(attribute[2]));
																	pstmt.setInt(4, Integer.parseInt(attribute[3]));
																	pstmt.executeUpdate();
																}
																else if (v.contains("\\passengers.csv")){
																	sql = "insert into passengers values(?,?);";
																	pstmt.setInt(1, Integer.parseInt(attribute[0]));
																	pstmt.setString(2, attribute[1]);
																	pstmt.executeUpdate();
																}																
																else if (v.contains("\\trips.csv")){
																	sql = "insert into trips values(?,?,?,?,?,?,?);";
																	Timestamp ts3 = new Timestamp(System.currentTimeMillis());
																	Timestamp ts4 = new Timestamp(System.currentTimeMillis());
																			try{
																				ts3 = Timestamp.valueOf(attribute[3]);
																				ts4 = Timestamp.valueOf(attribute[4]);
																			}catch (Exception e) {
																				e.printStackTrace();
																			}
																	pstmt.setInt(1, Integer.parseInt(attribute[0]));
																	pstmt.setInt(2, Integer.parseInt(attribute[1]));
																	pstmt.setInt(3, Integer.parseInt(attribute[2]));
																	pstmt.setTimestamp(4, ts3);
																	pstmt.setTimestamp(5, ts4);
																	pstmt.setInt(6, Integer.parseInt(attribute[5]));
																	int rating = Integer.parseInt(attribute[6]);
																	if(rating >= 5)
																		break;
																	pstmt.setInt(7, rating);
																	pstmt.executeUpdate();
																}
																else{}
															}
														}catch (IOException e) {
															System.err.println("Error: " + e);
														}	*/
													
													System.out.println("Processing...Data is loaded!");
													condition = 200;
													break;
										case 4:		System.out.println("Number of records in each table:");
													
													sql = "select count(*) as count from drivers;";
													stmt = mySQLDB.createStatement();
													ResultSet rs = stmt.executeQuery(sql);
													rs.next();
													System.out.print("drivers: ");
													System.out.println(rs.getString(1));
													
													sql = "select count(*) as count from vehicles;";
													rs = stmt.executeQuery(sql);
													rs.next();
													System.out.print("vehicles: ");
													System.out.println(rs.getString(1));
													
													sql = "select count(*) as count from passengers;";
													rs = stmt.executeQuery(sql);
													rs.next();
													System.out.print("passengers: ");
													System.out.println(rs.getString(1));
													
													sql = "select count(*) as count from trips;";
													rs = stmt.executeQuery(sql);
													rs.next();
													System.out.print("trips: ");
													System.out.println(rs.getString(1));
													
													sql = "select count(*) as count from request;";
													rs = stmt.executeQuery(sql);
													rs.next();
													System.out.print("requests: ");
													System.out.println(rs.getString(1));
													
													condition = 200;
													break;
										case 5:		condition = 100;
													break; 
										default:	System.out.println("[ERROR] invalid input.");
													condition = 200;
												}
													
									  } while (condition > 100);
									break; 
						case 2:		do{
									System.out.println("passenger, what would you like to do?");
									System.out.println("1. request a ride");
									System.out.println("2. check trip records");
									System.out.println("3. rate a trip");
									System.out.println("4. go back");
									System.out.println("please enter [1 - 4].");
									answer = menuAns.nextLine();
									ans = Integer.parseInt(answer);
									switch(ans){
										case 1:  System.out.println("please enter your ID");
												Scanner pvalue = new Scanner(System.in);
												String ID = pvalue.nextLine();//PID in request
												System.out.println("please enter the number of passengers");
												String no_passengers = pvalue.nextLine();//no_of_passenger in request
												System.out.println("please enter the earlist model year (press enter to skip)");
												String year = pvalue.nextLine();// Model_Year in request
												System.out.println("please enter the model (press enter to skip)");
												String model = pvalue.nextLine();//Model in request
												int IDR = Integer.parseInt(ID);
												if ( !year.equals("2010") &  !year.equals("2011") & !year.equals("2012") &  !year.equals("2013") &  !year.equals("2014") &  !year.equals("2015") &  !year.equals("2016") &  !year.equals("2017") &  !year.equals("2018") & !year.equals(null) & year.trim().length() != 0)
												{
													System.out.println("invalid model year");
													condition = 200;
													break;
												}
												else if (IDR > 150 | IDR < 1 )
												{
													System.out.println("PID not found");
													condition = 200;
													break;
												}
												
												else {
												
													if((model.equals(null) | model.trim().length() == 0) & (year.equals(null) | year.trim().length() == 0 ))
													{sql = " select  VID, Model_Year, Model, Seats  from vehicles where Seats >= ?;";
													pstmt = mySQLDB1.prepareStatement(sql);
													pstmt.setInt(1, Integer.parseInt(no_passengers));
													}
													else if (model.equals(null) | model.trim().length() == 0)
													{
														sql = " select VID, Model_Year, Model, Seats  from vehicles where Seats >= ? AND Model_Year >= ?;";
														pstmt = mySQLDB1.prepareStatement(sql);
														pstmt.setInt(1, Integer.parseInt(no_passengers));
														pstmt.setInt(2, Integer.parseInt(year));
														
													}
													else if (year.equals(null) | year.trim().length() == 0 )
													{
														sql = " select  VID, Model_Year, Model, Seats   from vehicles where Seats >= ? AND UPPER(Model) = UPPER(?);";
														pstmt = mySQLDB1.prepareStatement(sql);
														pstmt.setInt(1, Integer.parseInt(no_passengers));
														pstmt.setString(2, "%" + model + "%");
														
													}
													else 
													{
														sql = " select  VID, Model_Year, Model, Seats   from vehicles where Seats >= ? AND UPPER(Model) = UPPER(?) AND Mode_Year >= ?;";
														pstmt = mySQLDB1.prepareStatement(sql);
														pstmt.setInt(1, Integer.parseInt(no_passengers));
														pstmt.setString(2, "%" + model + "%");
														pstmt.setInt(3, Integer.parseInt(year));
													}
													
													
													ResultSet r = pstmt.executeQuery();
													
													sql = "insert into request(PID, Model_Year, Model, no_of_passenger, taken) values (?, ?, ?, ?, 'no');";
													pstmt = mySQLDB1.prepareStatement(sql);
													pstmt.setInt(1, IDR);
													pstmt.setInt(2, Integer.parseInt(year));
													pstmt.setString(3, model);
													pstmt.setInt(4, Integer.parseInt(no_passengers));
													pstmt.executeUpdate();
													
													if (!r.isBeforeFirst()){
														System.out.println("no request can be made under such criteria. please adjust your criteria.");
													
													}
													else{
														int i = 0;
														while(r.next()){
															i++;
														}
													System.out.print("your request is placed. ");
													System.out.print(i);
													System.out.println(" drivers are able to take the request.");
													}
												condition = 200;
												break;
													}
										case 2:		System.out.println("please enter your ID");
													Scanner pvalue1 = new Scanner(System.in);
													Scanner pvalue2 = new Scanner(System.in);
													int ID1 = pvalue1.nextInt();
													System.out.println("please enter the start date");
													String st = pvalue2.nextLine();
													System.out.println("please enter the end date");
													String ed = pvalue2.nextLine();
													if (ID1 > 150 | ID1 < 1 )
													{
														System.out.println("PID not found");
														condition = 200;
														break;
													}
													else if ((st.equals(null) | st.trim().length() == 0)  & (ed.equals(null) | ed.trim().length() == 0))
													{
														System.out.println("PID not found");
														condition = 200;
														break;
													}
													else
													{
														sql = "select T.TID, D.Name, D.VID, VE.Model, T.Start, T.End, T.Fee, T.Rating from drivers D, trips T, vehicles VE where T.PID = ? AND T.Start >= ?% AND T.End < ?% order by Start DESC;";
														pstmt = mySQLDB1.prepareStatement(sql);
														pstmt.setInt(1, ID1);
														SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
														String st1 = dateFormat.format(st);
														String ed1 = dateFormat.format(ed);
														pstmt.setString(2, st1);
														pstmt.setString(3, ed1);
														ResultSet rtrip = pstmt.executeQuery();
														if(!rtrip.isBeforeFirst())
																System.out.println("No records found.");
														else 
														{
																System.out.println("Trip ID, driver name, vehicle id, vehicle model, start, end, fee, rating");
																while(rtrip.next()){
																	System.out.print(rtrip.getString(1));
																	System.out.print(",   ");
																	System.out.print(rtrip.getString(2));
																	System.out.print(",   ");
																	System.out.print(rtrip.getString(3));
																	System.out.print(",   ");
																	System.out.print(rtrip.getString(4));
																	System.out.print(",   ");
																	System.out.print(rtrip.getString(5));
																	System.out.print(",   ");
																	System.out.print(rtrip.getString(6));
																	System.out.print(",   ");
																	System.out.print(rtrip.getString(7));
																	System.out.print(",   ");
																	System.out.println(rtrip.getString(8));
																}
														}
													}
													condition = 200;
													break;
										case 3:		System.out.println("please enter your ID");
													Scanner rate = new Scanner(System.in);
													int ID2 = rate.nextInt();
													System.out.println("please enter the trip ID");
													int TID1 = rate.nextInt();
													System.out.println("please enter the rating where 5 is the highest , 1 is the lowest");
													int rating1 = rate.nextInt();
													if (ID2 > 150 | ID2 < 1 )
													{
														System.out.println("PID not found");
														condition = 200;
														break;
													}
													else if (TID1 < 1 )
													{
														System.out.println("TID invalid");
														condition = 200;
														break;
													}
													else if (rating1 < 1 | rating1 > 5)
													{
														System.out.println("please rate between 1 to 5 in integer, choose your query again");
														condition = 200;
														break;
													}
													else {
													sql = "select T.TID, D.Name, D.VID, VE.Model, T.Start, T.End, T.Fee, T.Rating from drivers D, trips T, vehicles VE where T.DID = D.DID AND D.VID = VE.VID AND T.PID = ? AND T.TID = ? AND Rating = 0;";
														pstmt = mySQLDB1.prepareStatement(sql);
														pstmt.setInt(1, ID2);
														pstmt.setInt(2, TID1);
														ResultSet rk = pstmt.executeQuery();
														if(!rk.isBeforeFirst())
														{
																System.out.println("Invalid trip id");
																condition = 200;
																break;
														}
														else 
														{	
																sql = "Update trips set Rating = ? where TID = ? ;";
																pstmt = mySQLDB1.prepareStatement(sql);
																pstmt.setInt(1, rating1);
																pstmt.setInt(2, TID1);
																pstmt.executeUpdate();
																		sql = "select T.TID, D.Name, D.VID, VE.Model, T.Start, T.End, T.Fee, T.Rating from drivers D, trips T, vehicles VE where T.DID = D.DID AND D.VID = VE.VID AND T.PID = ? AND T.TID = ?;";
																		pstmt = mySQLDB1.prepareStatement(sql);
																		pstmt.setInt(1, ID2);
																		pstmt.setInt(2, TID1);
																		ResultSet rated = pstmt.executeQuery();
																System.out.println("Trip ID, driver name, vehicle id, vehicle model, start, end, fee, rating");
																while(rated.next()){
																	System.out.print(rated.getString(1));
																	System.out.print(",   ");
																	System.out.print(rated.getString(2));
																	System.out.print(",   ");
																	System.out.print(rated.getString(3));
																	System.out.print(",   ");
																	System.out.print(rated.getString(4));
																	System.out.print(",   ");
																	System.out.print(rated.getString(5));
																	System.out.print(",   ");
																	System.out.print(rated.getString(6));
																	System.out.print(",   ");
																	System.out.print(rated.getString(7));
																	System.out.print(",   ");
																	System.out.println(rated.getString(8));
																}
														}
														}
														condition = 200;
														break;
													
										case 4:		condition = 100;
													break;
										default:	System.out.println("[ERROR] invalid input.");
													condition = 200;
										}
									  }while (condition >100);
									break;
						case 3:		do{
									System.out.println("driver, what would you like to do?");
									System.out.println("1. take a request");
									System.out.println("2. finish a trip");
									System.out.println("3. check driver rating");
									System.out.println("4. go back");
									System.out.println("please enter [1 - 4]");
									answer = menuAns.nextLine();
									ans = Integer.parseInt(answer);
									switch(ans){
										case 1:		System.out.println("please enter your id");
													Scanner driverinput = new Scanner(System.in);
													int driverID = driverinput.nextInt();
													sql = "select R.RID, R.PID, R.no_of_passenger from request R, drivers D, vehicles VE where D.DID = ? AND VE.VID = D.VID AND UPPER(VE.Model) Like Upper('%R.Model%') AND VE.Model_Year >= R.Model_Year AND VE.Seats >= R.no_of_passenger AND R.taken = 'no';";
													pstmt = mySQLDB1.prepareStatement(sql);
													pstmt.setInt(1, driverID);
													ResultSet Driveraccept = pstmt.executeQuery();
													if(!Driveraccept.isBeforeFirst())
														System.out.println("no open request");
													else{
														System.out.println("Request ID, Passenger name, Passengers");
														while(Driveraccept.next()){
																	System.out.print(Driveraccept.getString(1));
																	System.out.print(",   ");
																	System.out.print(Driveraccept.getString(2));
																	System.out.print(",   ");
																	System.out.println(Driveraccept.getString(3));
														}
													
													System.out.println("please enter the request id");
													int RID1 = driverinput.nextInt();
													sql = "update request set taken = 'yes' where RID = ?;";
													pstmt.setInt(1, RID1);
													pstmt.executeUpdate();
													sql = "insert into trips(TID, DID, PID, Start) values (? + 500, ?, (select PID from request where RID = ?), ?);";
													pstmt.setInt(1, RID1);
													pstmt.setInt(2, driverID);
													pstmt.setInt(3, RID1);
													SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
													java.util.Date date = new java.util.Date();
													String str2 = dateFormat.format(date);
													pstmt.setString(4, str2);
													pstmt.executeUpdate();
													sql = "select T.TID, P.Name, T.Start from trips T, passengers P where T.TID = ? + 500 AND T.PID = P.PID;";
													pstmt.setInt(1, RID1);
													ResultSet ok = pstmt.executeQuery();
													System.out.println("Trip ID, Passenger name, Start");
														while(ok.next()){
																	System.out.print(ok.getInt(1));
																	System.out.print(",   ");
																	System.out.print(ok.getString(2));
																	System.out.print(",   ");
																	System.out.println(ok.getString(3));
														}
													}
													condition = 200;
													break;
										case 2:		System.out.println("please enter your id");
													Scanner giveid = new Scanner(System.in);
													Scanner giveid2 = new Scanner(System.in);
													int did = giveid.nextInt();
													sql = "select TID, PID, Start from trips where DID = ? AND End != null;";
													pstmt = mySQLDB1.prepareStatement(sql);
													pstmt.setInt(1, did);
													ResultSet fs = pstmt.executeQuery();
													System.out.println("Trip ID, Passenger ID, Start");
														while(fs.next()){
																	System.out.print(fs.getInt(1));
																	System.out.print(",   ");
																	System.out.print(fs.getInt(2));
																	System.out.print(",   ");
																	System.out.println(fs.getString(3));
														}
													System.out.println("do you wish to finish the trip?[y/n]");
													String resp = giveid2.nextLine();
													if (resp.equals("y"))
													{
														System.out.println("Trip ID, Passenger name, Start, End, Fee");
														sql = "Update trips set End = (Now()) Fee = (TIMESTAMPDIFF(MINUTE,T.Start,NOW())) where DID = ?;";
														/*SimpleDateFormat dateFormatq = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
														java.util.Date dateq = new java.util.Date();
														String str1 = dateFormatq.format(dateq);
														pstmt.setString(1, str1);
														String S = fs.getString(3);
														java.util.Date datew = dateFormatq.parse(S);
														long diff = (dateq.getTime() - datew.getTime())/1000;
														pstmt.setLong(2, diff);
														pstmt.setInt(3, did);
														pstmt.executeUpdate(); */
														pstmt.setInt(1,did);
														pstmt.executeUpdate();
														sql = "select TID, PID, Start, End, Fee from trips where DID = ?;";
													
													pstmt.setInt(1, did);
													ResultSet fk = pstmt.executeQuery();
													System.out.println("Trip ID, Passenger ID, Start, End, Fee");
														while(fk.next()){
																	System.out.print(fk.getInt(1));
																	System.out.print(",   ");
																	System.out.print(fk.getInt(2));
																	System.out.print(",   ");
																	System.out.println(fk.getString(3));
																	System.out.print(",   ");
																	System.out.println(fk.getString(4));
																	System.out.print(",   ");
																	System.out.println(fk.getInt(5));
														}
													}
													else if (resp.equals("n"))
													{
														System.out.println("nothing happens");
														condition = 200;
														break;
													}
													else
													{
														System.out.println("invalid input, please make query again.");
														condition = 200;
														break;
													}
										case 3:		System.out.println("please enter your id");
													Scanner giveid1 = new Scanner(System.in);
													int did1 = giveid1.nextInt();
													sql = " select sum(Rating) from trips where DID = ? Order by End DESC limit 5;";
													pstmt = mySQLDB1.prepareStatement(sql);
													pstmt.setInt(1, did1);
													ResultSet sum1 = pstmt.executeQuery();
													sum1.next();
													int i = sum1.getInt(1);
													if(i == 5){
													sql = "select Avg(Rating) from trips where DID = ? Order by End DESC limit 5;";
													
													pstmt.setInt(1, did1);
													ResultSet avg = pstmt.executeQuery();
													avg.next();
													String avgs = avg.getString(1);
													float avgf = Float.valueOf(avgs);
												
													System.out.printf("Your driver rating is %.2f\n", avgf);
													
													}
													else{
														System.out.println("Need to have at least 5 rated trips. Drivers please receive more request");
													}
													condition = 200;
													break;
											case 4:		condition = 100;
													break;
											default:	System.out.println("[ERROR] invalid input.");
													condition = 200;
														
									}
													
									
									}while (condition >100);
									break; 
						case 4:		System.out.println("Great! nothing have to do here now!");
									condition = 200;
									break;
						default:	System.out.println("[ERROR] invalid input.");
									condition = 100;
									}
					}while(condition <= 100);
					}
				

				break;
			}catch (Exception e){
				System.out.print("[Error]: ");
				System.out.println(e.getMessage());
			}
		} 
		menuAns.close();
		System.exit(0);
	}
	
	/*private static class Query {
		
			public static String [] CREATETABLE = {
				
				"CREATE TABLE drivers(
				DID int unsigned primary key,
				Name varchar(30) not null,
				VID char(6) not null);",
				
				
				"CREATE TABLE vehicles(
				VID char(6) primary key,
				Model varchar(30) not null,
				Model_Year int(4) unsigned,
				Seats int unsigned);"
				
				"CREATE TABLE passengers(
				PID int unsigned primary key,
				Name varchar(30) not null);",
				
				"CREATE TABLE trips(
				TID int unsigned primary key,
				DID int unsigned not null,
				PID int unsigned not null,
				Start DATETIME,
				End DATETIME,
				Fee int unsigned,
				Rating int Default '0');",
				// check rating between 0 to 5 by java coding
				
				"CREATE TABLE request(
				RID int unsigned primary key auto_increment,
				PID int unsigned not null,
				Model_Year int(4) unsigned,
				Model varchar(30) not null,
				no_of_passenger int unsigned not null,
				taken varchar(3) not null);"
				
			};
			
			public static String[] DROPTABLE ={
				"drop table drivers;",
				"drop table vehicles;",
				"drop table passengers;",
				"drop table trips;",
				"drop table request;"
				
			};
			*/
	}
	