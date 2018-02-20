package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import entity.StudentProbability;
import service.XblockService;

/**
 * Servlet implementation class getLValue
 */
@WebServlet("/getLValue")
public class getLValue extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getLValue() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String studentId = request.getParameter("student_id");
		String skillname = request.getParameter("skillname");
		String callback = request.getParameter("callback");
		System.out.println("Student Id is :" + studentId + ", skillname is " + skillname + ", callback is " + callback);
		response.setContentType("application/json;charset=UTF-8");
		
		try{
            Double currentL = XblockService.getLValue(studentId, skillname);
	        
	        StudentProbability sp = new StudentProbability();
	        sp.setProbability(currentL + "");
	        PrintWriter out = response.getWriter();
	        out.println(callback + "("+new Gson().toJson(sp)+")");
	      } catch(Exception e) {
	    	  e.printStackTrace();
	      }
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
