package mx.unam.dgpe.sample.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class MyController extends AbstractVerticle {
    private static final Logger logger = Logger.getLogger(MyController.class);
    
    public void start(Future<Void> fut) {
        logger.info("Inicializando Vertical");
        Router router = Router.router(vertx);
        //router.route("/*").handler(StaticHandler.create("assets")); // para invocar asi: http://localhost:8080/index.html
        // el directorio "upload-folder" será creado en la misma ubicación que el jar fue ejecutado
        router.route().handler(BodyHandler.create().setUploadsDirectory("upload-folder"));
        router.get("/api/cifrasfactorial").handler(this::cifrasFactorial);
       
        
        // Create the HTTP server and pass the "accept" method to the request handler.
        vertx.createHttpServer().requestHandler(router::accept).listen(
                config().getInteger("http.port", 8080), result -> {
                    if (result.succeeded()) {
                        fut.complete();
                    } else {
                        fut.fail(result.cause());
                    }
                });        
        
        logger.info("Vertical iniciada !!!");
    }  
    private void cifrasFactorial(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        HttpServerRequest request = routingContext.request();
        long numero = Long.parseLong(request.getParam("n"));
	double r = 0;
	Map<Object, Object> info = new HashMap<>();
	for(long n = 1; n <= numero; n++)
	{
		r += (Math.log(n)/ Math.log(10));
		
		//info.put((new Long(n)).toString(), (Math.log(n)/ Math.log(10)));
		
	}
	long resultado = (new Double(r)).longValue();
	info.put("cifras",(new Long(resultado + 1)).toString());
	



        
        response.setStatusCode(200).
        putHeader("content-type", "application/json; charset=utf-8").
        end(Json.encodePrettily(info));
    }
    
    

}
