package nextstep.jwp.controller;

import nextstep.jwp.http.request.HttpRequest;
import nextstep.jwp.http.request.requestline.RequestLine;
import nextstep.jwp.http.response.HttpResponse;
import nextstep.jwp.http.response.HttpStatus;

public class StaticResourceController implements Controller {

    @Override
    public void process(HttpRequest request, HttpResponse response) {
        final RequestLine requestLine = request.getRequestLine();
        response.of2(requestLine.getRequestURI());
    }
}
