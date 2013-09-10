package example;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Hello world!
 *
 */
public class HelloServlet extends HttpServlet
{
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException
    {
        resp.getWriter().println("Hello Servlet World!");
        resp.getWriter().println("this is the developer of the maven project myapp:");
        resp.getWriter().println(System.getProperty("developer"));
        
        resp.getWriter().println("this is the company for this platform:");
        resp.getWriter().println(System.getProperty("company"));
        
        resp.getWriter().println("now I am trying to connect to a database configured in cloud-bees.xml file");
        
        Context ctx;
		try {
			ctx = new InitialContext();
			  DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/testDynamicPass");
		        Connection conn = ds.getConnection();
		        Statement stmt = conn.createStatement();
		        stmt.executeUpdate("create table if not exists testdata (id INT,foo INT,bar INT)");

		        stmt.executeUpdate("insert into testdata (id,foo,bar) values(1,2,3)");

		        ResultSet rst = stmt.executeQuery("select id, foo, bar from testdata");
		        while(rst.next()) {
		         int id= rst.getInt(1);
		         String foo= rst.getString(2);
		         int bar= rst.getInt(3);
		         resp.getWriter().println(id);

		        }
		       
		        
		        resp.getWriter().println("now I am trying to send an email from valentina.armenise@gmail.com to varmenise@cloudbees.com");

		        
		        Context initCtx = new InitialContext();
	            Session session = (Session) initCtx.lookup("java:comp/env/mail/SendGrid");
		        
		        Message message = new MimeMessage(session);
	            try {
					message.setFrom(new InternetAddress("valentina.armenise@gmail.com"));
				    InternetAddress to[] = new InternetAddress[1];
		            to[0] = new InternetAddress("varmenise@cloudbees.com");
		            message.setRecipients(Message.RecipientType.TO, to);
		            message.setSubject("test sendGrid");
		            message.setContent("test sendGrid", "text/plain");
		            Transport.send(message);
				} catch (AddressException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        
	            
	            
	            conn.close();
	            
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
        
        resp.getWriter().close();
    }
}
