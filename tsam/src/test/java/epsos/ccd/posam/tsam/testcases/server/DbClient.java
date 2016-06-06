package epsos.ccd.posam.tsam.testcases.server;

import org.h2.tools.Server;

public class DbClient {
	public static void main(String[] args) throws Exception{
		Server server=Server.createWebServer();
		server.start();
	}
}
