/*
 * COMP 6302 - Web Services / Internet
 * Lab 2: ToDo with Servlet
 * Marc Badrian - Due 3/2/16
 * 
 */



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class TodoClient {

	public static void main(String args[]) {

		
		Scanner s = new Scanner(System.in);  // Reading from System.in
		int choice = 0;
		boolean again = true;
		do {
			try {
				System.out.print("\n" + "Choose one of the following options: " + "\n"
						+ "(1) POST [id] [todo message]" + "\n"
						+ "(2) GET [id]" + "\n"
						+ "(3) GET" + "\n"
						+ "(4) DELETE [id] " + "\n"
						+ "(5) PUT [id] [todo message]" + "\n"
						+ "(6) EXIT PROGRAM" + "\n" + "\n"
						+ "Please enter a number:  ");
				try {
					choice = Integer.parseInt(s.nextLine());
				}catch(NumberFormatException nfe){
					System.err.println("Invalid Format!");
				}
				System.out.println();
				switch (choice) {
				case 1: 
					// Insert an ToDo item.
					System.out.println("To POST a record...");
					System.out.print("Enter an id number: ");
					int id = Integer.parseInt(s.nextLine());
					System.out.print("Enter a message: ");
					String message = s.nextLine();
					try {
						System.out.println();
						System.out.println("<Making POST call>");
						System.out.println();

						// Parse the URL
						String urlParameters  = "choice=" + choice + "&id=" + id + "&message=" + message;
						byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
						int    postDataLength = postData.length;
						String request        = "http://localhost:8080/Lab2/Todo";
						URL    url            = new URL( request );
						HttpURLConnection conn= (HttpURLConnection) url.openConnection();   
						
						conn.setDoOutput( true );
						conn.setInstanceFollowRedirects( false );
						conn.setRequestMethod( "POST" );
						conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
						conn.setRequestProperty( "charset", "utf-8");
						conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
						conn.setUseCaches( false );
						try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
						   wr.write( postData );
						}
						

						BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						String next_record = null;
						while ((next_record = reader.readLine()) != null) {
							System.out.println(next_record);
						}
					} catch (IOException e) {
						throw new RuntimeException("Please try again. \n" + e);
					}
					break;
				case 2:
					// Get and display todo message as well as when it was posted.
					System.out.println("To GET a record...");
					System.out.print("Enter a ToDo id number: ");
					int todoId = Integer.parseInt(s.nextLine());

					//Retrieve row data
					try {
						System.out.println();
						System.out.println("<Making POST call>");
						System.out.println();
						
						// Parse the URL
						String urlParameters  = "choice=" + choice + "&id=" + todoId;
						byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
						int    postDataLength = postData.length;
						String request        = "http://localhost:8080/Lab2/Todo";
						URL    url            = new URL( request );
						HttpURLConnection conn= (HttpURLConnection) url.openConnection();   
						
						conn.setDoOutput( true );
						conn.setInstanceFollowRedirects( false );
						conn.setRequestMethod( "POST" );
						conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
						conn.setRequestProperty( "charset", "utf-8");
						conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
						conn.setUseCaches( false );
						try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
						   wr.write( postData );
						}
						

						BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						String next_record = null;
						while ((next_record = reader.readLine()) != null) {
							System.out.println(next_record);
						}
					} catch (IOException e) {
						throw new RuntimeException("Please try again. \n" + e);
					}
					break;
				case 3:
					//Get and display all todo items
					System.out.println("Getting ALL records...");
						//Retrieve row data
					try {
						System.out.println();
						System.out.println("<Making GET call>");
						System.out.println();
						
						String request        = "http://localhost:8080/Lab2/Todo";
						URL    url            = new URL( request );
						HttpURLConnection conn= (HttpURLConnection) url.openConnection();   
						
						conn.setInstanceFollowRedirects( false );
						conn.setRequestMethod( "GET" );
						conn.setUseCaches( false );
						
						BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						String next_record = null;
						while ((next_record = reader.readLine()) != null) {
							System.out.println(next_record);	
					}
							
					} catch (IOException e) {
						throw new RuntimeException("Please try again. \n" + e);
					}
					break;
				case 4:
					// Deletes the todo message at the given id from the database.
					System.out.println("To DELETE a record...");
					System.out.print("Enter an id number: ");
					int deleteId = Integer.parseInt(s.nextLine());

					try {
						System.out.println();
						System.out.println("<Making POST call>");
						System.out.println();
						
						// Parse the URL
						String urlParameters  = "choice=" + choice + "&id=" + deleteId;
						byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
						int    postDataLength = postData.length;
						String request        = "http://localhost:8080/Lab2/Todo";
						URL    url            = new URL( request );
						HttpURLConnection conn= (HttpURLConnection) url.openConnection();   
						
						conn.setDoOutput( true );
						conn.setInstanceFollowRedirects( false );
						conn.setRequestMethod( "POST" );
						conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
						conn.setRequestProperty( "charset", "utf-8");
						conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
						conn.setUseCaches( false );
						try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
						   wr.write( postData );
						}
						

						BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						String next_record = null;
						while ((next_record = reader.readLine()) != null) {
							System.out.println(next_record);
						}
					} catch (IOException e) {
						throw new RuntimeException("Please try again. \n" + e);
					}
					break;
				case 5:
					// Insert an ToDo item.
					System.out.println("To PUT a record...");
					System.out.print("Enter an id number: ");
					int putId = Integer.parseInt(s.nextLine());
					System.out.print("Enter a message: ");
					String putMessage = s.nextLine();
					try {
						System.out.println();
						System.out.println("<Making POST call>");
						System.out.println();
						
						// Parse the URL
						String urlParameters  = "choice=" + choice + "&id=" + putId + "&putMessage=" + putMessage;
						byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
						int    postDataLength = postData.length;
						String request        = "http://localhost:8080/Lab2/Todo";
						URL    url            = new URL( request );
						HttpURLConnection conn= (HttpURLConnection) url.openConnection();   
						
						conn.setDoOutput( true );
						conn.setInstanceFollowRedirects( false );
						conn.setRequestMethod( "POST" );
						conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
						conn.setRequestProperty( "charset", "utf-8");
						conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
						conn.setUseCaches( false );
						try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
						   wr.write( postData );
						}
						

						BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						String next_record = null;
						while ((next_record = reader.readLine()) != null) {
							System.out.println(next_record);
						}
					} catch (IOException e) {
						throw new RuntimeException("Please try again. \n" + e);
					}
					break;
				case 6:
					// exit program
					System.out.println("Goodbye!");
					again = false;
				};
			} catch (Exception e) {
				System.out.println("That is not a valid entry.");
				e.printStackTrace();

			};
		} while (again == true);
	
	}
	
}
