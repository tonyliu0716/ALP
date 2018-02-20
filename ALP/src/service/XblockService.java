package service;


import configuration.DBUtils;
import configuration.SimpleBKT;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * Created by tao on 8/3/17.
 */

public class XblockService {


    // This method help us to save student data from the table: ALP.L_Probability_BKT
    public static Double saveStudentProbability(String studentId, String skillname, String correctness, String questionId) {
    	Connection conn = DBUtils.getConnection();
    	PreparedStatement ps = null;
    	PreparedStatement ps1 = null;
    	PreparedStatement ps2 = null;
    	PreparedStatement ps3 = null;
    	ResultSet rs = null;
    	ResultSet rs1 = null;
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // get opportunity_counts from DB then add 1 to it:
        try {
            
            // how to make sure that ALP only accept the first attempt
            String sql0 = "select count(*) from ALP.L_Probability_BKT where problem_name = ? and student_pastel_id = ? and skillname = ?";
            ps = conn.prepareStatement(sql0);
            ps.setString(1, questionId);
            ps.setString(2, studentId);
            ps.setString(3, skillname);
            rs = ps.executeQuery();
            while(rs.next()) {
                Integer rowCount = rs.getInt(1);
                System.out.println("rowCount: " + rowCount);
                if (rowCount != 0) {
                    return null;
                }
            }

            String sql = "select * from ALP.L_Probability_BKT where student_pastel_id = ? and skillname = ? and problem_name = ?";
            ps1 = conn.prepareStatement(sql);
            ps1.setString(1, studentId);
            ps1.setString(2, skillname);
            ps1.setString(3, questionId);
            rs1 = ps1.executeQuery();

            int count = 0;
            double prevL = 0;
            while(rs1.next()) {
                count = rs1.getInt("opportunity_counts");
                prevL = rs1.getDouble("probability");
            }

            double L = 0;
            double S = 0;
            double T = 0;
            double G = 0;
            // which means this is the first time that the student answer the question
            if(count == 0) {

                // which means we don't have any records in the table init_parameter_bkt table - temporary fix method
                L = 0.01;
                G = 0.111;
                S = 0.001;
                T = 0.409;

                SimpleBKT bkt = new SimpleBKT(L, G, S, T, Integer.parseInt(correctness));
                double firstL = bkt.computeL();
                System.out.println("First L value is:" + firstL);
                // then we assume Lzero: 0.001, G: 0.111, S:0.001, T:0.409 -> got these assumed initial data from Neal's test.py file
                count ++;
                String sql1 = "insert into ALP.L_Probability_BKT(student_pastel_id, skillname, correctness, timestamp, probability, opportunity_counts, problem_name) values(?,?,?,?,?,?,?) ";
                ps2 = conn.prepareStatement(sql1);
                ps2.setString(1, studentId);
                ps2.setString(2, skillname);
                ps2.setString(3, correctness);
                ps2.setString(4, timeStamp);
                ps2.setString(5, firstL + "");
                ps2.setString(6, count + "");
                ps2.setString(7, questionId);
                ps2.executeUpdate();

                return firstL;
            } else {
                // we found the previous student data, so we will update the probability value and the opportunity_counts after that
                SimpleBKT bkt = new SimpleBKT(prevL, 0.111, 0.001, 0.409, Integer.parseInt(correctness));
                double currentL = bkt.computeL();

                count++;
                String sql1 = "insert into ALP.L_Probability_BKT(student_pastel_id, skillname, correctness, timestamp, probability, opportunity_counts, problem_name) values(?,?,?,?,?,?,?) ";
                ps3 = conn.prepareStatement(sql1);
                ps3.setString(1, studentId);
                ps3.setString(2, skillname);
                ps3.setString(3, correctness);
                ps3.setString(4, timeStamp);
                ps3.setString(5, currentL + "");
                ps3.setString(6, count + "");
                ps3.setString(7, questionId);
                ps3.executeUpdate();
                return currentL;
            }




        }catch (Exception e) {
            e.printStackTrace();  
            try {
                	if(ps1 != null) {
                		ps1.close();
                	}
                	if(ps2 != null) {
                		ps2.close();
                	}
                	if(ps3 != null) {
                		ps3.close();
                	}
                	if(rs1 != null) {
                		rs1.close();
                	}
                } catch (SQLException e1) {	
					e1.printStackTrace();
				}
            DBUtils.releaseResources(rs, ps, conn);
            
        } finally {
        	
            try {
                	if(ps1 != null) {
                		ps1.close();
                	}
                	if(ps2 != null) {
                		ps2.close();
                	}
                	if(ps3 != null) {
                		ps3.close();
                	}
                	if(rs1 != null) {
                		rs1.close();
                	}
                } catch (SQLException e1) {	
					e1.printStackTrace();
				}
            
            DBUtils.releaseResources(rs, ps, conn);
        }

        return null;
    }




