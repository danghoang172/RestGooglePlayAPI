
package com.google.play.rest;

import com.google.play.model.*;
import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;


public class GooglePlayVerticle extends AbstractVerticle {

    //get the param in the post body and return a token for user.
    private void signIn(RoutingContext routingContext){
        final User user = Json.decodeValue(routingContext.getBodyAsString(), User.class);
        try{
            Token token =(new Authentication(user)).signIn();
            routingContext.response().setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(token));
        }
        catch(Exception e){
            routingContext.fail(400);
        }
    }


    private void searchApps(RoutingContext routingContext){
        String appName = routingContext.request().getParam("name");
        routingContext.vertx().executeBlocking(future -> {
            List <App> appList;
            // Do the blocking operation in here
            // Imagine this was a call to a blocking API to get the result
            try {

                if (appName == null) {
                    routingContext.fail(400);
                }
                else{
                    try{
                        PlayStore userPlay = (new Authentication(routingContext).authenticateRequest());
                        if(userPlay != null){
                            appList =userPlay.searchApp(appName);
                            routingContext.response()
                                    .setStatusCode(200)
                                    .putHeader("content-type", "application/json; charset=utf-8")
                                    .end(Json.encodePrettily(appList));
                        }
                        else{
                            routingContext.fail(401);
                        }
                    }
                    catch(Exception e){
                        routingContext.fail(403);
                    }

                }


            } catch (Exception e) {
                routingContext.fail(403);
            }
        }, res -> {
            if (res.succeeded()) {
                System.out.println("Downloaded");
                //request.response().putHeader("content-type", "text/plain").end(res.result());
            } else {
                routingContext.fail(500);
            }
        });
    }

    //return the detail of the given package name
    private void getDetail(RoutingContext routingContext){
        String packageName = routingContext.request().getParam("package");
        if (packageName == null) {
            routingContext.fail(400);
        }
        else{
            try{
                PlayStore userPlay = (new Authentication(routingContext).authenticateRequest());
                if(userPlay != null){           //no authentication, wrong token
                    PlayStorePage page = userPlay.getAppDetail(packageName);
                    routingContext.response()
                            .setStatusCode(200)
                            .putHeader("content-type", "application/json; charset=utf-8")
                            .end(Json.encodePrettily(page));
                }
                else{
                    routingContext.fail(401);
                }

            }
            catch(IOException i){
                routingContext.fail(400);
            }
            catch(Exception e){
                routingContext.fail(403);
            }
        }

    }


    private void downloadApp(RoutingContext routingContext) {
        String packageName = routingContext.request().getParam("package");
        routingContext.vertx().executeBlocking(future -> {
            // Do the blocking operation in here
            // Imagine this was a call to a blocking API to get the result
            try {
                PlayStore userPlay = new Authentication(routingContext).authenticateRequest();
                if(userPlay != null){
                    final InputStream downloadStream = userPlay.downloadApp(packageName);
                    String fileName = packageName + ".apk";
                    byte[] appBuffer = new byte[1028];
                    int numberOfBytesRead;
                    Buffer buffer = Buffer.buffer();
                    routingContext.response().setChunked(true);
                    routingContext.response().setStatusCode(200)
                            .putHeader("Content-Type", "application/vnd.android.package-archive")
                            .putHeader("Content-Disposition", "attachment; filename=\""
                                    + URLEncoder.encode(fileName, "UTF-8") + "\"");

                    while ((numberOfBytesRead = downloadStream.read(appBuffer)) != -1) {
                        buffer.appendBytes(appBuffer, 0, numberOfBytesRead);
                        routingContext.response().write(buffer);
                        buffer = Buffer.buffer();

                    }
                    routingContext.response().end();
                }
                else{
                    routingContext.fail(401);
                }


            } catch (IOException e) {
                routingContext.fail(400);
            } catch (Exception e) {
                routingContext.fail(403);
            }
        }, res -> {
            if (res.succeeded()) {
                System.out.println("Downloaded");
                //request.response().putHeader("content-type", "text/plain").end(res.result());
            } else {
                routingContext.fail(500);
            }
        });
    }


    private void failureHandler(RoutingContext failureRoutingContext){
        int statusCode = failureRoutingContext.statusCode();
        String responseString;
        switch(statusCode){
            case 400:
                responseString = "Bad Request. Check if you have correct syntax";
                break;
            case 401:
                responseString = "No authorization";
                break;
            case 403:
                responseString ="Forbidden!!";
                break;
            case 404:
                responseString ="Resource not found.";
                break;
            case 500:
                responseString ="Server Error";
                break;
            default:
                responseString = "Unknown exception occurred. Lets log it for further debugging.";
                break;
        }

        HttpServerResponse response = failureRoutingContext.response();
        response.setStatusCode(statusCode).end(responseString);


    }


    @Override
    public void start(Future<Void> fut) {

        // Create a router object.
        Router router = Router.router(vertx);
        // Serve static resources from the /assets directory
        router.get("/homepage").handler(routingContext -> {
            routingContext.response()
                    .putHeader("content-type", "application/json")
                    .end(new JsonObject().put("greeting", "Welcome to APK downloader API").encode());
        });
        router.route("/swagger/*").handler(StaticHandler.create("api/swagger-editor/"));
        router.route("/api-docs/*").handler(StaticHandler.create("assets/dist/"));

        Route route = router.get("/api/store/*");
        router.route("/api/store/login*").handler(BodyHandler.create());   //this line allow to read body of the request
        router.post("/api/store/login").handler(this::signIn);
        router.get("/api/store/app/search").handler(this::searchApps);
        router.get("/api/store/app/detail").handler(this::getDetail);
        router.get("/api/store/app/download").handler(this::downloadApp);

        route.failureHandler(this::failureHandler);

        // Create the HTTP server and pass the "accept" method to the request handler.
        vertx
                .createHttpServer()
                .requestHandler(router::accept)
                .listen(
                        // Retrieve the port from the configuration,
                        // default to 8080.
                        config().getInteger("http.port", 8080),
                        result -> {
                            if (result.succeeded()) {
                                fut.complete();
                            } else {
                                fut.fail(result.cause());
                            }
                        }
                );
    }


    private void completeStartup(AsyncResult<HttpServer> http, Future<Void> fut) {
        if (http.succeeded()) {
            fut.complete();
        } else {
            fut.fail(http.cause());
        }
    }

}
