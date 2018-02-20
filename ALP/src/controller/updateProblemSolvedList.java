package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import entity.Flag;
import service.XblockService;

/**
 * Servlet implementation class updateProblemSolvedList
 */
@WebServlet("/updateProblemSolvedList")
public class updateProblemSolvedList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public updateProblemSolvedList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String studentId = request.getParameter("student_id");
		String tutorName = request.getParameter("tutor_name");
		String questionName = request.getParameter("question_name");
		String callback = request.getParameter("callback");
		
		System.out.println("Student Id is :" + studentId + ", tutor name is " + tutorName + ", callback is " + callback + ", question name is " + questionName);
		response.setContentType("application/json;charset=UTF-8");
		
		try{
            Boolean flag = XblockService.updateProblemSolovedList(studentId, tutorName, questionName);
	        Flag f = new Flag();
	        f.setFlag(flag);
	        PrintWriter out = response.getWriter();
	        out.println(callback + "("+new Gson().toJson(f)+")");
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
