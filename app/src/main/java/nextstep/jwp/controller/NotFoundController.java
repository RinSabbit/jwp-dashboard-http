package nextstep.jwp.controller;

import nextstep.jwp.http.request.HttpRequest;
import nextstep.jwp.http.response.HttpResponse;

public class NotFoundController extends AbstractController {

    private static final String NOT_FOUND_PATH = "/404.html";

    @Override
    public void process(HttpRequest request, HttpResponse response) {
        response.redirect2(NOT_FOUND_PATH);
    }
}
