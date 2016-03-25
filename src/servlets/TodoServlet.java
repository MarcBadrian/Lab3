/*
 * COMP 6302 - Web Services / Internet
 * Lab 2: ToDo with Servlet
 * Marc Badrian - Due 3/2/16
 * 
 */

package servlets;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;  


/**
 * Servlet implementation class EchoServlet
 */
@WebServlet(name = "TodoServlet",
description = "This servlet accepts HTTP requests from the Client and stores todo items",
urlPatterns = {"/TodoServlet", "/ToDo"})
public class TodoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    Scanner s = new Scanner(System.in);
    
    private static final ConcurrentHashMap<Integer, String[]> map = new ConcurrentHashMap<Integer, String[]>();
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    
    public TodoServlet() {
        super();
    }

    public void init() throws ServletException
    {
        // No initialization required
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
		System.out.println("GETTING SOMETHING!");
				
		response.setContentType("text/html");
        JSONObject json = new JSONObject();  
		PrintWriter out = response.getWriter();
		out.write("MY TEST STRING");
		//Get and display all todo items
		for (Integer key : map.keySet()) {
			String[] value = map.get(key);
			json.put("id: ", key); 
			json.put("message: ", value[0]); 
			json.put("timestamp: ", value[1]);
			out.write(json.toString());
			}
		// out.close();
		} catch (Exception e) {
			e.printStackTrace();

		};
		
	    
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			//Integer choice = Integer.parseInt(request.getParameter("choice"));
			int choice = 1;
			
			Integer id = Integer.parseInt(request.getParameter("id"));
			
			// create timestamp
			Calendar calendar = Calendar.getInstance();
			java.util.Date now = calendar.getTime();
			java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
			Timestamp timestamp = currentTimestamp;
			String time = String.format("%1$TD %1$TT", timestamp);
			
			PrintWriter out = response.getWriter();

			
			switch (choice) {
			case 1: 
				String message = request.getParameter("message");
				String[] record = {message, time};
				if (map.containsKey(id) || map.containsValue(record[0])) {
					out.println("Insert failed!");
					out.println("There's already a Todo record with that id or message!");
					break;
				} else {
				map.putIfAbsent(id, record);
				response.setContentType("text/html");
				out.println("Insert success!");
				out.println("id: " + id + "\n" + 
			    		    "message: " + record[0] + "\n" + 
			    		    "timestamp: " + record[1] + "\n");		
				};
				break;
			case 2:
				if (map.containsKey(id)) {
					String[] value = map.get(id);
					out.println("Todo found!");
				    out.println("id: " + id + "\n" + 
				    		    "message: " + value[0] + "\n" + 
				    		    "timestamp: " + value[1] + "\n");
				} else {
					out.println("NO Todo found!");
				}
				break;
			case 4:
				if (map.containsKey(id)) {
					map.remove(id);
				} else {
					out.println("NO Todo found! Nothing deleted!");
				}
				break;
			case 5:
				String putMessage = request.getParameter("message");
				String[] putRecord = {putMessage, time};
				map.put(id, putRecord);
				response.setContentType("text/html");
				out.println("Insert success!");
				out.println("id: " + id + "\n" + 
		    		    "message: " + putRecord[0] + "\n" + 
		    		    "timestamp: " + putRecord[1] + "\n");
				break;
			}
		
		} catch (Exception e) {
			//System.out.println("That is not a valid entry.");
			e.printStackTrace();

		};
	}

}
