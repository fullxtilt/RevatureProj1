package reimbsystem.frontcontroller;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class FrontController {

	Javalin app;
	Dispatcher dispatcher;

	public FrontController(Javalin app) {
		this.app = app;
		app.before("/api/*", FrontController::checkAPIRequests);
		app.before("/html/*", FrontController::checkHTMLRequests);

		this.dispatcher = new Dispatcher(app);

	}

	public static void checkAPIRequests(Context context) {

		// Don't check session if requester is attempting to log in
		if (context.path().equals("/api/login")) {
			return;
		}

		checkLoggedIn(context);
	}

	public static void checkHTMLRequests(Context context) {
		if (context.path().equals("/html/loginpage.html")) {
			return;
		}

		checkLoggedIn(context);
	}

	public static void checkLoggedIn(Context context) {
		// Reroute to login if not logged in
		if (context.sessionAttribute("currentUser") == null) {
			context.redirect("/html/loginpage.html");
		}
	}
}