    /*
    * get the most recent L value from temporary_probability
    * */
    public static Double getLValue(String studentId, String skillname) {
    	Connection conn = DBUtils.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
    	Double L = 0.0;
        try {

            String sql = "SELECT * FROM ALP.L_Probability_BKT WHERE student_pastel_id = ? AND skillname = ? ORDER BY id DESC LIMIT 1;";
            ps = conn.prepareStatement(sql);
            ps.setString(1, studentId);
            ps.setString(2, skillname);
            rs = ps.executeQuery();

            if(rs.next()) {
                L = rs.getDouble(6);
            }


            return L;

        }catch (Exception e) {
            e.printStackTrace();
            DBUtils.releaseResources(rs, ps, conn);


        } finally {
        	DBUtils.releaseResources(rs, ps, conn);
        }

        return null;

    }


    /*
    *
    * the method below should return the list of the question names for the student id, and the tutor name.
    * table: ALP.cog_tutor_problem_history
    *
    * */
    public static String getProblemSoloved(String studentId, String tutorName) {
    	Connection conn = DBUtils.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
    	String questionList = "";

        try {

            

            String sql = "SELECT question_list FROM ALP.cog_tutor_problem_history where student_pastel_id = ? and tutor_name = ?;";
            ps = conn.prepareStatement(sql);
            ps.setString(1, studentId);
            ps.setString(2, tutorName);
            rs = ps.executeQuery();
            if(!rs.wasNull()) {
                if(rs.next()) {
                    questionList = rs.getString(1);
                }
            }

            return questionList;

        }catch (Exception e) {
            e.printStackTrace();
            DBUtils.releaseResources(rs, ps, conn);
        } finally {
        	DBUtils.releaseResources(rs, ps, conn);
        }

        return questionList;
    }




    /*
    *
    * the method below should append the question name to the tail of the question names list.
    * table: ALP.cog_tutor_problem_history
    * Create Statement:
    * CREATE TABLE `ALP`.`cog_tutor_problem_history` (
        `id` INT NOT NULL AUTO_INCREMENT,
        `student_pastel_id` VARCHAR(100) NULL,
        `tutor_name` VARCHAR(100) NULL,
        `question_list` MEDIUMTEXT NULL,
            PRIMARY KEY (`id`));
    * the list of question using "|" as a separator
    * */
    public static boolean updateProblemSolovedList(String studentId, String tutorName, String questionName) {
    	Connection conn = DBUtils.getConnection();
    	PreparedStatement ps = null;
    	PreparedStatement ps2 = null;
    	ResultSet rs = null;
    	
        try {

            String sql = "SELECT question_list FROM ALP.cog_tutor_problem_history WHERE student_pastel_id = ? AND tutor_name = ?;";
            ps = conn.prepareStatement(sql);
            ps.setString(1, studentId);
            ps.setString(2, tutorName);
            rs = ps.executeQuery();

            if(rs.next()) {
                    // which means this is NOT the first time student answer the question
                    String questionList = rs.getString(1) + "|" + questionName;
                    String update_question = "UPDATE ALP.cog_tutor_problem_history SET question_list = ? WHERE student_pastel_id = ? AND tutor_name = ?";
                    PreparedStatement ps1 = conn.prepareStatement(update_question);
                    ps1.setString(1, questionList);
                    ps1.setString(2, studentId);
                    ps1.setString(3, tutorName);
                    ps1.executeUpdate();

                    // the problem has been solved then we need to update the temporary_probability table.
                    
                    return true;
            } else {
                // which means this is the first time student answer that question, then we do the insert
                String insert_question = "INSERT INTO ALP.cog_tutor_problem_history (student_pastel_id, tutor_name, question_list) VALUES(?, ?, ?)";
                ps2 = conn.prepareStatement(insert_question);
                ps2.setString(1, studentId);
                ps2.setString(2, tutorName);
                ps2.setString(3, questionName);
                ps2.executeUpdate();
                
                return true;
            }



        }catch (Exception e) {
            e.printStackTrace();
            try {
            	if(ps2 != null) {
                	ps2.close();
                }
                DBUtils.releaseResources(rs, ps, conn);
            } catch (Exception e1) {
                e1.printStackTrace();
            }


        } finally {
        	try {
            	if(ps2 != null) {
                	ps2.close();
                }
                DBUtils.releaseResources(rs, ps, conn);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }

        return false;
    }

}

