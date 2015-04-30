package Statistics;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Interface.Tweets;

@WebServlet("/Statistics")
public class StatisticServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Tweets tweet = new Tweets();

	public StatisticServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		request.setAttribute("tweets", tweet.getTopTweets());
		request.setAttribute("retweets", tweet.getTopRetweets());
		request.setAttribute("followers", tweet.getTopFollowers());
		request.setAttribute("mentioned", tweet.getTopMentioned());

		getServletContext().getRequestDispatcher("/WEB-INF/statistic.jsp")
				.forward(request, response);
	}

}
