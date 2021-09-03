package nextstep.jwp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;
import nextstep.jwp.controller.Controller;
import nextstep.jwp.controller.RequestMapping;
import nextstep.jwp.http.common.Body;
import nextstep.jwp.http.request.HttpRequest;
import nextstep.jwp.http.response.HttpResponse;
import nextstep.jwp.http.response.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final String INTERNAL_SERVER_ERROR_PATH = "/500.html";

    private final Socket connection;
    private final RequestMapping requestMapping;

    public RequestHandler(Socket connection) {
        this.connection = Objects.requireNonNull(connection);
        this.requestMapping = new RequestMapping();
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
            connection.getPort());

        try (final InputStream inputStream = connection.getInputStream();
            final OutputStream outputStream = connection.getOutputStream()) {

            final BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
            final HttpRequest request = HttpRequest.parse(bufferedReader);

            final HttpResponse response = HttpResponse.of(HttpStatus.OK, Body.empty());
            Controller controller = requestMapping.findController(request);
            System.out.println(controller.getClass().getName());
            service(request, response);
            writeResponse(outputStream, response);
        } catch (IOException exception) {
            log.error("Exception stream", exception);
        } finally {
            close();
        }
    }

    private void service(HttpRequest request, HttpResponse response) {
        try {
            System.out.println("START SERVICE!!");

//            controller.process(request, response);
        } catch (RuntimeException exception) {
            log.error("Internal server error", exception);
            response.redirect2(INTERNAL_SERVER_ERROR_PATH);
        }
    }

    private void writeResponse(OutputStream outputStream, HttpResponse response)
        throws IOException {
        final String responseByte = response.asString();
        outputStream.write(responseByte.getBytes());
        outputStream.flush();
    }

    private void close() {
        try {
            connection.close();
        } catch (IOException exception) {
            log.error("Exception closing socket", exception);
        }
    }
}
