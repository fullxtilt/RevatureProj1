package reimbsystem;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import reimbsystem.frontcontroller.FrontController;


public class MainDriver {

	public static void main(String[] args) {

		Javalin app = Javalin.create((config) -> {
			config.addStaticFiles(staticFiles -> {
				staticFiles.directory = "/webpage-resources";
				staticFiles.hostedPath = "/";
				staticFiles.location = Location.CLASSPATH;
			});
		}).start(9006);
		
		FrontController frontC = new FrontController(app);
		
	}

}
