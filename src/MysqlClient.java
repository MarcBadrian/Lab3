/*
 * COMP 6302 - Web Services / Internet
 * Lab 1: ToDo System using MySQL
 * Marc Badrian - Due 2/17/16
 * 
 */

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;


import model.ToDo;

public class MysqlClient {

	public MysqlConnector connector;

	public MysqlClient(Scanner s, String dbName){
		connector = new MysqlConnector(s, dbName);
		Connection conn = connector.getConnection();
		connector.createDB(conn, dbName);
		connector.createTable(conn);
	}

	public MysqlClient(Scanner s, String dbName, String user, String pass){
		connector = new MysqlConnector(s, dbName, user, pass);
		Connection conn = connector.getConnection();
		connector.createDB(conn, dbName);
		connector.createTable(conn);
	}

	public static void main(String[] args) throws IOException {
		Scanner s = new Scanner(System.in);  // Reading from System.in
		String dbName = "Lab1";
		MysqlClient client = new MysqlClient(s, dbName);
		MysqlConnector conn = client.connector;
		String username = conn.getUser();
		String password = conn.getPassword();
		int choice = 0;
		boolean again = true;
		do {
			try {
				System.out.print("\n" + "Choose one of the following options: " + "\n"
						+ "(1) POST [id] [todo message]" + "\n"
						+ "(2) GET [id]" + "\n"
						+ "(3) GET" + "\n"
						+ "(4) DELETE [id] " + "\n"
						+ "(5) REPLICATE [URI]" + "\n"
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
					System.out.print("Enter an id number: ");
					int id = Integer.parseInt(s.nextLine());
					System.out.print("Enter a message: ");
					String message = s.nextLine();
					ToDo insertToDo = new ToDo();
					insertToDo.setId(id);
					insertToDo.setToDoMessage(message);
					insertToDo.setTimestamp();
					boolean success = conn.insertToDo(insertToDo);
					if(!success){
						System.err.println("Insert failed");
					}else{
						System.out.println("Insert success!");
					}
					break;
				case 2:
					// Get and display todo message as well as when it was posted.
					System.out.print("\n" + "Enter a ToDo id number: ");
					int todoId = Integer.parseInt(s.nextLine());
					ToDo todo = conn.getToDoMessage(todoId);
					System.out.println("\n" + "Displaying record...");

					//Retrieve row data
					int get_id  = todo.getId();
					String get_message = todo.getMessage();
					Timestamp get_time = todo.getTimestamp();

					//Display values
					System.out.println("id: 			" + get_id);
					System.out.println("message: 		" + get_message);
					System.out.println("date posted: 		" + get_time + "\n");
					break;
				case 3:
					//Get and display all todo items
					List<ToDo> todos = conn.getAllMessages();
					System.out.println("Displaying all records...");
					for(ToDo todo1 : todos){
						//Retrieve row data
						int all_ids  = todo1.getId();
						String all_messages = todo1.getMessage();
						Timestamp all_times = todo1.getTimestamp();

						//Display values
						System.out.println("id: 			" + all_ids);
						System.out.println("message:		" + all_messages);
						System.out.println("date posted: 	" + all_times + "\n");
					};
					break;
				case 4:
					// Deletes the todo message at the given id from the database.
					System.out.print("Enter an id number: ");
					int deleteId = Integer.parseInt(s.nextLine());
					boolean todoDelete = conn.deleteMessage(deleteId);
					if(!todoDelete){
						System.err.println("Delete failed");
					}else{
						System.out.println("Delete success!");
					};
					break;
				case 5:
					// replicate database and table
					System.out.print("Enter the URI (in [host]/[database] format) : ");
					String uri = s.nextLine();
					String delims = "[/]";
					String[] tokens = uri.split(delims);
					String old_dbName = dbName;
					dbName = tokens[1];
					System.out.println();
					// get all data from todo
					List<ToDo> copy_todos = conn.getAllMessages();
					MysqlClient new_client = new MysqlClient(s, dbName, username, password);
					MysqlConnector new_conn = new_client.connector;
					boolean replicate_success = false;
					// for each todo returned, insert it into the new table
					for(ToDo new_todo : copy_todos){
						//Retrieve row data
						int all_ids  = new_todo.getId();
						String all_messages = new_todo.getMessage();
						Timestamp all_times = new_todo.getTimestamp();
						ToDo insert_copy = new ToDo();
						insert_copy.setId(all_ids);
						insert_copy.setToDoMessage(all_messages);
						insert_copy.setTimestamp(all_times);
						replicate_success = new_conn.insertToDo(insert_copy);
						//Display replicated values
						System.out.println("id: 			" + all_ids);
						System.out.println("message:		" + all_messages);
						System.out.println("date posted: 	" + all_times + "\n");
					};
					if(!replicate_success){
						System.err.println("Replication failed");
					}else{
						System.out.println("Replication success!");
					};
					// return to old client
					MysqlClient old_client = new MysqlClient(s, old_dbName, username, password);
					conn = old_client.connector;
					break;
				case 6:
					// exit program
					System.out.println("Goodbye!");
					again = false;
				};
			} catch (Exception e) {
				System.out.println("That is not a valid entry.");
				//TODO Auto-generated catch block
				e.printStackTrace();

			};
		} while (again == true);
	}

}
