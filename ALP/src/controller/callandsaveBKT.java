package controller;

import java.io.IOException;
import java.io.PrintWriter;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import entity.StudentProbability;
import service.XblockService;

/**
 * Servlet implementation class callandsaveBKT
 */
@WebServlet("/callandsaveBKT")
public class callandsaveBKT extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public callandsaveBKT() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String studentId = request.getParameter("student_id");
		String skillname = request.getParameter("skillname");
		String correctness = request.getParameter("correctness");
		String questionId = request.getParameter("question_id");
		String callback = request.getParameter("callback");
		
		response.setContentType("application/json;charset=UTF-8");
		try{
			Double currentL = XblockService.saveStudentProbability(studentId, skillname, correctness, questionId);
	        System.out.println(studentId + ", " + skillname + ", " + correctness + ", " + questionId);
	        
	        StudentProbability sp = new StudentProbability();
	        sp.setStudentId(studentId);
	        sp.setSkillname(skillname);
	        sp.setCorrectness(correctness);
	        sp.setProbability(currentL + "");
	        sp.setProblemName(questionId);
	        
	        PrintWriter out = response.getWriter();
	        //out.println(new Gson().toJson(sp));
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
