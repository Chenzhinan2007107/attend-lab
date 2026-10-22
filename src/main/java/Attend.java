import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Attend {
    public static List<Attendee> list = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        Server server = new Server(Integer.parseInt(System.getenv().getOrDefault("PORT", "8080")));
        ServletContextHandler ctx = new ServletContextHandler(ServletContextHandler.SESSIONS);
        ctx.setContextPath("/");
        server.setHandler(ctx);
        ctx.addServlet(new ServletHolder(new AttendServlet()), "/*");
        server.start();
        server.join();
    }

    static class Attendee {
        String studentId;
        String name;
        long time;
        Attendee(String sid, String n){
            studentId = sid;
            name = n;
            time = System.currentTimeMillis();
        }
    }

    static class AttendServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            resp.setContentType("text/html;charset=utf-8");
            PrintWriter w = resp.getWriter();
            String sid = req.getParameter("id");
            String name = req.getParameter("name");
            if(sid != null && name != null){
                list.add(new Attendee(sid, name));
            }
            w.println("<html><body>");
            w.println("<h2>Attendance</h2>");
            w.println("<form>ID:<input name='id'> Name:<input name='name'><button type='submit'>Sign</button></form>");
            w.println("<h3>Attendee List</h3>");
            for(var a : list){
                w.println(a.studentId + " | " + a.name + "<br>");
            }
            w.println("</body></html>");
        }
    }
}
