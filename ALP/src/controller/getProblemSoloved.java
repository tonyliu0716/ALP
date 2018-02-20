package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import entity.QuestionList;
import service.XblockService;

/**
 * Servlet implementation class getProblemSoloved
 */
@WebServlet("/getProblemSoloved")
public class getProblemSoloved extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getProblemSoloved() {
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
		String callback = request.getParameter("callback");
		
		System.out.println("Student id is " + studentId + ", tutor name is " + tutorName + ", callback is " + callback);
		response.setContentType("application/json;charset=UTF-8");
		
		try{
            String questionList = XblockService.getProblemSoloved(studentId, tutorName);
	        
	        QuestionList question = new QuestionList();
	        question.setQuestionList(questionList);
	        PrintWriter out = response.getWriter();
	        out.println(callback + "("+new Gson().toJson(question)+")");
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
