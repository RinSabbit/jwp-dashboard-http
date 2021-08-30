package nextstep.jwp.controller;

import nextstep.jwp.http.request.HttpRequest;
import nextstep.jwp.http.request.requestline.RequestLine;
import nextstep.jwp.http.response.HttpResponse;
import nextstep.jwp.http.response.HttpStatus;

public class StaticResourceController implements Controller {

    public StaticResourceController() {
    }

    @Override
    public HttpResponse process(HttpRequest request) {
        final RequestLine requestLine = request.getRequestLine();
        return HttpResponse.of(HttpStatus.OK, requestLine.getRequestURI());
    }

    @Override
    public boolean matchRequest(HttpRequest request) {
        return false;
    }
}