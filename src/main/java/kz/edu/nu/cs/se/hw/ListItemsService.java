package kz.edu.nu.cs.se.hw;

import java.util.Collections;
import java.util.Calendar;
import java.util.Date;
import java.time.format.DateTimeFormatter;
import java.text.DateFormat;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/items")
public class ListItemsService {
	DateFormat formatManager = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
    private List<Task> todoList = new CopyOnWriteArrayList<Task>();
    String regexOneAlphaCharacterAtLeast = "^.*[a-zA-Z]+.*$"; // contains at least one alphabetical letter
   
	private class Task {
		protected String name;
		protected String creationDate;
		
		public Task(String taskName) {
			name = taskName;
			creationDate = formatManager.format(Calendar.getInstance().getTime());
		}
	}
    
    
    public ListItemsService() {
        for (int i = 0; i < 20; i++) {
        	todoList.add(new Task("Entry " + String.valueOf(i)));
        }
        Collections.reverse(todoList);
    }
    
    @GET
    public Response getList() {
        Gson gson = new Gson();
        
        return Response.ok(gson.toJson(todoList)).build();
    }
    
    @GET
    @Path("{id: [0-9]+}")
    public Response getListItem(@PathParam("id") String id) {
        int i = Integer.parseInt(id);
        
        return Response.ok(todoList.get(i)).build();
    }
    
    @POST
    public Response postListItem(@FormParam("newEntry") String entry) {
    	if (entry.matches(regexOneAlphaCharacterAtLeast)) {
    		todoList.add(0, new Task(entry));
    	}
    	
        return Response.ok().build();
    }
    
    @DELETE
    @Path("/item")
    public Response removeItem(String entry) {
    	int removeid = -1;
    	for (int i = 0; i < todoList.size(); i++) {
    		if (todoList.get(i).name.equals(entry)) {
    			removeid = i;
    			break;
    		}
    	}
    	if (removeid >= 0) todoList.remove(removeid);
    	
    	return Response.ok().build();
    }
    
    
    @DELETE
    public Response clearListItems() {
    	todoList.clear();
    	
    	return Response.ok().build();
    }
}
